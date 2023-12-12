package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.models.Folder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddFolderActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button btnSave;
    Folder editingFolder;
    private boolean isEditMode = false;
    ImageView btnBack;
    EditText edt_folder_name;
    TextView activity_title;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_folder);

        initUI();
        initListener();
    }

    @SuppressLint("WrongViewCast")
    private void initUI() {
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        edt_folder_name = findViewById(R.id.edt_folder_name);
        activity_title = findViewById(R.id.activity_title);

        // Kiểm tra xem có phải là chỉnh sửa không
        if (getIntent().hasExtra("folder")) {
            isEditMode = true;
            editingFolder = getIntent().getParcelableExtra("folder");
            populateUIWithTopicData(editingFolder);
        }
    }

    private void populateUIWithTopicData(Folder editingFolder) {
        edt_folder_name.setText(editingFolder.getName());
        activity_title.setText("Edit folder " + editingFolder.getName());
    }

    private void initListener() {
        btnBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        btnSave.setOnClickListener(v -> {
            onClickSave();
        });
    }

    private void onClickSave() {
        if (isEditMode) {
            // Cập nhật folder hiện tại
            updateFolder(editingFolder);
        } else {
            // Thêm mới folder
            createFolder();
        }
    }

    public void updateFolder(Folder editingFolder){
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Updating folder");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        //get title
        String name = edt_folder_name.getText().toString().trim();

        if(name.isEmpty()){
            progressDialog.dismiss();
            Toast.makeText(AddFolderActivity.this, "Please enter folder name.", Toast.LENGTH_SHORT).show();
        }
        else {
            // chỉ update 1 trường sau
            Map<String, Object> folderUpdates = new HashMap<>();
            folderUpdates.put("name", name);

            String key = editingFolder.getId();
            DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("Folder");

            folderRef.child(key).updateChildren(folderUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Xử lý sau khi cập nhật
                            editingFolder.setName(name);

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("updatedFolder", editingFolder);

                            // Đặt kết quả và kết thúc Activity
                            setResult(RESULT_OK, returnIntent);
                            Toast.makeText(AddFolderActivity.this, "Update folder successfully.", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });
        }
    }

    public  void createFolder(){
        String name = edt_folder_name.getText().toString().trim();
        if(name.isEmpty()){
            Toast.makeText(AddFolderActivity.this, "Please enter folder name.", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("Folder");
            String key = folderRef.push().getKey();

            HashMap<String, Boolean> emptyTopics = new HashMap<>();

            folderRef.child(key).setValue(new Folder(name, user.getDisplayName(), key,user.getPhotoUrl().toString(), emptyTopics))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddFolderActivity.this, "Create folder successfully.", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            finish();
                        }
                    });
        }
    }
}