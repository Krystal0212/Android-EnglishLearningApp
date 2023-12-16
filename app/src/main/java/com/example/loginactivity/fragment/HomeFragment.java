package com.example.loginactivity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginactivity.FlashCardActivity;
import com.example.loginactivity.FlashCardResult;
import com.example.loginactivity.R;
import com.example.loginactivity.ZoomOutPageTransformer;
import com.example.loginactivity.adapter.HomeIntroAdapter;
import com.example.loginactivity.models.Photo;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;


public class HomeFragment extends Fragment {

    View mView;
    ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;

    List<Photo> photos = new ArrayList<>();

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // chuyển trang
            if(viewPager2.getCurrentItem() == photos.size() - 1){
               viewPager2.setCurrentItem(0);

            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }
        }
    };
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        initUI();
        initListener();

        return mView;
    }

    private void initUI() {
        viewPager2 = mView.findViewById(R.id.viewPager2);
        circleIndicator3 = mView.findViewById(R.id.circle_indicator_3);

        photos = getListPhoto();
        HomeIntroAdapter adapter = new HomeIntroAdapter(photos);

        viewPager2.setAdapter(adapter);
        circleIndicator3.setViewPager(viewPager2);
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        viewPager2.setPageTransformer(compositePageTransformer);
    }

    private void initListener() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 5000);
            }
        });
    }

    private List<Photo> getListPhoto(){
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img_1));
        list.add(new Photo(R.drawable.img_2));

        return list;
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable,5000);
    }
}