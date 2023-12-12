package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.loginactivity.adapter.AdapterFolderPicker;
import com.example.loginactivity.adapter.AdapterMyFolder;
import com.example.loginactivity.models.Folder;
import com.example.loginactivity.models.Topic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FolderPickerActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<Folder> folders = new ArrayList<>();
    Button btnConfirm;
    Topic topic;
    ImageView btnBack;
    AdapterFolderPicker adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_picker);

        initUI();
        initListener();
        setupFolderList();

    }

    private void initListener() {
        btnConfirm.setOnClickListener(v -> {
            onClickConfirm();
        });

        btnBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    private void initUI() {
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.my_folder_recyclerView);
        topic = getIntent().getParcelableExtra("topic");

        adapter = new AdapterFolderPicker(this, folders);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void onClickConfirm() {
        String topicID = topic.getId();
        Folder selectedFolder = null;
        //Xử lý folder nào có is selected là true sẽ thêm id của topic vào list topics của folder

        for (Folder f : folders) {
            if (f.isSelected()) {
                selectedFolder = f;
                break;
            }
        }

        if (selectedFolder != null) {
            // Tiến hành thêm topicId vào folder được chọn
            updateFolderWithTopic(selectedFolder, topicID);
        } else {
            Toast.makeText(FolderPickerActivity.this, "Please pick a folder to continue !", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFolderWithTopic(Folder selectedFolder, String topicID) {
        DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("Folder").child(selectedFolder.getId());
        HashMap<String, Object> topicUpdates = new HashMap<>();
        topicUpdates.put("topics/" + topicID, true);
        folderRef.updateChildren(topicUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(FolderPickerActivity.this, "Add to folder " + selectedFolder.getName() + " successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // Xử lý lỗi
            }
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