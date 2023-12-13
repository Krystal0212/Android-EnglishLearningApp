package com.example.loginactivity.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginactivity.R;
import com.example.loginactivity.TextToSpeechHelper;
import com.example.loginactivity.models.Question;

import java.util.ArrayList;
import java.util.Locale;

public class AdapterWordListTest extends RecyclerView.Adapter<AdapterWordListTest.WordListViewHolder> {

    Context context;
    ArrayList<Question> questions;
    public TextToSpeechHelper textToSpeechEnglishHelper, textToSpeechVietnameseHelper;
    ViewPager2 viewPager2;
    private OnAnswerClickListener onAnswerClickListener;

    private Handler mHandler = new Handler();

    ArrayList<Question> incorrectlyAnsweredQuestions = new ArrayList<>();

    public interface OnAnswerClickListener {
        void onAnswerClick(boolean isClicked);
    }

    public void setOnAnswerClickListener(OnAnswerClickListener listener) {
        this.onAnswerClickListener = listener;
    }

    public AdapterWordListTest(Context context, ArrayList<Question> questions, ViewPager2 viewPager2) {
        this.context = context;
        this.questions = questions;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public WordListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_word_test, parent, false);
        return new AdapterWordListTest.WordListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListViewHolder holder, int position) {
        Question question = questions.get(position);

        if (question == null) {
            return;
        }

        textToSpeechEnglishHelper = new TextToSpeechHelper(context);
        textToSpeechVietnameseHelper = new TextToSpeechHelper(context);

        holder.term.setText(question.getEnglish());
        holder.description.setText(question.getDescription());
        String rightAnswer = question.getVietnamese();

        String[] options = question.getAnswerOptions();
        int rightPosition = question.getRightAnswerPosition();
        holder.button1.setText(options[0]);
        holder.button2.setText(options[1]);
        holder.button3.setText(options[2]);
        holder.button4.setText(options[3]);

        AppCompatButton rightButton = null;

        switch (rightPosition) {
            case 0:
                rightButton = holder.button1;
                break;
            case 1:
                rightButton = holder.button2;
                break;
            case 2:
                rightButton = holder.button3;
                break;
            case 3:
                rightButton = holder.button4;
                break;
            default:
                break;
        };

        AppCompatButton finalRightButton = rightButton;
        holder.button1.setOnClickListener(v -> {
            checkAnswer(holder.button1, finalRightButton, question);
            disableAllButtons(holder);
        });

        holder.button2.setOnClickListener(v -> {
            checkAnswer(holder.button2, finalRightButton, question);
            disableAllButtons(holder);
        });

        holder.button3.setOnClickListener(v -> {
            checkAnswer(holder.button3, finalRightButton, question);
            disableAllButtons(holder);
        });

        holder.button4.setOnClickListener(v -> {
            checkAnswer(holder.button4, finalRightButton, question);
            disableAllButtons(holder);
        });

        holder.sound.setOnClickListener(view -> {
            textToSpeechEnglishHelper.setLanguage(Locale.ENGLISH);
            textToSpeechEnglishHelper.speak(question.getEnglish());
        });
    }

    private void checkAnswer(AppCompatButton buttonChoosen, AppCompatButton rightButton, Question question) {


        int styleResourceId = R.style.btnSolidRoundedOutline_Right;
        TypedArray attributes = context.obtainStyledAttributes(styleResourceId, new int[]{android.R.attr.background});
        int rightResourceId = attributes.getResourceId(0, 0);

        int styleResourceId2 = R.style.btnSolidRoundedOutline_Wrong;
        TypedArray attributes2 = context.obtainStyledAttributes(styleResourceId2, new int[]{android.R.attr.background});
        int wrongResourceId = attributes2.getResourceId(0, 0);

        attributes.recycle();
        attributes2.recycle();

        if (buttonChoosen.getText().equals(rightButton.getText())) {
            buttonChoosen.setBackgroundResource(rightResourceId);

            textToSpeechEnglishHelper.setLanguage(Locale.ENGLISH);
            textToSpeechEnglishHelper.speak("Right");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textToSpeechEnglishHelper.speak(question.getEnglish());
                    textToSpeechVietnameseHelper.setLanguage(new Locale("vi", "VN"));
                    textToSpeechVietnameseHelper.speak((String) rightButton.getText());
                }
            }, 1000);

            if (onAnswerClickListener != null) {
                onAnswerClickListener.onAnswerClick(true);
            }
        } else {
            buttonChoosen.setBackgroundResource(wrongResourceId);

            rightButton.setBackgroundResource(rightResourceId);

            textToSpeechEnglishHelper.setLanguage(Locale.ENGLISH);
            textToSpeechEnglishHelper.speak("Wrong");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textToSpeechEnglishHelper.speak(question.getEnglish());
                    textToSpeechVietnameseHelper.setLanguage(new Locale("vi", "VN"));
                    textToSpeechVietnameseHelper.speak((String) rightButton.getText());
                }
            }, 1000);

            incorrectlyAnsweredQuestions.add(question);

            if (onAnswerClickListener != null) {
                onAnswerClickListener.onAnswerClick(true);
            }
        }
    }

    public Question getCurrentQuestion(int position) {
        if (position >= 0 && position < questions.size()) {
            return questions.get(position);
        }
        return null;
    }

    public void disableAllButtons(WordListViewHolder holder) {
        holder.button1.setEnabled(false);
        holder.button2.setEnabled(false);
        holder.button3.setEnabled(false);
        holder.button4.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        if (questions != null) {
            return questions.size();
        }
        return 0;
    }

    public ArrayList<Question> getIncorrectlyAnsweredQuestions() {
        return incorrectlyAnsweredQuestions;
    }

    public void setIncorrectlyAnsweredQuestions(ArrayList<Question> incorrectlyAnsweredQuestions) {
        this.incorrectlyAnsweredQuestions = incorrectlyAnsweredQuestions;
    }

    public class WordListViewHolder extends RecyclerView.ViewHolder {
        public TextView term, description;
        public LinearLayout frameTest;
        public ImageView sound;

        public AppCompatButton button1, button2, button3, button4;

        public WordListViewHolder(@NonNull View itemView) {
            super(itemView);
            term = itemView.findViewById(R.id.txtTerm);
            description = itemView.findViewById(R.id.txtDescription);
            frameTest = itemView.findViewById(R.id.frameTest);
            button1 = itemView.findViewById(R.id.btn1);
            button2 = itemView.findViewById(R.id.btn2);
            button3 = itemView.findViewById(R.id.btn3);
            button4 = itemView.findViewById(R.id.btn4);
            sound = itemView.findViewById(R.id.imgSound);
        }


    }

}
