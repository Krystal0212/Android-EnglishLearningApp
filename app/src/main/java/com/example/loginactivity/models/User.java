package com.example.loginactivity.models;

public class User {
    String email;
    String avtUrl;
    String displayName;
    Boolean verify;
    String id;
    RecentActivity recentActivity;
    public User() {
    }

    public User(String email, String displayName, String avtUrl, Boolean verify, String id) {
        this.email = email;
        this.avtUrl = avtUrl;
        this.displayName = displayName;
        this.verify = verify;
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvtUrl() {
        return avtUrl;
    }

    public void setAvtUrl(String avtUrl) {
        this.avtUrl = avtUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public RecentActivity getRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(RecentActivity recentActivity) {
        this.recentActivity = recentActivity;
    }
}
