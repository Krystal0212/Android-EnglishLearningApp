package com.example.loginactivity.models;

public class Word {
    String english;
    String vietnamese;

    String description;

    public Word(String english, String vietnamese, String description) {
        this.english = english;
        this.vietnamese = vietnamese;
        this.description = description;
    }
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

}
