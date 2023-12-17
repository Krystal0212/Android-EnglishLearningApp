package com.example.loginactivity.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question implements Parcelable {
    private String firstOtherWord;
    private String secondOtherWord;

    public Question(Parcel in) {
        firstOtherWord = in.readString();
        secondOtherWord = in.readString();
        thirdOtherWord = in.readString();
        rightAnswerPosition = in.readInt();
        testMode = in.readString();
        word = in.readParcelable(Word.class.getClassLoader());
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    private String thirdOtherWord;
    private int rightAnswerPosition;
    private String testMode;
    private Word word;

    public String getTestMode() {
        return testMode;
    }

    public void setTestMode(String testMode) {
        this.testMode = testMode;
    }

    public Question(Word word, String[] otherWords, String testMode) {
        this.word = new Word(word);
        this.firstOtherWord = otherWords[0];
        this.secondOtherWord = otherWords[1];
        this.thirdOtherWord = otherWords[2];
        this.testMode = testMode;
    }

    public String getThirdOtherWord() {
        return thirdOtherWord;
    }

    public void setThirdOtherWord(String thirdOtherWord) {
        this.thirdOtherWord = thirdOtherWord;
    }

    public String getSecondOtherWord() {
        return secondOtherWord;
    }

    public void setSecondOtherWord(String secondOtherWord) {
        this.secondOtherWord = secondOtherWord;
    }

    public String getFirstOtherWord() {
        return firstOtherWord;
    }

    public void setFirstOtherWord(String firstOtherWord) {
        this.firstOtherWord = firstOtherWord;
    }

    public String[] getAnswerOptions() {
        List<String> options = new ArrayList<>();

        String rightAnswer = word.getEnglish();
        if(testMode.equals("english")){
            rightAnswer = word.getVietnamese();
        }

        options.add(rightAnswer);

        options.add(firstOtherWord);
        options.add(secondOtherWord);
        options.add(thirdOtherWord);

        Collections.shuffle(options);
        rightAnswerPosition = options.indexOf(rightAnswer);

        String[] answerOptions = new String[options.size()];
        answerOptions = options.toArray(answerOptions);

        return answerOptions;
    }

    public int getRightAnswerPosition() {
        return rightAnswerPosition;
    }

    public void setRightAnswerPosition(int rightAnswerPosition) {
        this.rightAnswerPosition = rightAnswerPosition;
    }

    @Override
    public String toString() {
        return "Question{" +
                "firstOtherWord='" + firstOtherWord + '\'' +
                ", secondOtherWord='" + secondOtherWord + '\'' +
                ", thirdOtherWord='" + thirdOtherWord + '\'' +
                ", rightAnswerPosition=" + rightAnswerPosition +
                ", testMode='" + testMode + '\'' +
                ", word=" + word.toString() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(firstOtherWord);
        dest.writeString(secondOtherWord);
        dest.writeString(thirdOtherWord);
        dest.writeInt(rightAnswerPosition);
        dest.writeString(testMode);
        dest.writeParcelable(word, flags);
    }
}
