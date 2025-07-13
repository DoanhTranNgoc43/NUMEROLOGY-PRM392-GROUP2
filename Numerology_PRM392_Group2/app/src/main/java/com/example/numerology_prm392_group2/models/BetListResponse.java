package com.example.numerology_prm392_group2.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BetListResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("bets")
    private List<BetResponse.Bet> bets;

    @SerializedName("totalAmount")
    private double totalAmount;

    @SerializedName("totalCount")
    private int totalCount;

    public BetListResponse() {}

    public boolean isSuccess() {
        return "Success".equalsIgnoreCase(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BetResponse.Bet> getBets() {
        return bets;
    }

    public void setBets(List<BetResponse.Bet> bets) {
        this.bets = bets;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}