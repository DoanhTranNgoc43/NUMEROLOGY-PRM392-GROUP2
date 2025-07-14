package com.example.numerology_prm392_group2.models;

public class BettingInfo {
    private String bettorName;
    private String bettingNumber;
    private double bettingAmount;
    private boolean isWinner;
    private double winningAmount;
    private String winningPrize; //  (ĐB, G1, G2, etc.)

    public BettingInfo(String bettorName, String bettingNumber, double bettingAmount) {
        this.bettorName = bettorName;
        this.bettingNumber = bettingNumber;
        this.bettingAmount = bettingAmount;
        this.isWinner = false;
        this.winningAmount = 0;
        this.winningPrize = "";
    }

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

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public double getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(double winningAmount) {
        this.winningAmount = winningAmount;
    }

    public String getWinningPrize() {
        return winningPrize;
    }

    public void setWinningPrize(String winningPrize) {
        this.winningPrize = winningPrize;
    }

    public void resetWinnerStatus() {
        this.isWinner = false;
        this.winningAmount = 0;
        this.winningPrize = "";
    }
}