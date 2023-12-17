package com.example.loginactivity.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Word implements Parcelable {
    String english;
    String vietnamese;

    @Override
    public String toString() {
        return "Word{" +
                "english='" + english + '\'' +
                ", vietnamese='" + vietnamese + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    String description;

    public Word(String english, String vietnamese, String description) {
        this.english = english;
        this.vietnamese = vietnamese;
        this.description = description;
    }

    public Word (){
    }
    public Word (Word word){
        english = word.getEnglish();
        vietnamese = word.getVietnamese();
        description = word.getDescription();
    }

    protected Word(Parcel in) {
        english = in.readString();
        vietnamese = in.readString();
        description = in.readString();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
        this.vietnamese = vietnamese;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(english);
        dest.writeString(vietnamese);
        dest.writeString(description);
    }
}
