package com.example.loginactivity.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.FlashCardActivity;
import com.example.loginactivity.TopicDetailActivity;
import com.example.loginactivity.models.Participant;
import com.example.loginactivity.models.Topic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPublicTopic extends RecyclerView.Adapter<AdapterPublicTopic.MyViewHolder> {
    Context context;
    ArrayList<Topic> topics;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public AdapterPublicTopic(Context context, ArrayList<Topic> topics) {
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
            onClickTopic(topics.get(position));
        });
    }

    private void onClickTopic(Topic clickedTopic) {
        ArrayList<Participant> participants = new ArrayList<>();
        participants = clickedTopic.getParticipant();
        Boolean isParticipant = false;
        for(Participant i : participants){
            if(i.getUserID().equals(user.getUid())){
                Intent intent = new Intent(context, TopicDetailActivity.class);
                intent.putExtra("topic", clickedTopic);
                context.startActivity(intent);
                isParticipant = true;
                break;
            }
        }
        if (isParticipant == false){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_topic_dialog);
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            Button btnCancel = dialog.findViewById(R.id.btnCancel);
            Button btnApply = dialog.findViewById(R.id.btnApply);

            btnCancel.setOnClickListener(view -> dialog.dismiss());
            ArrayList<Participant> finalParticipants = participants;
            btnApply.setOnClickListener(view -> {
                String topicID = clickedTopic.getId();
                finalParticipants.add(new Participant(user.getUid()));
                DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("Topic/" + topicID);

                topicRef.child("participant").setValue(finalParticipants)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, TopicDetailActivity.class);
                                intent.putExtra("topic", clickedTopic);
                                context.startActivity(intent);
                            }
                        });
            });
            dialog.show();
        }
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
