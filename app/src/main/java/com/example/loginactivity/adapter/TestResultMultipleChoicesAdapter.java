package com.example.loginactivity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.models.TestResultMultipleChoices;

import java.util.ArrayList;

public class TestResultMultipleChoicesAdapter extends RecyclerView.Adapter<TestResultMultipleChoicesAdapter.TestResult2ViewHolder>{

    private final ArrayList<TestResultMultipleChoices> testResults;

    public TestResultMultipleChoicesAdapter(ArrayList<TestResultMultipleChoices> testResults) {
        this.testResults = testResults;
    }

    @NonNull
    @Override
    public TestResult2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_result, parent, false);
        return new TestResult2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestResult2ViewHolder holder, int position) {
        TestResultMultipleChoices result = testResults.get(position);
        holder.bind(result);
    }

    @Override
    public int getItemCount() {
        return testResults.size();
    }

    static class TestResult2ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtQuestion;
        private final TextView txtCorrectAnswer;
        private final ImageView imgResult;

        public TestResult2ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            txtCorrectAnswer = itemView.findViewById(R.id.txtCorrectAnswer);
            imgResult = itemView.findViewById(R.id.imgResult);
        }

        void bind(TestResultMultipleChoices result) {
            txtQuestion.setText(result.getWord().getEnglish()); // or getVietnamese based on your need
            txtCorrectAnswer.setText(result.getWord().getVietnamese()); // or getEnglish
            imgResult.setImageResource(result.isCorrect() ? R.drawable.green_checkmark_line_icon : R.drawable.red_x_line_icon);
        }
    }
}
