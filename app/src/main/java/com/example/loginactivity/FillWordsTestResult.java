package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginactivity.R;
import com.example.loginactivity.adapter.TestResultFillWordsAdapter;
import com.example.loginactivity.models.TestResultFillWords;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;

public class FillWordsTestResult extends AppCompatActivity {

    TextView txtResult, txtSentenceRight, txtCongrats;
    Button btn_back_Topic, btn_showTestResults;

    ArrayList<TestResultFillWords> testResult = new ArrayList<>();
    ArrayList<Word> words = new ArrayList<Word>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_words_test_result);

        initUI();
        initListener();
    }

    private void initUI() {
        btn_showTestResults = findViewById(R.id.btn_showTestResults);
        txtResult = findViewById(R.id.txtResult);
        txtCongrats = findViewById(R.id.txtCongrats);
        txtSentenceRight = findViewById(R.id.txtSentenceRight);
        btn_back_Topic = findViewById(R.id.btn_back_Topic);

        words = getIntent().getParcelableArrayListExtra("words");
        testResult = getIntent().getParcelableArrayListExtra("testResults");
        int totalScore = getIntent().getIntExtra("totalScore", 0);
        if(totalScore == 0){
            txtResult.setText("Completed 0/" + words.size());
            txtCongrats.setText("You failed!");
        } else {
            txtSentenceRight.setText("Your point is " + totalScore + " !!!");
            txtResult.setText("Completed " + totalScore / 500 + "/" + words.size());
        }
    }

    private void initListener() {
        btn_back_Topic.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        btn_showTestResults.setOnClickListener(v -> {
            showTestResults();

        });
    }

    private void showTestResults() {
        // Tạo và hiển thị Dialog hoặc mở Activity mới với kết quả
        // Ví dụ: Mở một Dialog
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
        recyclerViewTestResults.setAdapter(new TestResultFillWordsAdapter(testResult));

        resultDialog.show();
    }
}