package com.wikav.coromobileapp.models;

public class myList {
    String friendName;
    String date;

    public myList(String friendName, String date) {
        this.friendName = friendName;
        this.date = date;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
