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

import com.example.loginactivity.R;
import com.example.loginactivity.FlashCardActivity;
import com.example.loginactivity.TopicDetailActivity;
import com.example.loginactivity.models.Topic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterMyTopic extends RecyclerView.Adapter<AdapterMyTopic.MyViewHolder> {
    Context context;
    ArrayList<Topic> topics;

    public AdapterMyTopic(Context context, ArrayList<Topic> topics) {
        //constructor
        this.context = context;
        this.topics = topics;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_topic_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_title.setText(topics.get(position).getTitle());
        holder.txt_total_terms.setText(String.valueOf(topics.get(position).getWord().size()).toString() + " terms");
        holder.txt_owner_name.setText(topics.get(position).getOwner());
        holder.txt_total_participants.setText(String.valueOf(topics.get(position).getParticipant().size()).toString() + " participants");

        Picasso.get().load(topics.get(position).getOwnerAvtUrl()).into(holder.owner_avatar);


        holder.itemView.setOnClickListener(v -> {
            onClickWord(topics.get(position));
        });
    }

    private void onClickWord(Topic clickedTopic) {
        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra("topic", clickedTopic);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        //count
        return topics.size();
    }

    public void newWordList(ArrayList<Topic> newList) {
        topics = newList;
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title, txt_total_terms, txt_owner_name, txt_total_participants;

        ImageView owner_avatar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.txt_title);
            txt_total_terms = itemView.findViewById(R.id.txt_total_terms);
            txt_owner_name = itemView.findViewById(R.id.txt_owner_name);
            txt_total_participants = itemView.findViewById(R.id.txt_total_participants);
            owner_avatar = itemView.findViewById(R.id.owner_avatar);

        }
    }
}
