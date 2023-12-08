package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.example.loginactivity.adapter.FlashCardViewPagerAdapter;
import com.example.loginactivity.models.Topic;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class TopicDetailActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;

    Topic topic;

    ArrayList<Word> words = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        initUI();
    }

    private void initUI() {
        viewPager2 = findViewById(R.id.viewPager2);
        circleIndicator3 = findViewById(R.id.circle_indicator_3);

        Intent intent = getIntent();
        topic = intent.getParcelableExtra("topic");
        words = topic.getWord();

        FlashCardViewPagerAdapter adapter = new FlashCardViewPagerAdapter(this, words);
        viewPager2.setAdapter(adapter);

        circleIndicator3.setViewPager(viewPager2);

    }
}