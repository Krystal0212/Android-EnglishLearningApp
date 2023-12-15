package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginactivity.models.Question;

import java.util.ArrayList;

public class TestResultActivity extends AppCompatActivity {
    ArrayList<Question> incorrectlyAnsweredQuestions = new ArrayList<>();
    ArrayList<Question> questions = new ArrayList<>();
    Button btn_Topic;
    TextView txtResult, txtPoint;
    private static int point = 500;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_complete_testing);

        initUI();
        initListener();
    }

    private void initUI() {
        btn_Topic = findViewById(R.id.btn_back_Topic);
        txtResult = findViewById(R.id.txtResult);
        txtPoint = findViewById(R.id.txtSentenceRight);

        Intent intent = getIntent();
        incorrectlyAnsweredQuestions = intent.getParcelableArrayListExtra("incorrectlyAnsweredQuestions");
        questions = intent.getParcelableArrayListExtra("questions");

        if (incorrectlyAnsweredQuestions.size() >= 0) {
            int rightNumber = questions.size() - incorrectlyAnsweredQuestions.size();
            txtPoint.setText("Your point is " + rightNumber * point + " !!!");
            txtResult.setText("Completed " + rightNumber + "/" + questions.size());
        }
    }

    private void initListener() {
        btn_Topic.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }
}
