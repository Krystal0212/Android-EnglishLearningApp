package com.example.loginactivity.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Participant implements Parcelable {


    String userID;
    int multipleChoicesResult;
    int fillWordResult;

    public Participant(String userID) {
        this.userID = userID;
    }
    public Participant (){

    }
    public Participant(String userID, int multipleChoicesResult, int fillWordResult) {
        this.userID = userID;
        this.multipleChoicesResult = multipleChoicesResult;
        this.fillWordResult = fillWordResult;
    }

    protected Participant(Parcel in) {
        userID = in.readString();
        multipleChoicesResult = in.readInt();
        fillWordResult = in.readInt();
    }

    public static final Creator<Participant> CREATOR = new Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public int getMultipleChoicesResult() {
        return multipleChoicesResult;
    }

    public void setMultipleChoicesResult(int multipleChoicesResult) {
        this.multipleChoicesResult = multipleChoicesResult;
    }

    public int getFillWordResult() {
        return fillWordResult;
    }

    public void setFillWordResult(int fillWordResult) {
        this.fillWordResult = fillWordResult;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeInt(multipleChoicesResult);
        dest.writeInt(fillWordResult);
    }
}
