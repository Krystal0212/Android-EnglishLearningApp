package com.example.loginactivity.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.models.Word;

import java.util.ArrayList;

public class AdapterWordListAddTopic extends RecyclerView.Adapter<AdapterWordListAddTopic.MyViewHolder> {
    Context context;
    public ArrayList<Word> words;

    public AdapterWordListAddTopic(Context context, ArrayList<Word> words) {
        //constructor
        this.context = context;
        this.words = words;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_add_topic_word_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Word word = words.get(position);
        holder.txt_english.setText(word.getEnglish());
        holder.txt_vietnamese.setText(word.getVietnamese());
        if(word.getDescription().equals("")){
            holder.txt_description.setText("No description");
        } else {
            holder.txt_description.setText(word.getDescription());
        }

        holder.itemView.setOnClickListener(v -> {
            onClickWord(holder, position);
        });

        //xu ly xoa word trong list
        holder.imageTrash.setOnClickListener(v -> {
            for(int i = 0; i < words.size(); i++){
                if(word.getEnglish().equals(words.get(i).getEnglish())){
                    words.remove(words.get(i));
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void onClickWord(MyViewHolder holder, int position) {
        Word word = words.get(position);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_word_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edt_term = dialog.findViewById(R.id.edt_term);
        EditText edt_meaning = dialog.findViewById(R.id.edt_meaning);
        EditText edt_description = dialog.findViewById(R.id.edt_description);
        TextView title = dialog.findViewById(R.id.txtTitle);

        edt_term.setText(holder.txt_english.getText().toString().trim());
        edt_meaning.setText(holder.txt_vietnamese.getText().toString().trim());
        edt_description.setText(holder.txt_description.getText().toString().trim());
        title.setText("Edit your word");

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnApply = dialog.findViewById(R.id.btnApply);

        btnCancel.setOnClickListener(view -> dialog.dismiss());
        btnApply.setOnClickListener(view -> {
            // gia tri sau khi edit cac kieu
            String term = edt_term.getText().toString().trim();
            String meaning = edt_meaning.getText().toString().trim();
            String description = edt_description.getText().toString().trim();
            if(term.isEmpty() || meaning.isEmpty()){
                Toast.makeText(context, "Please enter term and meaning.", Toast.LENGTH_SHORT).show();
            }
            else{
                Boolean flag = false;
                Boolean termDup = false;
                Word newWord = new Word(term, meaning, description);
                //kiem tra trong list co term này chưa
                for(int i = 0; i < words.size(); i++){
                    if(words.get(i).getEnglish().equals(newWord.getEnglish())){
                        termDup = true;
                        break;
                    }
                }
                if(termDup && !words.get(position).getEnglish().equals(newWord.getEnglish())){
                    Toast.makeText(context, "This term has been added.", Toast.LENGTH_SHORT).show();
                    flag = true;
                }

                if(flag == false){
                    dialog.dismiss();
                    words.set(position, newWord);
                    notifyDataSetChanged();
                }
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        //count
        return words.size();
    }

    public void newWordList(ArrayList<Word> newList) {
        words = newList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_english, txt_vietnamese, txt_description;

        ImageView imageTrash;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_english = itemView.findViewById(R.id.txt_english);
            txt_vietnamese = itemView.findViewById(R.id.txt_vietnamese);
            txt_description = itemView.findViewById(R.id.txt_description);
            imageTrash = itemView.findViewById(R.id.imageTrash);
        }
    }
}
