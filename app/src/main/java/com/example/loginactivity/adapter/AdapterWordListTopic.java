package com.example.loginactivity.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.TextToSpeechHelper;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;
import java.util.Locale;

public class AdapterWordListTopic extends RecyclerView.Adapter<AdapterWordListTopic.MyViewHolder> {
    Context context;
    ArrayList<Word> words;
    public TextToSpeechHelper textToSpeechHelper;
    public ArrayList<Word> markedWords = new ArrayList<>();

    public AdapterWordListTopic(Context context, ArrayList<Word> words) {
        //constructor
        this.context = context;
        this.words = words;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_term_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Word word = words.get(position);
        if(word == null){
            return;
        }

        holder.txt_english.setText(words.get(position).getEnglish());
        holder.txt_vietnamese.setText(words.get(position).getVietnamese());

        holder.itemView.setOnClickListener(v -> {
            onClickWord();
        });

        textToSpeechHelper = new TextToSpeechHelper(context);
        Handler mHandler = new Handler();

        holder.soundIcon.setOnClickListener(v -> {
            textToSpeechHelper.setLanguage(Locale.ENGLISH);
            textToSpeechHelper.speak(word.getEnglish());

            mHandler.postDelayed(() -> {
                textToSpeechHelper.setLanguage(new Locale("vi", "VN"));
                textToSpeechHelper.speak(word.getVietnamese());
            }, 500); // Thời gian chờ giữa hai bước
        });

        holder.marker.setOnClickListener(v -> {
            if(holder.markerChecked){
                //marker đang được check
                holder.marker.setImageResource(R.drawable.baseline_star_24);
                markedWords.add(word);
            } else {
                //marker de-check
                holder.marker.setImageResource(R.drawable.baseline_star_border_24);
                for(int i = 0; i < markedWords.size(); i++){
                    if(word.getEnglish().equals(markedWords.get(i).getEnglish())){
                        markedWords.remove(markedWords.get(i));
                    }
                }
            }
            holder.markerChecked = !holder.markerChecked; // đảo state
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
        TextView txt_english, txt_vietnamese;
        ImageView marker, soundIcon;
        private boolean markerChecked = true;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_english = itemView.findViewById(R.id.txt_term);
            txt_vietnamese = itemView.findViewById(R.id.txt_meaning);
            soundIcon = itemView.findViewById(R.id.soundIcon);
            marker = itemView.findViewById(R.id.marker);
        }
    }
}