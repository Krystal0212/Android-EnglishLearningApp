package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.loginactivity.models.Folder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddFolderActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button btnSave;

    ImageView btnBack;
    EditText edt_folder_name;

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