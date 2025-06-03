package com.example.numerology_prm392_group2;

import java.io.Serializable;

public class BettingInfo implements Serializable {
    private String bettorName;
    private String bettingNumber;
    private double bettingAmount;

    public BettingInfo() {}

    public BettingInfo(String bettorName, String bettingNumber, double bettingAmount) {
        this.bettorName = bettorName;
        this.bettingNumber = bettingNumber;
        this.bettingAmount = bettingAmount;
    }

    // Getters and Setters
    public String getBettorName() {
        return bettorName;
    }

    public void setBettorName(String bettorName) {
        this.bettorName = bettorName;
    }

    public String getBettingNumber() {
        return bettingNumber;
    }

    public void setBettingNumber(String bettingNumber) {
        this.bettingNumber = bettingNumber;
    }

    public double getBettingAmount() {
        return bettingAmount;
    }

    public void setBettingAmount(double bettingAmount) {
        this.bettingAmount = bettingAmount;
    }
}
