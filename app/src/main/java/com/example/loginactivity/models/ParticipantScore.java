package com.example.loginactivity.models;

public class ParticipantScore {
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String userID;
    public String userName;
    public String userImage;
    public int totalScore;

    public ParticipantScore(String userID, int totalScore) {
        this.userID = userID;
        this.totalScore = totalScore;
        // userName và userImage sẽ được đặt sau khi truy xuất thông tin từ userID
    }

    // Getters và Setters cho tất cả thuộc tính nếu cần
}
