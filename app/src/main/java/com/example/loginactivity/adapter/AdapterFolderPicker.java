package com.example.loginactivity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.models.Folder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterFolderPicker extends RecyclerView.Adapter<AdapterFolderPicker.MyViewHolder>{
    Context context;
    ArrayList<Folder> folders;

    public AdapterFolderPicker(Context context, ArrayList<Folder> folders) {
        //constructor
        this.context = context;
        this.folders = folders;
    }

    @NonNull
    @Override
    public AdapterFolderPicker.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_folder_item, parent, false);
        return new AdapterFolderPicker.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFolderPicker.MyViewHolder holder, int position) {
        Folder folder = folders.get(position);

        holder.txt_name.setText(folder.getName());
        holder.txt_owner_name.setText(folder.getOwner());
        if(folders.get(position).getTopics() != null){
            holder.txt_total_topics.setText(String.valueOf(folder.getTopics().size()).toString() + " topics");
        } else {
            holder.txt_total_topics.setText("No topic");
        }

        Picasso.get().load(folder.getOwnerAvtUrl()).into(holder.owner_avatar);


        holder.itemView.setOnClickListener(v -> {
            // Cập nhật trạng thái được chọn
            for (Folder f : folders) {
                f.setSelected(false);
            }
            folder.setSelected(true);
            notifyDataSetChanged(); // Thông báo thay đổi dữ liệu để cập nhật UI
        });

        // Cập nhật giao diện dựa trên trạng thái được chọn
        if (folder.isSelected()) {
            holder.itemView.setBackgroundResource(R.drawable.background_item_selected);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.background_item);
        }
    }


    @Override
    public int getItemCount() {
        //count
        return folders.size();
    }

    public void newWordList(ArrayList<Folder> newList) {
        folders = newList;
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_owner_name, txt_total_topics;

        ImageView owner_avatar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_owner_name = itemView.findViewById(R.id.txt_owner_name);
            txt_total_topics = itemView.findViewById(R.id.txt_total_topics);
            owner_avatar = itemView.findViewById(R.id.owner_avatar);
        }
    }
}
