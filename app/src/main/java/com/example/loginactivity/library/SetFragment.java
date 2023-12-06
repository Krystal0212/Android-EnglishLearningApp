package com.example.loginactivity.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.loginactivity.AddTopicActivity;
import com.example.loginactivity.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SetFragment extends Fragment {

    FloatingActionButton btnAddTopic;

    View mView;

    public SetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_sets, container, false);
        initUI();
        initListener();

        return mView;
    }

    private void initListener() {
        btnAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTopicActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        btnAddTopic = mView.findViewById(R.id.btnAddTopic);
    }
}