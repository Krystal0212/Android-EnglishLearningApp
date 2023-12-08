package com.example.loginactivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;

public class AdapterWordList extends RecyclerView.Adapter<AdapterWordList.MyViewHolder> {
    Context context;
    ArrayList<Word> words;

    public AdapterWordList(Context context, ArrayList<Word> words) {
        //constructor
        this.context = context;
        this.words = words;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_topic_make_new_word, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_english.setText(words.get(position).getEnglish());
        holder.txt_vietnamese.setText(words.get(position).getVietnamese());
        if(words.get(position).getDescription().equals("")){
            holder.txt_description.setText("No description");
        } else {
            holder.txt_description.setText(words.get(position).getDescription());
        }

        holder.itemView.setOnClickListener(v -> {
            onClickWord();
        });
    }

    private void onClickWord() {
    }

    @Override
    public int getItemCount() {
        //count
        return words.size();
    }

    public void newWordList(ArrayList<Word> newList) {
        words = newList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_english, txt_vietnamese, txt_description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_english = itemView.findViewById(R.id.txt_english);
            txt_vietnamese = itemView.findViewById(R.id.txt_vietnamese);
            txt_description = itemView.findViewById(R.id.txt_description);
        }
    }
}
