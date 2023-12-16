package com.example.loginactivity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;
import com.example.loginactivity.models.Photo;

import java.util.List;

public class HomeIntroAdapter extends  RecyclerView.Adapter<HomeIntroAdapter.PhotoViewHolder> {
    public HomeIntroAdapter(List<Photo> mListPhoto) {
        this.mListPhoto = mListPhoto;
    }

    private List<Photo> mListPhoto;
    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = mListPhoto.get(position);
        if(photo == null){
            return;
        }
        holder.img_photo.setImageResource(photo.getResourceID());
    }

    @Override
    public int getItemCount() {
        if(mListPhoto != null ){
            return mListPhoto.size();
        }
        return 0;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_photo;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            img_photo = itemView.findViewById(R.id.img_photo);
        }
    }
}
