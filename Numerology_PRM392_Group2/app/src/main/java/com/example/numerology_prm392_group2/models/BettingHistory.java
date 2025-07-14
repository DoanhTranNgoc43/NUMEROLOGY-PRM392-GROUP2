package com.example.numerology_prm392_group2.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BettingHistory {
    private String historyId;
    private String title;
    private Date saveDate;
    private List<BettingInfo> bettingList;
    private double totalBetAmount;
    private int totalBets;
    private String lotteryDate;
    private boolean hasResults;
    private double totalPayout;
    private double netProfit;

    public BettingHistory() {
        this.bettingList = new ArrayList<>();
        this.saveDate = new Date();
        this.hasResults = false;
    }

    public BettingHistory(String title, List<BettingInfo> bettingList, String lotteryDate) {
        this.historyId = generateHistoryId();
        this.title = title;
        this.bettingList = new ArrayList<>(bettingList);
        this.lotteryDate = lotteryDate;
        this.saveDate = new Date();
        this.hasResults = false;
        calculateStatistics();
    }

    private String generateHistoryId() {
        return "HIST_" + System.currentTimeMillis();
    }

    private void calculateStatistics() {
        this.totalBets = bettingList.size();
        this.totalBetAmount = 0;
        this.totalPayout = 0;

        for (BettingInfo betting : bettingList) {
            this.totalBetAmount += betting.getBettingAmount();
            if (betting.isWinner()) {
                this.totalPayout += betting.getWinningAmount();
            }
        }

        this.netProfit = this.totalBetAmount - this.totalPayout;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }

    public List<BettingInfo> getBettingList() {
        return bettingList;
    }

    public void setBettingList(List<BettingInfo> bettingList) {
        this.bettingList = bettingList;
        calculateStatistics();
    }

    public double getTotalBetAmount() {
        return totalBetAmount;
    }

    public void setTotalBetAmount(double totalBetAmount) {
        this.totalBetAmount = totalBetAmount;
    }

    public int getTotalBets() {
        return totalBets;
    }

    public void setTotalBets(int totalBets) {
        this.totalBets = totalBets;
    }

    public String getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(String lotteryDate) {
        this.lotteryDate = lotteryDate;
    }

    public boolean isHasResults() {
        return hasResults;
    }

    public void setHasResults(boolean hasResults) {
        this.hasResults = hasResults;
    }

    public double getTotalPayout() {
        return totalPayout;
    }

    public void setTotalPayout(double totalPayout) {
        this.totalPayout = totalPayout;
    }

    public double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(double netProfit) {
        this.netProfit = netProfit;
    }

    public void updateResults(double totalPayout, double netProfit) {
        this.totalPayout = totalPayout;
        this.netProfit = netProfit;
        this.hasResults = true;
    }
}