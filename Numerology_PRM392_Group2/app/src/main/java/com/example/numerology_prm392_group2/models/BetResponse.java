package com.example.numerology_prm392_group2.models;

import com.google.gson.annotations.SerializedName;

public class BetResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("bet")
    private Bet bet;

    public BetResponse() {}

    // Getters and Setters
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

    public Bet getBet() {
        return bet;
    }

    public void setBet(Bet bet) {
        this.bet = bet;
    }

    // Bet class
    public static class Bet {
        @SerializedName("id")
        private String id;

        @SerializedName("bettorName")
        private String bettorName;

        @SerializedName("number")
        private int number;

        @SerializedName("amount")
        private double amount;

        @SerializedName("createdDate")
        private String createdDate;

        @SerializedName("userId")
        private String userId;

        public Bet() {}

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}