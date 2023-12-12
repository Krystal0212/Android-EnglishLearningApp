package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.adapter.AdapterWordListAddTopic;
import com.example.loginactivity.models.Participant;
import com.example.loginactivity.models.Topic;
import com.example.loginactivity.models.Word;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddTopicActivity extends AppCompatActivity {
    private static final int CSV_FILE_PICK_REQUEST_CODE = 101;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FloatingActionButton btnAddWord;
    RadioGroup radio;
    ImageView btnBack;
    Button btnSave;
    EditText edt_title;
    ArrayList<Participant> participant = new ArrayList<>();
    ArrayList<Word> words = new ArrayList<>();
    AdapterWordListAddTopic adapter;
    RecyclerView recyclerView;
    LinearLayout llTopic;
    private ProgressDialog progressDialog;
    private boolean isEditMode = false;
    private Topic editingTopic;

    TextView activity_title, txtAccess, btn_import;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);

        initUI();
        initListener();
    }

    private void initUI() {
        btn_import = findViewById(R.id.btn_import);
        txtAccess = findViewById(R.id.txtAccess);
        activity_title = findViewById(R.id.activity_title);
        btnAddWord = findViewById(R.id.btnAddWord);
        edt_title = findViewById(R.id.edt_title);
        recyclerView = findViewById(R.id.addTopic_recyclerView);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        radio = findViewById(R.id.access);
        llTopic = findViewById(R.id.linearMakeNewTopic);

        adapter = new AdapterWordListAddTopic(this, words);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Kiểm tra xem có phải là chỉnh sửa không
        if (getIntent().hasExtra("topic")) {
            isEditMode = true;
            editingTopic = getIntent().getParcelableExtra("topic");
            populateUIWithTopicData(editingTopic);
        }
    }

    private void populateUIWithTopicData(Topic editingTopic) {
        edt_title.setText(editingTopic.getTitle());
        activity_title.setText("Edit topic " + editingTopic.getTitle());
        //khong cho chinh sua access
        txtAccess.setVisibility(View.GONE);
        radio.setVisibility(View.GONE);

        words.addAll(editingTopic.getWord());
        adapter.notifyDataSetChanged();
    }

    private void initListener() {
        btnAddWord.setOnClickListener(v -> {
            onClickAddWord();
        });
        
        btnSave.setOnClickListener(v -> {
            onClickSave();
        });

        btnBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        btn_import.setOnClickListener(v -> {
            onClickImportWordList();
        });
    }

    private void onClickImportWordList() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, CSV_FILE_PICK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CSV_FILE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri csvFileUri = data.getData();
            String fileName = getFileName(csvFileUri);
            if (!fileName.endsWith(".csv")) {
                Toast.makeText(this, "Please select a CSV file.", Toast.LENGTH_SHORT).show();
            } else {
                importCsvData(csvFileUri);
            }
        } else {
            Toast.makeText(this, "File selection canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void importCsvData(Uri csvFileUri) {
        int wordsAddedCount = 0; // Biến để theo dõi số lượng từ được thêm vào

        try (InputStream inputStream = getContentResolver().openInputStream(csvFileUri);
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // Đảm bảo mỗi dòng có ít nhất 3 cột (English, Vietnamese, Description)
                if (nextLine.length >= 3) {
                    Word newWord = new Word(nextLine[0], nextLine[1], nextLine[2]);
                    // Kiểm tra và thêm từ nếu nó chưa tồn tại
                    if (addWordIfNotExists(newWord)) {
                        wordsAddedCount++; // Tăng số đếm nếu từ mới được thêm vào
                    }
                }
            }
            adapter.notifyDataSetChanged();

            // Hiển thị thông báo với số lượng từ đã được thêm vào
            Toast.makeText(this, wordsAddedCount + " new words added to the list.", Toast.LENGTH_LONG).show();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error during import: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean addWordIfNotExists(Word newWord) {
        for (Word word : words) {
            if (word.getEnglish().equalsIgnoreCase(newWord.getEnglish())) {
                return false; // Nếu từ đã tồn tại, trả về false và không thêm vào danh sách
            }
        }
        words.add(newWord); // Thêm từ mới vào danh sách nếu nó chưa tồn tại và trả về true
        return true;
    }

    private void onClickSave() {
        if (isEditMode) {
            // Cập nhật topic hiện tại
            updateTopic(editingTopic);
        } else {
            // Thêm mới topic
            createNewTopic();
        }
    }

    private void updateTopic(Topic editingTopic) {
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Updating topic");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        //get title
        String title = edt_title.getText().toString().trim();

        if(title.isEmpty()){
            progressDialog.dismiss();
            Toast.makeText(AddTopicActivity.this, "Please enter topic title.", Toast.LENGTH_SHORT).show();
        } else if(words.size() < 4){
            progressDialog.dismiss();
            Toast.makeText(AddTopicActivity.this, "Add at least 4 term to continue.", Toast.LENGTH_SHORT).show();
        } else if(words.isEmpty()){
            progressDialog.dismiss();
            Toast.makeText(AddTopicActivity.this, "You have not added any term yet.", Toast.LENGTH_SHORT).show();
        }
        else {
            // chỉ update 2 trường sau
            Map<String, Object> topicUpdates = new HashMap<>();
            topicUpdates.put("title", title);
            topicUpdates.put("word", words);

            String key = editingTopic.getId();
            DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("Topic");

            topicRef.child(key).updateChildren(topicUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Xử lý sau khi cập nhật
                            editingTopic.setTitle(title);
                            editingTopic.setWord(words);

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("updatedTopic", editingTopic);

                            // Đặt kết quả và kết thúc Activity
                            setResult(RESULT_OK, returnIntent);
                            Toast.makeText(AddTopicActivity.this, "Update topic successfully.", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });
        }
    }

    public void createNewTopic(){
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Adding topic");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        //get access
        int radioButtonID = radio.getCheckedRadioButtonId();
        View radioButton = radio.findViewById(radioButtonID);
        int idx = radio.indexOfChild(radioButton);
        RadioButton r = (RadioButton) radio.getChildAt(idx);
        //get title
        String title = edt_title.getText().toString().trim();

        if(title.isEmpty()){
            progressDialog.dismiss();
            Toast.makeText(AddTopicActivity.this, "Please enter topic title.", Toast.LENGTH_SHORT).show();
        } else if (r == null){
            progressDialog.dismiss();
            Toast.makeText(AddTopicActivity.this, "Please choose topic access.", Toast.LENGTH_SHORT).show();
        } else if(words.size() < 4){
            progressDialog.dismiss();
            Toast.makeText(AddTopicActivity.this, "Add at least 4 term to continue.", Toast.LENGTH_SHORT).show();
        } else if(words.isEmpty()){
            progressDialog.dismiss();
            Toast.makeText(AddTopicActivity.this, "You have not added any term yet.", Toast.LENGTH_SHORT).show();
        }
        else {
            // Lấy access
            String access = r.getText().toString();
            // Lấy ngày hiện tại
            Date currentDate = new Date();

            // Định dạng ngày theo yêu cầu "dd/MM/yyyy"
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String createDate = dateFormat.format(currentDate);

            DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("Topic");
            String key = topicRef.push().getKey();

            participant.add(new Participant(user.getUid()));

            topicRef.child(key).setValue(new Topic(access, createDate, key, title, user.getDisplayName(),user.getPhotoUrl().toString(),participant ,words))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(AddTopicActivity.this, "Create topic successfully.", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            finish();
                        }
                    });
        }
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
                    Toast.makeText(AddTopicActivity.this, "Add word successfully.", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(AddTopicActivity.this, "This term has been added.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

}