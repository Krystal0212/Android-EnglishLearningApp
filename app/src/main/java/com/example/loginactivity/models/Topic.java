package com.example.loginactivity.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Topic implements Parcelable {
    String access;
    String createDate;
    String id;
    String title;
    String owner;
    String ownerAvtUrl;
    ArrayList<Participant> participant;
    ArrayList<Word> word;
    public Topic(String access, String createDate, String id, String title, String owner, String ownerAvtUrl, ArrayList<Participant> participant, ArrayList<Word> word) {
        this.access = access;
        this.createDate = createDate;
        this.id = id;
        this.title = title;
        this.owner = owner;
        this.participant = participant;
        this.word = word;
        this.ownerAvtUrl = ownerAvtUrl;
    }

    protected Topic(Parcel in) {
        access = in.readString();
        createDate = in.readString();
        id = in.readString();
        title = in.readString();
        owner = in.readString();
        ownerAvtUrl = in.readString();
        participant = in.createTypedArrayList(Participant.CREATOR);
        word = in.createTypedArrayList(Word.CREATOR);
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwnerAvtUrl() {
        return ownerAvtUrl;
    }

    public void setOwnerAvtUrl(String ownerAvtUrl) {
        this.ownerAvtUrl = ownerAvtUrl;
    }
    public Topic(){

    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<Participant> getParticipant() {
        return participant;
    }

    public void setParticipant(ArrayList<Participant> participant) {
        this.participant = participant;
    }

    public ArrayList<Word> getWord() {
        return word;
    }

    public void setWord(ArrayList<Word> word) {
        this.word = word;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(access);
        dest.writeString(createDate);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(owner);
        dest.writeString(ownerAvtUrl);
        dest.writeTypedList(participant);
        dest.writeTypedList(word);
    }
}
