package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.loginactivity.adapter.FlashCardViewPagerAdapter;
import com.example.loginactivity.models.Topic;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class TopicDetailActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;

    Topic topic;

    ArrayList<Word> words = new ArrayList<>();

    TextView status;

    Switch switchButton;

    FlashCardViewPagerAdapter adapter;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(viewPager2.getCurrentItem() == words.size() - 1){
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        initUI();
        initListerner();
    }

    private void initUI() {
        viewPager2 = findViewById(R.id.viewPager2);
        circleIndicator3 = findViewById(R.id.circle_indicator_3);
        status = findViewById(R.id.starus);
        switchButton = findViewById(R.id.switchButton);

        Intent intent = getIntent();
        topic = intent.getParcelableExtra("topic");
        words = topic.getWord();

        //viet su kien thay doi status
        adapter = new FlashCardViewPagerAdapter(this, words);
        viewPager2.setAdapter(adapter);

        circleIndicator3.setViewPager(viewPager2);
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());
    }

    private void initListerner() {

        ViewPager2.OnPageChangeCallback UpdatePage = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                String page = (position + 1) + " / " + adapter.getItemCount();
                status.setText(page);
            }
        };

        viewPager2.registerOnPageChangeCallback(UpdatePage);
        ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (switchButton.isChecked()) {
                    String page = (position + 1) + " / " + adapter.getItemCount();
                    status.setText(page);
                    mHandler.removeCallbacks(mRunnable);
                    mHandler.postDelayed(mRunnable, 3000);
                }
            }
        };

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mHandler.removeCallbacks(mRunnable);
                    mHandler.postDelayed(mRunnable, 5000);
                    viewPager2.registerOnPageChangeCallback(onPageChangeCallback);
                } else {
                    // Tắt sự kiện khi Switch được tắt
                    viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback);
                    mHandler.removeCallbacks(mRunnable);  // Cần phải đảm bảo rằng Runnable cũng được hủy bỏ khi tắt sự kiện
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

}