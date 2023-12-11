package com.example.loginactivity.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.AddFolderActivity;
import com.example.loginactivity.AddTopicActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.adapter.AdapterMyFolder;
import com.example.loginactivity.adapter.AdapterMyTopic;
import com.example.loginactivity.models.Folder;
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


public class FolderFragment extends Fragment {

    View mView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<Folder> folders = new ArrayList<>();

    FloatingActionButton btnAddFolder;

    AdapterMyFolder adapter;

    RecyclerView recyclerView;

    public FolderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_folders, container, false);

        initUI();
        initListener();
        setupFolderList();
        return mView;
    }

    private void initUI() {
        btnAddFolder = mView.findViewById(R.id.btnAddFolder);
        recyclerView = mView.findViewById((R.id.my_folder_recyclerView));

        adapter = new AdapterMyFolder(getActivity(), folders);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initListener() {
        btnAddFolder.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddFolderActivity.class);
            startActivity(intent);
        });
    }

    private void setupFolderList() {
        DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("Folder");
        folderRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Folder folder = snapshot.getValue(Folder.class);
                if(folder != null){
                    if(folder.getOwner().equals(user.getDisplayName())){
                        folders.add(folder);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Folder folder = snapshot.getValue(Folder.class);

                for(int i = 0; i < folders.size(); i++){
                    if(folder.getId() == folders.get(i).getId()){
                        folders.set(i, folder);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Folder folder = snapshot.getValue(Folder.class);
                if(folders == null || folders.isEmpty()){
                    return;
                }

                for(int i = 0; i < folders.size(); i++){
                    if(folder.getId().equals(folders.get(i).getId())){
                        folders.remove(folders.get(i));
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