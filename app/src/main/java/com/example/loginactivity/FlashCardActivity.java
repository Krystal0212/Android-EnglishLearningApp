package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.loginactivity.adapter.FlashCardViewPagerAdapter;
import com.example.loginactivity.models.Topic;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;
import java.util.Locale;

import me.relex.circleindicator.CircleIndicator3;

public class FlashCardActivity extends AppCompatActivity {

    public ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;

    Topic topic;

    ArrayList<Word> words = new ArrayList<>();

    TextView status;

    Switch switchButton;

    FlashCardViewPagerAdapter adapter;

    Button btn_complete;
    ImageView btn_back;
    public TextToSpeechHelper textToSpeechHelper;
    private boolean isLastPage = false;
    private boolean isFirstPage = true;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // chuyển trang
            if(viewPager2.getCurrentItem() == words.size() - 1){
                Intent intent = new Intent(FlashCardActivity.this, FlashCardResult.class);
                startActivity(intent);
                finish();

            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                // Gọi hàm speechAndFlipCard() sau khi chuyển trang
                new Handler().postDelayed(() -> speechAndFlipCard(), 500);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        initUI();
        initListerner();
    }

    private void initUI() {
        viewPager2 = findViewById(R.id.viewPager2);
        circleIndicator3 = findViewById(R.id.circle_indicator_3);
        status = findViewById(R.id.starus);
        switchButton = findViewById(R.id.switchButton);
        btn_back = findViewById(R.id.btn_back);
        btn_complete = findViewById(R.id.btn_complete);

        textToSpeechHelper = new TextToSpeechHelper(this);

        Intent intent = getIntent();
        topic = intent.getParcelableExtra("topic");
        words = topic.getWord();

        //Setting viewpager2
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        viewPager2.setPageTransformer(compositePageTransformer);
        //viet su kien thay doi status
        adapter = new FlashCardViewPagerAdapter(this, words, viewPager2);
        viewPager2.setAdapter(adapter);

        circleIndicator3.setViewPager(viewPager2);
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());

    }

    private void initListerner() {
        btn_complete.setOnClickListener(v -> {
            Intent intent = new Intent(FlashCardActivity.this, FlashCardResult.class);
            startActivity(intent);
            finish();
        });

        btn_back.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        ViewPager2.OnPageChangeCallback UpdatePage = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                String page = (position + 1) + " / " + adapter.getItemCount();
                status.setText(page);
                // Kiểm tra nếu đang ở trang cuối cùng
                isLastPage = (position == adapter.getItemCount() - 1);

                // Nếu đang ở trang cuối cùng và đang swipe
                if (isLastPage) {
                   btn_complete.setVisibility(View.VISIBLE);
                } else {
                    btn_complete.setVisibility(View.GONE);
                }
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
                    mHandler.postDelayed(mRunnable, 6000);
                }
            }
        };

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    speechAndFlipCard();
                    mHandler.removeCallbacks(mRunnable);
                    mHandler.postDelayed(mRunnable, 5000);
                    viewPager2.registerOnPageChangeCallback(onPageChangeCallback);
                } else {
                    // Tắt sự kiện khi Switch được tắt
                    mHandler.removeCallbacks(mRunnable);
                    viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback);
                }
            }
        });
    }

    private void speechAndFlipCard() {
        int currentItem = viewPager2.getCurrentItem();
        FlashCardViewPagerAdapter.FlashCardViewHolder holder = adapter.getViewHolderAtPosition(currentItem);
        Word word = words.get(currentItem);

        //doc tieng anh
        textToSpeechHelper.setLanguage(Locale.ENGLISH);
        textToSpeechHelper.speak(word.getEnglish());

        // Sử dụng Handler để chờ một khoảng thời gian trước khi thực hiện thao tác tiếp theo
        new Handler().postDelayed(() -> {
            //flip
            holder.cardFront.animate().rotationYBy(180).setDuration(300);
            holder.cardBack.animate().rotationYBy(180).setDuration(300);

            // Đảo chiều hiển thị của ViewFlipper để hiển thị mặt khác của card
            holder.flipInterface.showNext();

            // Sử dụng Handler để chờ một khoảng thời gian trước khi thực hiện thao tác tiếp theo
            new Handler().postDelayed(() -> {
                //doc tieng viet
                textToSpeechHelper.setLanguage(new Locale("vi", "VN"));
                textToSpeechHelper.speak(word.getVietnamese());
            }, 1000); // Thời gian chờ giữa hai bước

        }, 1000); // Thời gian chờ giữa hai bước
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(switchButton.isChecked()){
            mHandler.postDelayed(mRunnable, 6000);
        }
    }
}