package com.example.loginactivity.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class Folder implements Parcelable {
    String name;
    String owner;
    String id;
    String ownerAvtUrl;
    HashMap<String, Boolean> topics;
    public Folder(){

    }

    public Folder(String name, String owner, String id, String ownerAvtUrl, HashMap<String, Boolean> topics) {
        this.name = name;
        this.owner = owner;
        this.id = id;
        this.ownerAvtUrl = ownerAvtUrl;
        this.topics = topics;
    }

    protected Folder(Parcel in) {
        name = in.readString();
        owner = in.readString();
        id = in.readString();
        ownerAvtUrl = in.readString();
        topics = in.readHashMap(ClassLoader.getSystemClassLoader()); // Thêm dòng này để giải nén topics
    }

    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel in) {
            return new Folder(in);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerAvtUrl() {
        return ownerAvtUrl;
    }

    public void setOwnerAvtUrl(String ownerAvtUrl) {
        this.ownerAvtUrl = ownerAvtUrl;
    }

    public HashMap<String, Boolean> getTopics() {
        return topics;
    }

    public void setTopics(HashMap<String, Boolean> topics) {
        this.topics = topics;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(owner);
        dest.writeString(id);
        dest.writeString(ownerAvtUrl);
        dest.writeMap(topics);
    }
}
