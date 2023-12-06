package com.example.loginactivity.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Topic {
    String access;
    String createDate;
    String id;
    String title;
    String owner;
    ArrayList<Participant> participant;
    ArrayList<Word> word;
    HashMap<String, Boolean> belongToFolder;

    public Topic(String access, String createDate, String id, String title, String owner, ArrayList<Participant> participant, ArrayList<Word> word) {
        this.access = access;
        this.createDate = createDate;
        this.id = id;
        this.title = title;
        this.owner = owner;
        this.participant = participant;
        this.word = word;
        HashMap<String,Boolean> belong = new HashMap<>();
        belong.put("temp_folder", false);
        this.belongToFolder = belong;
    }

    public Topic(){

    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<Participant> getParticipant() {
        return participant;
    }

    public void setParticipant(ArrayList<Participant> participant) {
        this.participant = participant;
    }

    public ArrayList<Word> getWord() {
        return word;
    }

    public void setWord(ArrayList<Word> word) {
        this.word = word;
    }

    public HashMap<String, Boolean> getBelongToFolder() {
        return belongToFolder;
    }

    public void setBelongToFolder(HashMap<String, Boolean> belongToFolder) {
        this.belongToFolder = belongToFolder;
    }
}
