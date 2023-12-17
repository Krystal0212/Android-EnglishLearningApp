package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.adapter.AdapterMyTopic;
import com.example.loginactivity.models.Folder;
import com.example.loginactivity.models.Topic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FolderDetailActivity extends AppCompatActivity {
    private static final int EDIT_FOLDER_REQUEST_CODE = 10;
    RecyclerView recyclerView;
    ImageView btn_back, btn_more;
    TextView txt_folder_name, txt_empty;
    Folder folder;
    AdapterMyTopic adapter;
    ArrayList<Topic> topics = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_detail);

        initUI();
        initListener();
    }

    private void initUI() {
        btn_back = findViewById(R.id.btn_back);
        btn_more = findViewById(R.id.btn_more);
        recyclerView = findViewById(R.id.folder_topic_recyclerView);
        txt_folder_name = findViewById(R.id.txt_folder_name);
        txt_empty = findViewById(R.id.txt_empty);

        adapter = new AdapterMyTopic(this, topics);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Nhận dữ liệu Folder từ Intent
        folder = getIntent().getParcelableExtra("folder");
        if (folder != null) {
            txt_folder_name.setText(folder.getName());
            setUpTopic();
        }
    }

    private void initListener() {
        btn_back.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        btn_more.setOnClickListener(v -> {
            onClickMore();
        });
    }

    private void setUpTopic() {
        if(folder.getTopics() != null){
            ArrayList<String> topicIds = new ArrayList<>(folder.getTopics().keySet());
            DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("Topic");

            for (String topicId : topicIds) {
                topicsRef.child(topicId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Topic topic = dataSnapshot.getValue(Topic.class);
                        if (topic != null) {
                            updateOrAddTopic(topic);
                        } else {
                            removeTopic(topicId);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        } else {
            txt_empty.setVisibility(View.VISIBLE);
        }

    }

    private void updateOrAddTopic(Topic newTopic) {
        int index = -1;
        for (int i = 0; i < topics.size(); i++) {
            if (topics.get(i).getId().equals(newTopic.getId())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            topics.set(index, newTopic); // Cập nhật topic
        } else {
            topics.add(newTopic); // Thêm topic mới
        }
    }

    private void removeTopic(String topicId) {
        topics.removeIf(topic -> topic.getId().equals(topicId));
    }
    private void onClickMore() {
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_folder, null);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.show();

        Button btn_cancel = viewDialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        TextView txt_edit_folder = viewDialog.findViewById(R.id.txt_edit_folder);
        TextView txt_delete_folder = viewDialog.findViewById(R.id.txt_delete_folder);

        txt_edit_folder.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            onClickEditFolder();
        });

        txt_delete_folder.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            onClickDeleteFolder();
        });
    }

    private void onClickEditFolder() {
        Intent intent = new Intent(this, AddFolderActivity.class);
        intent.putExtra("folder", folder);
        startActivityForResult(intent, EDIT_FOLDER_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_FOLDER_REQUEST_CODE && resultCode == RESULT_OK) {
            Folder updatedFolder = data.getParcelableExtra("updatedFolder");

            this.folder = updatedFolder;
            txt_folder_name.setText(folder.getName());
        }
    }
    private void onClickDeleteFolder() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_topic_confirm_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnApply = dialog.findViewById(R.id.btnApply);
        TextView txt_message = dialog.findViewById(R.id.txt_message);

        txt_message.setText("Are you sure you want to delete folder " + folder.getName() + " ?");

        btnCancel.setOnClickListener(view -> dialog.dismiss());
        btnApply.setOnClickListener(view -> {
            String folderID = folder.getId();
            DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("Folder/" + folderID);
            folderRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    onBackPressed();
                    finish();
                }
            });
        });
        dialog.show();
    }
    public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        public SwipeToDeleteCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false; // Không xử lý di chuyển
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Topic topicToRemove = topics.get(position);

            // Xóa topic khỏi ArrayList
            topics.remove(position);
            adapter.notifyItemRemoved(position);

            // Cập nhật Firebase
            updateFolderInFirebase(topicToRemove);
        }

        private void updateFolderInFirebase(Topic topicToRemove) {
            DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("Folder").child(folder.getId());
            folder.getTopics().remove(topicToRemove.getId());

            folderRef.setValue(folder)
                    .addOnSuccessListener(unused -> {
                        // Thông báo cho người dùng hoặc xử lý thành công
                        if(folder.getTopics() == null || folder.getTopics().isEmpty()){
                            txt_empty.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(FolderDetailActivity.this, "Remove topic successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý thất bại
                    });
        }
    }
}