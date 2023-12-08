package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.loginactivity.models.Topic;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;

public class TopicDetailActivity extends AppCompatActivity {

    Button btn_Flashcard;

    Topic topic;

    ArrayList<Word> words = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        initUI();
        initListener();
    }

    private void initUI() {
        btn_Flashcard = findViewById(R.id.btn_FLashcard);
        Intent intent = getIntent();
        topic = intent.getParcelableExtra("topic");
        words = topic.getWord();
    }

    private void initListener() {
        btn_Flashcard.setOnClickListener(v -> {
            Intent intent = new Intent(this, FlashCardActivity.class);
            intent.putExtra("topic", topic);
            startActivity(intent);
        });
    }
}