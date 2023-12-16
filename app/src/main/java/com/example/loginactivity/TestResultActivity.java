package com.example.loginactivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.adapter.TestResultFillWordsAdapter;
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

        if (incorrectlyAnsweredQuestions.size() == questions.size()) {
            txtResult.setText("Completed 0/" + questions.size());

        } else if (incorrectlyAnsweredQuestions.size() >= 0){
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

    private void showResult(){
        final Dialog resultDialog = new Dialog(this,  R.style.RoundedDialog);
        resultDialog.setContentView(R.layout.layout_test_result_dialog);

        // Set chiều rộng cho dialog bằng cách sử dụng layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(resultDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Áp dụng các thay đổi về layout
        resultDialog.getWindow().setAttributes(layoutParams);

        RecyclerView recyclerViewTestResults = resultDialog.findViewById(R.id.recyclerViewTestResults);
        Button btnClose = resultDialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> resultDialog.dismiss());

        recyclerViewTestResults.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewTestResults.setAdapter(new TestResultFillWordsAdapter(testResult));

        resultDialog.show();
    }
}
