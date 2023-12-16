package com.example.loginactivity.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TestResultMultipleChoices implements Parcelable {
    private Word word;
    private boolean isCorrect;

    public TestResultMultipleChoices(Word word, boolean isCorrect) {
        this.word = word;
        this.isCorrect = isCorrect;
    }
    public TestResultMultipleChoices(){

    }

    protected TestResultMultipleChoices(Parcel in) {
        word = in.readParcelable(Word.class.getClassLoader());
        isCorrect = in.readByte() != 0;
    }

    public static final Parcelable.Creator<TestResultMultipleChoices> CREATOR = new Parcelable.Creator<TestResultMultipleChoices>() {
        @Override
        public TestResultMultipleChoices createFromParcel(Parcel in) {
            return new TestResultMultipleChoices(in);
        }

        @Override
        public TestResultMultipleChoices[] newArray(int size) {
            return new TestResultMultipleChoices[size];
        }
    };

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
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
        dest.writeByte((byte) (isCorrect ? 1 : 0));
    }
}
