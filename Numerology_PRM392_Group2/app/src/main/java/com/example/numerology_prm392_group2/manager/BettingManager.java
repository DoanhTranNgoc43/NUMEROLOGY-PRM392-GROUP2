package com.example.numerology_prm392_group2.manager;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.numerology_prm392_group2.models.BettingInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BettingManager {
    private static BettingManager instance;
    private List<BettingInfo> bettingList;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private static final String PREFS_NAME = "BettingPrefs";
    private static final String KEY_BETTING_LIST = "betting_list";

    private BettingManager() {
        bettingList = new ArrayList<>();
        gson = new Gson();
    }

    public static synchronized BettingManager getInstance() {
        if (instance == null) {
            instance = new BettingManager();
        }
        return instance;
    }


    public void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            loadBettingList();
        }
    }

    public void addBetting(BettingInfo bettingInfo) {
        bettingList.add(bettingInfo);
        saveBettingList();
    }

    public List<BettingInfo> getBettingList() {
        return new ArrayList<>(bettingList);
    }

    public void clearList() {
        bettingList.clear();
        saveBettingList();
    }


    private void saveBettingList() {
        if (sharedPreferences != null) {
            String json = gson.toJson(bettingList);
            sharedPreferences.edit()
                    .putString(KEY_BETTING_LIST, json)
                    .apply();
        }
    }

    private void loadBettingList() {
        if (sharedPreferences != null) {
            String json = sharedPreferences.getString(KEY_BETTING_LIST, "");
            if (!json.isEmpty()) {
                Type type = new TypeToken<List<BettingInfo>>(){}.getType();
                List<BettingInfo> savedList = gson.fromJson(json, type);
                if (savedList != null) {
                    bettingList = savedList;
                }
            }
        }
    }

    // Cập nhật danh sách và lưu
    public void updateBettingList(List<BettingInfo> newList) {
        bettingList.clear();
        bettingList.addAll(newList);
        saveBettingList();
    }

    // Xóa một item cụ thể
    public void removeBetting(int position) {
        if (position >= 0 && position < bettingList.size()) {
            bettingList.remove(position);
            saveBettingList();
        }
    }

    // Xóa betting theo thông tin cụ thể
    public void removeBetting(BettingInfo bettingInfo) {
        bettingList.remove(bettingInfo);
        saveBettingList();
    }

    // Lấy tổng số lượng betting
    public int getTotalBettingCount() {
        return bettingList.size();
    }

    // Lấy tổng số tiền cược
    public double getTotalBettingAmount() {
        double total = 0;
        for (BettingInfo betting : bettingList) {
            total += betting.getBettingAmount();
        }
        return total;
    }
}