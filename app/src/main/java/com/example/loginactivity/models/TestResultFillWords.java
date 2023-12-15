package com.example.loginactivity.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TestResultFillWords implements Parcelable {
    private Word word;
    private String userAnswer;
    private boolean isCorrect;

    public TestResultFillWords(Word word, String userAnswer, boolean isCorrect) {
        this.word = word;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
    }
    public TestResultFillWords(){

    }

    protected TestResultFillWords(Parcel in) {
        word = in.readParcelable(Word.class.getClassLoader());
        userAnswer = in.readString();
        isCorrect = in.readByte() != 0;
    }

    public static final Creator<TestResultFillWords> CREATOR = new Creator<TestResultFillWords>() {
        @Override
        public TestResultFillWords createFromParcel(Parcel in) {
            return new TestResultFillWords(in);
        }

        @Override
        public TestResultFillWords[] newArray(int size) {
            return new TestResultFillWords[size];
        }
    };

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(word, flags);
        dest.writeString(userAnswer);
        dest.writeByte((byte) (isCorrect ? 1 : 0));
    }
}
