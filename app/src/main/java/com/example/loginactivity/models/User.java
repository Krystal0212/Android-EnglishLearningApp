package com.example.loginactivity.models;

public class User {
    String email;
    String avtUrl;
    String displayName;
    Boolean verify;

    public User() {
    }

    public User(String email, String displayName, String avtUrl, Boolean verify) {
        this.email = email;
        this.avtUrl = avtUrl;
        this.displayName = displayName;
        this.verify = verify;
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

}
