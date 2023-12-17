package com.example.loginactivity.models;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question extends Word implements Parcelable {
    private String firstOtherWord;
    private String secondOtherWord;

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
        super(word);
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

        String rightAnswer = getEnglish();
        if(testMode.equals("english")){
            rightAnswer = getVietnamese();
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
}
