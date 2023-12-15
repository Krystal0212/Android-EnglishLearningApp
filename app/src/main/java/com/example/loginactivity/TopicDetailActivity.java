package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.adapter.AdapterWordListTopic;
import com.example.loginactivity.adapter.FlashCardBasicViewPagerAdapter;
import com.example.loginactivity.models.Folder;
import com.example.loginactivity.models.Topic;
import com.example.loginactivity.models.Word;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


public class TopicDetailActivity extends AppCompatActivity {
    private static final int EDIT_TOPIC_REQUEST_CODE = 10;
    private static final int PERMISSION_REQUEST_CODE = 100;
    public ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    Button btn_Flashcard, btn_test;

    ImageView btn_back, btn_more ;

    Topic topic;

    ArrayList<Word> words = new ArrayList<>();

    FlashCardBasicViewPagerAdapter adapter;

    AdapterWordListTopic adapterWordListTopic;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        initUI();
        initListener();
    }

    private void initUI() {
        btn_Flashcard = findViewById(R.id.btn_FLashcard);
        btn_back = findViewById(R.id.btn_back);
        btn_more = findViewById(R.id.btn_more);
        btn_test = findViewById(R.id.btn_test);
        Intent intent = getIntent();
        topic = intent.getParcelableExtra("topic");
        words = topic.getWord();

        viewPager2 = findViewById(R.id.viewPager2);
        circleIndicator3 = findViewById(R.id.circle_indicator_3);

        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        viewPager2.setPageTransformer(compositePageTransformer);

        adapter = new FlashCardBasicViewPagerAdapter(this, words, viewPager2, topic.getTitle());
        viewPager2.setAdapter(adapter);

        circleIndicator3.setViewPager(viewPager2);
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());

        //init recycler view trong topic
        recyclerView = findViewById(R.id.recyclerView);

        adapterWordListTopic = new AdapterWordListTopic(this, words, recyclerView);
        recyclerView.setAdapter(adapterWordListTopic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListener() {
        btn_more.setOnClickListener(v -> {
            clickOpenBottomSheet();
        });
        btn_back.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });


        btn_Flashcard.setOnClickListener(v -> {
            ArrayList<Word> markedWords = adapterWordListTopic.markedWords;
            if(markedWords.isEmpty()) {
                // khong co word nao trong danh sach marked
                Intent intent = new Intent(this, FlashCardActivity.class);
                intent.putParcelableArrayListExtra("words", topic.getWord());
                intent.putExtra("title", topic.getTitle());
                startActivity(intent);
            } else {
                //co it nhat 1 word
                // tien hanh hỏi
                final Dialog dialog = new Dialog(TopicDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.flashcard_mode_dialog);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView txt_message = dialog.findViewById(R.id.txt_message);
                txt_message.setText("Learn all words or learn your " + markedWords.size() + " marked words ?");

                Button btn_all = dialog.findViewById(R.id.btn_all);
                Button btn_marked = dialog.findViewById(R.id.btn_marked);

                btn_all.setOnClickListener(view -> {
                    dialog.dismiss();
                    Intent intent = new Intent(this, FlashCardActivity.class);
                    intent.putParcelableArrayListExtra("words", topic.getWord());
                    startActivity(intent);
                });

                btn_marked.setOnClickListener(view -> {
                    dialog.dismiss();
                    Intent intent = new Intent(this, FlashCardActivity.class);
                    intent.putParcelableArrayListExtra("words", markedWords);
                    startActivity(intent);
                });

                dialog.show();
            }

        });

        btn_test.setOnClickListener(v -> {


            final Dialog dialog = new Dialog(TopicDetailActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.test_mode_dialog);
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView txt_message = dialog.findViewById(R.id.txt_message);

            RadioButton radio_eng_viet = dialog.findViewById(R.id.radio_eng_viet);
            RadioButton radio_viet_eng = dialog.findViewById(R.id.radio_viet_eng);
            SwitchCompat switch_shuffle = dialog.findViewById(R.id.switch_shuffle);
            RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);

            Button btn_all = dialog.findViewById(R.id.btn_all);
            Button btn_marked = dialog.findViewById(R.id.btn_marked);
            Button btn_test = dialog.findViewById(R.id.btn_take);

            ArrayList<Word> words = topic.getWord();
            ArrayList<Word> markedWords = adapterWordListTopic.markedWords;

            radio_eng_viet.setOnClickListener(i ->{
                radio_viet_eng.setTextColor(ContextCompat.getColor(this, R.color.gray_700));
                radio_eng_viet.setTextColor(ContextCompat.getColor(this, R.color.black));
            });
            radio_viet_eng.setOnClickListener(i->{
                radio_viet_eng.setTextColor(ContextCompat.getColor(this, R.color.black));
                radio_eng_viet.setTextColor(ContextCompat.getColor(this, R.color.gray_700));
            });

            if (markedWords.isEmpty()) {
                // khong co word nao trong danh sach marked
                txt_message.setText("Choose modes for your test");


                btn_marked.setVisibility(View.GONE);
                btn_all.setVisibility(View.GONE);
                btn_test.setOnClickListener(i ->{
                    dialog.dismiss();

                    String testMode = null;
                    if(radio_eng_viet.isChecked()){
                        testMode = "english";
                    }
                    else if (radio_viet_eng.isChecked()){
                        testMode = "vietnamese";
                    }

                    if (switch_shuffle.isChecked()) {
                        // Shuffle the array
                        Collections.shuffle(words);
                    }

                    Intent intent = new Intent(this, TestVocabularyActivity.class);
                    intent.putParcelableArrayListExtra("words", words);
                    intent.putExtra("test_mode",testMode);
                    startActivity(intent);
                });
                dialog.show();
            }
            else {
                // co it nhat 1 word tien hanh hỏi
                txt_message.setText("Test all words or test your " + markedWords.size() + " marked words ?");

                btn_test.setVisibility(View.GONE);

                btn_all.setOnClickListener(view -> {

                    if (radioGroup.getCheckedRadioButtonId() != -1) {
                        dialog.dismiss();

                        String testMode = null;
                        if(radio_eng_viet.isChecked()){
                            testMode = "english";
                           }
                        else if (radio_viet_eng.isChecked()){
                            testMode = "vietnamese"; }

                        if (switch_shuffle.isChecked()) {
                            // Shuffle the array
                            Collections.shuffle(words);
                        }

                        Intent intent = new Intent(this, TestVocabularyActivity.class);
                        intent.putExtra("words", words);
                        intent.putExtra("test_mode",testMode);
                        startActivity(intent);
                    } else {
                        // Notify the user to choose a test mode
                        Toast.makeText(this, "Please choose a test mode", Toast.LENGTH_SHORT).show();
                    }
                });

                btn_marked.setOnClickListener(view -> {
                    if (radioGroup.getCheckedRadioButtonId() != -1) {
                        dialog.dismiss();

                        String testMode = null;
                        if(radio_eng_viet.isChecked()){
                            testMode = "english";
                        }
                        else if (radio_viet_eng.isChecked()){
                            testMode = "vietnamese";
                        }

                        if (switch_shuffle.isChecked()) {
                            // Shuffle the array
                            Collections.shuffle(markedWords);
                        }

                        Intent intent = new Intent(this, TestVocabularyActivity.class);
                        intent.putExtra("words", words);
                        intent.putExtra("marked_words", markedWords);
                        intent.putExtra("test_mode",testMode);
                        startActivity(intent);
                    } else {
                        // Notify the user to choose a test mode
                        Toast.makeText(this, "Please choose a test mode", Toast.LENGTH_SHORT).show();
                    }
                });


                dialog.show();
            }
        });
    }

    private void clickOpenBottomSheet() {
        String owner = topic.getOwner();
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_topic, null);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.show();

        Button btn_cancel = viewDialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        TextView txt_add_to_folder = viewDialog.findViewById(R.id.txt_add_to_folder);
        TextView txt_edit_topic = viewDialog.findViewById(R.id.txt_edit_topic);
        TextView txt_delete_topic = viewDialog.findViewById(R.id.txt_delete_topic);
        TextView txt_export_word_list = viewDialog.findViewById(R.id.txt_export_word_list);

        if(!user.getDisplayName().equals(owner)){
            txt_delete_topic.setVisibility(View.GONE);
            txt_edit_topic.setVisibility(View.GONE);
        }
        txt_add_to_folder.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            onClickAddFolder();
        });

        txt_delete_topic.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            onClickDeleteTopic();
        });

        txt_edit_topic.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            onClickEditTopic();
        });

        txt_export_word_list.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            try {
                checkAndRequestPermissionsThenExport();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void checkAndRequestPermissionsThenExport() throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                exportWordListToCSV();
            }
        } else {
            exportWordListToCSV();
        }
    }

    // Phương thức xử lý kết quả yêu cầu quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                exportWordListToCSV();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast.makeText(this, "Permission denied to write to storage", Toast.LENGTH_SHORT).show();
        }
    }
    private void exportWordListToCSV() throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // Đối với các thiết bị chạy Android 9 (Pie) trở xuống, cần quyền WRITE_EXTERNAL_STORAGE để ghi vào bộ nhớ ngoại vi
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                File documentsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Quizzme");
                if (!documentsFolder.exists()) documentsFolder.mkdirs();
                File csvFile = new File(documentsFolder, topic.getTitle() + ".csv");
                writeCsvFile(csvFile);
            } else {
                // Yêu cầu quyền nếu chưa có
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            // Đối với Android 10 (Q) trở lên, không cần quyền WRITE_EXTERNAL_STORAGE để ghi vào MediaStore
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, topic.getTitle() + ".csv");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);

            Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);
            try {
                if (uri != null) {
                    writeCsvFile(getContentResolver().openOutputStream(uri));
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error during export: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void writeCsvFile(OutputStream outputStream) throws IOException {
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            for (Word word : words) {
                String[] entries = new String[]{word.getEnglish(), word.getVietnamese(), word.getDescription()};
                writer.writeNext(entries);
            }
            Toast.makeText(this, "Exported to Documents", Toast.LENGTH_LONG).show();
        }
    }

    private void writeCsvFile(File file) throws IOException {
        writeCsvFile(new FileOutputStream(file));
    }

    private void onClickAddFolder() {
        Intent intent = new Intent(this, FolderPickerActivity.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }

    private void onClickEditTopic() {
        Intent intent = new Intent(this, AddTopicActivity.class);
        intent.putExtra("topic", topic);
        startActivityForResult(intent, EDIT_TOPIC_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_TOPIC_REQUEST_CODE && resultCode == RESULT_OK) {
            Topic updatedTopic = data.getParcelableExtra("updatedTopic");

            updateAdapters(updatedTopic);
            adapterWordListTopic.uncheckAllMarkers();
        }
    }

    private void updateAdapters(Topic updatedTopic) {
        this.topic = updatedTopic;
        adapter.title = updatedTopic.getTitle();
        // không dùng this.words = updatedTopic.getWord() vì nó sẽ thay đổi tham chiếu, dẫn đến việc không
        // update được list trên RecyclerView
        words.clear();
        words.addAll(updatedTopic.getWord());
        // Sau này sẽ update thêm textView của tên topic và lượng từ ở hàm này nữa
        adapter.notifyDataSetChanged();
        adapterWordListTopic.notifyDataSetChanged();
    }


    public void onClickDeleteTopic(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_topic_confirm_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnApply = dialog.findViewById(R.id.btnApply);

        btnCancel.setOnClickListener(view -> dialog.dismiss());
        btnApply.setOnClickListener(view -> {
            String currentTopicID = topic.getId();
            DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("Topic/" + currentTopicID);
            DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("Folder");

            // Xóa topic từ cơ sở dữ liệu
            topicRef.removeValue().addOnSuccessListener(unused -> {
                // Xóa topic ID từ tất cả các folder liên quan
                folderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot folderSnapshot : dataSnapshot.getChildren()) {
                            Folder folder = folderSnapshot.getValue(Folder.class);
                            if (folder != null && folder.getTopics() != null && folder.getTopics().containsKey(currentTopicID)) {
                                folder.getTopics().remove(currentTopicID);
                                folderRef.child(folderSnapshot.getKey()).setValue(folder);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi
                    }
                });

                // Đóng dialog và quay lại màn hình trước
                onBackPressed();
                finish();
            });
        });

        dialog.show();
    }
}