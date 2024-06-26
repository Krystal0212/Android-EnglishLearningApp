package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.loginactivity.models.Question;

import java.util.ArrayList;

public class FlashCardResult extends AppCompatActivity {
    ArrayList<Question> incorrectlyAnsweredQuestions = new ArrayList<>();
    Button btn_Topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_complete_learning);

        initUI();
        initListener();
    }

    private void initUI() {
        btn_Topic = findViewById(R.id.btn_back_Topic);
    }

    private void initListener() {
        btn_Topic.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }
}