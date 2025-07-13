package com.example.numerology_prm392_group2.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.numerology_prm392_group2.models.BettingInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BettingHistoryManager {
    private static BettingHistoryManager instance;
    private SharedPreferences preferences;
    private Gson gson;
    private static final String PREF_NAME = "BettingHistory";
    private static final String KEY_HISTORY = "betting_history";

    private BettingHistoryManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized BettingHistoryManager getInstance(Context context) {
        if (instance == null) {
            instance = new BettingHistoryManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveBettingList(List<BettingInfo> bettingList) {
        List<SavedBettingList> history = getBettingHistory();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        history.add(new SavedBettingList(timestamp, new ArrayList<>(bettingList)));
        saveHistory(history);
    }

    public List<SavedBettingList> getBettingHistory() {
        String json = preferences.getString(KEY_HISTORY, "[]");
        Type type = new TypeToken<List<SavedBettingList>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void deleteBettingList(String timestamp) {
        List<SavedBettingList> history = getBettingHistory();
        history.removeIf(savedList -> savedList.getTimestamp().equals(timestamp));
        saveHistory(history);
    }

    private void saveHistory(List<SavedBettingList> history) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_HISTORY, gson.toJson(history));
        editor.apply();
    }

    public static class SavedBettingList {
        private String timestamp;
        private List<BettingInfo> bettingList;

        public SavedBettingList(String timestamp, List<BettingInfo> bettingList) {
            this.timestamp = timestamp;
            this.bettingList = bettingList;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public List<BettingInfo> getBettingList() {
            return bettingList;
        }
    }
}