package com.example.numerology_prm392_group2.models;

import com.google.gson.annotations.SerializedName;

public class BetRequest {
    @SerializedName("bettorName")
    private String bettorName;

    @SerializedName("number")
    private int number;

    @SerializedName("amount")
    private double amount;

    @SerializedName("userId")
    private String userId;

    public BetRequest() {}

    public BetRequest(String bettorName, int number, double amount, String userId) {
        this.bettorName = bettorName;
        this.number = number;
        this.amount = amount;
        this.userId = userId;
    }

    // Getters and Setters
    public String getBettorName() {
        return bettorName;
    }

    public void setBettorName(String bettorName) {
        this.bettorName = bettorName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}