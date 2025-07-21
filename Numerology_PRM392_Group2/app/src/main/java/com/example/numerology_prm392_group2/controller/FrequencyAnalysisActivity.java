package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.adapters.RecommendationAdapter;
import com.example.numerology_prm392_group2.adapters.TopNumbersAdapter;
import com.example.numerology_prm392_group2.manager.BettingManager;
import com.example.numerology_prm392_group2.models.BettingInfo;
import com.example.numerology_prm392_group2.models.NumberStats;
import com.example.numerology_prm392_group2.models.Recommendation;
import com.example.numerology_prm392_group2.service.LotteryService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FrequencyAnalysisActivity extends AppCompatActivity {

    private TextView hotNumbersText;
    private TextView coldNumbersText;
    private RecyclerView frequencyRecyclerView;
    private RecyclerView recommendationRecyclerView;
    private TopNumbersAdapter frequencyAdapter;
    private RecommendationAdapter recommendationAdapter;
    private LotteryService lotteryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_analysis);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupRecyclerViews();
        fetchAndAnalyzeFrequency();
    }

    private void initViews() {
        hotNumbersText = findViewById(R.id.hotNumbersText);
        coldNumbersText = findViewById(R.id.coldNumbersText);
        frequencyRecyclerView = findViewById(R.id.frequencyRecyclerView);
        recommendationRecyclerView = findViewById(R.id.recommendationRecyclerView);
        lotteryService = LotteryService.getInstance();
    }

    private void setupRecyclerViews() {
        frequencyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recommendationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchAndAnalyzeFrequency() {
        new Thread(() -> {
            try {
                String csvContent = fetchCsvContent();
                List<NumberStats> frequencyStats = analyzeFrequency(csvContent);
                runOnUiThread(() -> displayFrequencyAnalysis(frequencyStats));
            } catch (Exception e) {
                runOnUiThread(() -> hotNumbersText.setText("Lỗi khi phân tích dữ liệu: " + e.getMessage()));
            }
        }).start();
    }

    private String fetchCsvContent() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://raw.githubusercontent.com/khiemdoan/vietnam-lottery-xsmb-analysis/main/data/xsmb-2-digits.csv")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Lỗi server: " + response.code());
            return response.body().string();
        }
    }

    private List<NumberStats> analyzeFrequency(String csvContent) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -3); // 3 months ago from July 21, 2025
        startDate.set(Calendar.DAY_OF_MONTH, 21);

        String[] lines = csvContent.split("\n");
        if (lines.length < 2) return new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            String[] columns = lines[i].trim().split(",", -1);
            if (columns.length < 2) continue;

            try {
                Date drawDate = sdf.parse(columns[0]);
                if (drawDate == null || drawDate.before(startDate.getTime())) continue;

                String specialPrize = columns[1]; // Special Prize (2-digit number)
                if (!specialPrize.isEmpty()) {
                    frequencyMap.put(specialPrize, frequencyMap.getOrDefault(specialPrize, 0) + 1);
                }
            } catch (Exception e) {
                // Skip invalid lines
            }
        }

        List<NumberStats> frequencyStats = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            frequencyStats.add(new NumberStats(entry.getKey(), 0, entry.getValue()));
        }

        Collections.sort(frequencyStats, (o1, o2) -> Integer.compare(o2.getTicketCount(), o1.getTicketCount()));
        return frequencyStats;
    }

    private void displayFrequencyAnalysis(List<NumberStats> frequencyStats) {
        if (frequencyStats.isEmpty()) {
            hotNumbersText.setText("Không có dữ liệu số nóng");
            coldNumbersText.setText("Không có dữ liệu số lạnh");
            recommendationRecyclerView.setAdapter(new RecommendationAdapter(new ArrayList<>()));
            return;
        }

        StringBuilder hotNumbers = new StringBuilder();
        for (int i = 0; i < Math.min(5, frequencyStats.size()); i++) {
            NumberStats stat = frequencyStats.get(i);
            hotNumbers.append("• ").append(stat.getNumber()).append(": ").append(stat.getTicketCount()).append(" lần\n");
        }
        hotNumbersText.setText(hotNumbers.toString());


        StringBuilder coldNumbers = new StringBuilder();
        for (int i = Math.max(0, frequencyStats.size() - 5); i < frequencyStats.size(); i++) {
            NumberStats stat = frequencyStats.get(i);
            coldNumbers.append("• ").append(stat.getNumber()).append(": ").append(stat.getTicketCount()).append(" lần\n");
        }
        coldNumbersText.setText(coldNumbers.toString());


        frequencyAdapter = new TopNumbersAdapter(frequencyStats);
        frequencyRecyclerView.setAdapter(frequencyAdapter);


        generateRecommendations(frequencyStats);
    }

    private void generateRecommendations(List<NumberStats> frequencyStats) {
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        List<Recommendation> recommendations = new ArrayList<>();

        if (bettingList.isEmpty()) {
            recommendations.add(new Recommendation("", "NEUTRAL", 0, "Không có dữ liệu cược để phân tích."));
            recommendationAdapter = new RecommendationAdapter(recommendations);
            recommendationRecyclerView.setAdapter(recommendationAdapter);
            return;
        }

        // Calculate average frequency
        int totalFrequency = frequencyStats.stream().mapToInt(NumberStats::getTicketCount).sum();
        double avgFrequency = totalFrequency / (double) frequencyStats.size();

        // Generate recommendations
        for (BettingInfo bet : bettingList) {
            String number = bet.getBettingNumber();
            int frequency = getFrequency(number, frequencyStats);
            String type;
            String message;

            if (frequency > avgFrequency) {
                type = "KEEP";
                message = String.format("NÓNG: Trúng %d lần - Nên ÔM để tối ưu lợi nhuận.", frequency);
            } else if (frequency < avgFrequency && frequency > 0) {
                type = "FORWARD";
                message = String.format("LẠNH: Trúng %d lần - Nên GỬI ĐI để giảm rủi ro.", frequency);
            } else {
                type = "NEUTRAL";
                message = String.format("TRUNG BÌNH: Trúng %d lần - Cân nhắc ôm với tỷ lệ thấp.", frequency);
            }

            recommendations.add(new Recommendation(number, type, frequency, message));
        }

        recommendationAdapter = new RecommendationAdapter(recommendations);
        recommendationRecyclerView.setAdapter(recommendationAdapter);
    }

    private int getFrequency(String number, List<NumberStats> frequencyStats) {
        for (NumberStats stat : frequencyStats) {
            if (stat.getNumber().equals(number)) {
                return stat.getTicketCount();
            }
        }
        return 0;
    }
}