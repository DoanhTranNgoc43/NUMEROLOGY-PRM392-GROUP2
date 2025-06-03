package com.example.numerology_prm392_group2;

public class NumberStats {
    private String number;
    private double totalAmount;
    private int ticketCount;
    private String riskLevel;

    public NumberStats(String number, double totalAmount, int ticketCount) {
        this.number = number;
        this.totalAmount = totalAmount;
        this.ticketCount = ticketCount;
        this.riskLevel = calculateRiskLevel();
    }

    private String calculateRiskLevel() {
        if (totalAmount > 500000) {
            return "Rất Cao";
        } else if (totalAmount > 200000) {
            return "Cao";
        } else if (totalAmount > 100000) {
            return "Trung Bình";
        } else {
            return "Thấp";
        }
    }

    public String getRiskColor() {
        switch (riskLevel) {
            case "Rất Cao":
                return "#D32F2F";
            case "Cao":
                return "#FF5722";
            case "Trung Bình":
                return "#FF9800";
            case "Thấp":
                return "#4CAF50";
            default:
                return "#666666";
        }
    }

    // Getters
    public String getNumber() { return number; }
    public double getTotalAmount() { return totalAmount; }
    public int getTicketCount() { return ticketCount; }
    public String getRiskLevel() { return riskLevel; }
}