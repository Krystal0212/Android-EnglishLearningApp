package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.loginactivity.adapter.AdapterWordList;
import com.example.loginactivity.adapter.AdapterWordListTopic;
import com.example.loginactivity.adapter.FlashCardViewPagerAdapter;
import com.example.loginactivity.models.Topic;
import com.example.loginactivity.models.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class TopicDetailActivity extends AppCompatActivity {
    public ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;

    Button btn_Flashcard;

    Topic topic;

    ArrayList<Word> words = new ArrayList<>();

    FlashCardViewPagerAdapter adapter;

    AdapterWordListTopic adapterWordListTopic;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        initUI();
        initListener();
    }

    private void initUI() {
        btn_Flashcard = findViewById(R.id.btn_FLashcard);
        Intent intent = getIntent();
        topic = intent.getParcelableExtra("topic");
        words = topic.getWord();

        viewPager2 = findViewById(R.id.viewPager2);
        circleIndicator3 = findViewById(R.id.circle_indicator_3);

        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        viewPager2.setPageTransformer(compositePageTransformer);

        adapter = new FlashCardViewPagerAdapter(this, words, viewPager2);
        viewPager2.setAdapter(adapter);

        circleIndicator3.setViewPager(viewPager2);
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());

        //init recycler view trong topic
        recyclerView = findViewById(R.id.recyclerView);

        adapterWordListTopic = new AdapterWordListTopic(this, words);
        recyclerView.setAdapter(adapterWordListTopic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListener() {
        btn_Flashcard.setOnClickListener(v -> {
            ArrayList<Word> markedWords = adapterWordListTopic.markedWords;
            if(markedWords.isEmpty()) {
                // khong co word nao trong danh sach marked
                Intent intent = new Intent(this, FlashCardActivity.class);
                intent.putParcelableArrayListExtra("words", topic.getWord());
                startActivity(intent);
            } else {
                //co it nhat 1 word
                // tien hanh hỏi
                final Dialog dialog = new Dialog(TopicDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.flashcard_mode_dialog);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView txt_message = dialog.findViewById(R.id.txt_message);
                txt_message.setText("Learn all words or learn your " + markedWords.size() + " marked words ?");

                Button btn_all = dialog.findViewById(R.id.btn_all);
                Button btn_marked = dialog.findViewById(R.id.btn_marked);

                btn_all.setOnClickListener(view -> {
                    dialog.dismiss();
                    Intent intent = new Intent(this, FlashCardActivity.class);
                    intent.putExtra("words", topic.getWord());
                    startActivity(intent);
                });

                btn_marked.setOnClickListener(view -> {
                    dialog.dismiss();
                    Intent intent = new Intent(this, FlashCardActivity.class);
                    intent.putExtra("words", markedWords);
                    startActivity(intent);
                });

                dialog.show();
            }

        });
    }
}