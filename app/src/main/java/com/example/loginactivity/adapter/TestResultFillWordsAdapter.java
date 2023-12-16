package com.example.loginactivity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.models.TestResultFillWords;

import java.util.ArrayList;

public class TestResultFillWordsAdapter extends RecyclerView.Adapter<TestResultFillWordsAdapter.TestResultViewHolder>{
    private final ArrayList<TestResultFillWords> testResults;

    public TestResultFillWordsAdapter(ArrayList<TestResultFillWords> testResults) {
        this.testResults = testResults;
    }

    @NonNull
    @Override
    public TestResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_result, parent, false);
        return new TestResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestResultViewHolder holder, int position) {
        TestResultFillWords result = testResults.get(position);
        holder.bind(result);
    }

    @Override
    public int getItemCount() {
        return testResults.size();
    }

    static class TestResultViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtQuestion;
        private final TextView txtUserAnswer;
        private final TextView txtCorrectAnswer;
        private final ImageView imgResult;

        public TestResultViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            txtUserAnswer = itemView.findViewById(R.id.txtUserAnswer);
            txtCorrectAnswer = itemView.findViewById(R.id.txtCorrectAnswer);
            imgResult = itemView.findViewById(R.id.imgResult);
        }

        void bind(TestResultFillWords result) {
            txtQuestion.setText(result.getWord().getEnglish());
            txtUserAnswer.setText(result.getUserAnswer());
            txtCorrectAnswer.setText(result.getWord().getVietnamese());
            imgResult.setImageResource(result.isCorrect() ? R.drawable.green_checkmark_line_icon : R.drawable.red_x_line_icon);
        }
    }
}
