package com.example.loginactivity.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginactivity.FillWordsTestResult;
import com.example.loginactivity.R;
import com.example.loginactivity.TestResultActivity;
import com.example.loginactivity.TestVocabularyActivity;
import com.example.loginactivity.TextToSpeechHelper;
import com.example.loginactivity.models.Participant;
import com.example.loginactivity.models.TestResultFillWords;
import com.example.loginactivity.models.Topic;
import com.example.loginactivity.models.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class AdapterFillWords extends RecyclerView.Adapter<AdapterFillWords.WordListViewHolder> {
    Context context;
    ArrayList<Word> words;
    private String title;
    private ArrayList<TestResultFillWords> testResults = new ArrayList<>();
    boolean isSwapMode;
    boolean isFeedbackMode;
    public TextToSpeechHelper textToSpeechHelper;
    ViewPager2 viewPager2;
    private int totalScore = 0;

    Topic topic;

    public AdapterFillWords(Context context, ArrayList<Word> words, String title, boolean isFeedbackMode, boolean isSwapMode, ViewPager2 viewPager2, Topic topic) {
        this.context = context;
        this.words = words;
        this.title = title;
        this.isFeedbackMode = isFeedbackMode;
        this.isSwapMode = isSwapMode;
        this.viewPager2 = viewPager2;
        this.topic = topic;
    }
    @NonNull
    @Override
    public WordListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fill_word_test_item, parent, false);
        return new AdapterFillWords.WordListViewHolder(view);
    }

    private boolean compareStringsIgnoringDiacriticsAndCase(String str1, String str2) {
        return removeDiacritics(str1).equalsIgnoreCase(removeDiacritics(str2));
    }

    private String removeDiacritics(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
    @Override
    public void onBindViewHolder(@NonNull WordListViewHolder holder, int position) {
        Word word = words.get(position);
        holder.txtTopicTitle.setText(title);
        textToSpeechHelper = new TextToSpeechHelper(context);

        // Đặt câu hỏi và mô tả dựa trên chế độ swap
        String questionText = isSwapMode ? word.getVietnamese() : word.getEnglish();
        String answerText = isSwapMode ? word.getEnglish() : word.getVietnamese();

        holder.txt_question.setText(questionText);
        holder.edt_answer.setHint("Enter the " + (isSwapMode ? "English" : "Vietnamese") + " meaning");
        holder.txt_description.setText(word.getDescription());


        // Phát âm thanh dựa trên chế độ swap
        holder.soundIcon.setOnClickListener(view -> {
            Locale locale = isSwapMode ? new Locale("vi", "VN") : Locale.ENGLISH;
            textToSpeechHelper.setLanguage(locale);
            textToSpeechHelper.speak(questionText);
        });

        holder.btn_submit.setOnClickListener(v -> {
            String userAnswer = holder.edt_answer.getText().toString().trim();
            if (userAnswer.isEmpty()) {
                Toast.makeText(context, "Please enter an answer.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isAnswerCorrect = compareStringsIgnoringDiacriticsAndCase(userAnswer, answerText);

            Word wordSwap = new Word(word.getVietnamese(), word.getEnglish(), word.getDescription());
            testResults.add(new TestResultFillWords(isSwapMode ? wordSwap : word, userAnswer, isAnswerCorrect));

            if (isAnswerCorrect) {
                totalScore += 500;  // Cộng 500 điểm cho mỗi câu trả lời đúng
            }

            if (isFeedbackMode) {
                showFeedbackDialog(isAnswerCorrect, position, word);
            } else {
                moveToNextQuestion(position);
            }
        });

        // Xử lý nút Skip
        holder.btn_skip.setOnClickListener(v -> {
            Word wordSwap = new Word(word.getVietnamese(), word.getEnglish(), word.getDescription());
            testResults.add(new TestResultFillWords(isSwapMode ? wordSwap : word, "", false));
            if (position < words.size() - 1) {
                viewPager2.setCurrentItem(position + 1);
            } else {
                updateScoreToFirebase();
            }
        });
    }

    private void showFeedbackDialog(boolean isCorrect, int currentPosition, Word word) {
        final Dialog feedbackDialog = new Dialog(context, R.style.RoundedDialog);
        feedbackDialog.setContentView(R.layout.feedback_dialog);

        ImageView feedbackIcon = feedbackDialog.findViewById(R.id.image_feedback_icon);
        TextView feedbackMessage = feedbackDialog.findViewById(R.id.text_feedback_message);

        if (isCorrect) {
            feedbackIcon.setImageResource(R.drawable.green_checkmark_line_icon);
            feedbackMessage.setText("This is a correct answer!");
        } else {
            feedbackIcon.setImageResource(R.drawable.red_x_line_icon);
            String answerText = isSwapMode ? word.getEnglish() : word.getVietnamese();
            feedbackMessage.setText("Incorrect! The correct answer is: " + answerText);
        }

        feedbackDialog.show();

        // Đóng dialog sau 2 giây và chuyển câu
        new android.os.Handler().postDelayed(() -> {
            feedbackDialog.dismiss();
            moveToNextQuestion(currentPosition);
        }, 3000);
    }

    private void moveToNextQuestion(int currentPosition) {
        if (currentPosition < words.size() - 1) {
            viewPager2.setCurrentItem(currentPosition + 1);
        } else {
            // Xử lý kết thúc bài test
            // Chuyển đến Activity kết quả với tổng điểm
            updateScoreToFirebase();
        }
    }

    public void updateScoreToFirebase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentTopicID = topic.getId();
        DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("Topic");
        DatabaseReference participantsRef = topicRef.child(currentTopicID).child("participant");
        participantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Participant participant = childSnapshot.getValue(Participant.class);
                    if (participant != null && participant.getUserID().equals(user.getUid())) {
                        int currentScore = participant.getFillWordResult();
                        int newScore = totalScore;
                        // so sanh voi diem hien tai tren firebase
                        if (newScore > currentScore) {
                            // Cập nhật điểm số mới trên Firebase
                            childSnapshot.getRef().child("fillWordResult").setValue(newScore)
                                    .addOnSuccessListener(unused -> {
                                        Intent intent = new Intent(context, FillWordsTestResult.class);
                                        intent.putParcelableArrayListExtra("words",words);
                                        intent.putParcelableArrayListExtra("testResults", testResults);
                                        intent.putExtra("totalScore", totalScore);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                    });
                        } else {
                            // ko cap nhat
                            Intent intent = new Intent(context, FillWordsTestResult.class);
                            intent.putParcelableArrayListExtra("words",words);
                            intent.putParcelableArrayListExtra("testResults", testResults);
                            intent.putExtra("totalScore", totalScore);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // never ignore errors
            }
        });

    }
    @Override
    public int getItemCount() {
        if(words != null){
            return words.size();
        }
        return 0;    }

    public class WordListViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_question, txt_description, txtTopicTitle;
        public ImageView soundIcon;

        public EditText edt_answer;

        public Button btn_submit, btn_skip;
        public WordListViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_question = itemView.findViewById(R.id.txt_question);
            txt_description = itemView.findViewById(R.id.txt_description);
            txtTopicTitle = itemView.findViewById(R.id.txtTopicTitle);
            edt_answer  = itemView.findViewById(R.id.edt_answer);
            soundIcon = itemView.findViewById(R.id.soundIcon);
            btn_submit = itemView.findViewById(R.id.btn_submit);
            btn_skip = itemView.findViewById(R.id.btn_skip);
        }
    }
}
