package com.example.numerology_prm392_group2.models;

public class Notification {
    private String title;
    private String message;
    private String timestamp;

    public Notification(String title, String message, String timestamp) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}