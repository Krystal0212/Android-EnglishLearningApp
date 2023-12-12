package com.example.loginactivity.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.AddTopicActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.adapter.AdapterMyTopic;
import com.example.loginactivity.adapter.AdapterPublicTopic;
import com.example.loginactivity.models.Participant;
import com.example.loginactivity.models.Topic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class PublicFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<Topic> topics = new ArrayList<>();

    AdapterPublicTopic adapter;

    View mView;
    RecyclerView recyclerView;

    public PublicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_public, container, false);
        initUI();
        initListener();
        setUpTopicList();

        return mView;
    }

    private void initListener() {
    }

    private void initUI() {
        recyclerView = mView.findViewById((R.id.my_topic_recyclerView));

        adapter = new AdapterPublicTopic(getActivity(), topics);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void setUpTopicList() {
        DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("Topic");
        topicRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Topic topic = snapshot.getValue(Topic.class);
                if(topic != null){
                    if(topic.getAccess().equals("PUBLIC")){
                        topics.add(topic);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Topic topic = snapshot.getValue(Topic.class);

                for(int i = 0; i < topics.size(); i++){
                    if(topic.getId().equals(topics.get(i).getId())){
                        topics.set(i, topic);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Topic topic = snapshot.getValue(Topic.class);
                if(topics == null || topics.isEmpty()){
                    return;
                }

                for(int i = 0; i < topics.size(); i++){
                    if(topic.getId().equals(topics.get(i).getId())){
                        topics.remove(topics.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}