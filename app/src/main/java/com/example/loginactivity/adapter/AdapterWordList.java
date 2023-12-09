package com.example.loginactivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;

public class AdapterWordList extends RecyclerView.Adapter<AdapterWordList.MyViewHolder> {
    Context context;
    public ArrayList<Word> words;

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
        Word word = words.get(position);
        holder.txt_english.setText(word.getEnglish());
        holder.txt_vietnamese.setText(word.getVietnamese());
        if(word.getDescription().equals("")){
            holder.txt_description.setText("No description");
        } else {
            holder.txt_description.setText(word.getDescription());
        }

        holder.itemView.setOnClickListener(v -> {
            onClickWord();
        });

        //xu ly xoa word trong list
        holder.imageTrash.setOnClickListener(v -> {
            for(int i = 0; i < words.size(); i++){
                if(word.getEnglish().equals(words.get(i).getEnglish())){
                    words.remove(words.get(i));
                    notifyDataSetChanged();
                }
            }
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

        ImageView imageTrash;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_english = itemView.findViewById(R.id.txt_english);
            txt_vietnamese = itemView.findViewById(R.id.txt_vietnamese);
            txt_description = itemView.findViewById(R.id.txt_description);
            imageTrash = itemView.findViewById(R.id.imageTrash);
        }
    }
}
