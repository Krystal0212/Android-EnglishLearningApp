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
    ArrayList<Word> answeredQuestions;
    ViewPager2 viewPager2;
    ArrayList<Question> questions = new ArrayList<>();
    TextView status;
    ArrayList<Question> incorrectlyAnsweredQuestions = new ArrayList<>();


    private boolean isLastPage = false;
    private boolean isFirstPage = true;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // chuyển trang
            if(viewPager2.getCurrentItem() == words.size() - 1){
                Intent intent = new Intent(TestVocabularyActivity.this, TestResultActivity.class);
                incorrectlyAnsweredQuestions = adapter.getIncorrectlyAnsweredQuestions();
                intent.putParcelableArrayListExtra("incorrectlyAnsweredQuestions", incorrectlyAnsweredQuestions);
                intent.putParcelableArrayListExtra("questions",questions);
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

    public void initSetUp(){
        Intent intent = getIntent();
        words = intent.getParcelableArrayListExtra("words");

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
                    progressBar.setProgress(progressBar.getProgress()+ratio);
            }
        };

        viewPager2.registerOnPageChangeCallback(UpdatePage);
    }

    public void createAndSetQuestions(){

        for(Word word : words){
            String[] otherVietnameseWords = getThreeDifferentWords(word.getVietnamese());
            questions.add(new Question(word, otherVietnameseWords));
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

    private String[] getThreeDifferentWords(String currentWord) {
        String[] otherWords = new String[3];

        ArrayList<String> allVietnameseWords = new ArrayList<>();

        for(Word word : words){
            allVietnameseWords.add(word.getVietnamese());
        }

        allVietnameseWords.remove(currentWord);

        Collections.shuffle(allVietnameseWords);

        for (int i = 0; i < 3; i++) {
            otherWords[i] = allVietnameseWords.get(i);
        }

        return otherWords;
    }
}
