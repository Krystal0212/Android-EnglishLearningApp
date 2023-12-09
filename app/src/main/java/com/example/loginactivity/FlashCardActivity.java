package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
    ArrayList<Word> words = new ArrayList<>();

    TextView status;

    SwitchCompat switchButton;

    FlashCardViewPagerAdapter adapter;

    Button btn_back, btn_complete;

    public TextToSpeechHelper textToSpeechHelper;
    private boolean isLastPage = false;

    private float initialX, finalX;


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
                int currentItem = viewPager2.getCurrentItem();
                adapter.flipCardToFront(currentItem);

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
        words = intent.getParcelableArrayListExtra("words");

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
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    int currentItem = viewPager2.getCurrentItem();
                    adapter.flipCardToFront(currentItem);
                }
            }
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

        viewPager2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        finalX = event.getX();
                        if (isLastPage && (finalX > initialX)) {
                            // Người dùng vuốt sang phải ở trang cuối cùng
                            Intent intent = new Intent(FlashCardActivity.this, FlashCardResult.class);
                            startActivity(intent);
                        }
                        break;
                }
                return false;
            }
        });

        ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (switchButton.isChecked()) {
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
        if (holder != null && !holder.isFlipping) { // Kiểm tra nếu ViewHolder tồn tại và không đang lật
            if (holder.flipInterface.getDisplayedChild() != 0) {
                // Lật card về mặt trước nếu nó đang ở mặt sau
                adapter.flipCardToFront(currentItem);
            }
            Word word = words.get(currentItem);

            textToSpeechHelper.setLanguage(Locale.ENGLISH);
            textToSpeechHelper.speak(word.getEnglish());

            mHandler.postDelayed(() -> {
                //flip
                holder.cardFront.animate().rotationY(180).setDuration(300);
                holder.cardBack.animate().rotationY(0).setDuration(300);

                holder.flipInterface.showNext();

                mHandler.postDelayed(() -> {
                    textToSpeechHelper.setLanguage(new Locale("vi", "VN"));
                    textToSpeechHelper.speak(word.getVietnamese());
                }, 1000); // Thời gian chờ giữa hai bước
            }, 1000); // Thời gian chờ giữa hai bước
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
        // Tắt TextToSpeech khi không cần thiết
        if (textToSpeechHelper != null) {
            textToSpeechHelper.shutdown();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textToSpeechHelper == null) {
            textToSpeechHelper = new TextToSpeechHelper(this);
        }
        if (switchButton.isChecked()) {
            mHandler.postDelayed(mRunnable, 6000);
        }
    }
}