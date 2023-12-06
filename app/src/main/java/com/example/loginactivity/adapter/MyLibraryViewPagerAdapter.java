package com.example.loginactivity.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.loginactivity.library.ClassFragment;
import com.example.loginactivity.library.FolderFragment;
import com.example.loginactivity.library.SetFragment;

public class MyLibraryViewPagerAdapter extends FragmentStateAdapter {

    public MyLibraryViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SetFragment();
            case 1:
                return new ClassFragment();
            case 2:
                return new FolderFragment();

            default:
                return new SetFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}