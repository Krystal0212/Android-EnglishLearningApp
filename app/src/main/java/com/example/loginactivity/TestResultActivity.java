package com.example.loginactivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.adapter.TestResultMultipleChoicesAdapter;
import com.example.loginactivity.models.Question;
import com.example.loginactivity.models.TestResultMultipleChoices;

import java.util.ArrayList;

public class TestResultActivity extends AppCompatActivity {
    ArrayList<Question> incorrectlyAnsweredQuestions = new ArrayList<>();
    ArrayList<Question> questions = new ArrayList<>();
    ArrayList<TestResultMultipleChoices> testResults = new ArrayList<>();
    Button btn_Topic, btn_showTestResults;
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
        btn_showTestResults = findViewById(R.id.btn_showTestResults);

        Intent intent = getIntent();
        incorrectlyAnsweredQuestions = intent.getParcelableArrayListExtra("incorrectlyAnsweredQuestions");
        questions = intent.getParcelableArrayListExtra("questions_mcq");


            for (Question question : questions) {
                // Check if the question is in the incorrectly answered list
                boolean isCorrect = true;
                for(Question wrongQuestion : incorrectlyAnsweredQuestions){
                        if(wrongQuestion.getWord().getEnglish().equals(question.getWord().getEnglish())) {
                            isCorrect = false;
                            break;
                        }
                }
                testResults.add(new TestResultMultipleChoices(question.getWord(), isCorrect));
            }

//        for(int i = 0; i < questions.size(); i++){
//            Log.d("question", questions.get(i).toString());
//        }
//
////        for(Question question : questions){
////            Log.d("question", question.toString());
////        }


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
        btn_showTestResults.setOnClickListener(v->{
            showResult();
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
        recyclerViewTestResults.setAdapter(new TestResultMultipleChoicesAdapter(testResults));

        resultDialog.show();
    }
}
