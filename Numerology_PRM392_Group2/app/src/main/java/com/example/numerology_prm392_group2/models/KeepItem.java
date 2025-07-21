package com.example.numerology_prm392_group2.models;
import java.io.Serializable;
public class KeepItem implements Serializable {
    private String number;
    private double selfBetAmount;
    private double selfBetPercentage;
    private double profitIfNotWin;
    private double profitIfWin;

    public KeepItem(String number, double selfBetAmount, double selfBetPercentage, double profitIfNotWin, double profitIfWin) {
        this.number = number;
        this.selfBetAmount = selfBetAmount;
        this.selfBetPercentage = selfBetPercentage;
        this.profitIfNotWin = profitIfNotWin;
        this.profitIfWin = profitIfWin;
    }

    public String getNumber() {
        return number;
    }

    public double getSelfBetAmount() {
        return selfBetAmount;
    }

    public double getSelfBetPercentage() {
        return selfBetPercentage;
    }

    public double getProfitIfNotWin() {
        return profitIfNotWin;
    }

    public double getProfitIfWin() {
        return profitIfWin;
    }
}