package com.example.loginactivity.models;

public class RecentActivity {
    String topicTitle;
    String owner;
    long lastVisitedTime;
    int score;

    public RecentActivity(String topicTitle, String owner, long lastVisitedTime, int score) {
        this.topicTitle = topicTitle;
        this.owner = owner;
        this.lastVisitedTime = lastVisitedTime;
        this.score = score;
    }

    public RecentActivity() {
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public long getLastVisitedTime() {
        return lastVisitedTime;
    }

    public void setLastVisitedTime(long lastVisitedTime) {
        this.lastVisitedTime = lastVisitedTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
