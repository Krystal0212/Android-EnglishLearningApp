package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginactivity.adapter.AdapterWordList;
import com.example.loginactivity.models.Participant;
import com.example.loginactivity.models.Topic;
import com.example.loginactivity.models.Word;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

public class AddTopicActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FloatingActionButton btnAddWord;
    
    Button btnSave;
    EditText edt_title;
    ArrayList<Participant> participant = new ArrayList<>();
    ArrayList<Word> words = new ArrayList<>();
    AdapterWordList adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);

        initUI();
        initListener();
        setupWordList();
    }

    private void setupWordList() {

    }

    private void initUI() {
        btnAddWord = findViewById(R.id.btnAddWord);
        edt_title = findViewById(R.id.edt_title);
        recyclerView = findViewById(R.id.addTopic_recyclerView);
        btnSave = findViewById(R.id.btnSave);
        
        adapter = new AdapterWordList(this, words);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initListener() {
        btnAddWord.setOnClickListener(v -> {
            onClickAddWord();
        });
        
        btnSave.setOnClickListener(v -> {
            onClickSave();
        });
    }

    private void onClickSave() {

    }

    private void onClickAddWord() {
        final Dialog dialog = new Dialog(AddTopicActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_word_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        EditText edt_term = dialog.findViewById(R.id.edt_term);
        EditText edt_meaning = dialog.findViewById(R.id.edt_meaning);
        EditText edt_description = dialog.findViewById(R.id.edt_description);


        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnApply = dialog.findViewById(R.id.btnApply);

        btnCancel.setOnClickListener(view -> dialog.dismiss());
        btnApply.setOnClickListener(view -> {
            String term = edt_term.getText().toString().trim();
            String meaning = edt_meaning.getText().toString().trim();
            String description = edt_description.getText().toString().trim();
            if(term.isEmpty() || meaning.isEmpty()){
                Toast.makeText(AddTopicActivity.this, "Please enter term and meaning.", Toast.LENGTH_SHORT).show();
            }
            else{
                Boolean flag = false;
                Word newWord = new Word(term, meaning, description);
                //kiem tra trong list co term này chưa
                for(int i = 0; i < words.size(); i++){
                    if(words.get(i).getEnglish().equals(newWord.getEnglish())){
                        flag = true;
                    }
                }

                if(flag == false){
                    dialog.dismiss();
                    words.add(newWord);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(AddTopicActivity.this, "This term has been added.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public void addParticipant(){
        participant.add(new Participant(user.getDisplayName()));
    }
}