package com.example.numerology_prm392_group2.models;

import java.io.Serializable;

public class ForwardItem implements Serializable {
    private String number;
    private double forwardAmount;
    private double forwardPercentage;
    private double commission;

    public ForwardItem(String number, double forwardAmount, double forwardPercentage, double commission) {
        this.number = number;
        this.forwardAmount = forwardAmount;
        this.forwardPercentage = forwardPercentage;
        this.commission = commission;
    }

    public String getNumber() {
        return number;
    }

    public double getForwardAmount() {
        return forwardAmount;
    }

    public double getForwardPercentage() {
        return forwardPercentage;
    }

    public double getCommission() {
        return commission;
    }
}