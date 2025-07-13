package com.example.numerology_prm392_group2.service;

import com.example.numerology_prm392_group2.models.BettingInfo;
import com.example.numerology_prm392_group2.models.LotteryResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayoutCalculator {
    private static final double DEFAULT_PAYOUT_RATE = 70.0;
    private static PayoutCalculator instance;

    private PayoutCalculator() {}

    public static synchronized PayoutCalculator getInstance() {
        if (instance == null) {
            instance = new PayoutCalculator();
        }
        return instance;
    }

    public static class PayoutResult {
        private boolean isWinner;
        private double payoutAmount;
        private String winningNumber;
        private double betAmount;
        private String bettorName;

        public PayoutResult(boolean isWinner, double payoutAmount, String winningNumber,
                            double betAmount, String bettorName) {
            this.isWinner = isWinner;
            this.payoutAmount = payoutAmount;
            this.winningNumber = winningNumber;
            this.betAmount = betAmount;
            this.bettorName = bettorName;
        }


        public boolean isWinner() { return isWinner; }
        public double getPayoutAmount() { return payoutAmount; }
        public String getWinningNumber() { return winningNumber; }
        public double getBetAmount() { return betAmount; }
        public String getBettorName() { return bettorName; }

        public double getNetProfit() {
            return payoutAmount - betAmount;
        }
    }

    public PayoutResult calculatePayout(BettingInfo bettingInfo, LotteryResult lotteryResult) {
        return calculatePayout(bettingInfo, lotteryResult, DEFAULT_PAYOUT_RATE);
    }

    public PayoutResult calculatePayout(BettingInfo bettingInfo, LotteryResult lotteryResult, double payoutRate) {
        String bettingNumber = bettingInfo.getBettingNumber();
        double bettingAmount = bettingInfo.getBettingAmount();
        String bettorName = bettingInfo.getBettorName();

        if (bettingNumber.length() == 1) {
            bettingNumber = "0" + bettingNumber;
        }

        // Chỉ xét 2 số cuối của giải đặc biệt
        String specialPrize = lotteryResult.getSpecialPrize();
        String specialTwoDigits = (specialPrize != null && specialPrize.length() >= 2)
                ? specialPrize.substring(specialPrize.length() - 2)
                : "";

        boolean isWinner = bettingNumber.equals(specialTwoDigits);

        double payoutAmount = isWinner ? bettingAmount * payoutRate : 0;
        String matchedNumber = isWinner ? bettingNumber : null;

        return new PayoutResult(isWinner, payoutAmount, matchedNumber, bettingAmount, bettorName);
    }


    public Map<String, PayoutResult> calculateAllPayouts(List<BettingInfo> bettingList, LotteryResult lotteryResult) {
        return calculateAllPayouts(bettingList, lotteryResult, DEFAULT_PAYOUT_RATE);
    }

    public Map<String, PayoutResult> calculateAllPayouts(List<BettingInfo> bettingList,
                                                         LotteryResult lotteryResult, double payoutRate) {
        Map<String, PayoutResult> results = new HashMap<>();

        for (BettingInfo bettingInfo : bettingList) {
            String key = bettingInfo.getBettorName() + "_" + bettingInfo.getBettingNumber();
            PayoutResult result = calculatePayout(bettingInfo, lotteryResult, payoutRate);
            results.put(key, result);
        }

        return results;
    }

    public double calculateTotalPayout(List<BettingInfo> bettingList, LotteryResult lotteryResult) {
        return calculateTotalPayout(bettingList, lotteryResult, DEFAULT_PAYOUT_RATE);
    }

    public double calculateTotalPayout(List<BettingInfo> bettingList, LotteryResult lotteryResult, double payoutRate) {
        double totalPayout = 0;
        Map<String, PayoutResult> results = calculateAllPayouts(bettingList, lotteryResult, payoutRate);

        for (PayoutResult result : results.values()) {
            totalPayout += result.getPayoutAmount();
        }

        return totalPayout;
    }

    public double calculateTotalBetAmount(List<BettingInfo> bettingList) {
        double totalBetAmount = 0;
        for (BettingInfo bettingInfo : bettingList) {
            totalBetAmount += bettingInfo.getBettingAmount();
        }
        return totalBetAmount;
    }

    public double calculateNetProfit(List<BettingInfo> bettingList, LotteryResult lotteryResult) {
        return calculateNetProfit(bettingList, lotteryResult, DEFAULT_PAYOUT_RATE);
    }

    public double calculateNetProfit(List<BettingInfo> bettingList, LotteryResult lotteryResult, double payoutRate) {
        double totalBetAmount = calculateTotalBetAmount(bettingList);
        double totalPayout = calculateTotalPayout(bettingList, lotteryResult, payoutRate);
        return totalBetAmount - totalPayout;
    }
}