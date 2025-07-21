package com.example.numerology_prm392_group2.models;

public class Recommendation {
    private String number;
    private String type; // "KEEP", "FORWARD", or "NEUTRAL"
    private int frequency;
    private String message;

    public Recommendation(String number, String type, int frequency, String message) {
        this.number = number;
        this.type = type;
        this.frequency = frequency;
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getMessage() {
        return message;
    }
}



