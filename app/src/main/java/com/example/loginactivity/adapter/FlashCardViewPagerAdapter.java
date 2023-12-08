package com.example.loginactivity.adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginactivity.R;
import com.example.loginactivity.TextToSpeechHelper;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;
import java.util.Locale;

public class FlashCardViewPagerAdapter extends  RecyclerView.Adapter<FlashCardViewPagerAdapter.FlashCardViewHolder>{

    public ArrayList<Word> words;
    Context context;
    public TextToSpeechHelper textToSpeechHelper;

    public ViewPager2 viewPager2;


    public FlashCardViewPagerAdapter(Context context, ArrayList<Word> words, ViewPager2 viewPager2) {
        this.words = words;
        this.context = context;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public FlashCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.flashcard, parent, false);
        return new FlashCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashCardViewHolder holder, int position) {
        Word word = words.get(position);
        if(word == null){
            return;
        }
        textToSpeechHelper = new TextToSpeechHelper(context);

        holder.term.setText(word.getEnglish());
        holder.meaning.setText(word.getVietnamese());

        holder.flipInterface.setOnClickListener(view -> {
            holder.cardFront.animate().rotationYBy(180).setDuration(300);
            holder.cardBack.animate().rotationYBy(180).setDuration(300);

            // Đảo chiều hiển thị của ViewFlipper để hiển thị mặt khác của card
            holder.flipInterface.showNext();

        });

        holder.soundFront.setOnClickListener(view -> {
            textToSpeechHelper.setLanguage(Locale.ENGLISH);
            textToSpeechHelper.speak(word.getEnglish());
        });

        holder.soundBack.setOnClickListener(view -> {
            textToSpeechHelper.setLanguage(new Locale("vi", "VN"));
            textToSpeechHelper.speak(word.getVietnamese());
        });

    }

    @Override
    public int getItemCount() {
        if(words != null){
            return words.size();
        }
        return 0;
    }

    // Phương thức để lấy ViewHolder tại vị trí cụ thể
    public FlashCardViewHolder getViewHolderAtPosition(int position) {
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        FlashCardViewPagerAdapter.FlashCardViewHolder viewHolder = (FlashCardViewPagerAdapter.FlashCardViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder instanceof FlashCardViewHolder) {
            return (FlashCardViewHolder) viewHolder;
        }
        return null;
    }

    public class FlashCardViewHolder extends RecyclerView.ViewHolder{

        public ViewFlipper flipInterface;
        public TextView term, meaning;
        public CardView cardFront, cardBack;

        public ImageView soundFront, soundBack;
        public FlashCardViewHolder(@NonNull View itemView) {
            super(itemView);
            term = itemView.findViewById(R.id.txt_term);
            meaning = itemView.findViewById(R.id.txt_meaning);
            flipInterface = itemView.findViewById(R.id.flashCard);
            cardBack = itemView.findViewById(R.id.cardBack);
            cardFront = itemView.findViewById(R.id.cardFront);
            soundBack = itemView.findViewById(R.id.soundIconBack);
            soundFront = itemView.findViewById(R.id.soundIconFront);
        }
    }
}
