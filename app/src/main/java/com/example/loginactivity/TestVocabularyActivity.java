package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginactivity.adapter.AdapterWordListTest;
import com.example.loginactivity.models.Question;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestVocabularyActivity extends AppCompatActivity {
    ImageButton btn_back;
    AdapterWordListTest adapter;
    ProgressBar progressBar;
    ArrayList<Word> words = new ArrayList<>();
    ArrayList<Word> marked_words = new ArrayList<>();
    ArrayList<Word> answeredQuestions;
    ViewPager2 viewPager2;
    ArrayList<Question> questions = new ArrayList<>();
    TextView status;
    ArrayList<Question> incorrectlyAnsweredQuestions = new ArrayList<>();
    String testMode;

    private boolean isLastPage = false;
    private boolean isFirstPage = true;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // chuyển trang
            if (viewPager2.getCurrentItem() == words.size() - 1) {
                Intent intent = new Intent(TestVocabularyActivity.this, TestResultActivity.class);
                incorrectlyAnsweredQuestions = adapter.getIncorrectlyAnsweredQuestions();
                intent.putParcelableArrayListExtra("incorrectlyAnsweredQuestions", incorrectlyAnsweredQuestions);
                intent.putParcelableArrayListExtra("questions", questions);
                startActivity(intent);
                finish();

            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initSetUp();
        createAndSetQuestions();
    }

    public void initSetUp() {
        Intent intent = getIntent();
        words = intent.getParcelableArrayListExtra("words");
        marked_words = intent.getParcelableArrayListExtra("marked_words");
        testMode = intent.getStringExtra("test_mode");

        viewPager2 = findViewById(R.id.viewPager2);
        btn_back = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBarComponent);
        status = findViewById(R.id.status);

        viewPager2.setUserInputEnabled(false);

        btn_back.setOnClickListener(v -> {
            finish();
        });

        progressBar.setProgress(0);

        ViewPager2.OnPageChangeCallback UpdatePage = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                String page = (position + 1) + " / " + adapter.getItemCount();
                status.setText(page);

                int ratio = Math.round(1 * 100 / (float) adapter.getItemCount());
                progressBar.setProgress(progressBar.getProgress() + ratio);
            }
        };

        viewPager2.registerOnPageChangeCallback(UpdatePage);
    }

    public void createAndSetQuestions() {

        if (marked_words != null) {
//            if (marked_words.size() <= 4){
                for (Word word : marked_words) {
                    String[] otherWords = getThreeDifferentWords(word);
                    questions.add(new Question(word, otherWords, testMode));
                }
//            }
        } else {
            for (Word word : words) {
                String[] otherWords = getThreeDifferentWords(word);
                questions.add(new Question(word, otherWords, testMode));
            }
        }

        adapter = new AdapterWordListTest(this, questions, viewPager2);
        adapter.setOnAnswerClickListener(isClicked -> {
            if (isClicked) {
                mHandler.postDelayed(mRunnable, 4000); // Delayed transition to the next page
            }
        });

        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setAdapter(adapter);
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());
    }

    private String[] getThreeDifferentWords(Word currentWord) {
        String[] otherWords = new String[3];

        ArrayList<String> allWords = new ArrayList<>();

        for (Word word : words) {
            if (testMode.equals("vietnamese")) {
                allWords.add(word.getEnglish());
            } else if (testMode.equals("english")) {
                allWords.add(word.getVietnamese());
            }
        }

        if (testMode.equals("vietnamese")) {
            allWords.remove(currentWord.getEnglish());
        } else if (testMode.equals("english")) {
            allWords.remove(currentWord.getVietnamese());
        }

        Collections.shuffle(allWords);

        for (int i = 0; i < 3; i++) {
            otherWords[i] = allWords.get(i);
        }

        return otherWords;
    }
}
