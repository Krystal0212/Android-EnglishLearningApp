package com.example.loginactivity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginactivity.FlashCardActivity;
import com.example.loginactivity.FlashCardResult;
import com.example.loginactivity.OnStartNowClickedListener;
import com.example.loginactivity.R;
import com.example.loginactivity.ZoomOutPageTransformer;
import com.example.loginactivity.adapter.HomeIntroAdapter;
import com.example.loginactivity.models.Photo;
import com.example.loginactivity.models.RecentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;


public class HomeFragment extends Fragment {

    View mView;
    ViewPager2 viewPager2;
    Button btnStartNow;
    CircleIndicator3 circleIndicator3;
    TextView txtUsername, txtRecentlyAccess, txtOwner, txtTitle, txtScore;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<Photo> photos = new ArrayList<>();
    private OnStartNowClickedListener mListener;

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
        setUpRecentActivity();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStartNowClickedListener) {
            mListener = (OnStartNowClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStartNowClickedListener");
        }
    }

    private void initUI() {
        btnStartNow = mView.findViewById(R.id.btnStartNow);
        txtRecentlyAccess = mView.findViewById(R.id.txtRecentlyAccess);
        txtOwner = mView.findViewById(R.id.txtOwner);
        txtTitle = mView.findViewById(R.id.txtTitle);
        txtScore = mView.findViewById(R.id.txtScore);
        txtUsername = mView.findViewById(R.id.txtUsername);
        txtUsername.setText(user.getDisplayName());
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

        btnStartNow.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onStartNowClicked();
            }
        });
    }

    private void setUpRecentActivity() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(user.getUid()).child("recentActivity");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    RecentActivity recentActivity = snapshot.getValue(RecentActivity.class);
                    txtOwner.setText("by " + recentActivity.getOwner());
                    txtScore.setText(String.valueOf(recentActivity.getScore()));
                    txtTitle.setText(recentActivity.getTopicTitle());
                    String lastVisitedTime = convertTimestampToRelativeTime(recentActivity.getLastVisitedTime());
                    txtRecentlyAccess.setText(lastVisitedTime);
                } else {
                    txtOwner.setText("N/A");
                    txtScore.setText("N/A");
                    txtTitle.setText("No recent activity");
                    txtRecentlyAccess.setText("No recent visits");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String convertTimestampToRelativeTime(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - timestamp;

        long seconds = timeDifference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else {
            return days + " days ago";
        }
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