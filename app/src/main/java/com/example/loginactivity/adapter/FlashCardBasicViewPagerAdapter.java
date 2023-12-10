package com.example.loginactivity.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class FlashCardBasicViewPagerAdapter extends  RecyclerView.Adapter<FlashCardBasicViewPagerAdapter.FlashCardViewHolder>{

    public ArrayList<Word> words;
    Context context;
    public TextToSpeechHelper textToSpeechHelper;
    private boolean isLastPage = false;
    private boolean isFirstPage = true;

    public ViewPager2 viewPager2;


    public FlashCardBasicViewPagerAdapter(Context context, ArrayList<Word> words, ViewPager2 viewPager2) {
        this.words = words;
        this.context = context;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public FlashCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_flashcard_one, parent, false);
        return new FlashCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashCardViewHolder holder, int position) {
        Word word = words.get(position);
        isLastPage = (position == getItemCount() - 1);
        isFirstPage = (position == 0);

        // Lay style cua cac khung
        int styleResourceId = R.style.groupStyleyellow_600_75cornerRadius;
        TypedArray attributes = context.obtainStyledAttributes(styleResourceId, new int[]{android.R.attr.background});
        int backgroundResourceId = attributes.getResourceId(0, 0);

        int styleResourceId2 = R.style.groupStyleyellow_600cornerRadius;
        TypedArray attributes2 = context.obtainStyledAttributes(styleResourceId2, new int[]{android.R.attr.background});
        int backgroundResourceId2 = attributes2.getResourceId(0, 0);

        attributes.recycle();

        if(word == null){
            return;
        }
        textToSpeechHelper = new TextToSpeechHelper(context);

        holder.term.setText(word.getEnglish());
        holder.meaning.setText(word.getVietnamese());

        if(isFirstPage){
            holder.llPreviousBack.setBackgroundResource(backgroundResourceId);
            holder.llPreviousFront.setBackgroundResource(backgroundResourceId);
        }
        else{
            holder.llPreviousBack.setBackgroundResource(backgroundResourceId2);
            holder.llPreviousFront.setBackgroundResource(backgroundResourceId2);
        }

        if(isLastPage){
            holder.llNextBack.setBackgroundResource(backgroundResourceId);
            holder.llNextFront.setBackgroundResource(backgroundResourceId);
        }else {
            holder.llNextBack.setBackgroundResource(backgroundResourceId2);
            holder.llNextFront.setBackgroundResource(backgroundResourceId2);
        }

        holder.flipInterface.setOnClickListener(view -> {
                    // Kiểm tra nếu thẻ không đang lật
                    if (!holder.isFlipping) {
                        holder.isFlipping = true; // Đánh dấu bắt đầu lật thẻ

                        holder.cardFront.animate().rotationYBy(180).setDuration(300);
                        holder.cardBack.animate().rotationYBy(180).setDuration(300).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                holder.flipInterface.showNext(); // Hiển thị mặt khác của thẻ
                                holder.isFlipping = false; // Đánh dấu kết thúc lật thẻ
                            }
                        });
                    }
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

    // Phương thức lật thẻ về mặt trước
    public void flipCardToFront(int position) {
        FlashCardViewHolder viewHolder = getViewHolderAtPosition(position);
        if (viewHolder != null && viewHolder.flipInterface.getDisplayedChild() != 0) {
            viewHolder.cardFront.setRotationY(0);
            viewHolder.cardBack.setRotationY(180);
            viewHolder.flipInterface.showPrevious();
        }
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
        FlashCardBasicViewPagerAdapter.FlashCardViewHolder viewHolder = (FlashCardBasicViewPagerAdapter.FlashCardViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder instanceof FlashCardViewHolder) {
            return (FlashCardViewHolder) viewHolder;
        }
        return null;
    }

    public class FlashCardViewHolder extends RecyclerView.ViewHolder{

        public ViewFlipper flipInterface;
        public TextView term, meaning;
        public CardView cardFront, cardBack;
        LinearLayout llPreviousFront, llPreviousBack, llNextFront, llNextBack;

        public ImageView soundFront, soundBack;
        public boolean isFlipping = false; // Biến theo dõi trạng thái lật thẻ
        public FlashCardViewHolder(@NonNull View itemView) {
            super(itemView);
            term = itemView.findViewById(R.id.txt_term);
            meaning = itemView.findViewById(R.id.txt_meaning);
            flipInterface = itemView.findViewById(R.id.flashCard);
            cardBack = itemView.findViewById(R.id.cardBack);
            cardFront = itemView.findViewById(R.id.cardFront);
            soundBack = itemView.findViewById(R.id.soundIconBack);
            soundFront = itemView.findViewById(R.id.soundIconFront);
            llPreviousFront = itemView.findViewById(R.id.llPreviousFront);
            llPreviousBack = itemView.findViewById(R.id.llPreviousBack);
            llNextBack = itemView.findViewById(R.id.llNextBack);
            llNextFront = itemView.findViewById(R.id.llNextFront);
        }

        // Phương thức kiểm tra và lật thẻ
        public void flipToFront() {
            if (flipInterface.getDisplayedChild() != 0) {
                // Cập nhật góc xoay
                cardFront.setRotationY(0);
                cardBack.setRotationY(180);
                flipInterface.showPrevious();
            }
        }
    }
}
