package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.loginactivity.adapter.AdapterFillWords;
import com.example.loginactivity.adapter.FlashCardLongViewPagerAdapter;
import com.example.loginactivity.models.Topic;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FillWordsTestActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    AdapterFillWords adapter;
    boolean shuffle, swap, feedback;
    ArrayList<Word> words = new ArrayList<>();
    String topicTitle;
    ImageView btnBack;
    ProgressBar progressBar;
    TextView status;
    Topic topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_words_test);

        initUI();
        initListener();
    }

    private void initUI() {
        viewPager2 = findViewById(R.id.viewPager2);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBarComponent);
        status = findViewById(R.id.status);

        shuffle = getIntent().getBooleanExtra("shuffle", false);
        feedback = getIntent().getBooleanExtra("feedback", false);
        swap = getIntent().getBooleanExtra("swap", false);
        words = getIntent().getParcelableArrayListExtra("words");
        topicTitle = getIntent().getStringExtra("title");
        topic = getIntent().getParcelableExtra("topic");

        //Setting viewpager2
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        viewPager2.setPageTransformer(compositePageTransformer);

        //setting adapter
        if(shuffle == true){
            Collections.shuffle(words);
        }
        adapter = new AdapterFillWords(this, words, topicTitle, feedback, swap, viewPager2, topic);
        viewPager2.setAdapter(adapter);
        viewPager2.setUserInputEnabled(false);

        viewPager2.setPageTransformer(new ZoomOutPageTransformer());
    }

    private void initListener() {
        btnBack.setOnClickListener(v -> {
            onBackPressed();
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

}