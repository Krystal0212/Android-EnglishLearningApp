package com.example.loginactivity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.FolderDetailActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.TopicDetailActivity;
import com.example.loginactivity.models.Folder;
import com.example.loginactivity.models.Topic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterMyFolder extends RecyclerView.Adapter<AdapterMyFolder.MyViewHolder>{
    Context context;
    ArrayList<Folder> folders;

    public AdapterMyFolder(Context context, ArrayList<Folder> folders) {
        //constructor
        this.context = context;
        this.folders = folders;
    }

    @NonNull
    @Override
    public AdapterMyFolder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_folder_item, parent, false);
        return new AdapterMyFolder.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMyFolder.MyViewHolder holder, int position) {
        holder.txt_name.setText(folders.get(position).getName());
        holder.txt_owner_name.setText(folders.get(position).getOwner());
        if(folders.get(position).getTopics() != null){
            holder.txt_total_topics.setText(String.valueOf(folders.get(position).getTopics().size()).toString() + " topics");
        } else {
            holder.txt_total_topics.setText("No topic");
        }

        Picasso.get().load(folders.get(position).getOwnerAvtUrl()).into(holder.owner_avatar);


        holder.itemView.setOnClickListener(v -> {
            onClickWord(folders.get(position));
        });
    }

    private void onClickWord(Folder clickedFolder) {
        Intent intent = new Intent(context, FolderDetailActivity.class);
        intent.putExtra("folder", clickedFolder);
        context.startActivity(intent);
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
