package com.example.numerology_prm392_group2.controller;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.manager.BettingManager;
import com.example.numerology_prm392_group2.models.BettingInfo;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DailySummaryActivity extends AppCompatActivity {

    private TextView textViewPlayerCount;
    private TextView textViewTotalBetAmount;
    private TextView textViewMostFrequentNumber;
    private TextView textViewMarquee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_summary);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        loadSummaryData();
    }

    private void initializeComponents() {
        textViewPlayerCount = findViewById(R.id.textViewPlayerCount);
        textViewTotalBetAmount = findViewById(R.id.textViewTotalBetAmount);
        textViewMostFrequentNumber = findViewById(R.id.textViewMostFrequentNumber);
        textViewMarquee = findViewById(R.id.textViewMarquee);
        textViewMarquee.setSelected(true);
    }

    private void loadSummaryData() {
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        int playerCount = bettingList.size();
        textViewPlayerCount.setText(String.valueOf(playerCount));

        double totalBetAmount = bettingList.stream()
                .mapToDouble(BettingInfo::getBettingAmount)
                .sum();
        textViewTotalBetAmount.setText(currencyFormat.format(totalBetAmount));

        Map<String, Integer> numberFrequency = new HashMap<>();
        for (BettingInfo bet : bettingList) {
            String number = bet.getBettingNumber();
            numberFrequency.put(number, numberFrequency.getOrDefault(number, 0) + 1);
        }

        String mostFrequentNumber = "Không có";
        int maxFrequency = 0;
        for (Map.Entry<String, Integer> entry : numberFrequency.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                mostFrequentNumber = entry.getKey();
                maxFrequency = entry.getValue();
            }
        }
        textViewMostFrequentNumber.setText(mostFrequentNumber + " (" + maxFrequency + " lần)");
    }
}