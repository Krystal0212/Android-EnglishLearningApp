package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.loginactivity.models.Folder;

public class FolderDetailActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView btn_back, btn_more;

    Folder folder;
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

    private void onClickMore() {
    }
}