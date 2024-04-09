package com.example.medihealth.models;

import com.google.firebase.Timestamp;

public class NotificationModel {
    private String title;
    private String body;
    private Timestamp timestamp;
    private boolean seen;
    private String userId;

    public NotificationModel() {
    }

    public NotificationModel(String title, String body, Timestamp timestamp, boolean seen, String userId) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
        this.seen = seen;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
