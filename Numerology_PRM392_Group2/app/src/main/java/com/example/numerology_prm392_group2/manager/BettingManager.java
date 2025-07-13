package com.example.numerology_prm392_group2;

import java.util.ArrayList;
import java.util.List;

public class BettingManager {
    private static BettingManager instance;
    private List<BettingInfo> bettingList;

    private BettingManager() {
        bettingList = new ArrayList<>();
    }

    public static synchronized BettingManager getInstance() {
        if (instance == null) {
            instance = new BettingManager();
        }
        return instance;
    }

    public void addBetting(BettingInfo bettingInfo) {
        bettingList.add(bettingInfo);
    }

    public List<BettingInfo> getBettingList() {
        return new ArrayList<>(bettingList);
    }

    public void clearList() {
        bettingList.clear();
    }
}

