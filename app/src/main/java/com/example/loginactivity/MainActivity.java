package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.loginactivity.adapter.MyMainViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements FragmentCallback {

    public ViewPager2 mViewPager;
    private BottomNavigationView bottomNavigationView;
    public MyMainViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNav);

        myViewPagerAdapter = new MyMainViewPagerAdapter(this);
        mViewPager.setAdapter(myViewPagerAdapter);

        mViewPager.setPageTransformer(new ZoomOutPageTransformer());
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);
                        break;

                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_library).setChecked(true);
                        break;

                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_profile).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.bottom_home){
                    mViewPager.setCurrentItem(0);
                }
                else if(item.getItemId() == R.id.bottom_library){
                    mViewPager.setCurrentItem(1);
                }
                else if(item.getItemId() == R.id.bottom_profile){
                    mViewPager.setCurrentItem(2);
                }
                return true;
            }
        });
    }

    @Override
    public void goTo(int pos) {
        mViewPager.setCurrentItem(pos);
    }

    @Override
    public void goBack() {
        getSupportFragmentManager().popBackStack();
    }
}