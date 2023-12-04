package com.example.loginactivity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginactivity.R;
import com.example.loginactivity.adapter.MyLibraryViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class LibraryFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private View mView;
    private MyLibraryViewPagerAdapter myViewPagerAdapter;
    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_library, container, false);
        tabLayout = mView.findViewById(R.id.tab_layout);
        viewPager2 = mView.findViewById(R.id.library_viewPager);

        myViewPagerAdapter = new MyLibraryViewPagerAdapter(getActivity());
        viewPager2.setAdapter(myViewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Study sets");
                    break;

                case 1:
                    tab.setText("Classes");
                    break;

                case 2:
                    tab.setText("Folders");
                    break;
            }
        }).attach();

        return mView;
    }
}