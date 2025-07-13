package com.example.numerology_prm392_group2.controller;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numerology_prm392_group2.models.BettingInfo;
import com.example.numerology_prm392_group2.manager.BettingManager;
import com.example.numerology_prm392_group2.models.NumberStats;
import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.adapters.TopNumbersAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProbabilityAnalysisActivity extends AppCompatActivity {

    private TextView totalTicketsText;
    private TextView totalAmountText;
    private TextView uniqueNumbersText;
    private TextView distributionAnalysisText;
    private TextView riskLevelText;
    private TextView expectedPayoutText;
    private TextView expectedProfitText;
    private TextView recommendationText;
    private RecyclerView topNumbersRecyclerView;

    private TopNumbersAdapter topNumbersAdapter;
    private NumberFormat currencyFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_probability_analysis);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupRecyclerView();
        analyzeData();
    }

    private void initViews() {
        totalTicketsText = findViewById(R.id.totalTicketsText);
        totalAmountText = findViewById(R.id.totalAmountText);
        uniqueNumbersText = findViewById(R.id.uniqueNumbersText);
        distributionAnalysisText = findViewById(R.id.distributionAnalysisText);
        riskLevelText = findViewById(R.id.riskLevelText);
        expectedPayoutText = findViewById(R.id.expectedPayoutText);
        expectedProfitText = findViewById(R.id.expectedProfitText);
        recommendationText = findViewById(R.id.recommendationText);
        topNumbersRecyclerView = findViewById(R.id.topNumbersRecyclerView);

        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }

    private void setupRecyclerView() {
        topNumbersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void analyzeData() {
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();

        if (bettingList.isEmpty()) {
            displayEmptyState();
            return;
        }

        int totalTickets = bettingList.size();
        double totalAmount = 0;
        Map<String, NumberStats> numberStatsMap = new HashMap<>();

        for (BettingInfo betting : bettingList) {
            totalAmount += betting.getBettingAmount();

            String number = betting.getBettingNumber();
            if (numberStatsMap.containsKey(number)) {
                NumberStats stats = numberStatsMap.get(number);
                numberStatsMap.put(number, new NumberStats(
                        number,
                        stats.getTotalAmount() + betting.getBettingAmount(),
                        stats.getTicketCount() + 1
                ));
            } else {
                numberStatsMap.put(number, new NumberStats(
                        number,
                        betting.getBettingAmount(),
                        1
                ));
            }
        }

        int uniqueNumbers = numberStatsMap.size();

        totalTicketsText.setText(String.valueOf(totalTickets));
        totalAmountText.setText(currencyFormat.format(totalAmount));
        uniqueNumbersText.setText(uniqueNumbers + "/100");

        analyzeDistribution(uniqueNumbers, totalAmount);

        calculateExpectedProfit(totalAmount);

        List<NumberStats> topNumbers = new ArrayList<>(numberStatsMap.values());
        Collections.sort(topNumbers, new Comparator<NumberStats>() {
            @Override
            public int compare(NumberStats o1, NumberStats o2) {
                return Double.compare(o2.getTotalAmount(), o1.getTotalAmount());
            }
        });

        topNumbersAdapter = new TopNumbersAdapter(topNumbers);
        topNumbersRecyclerView.setAdapter(topNumbersAdapter);

        generateRecommendation(uniqueNumbers, totalAmount, topNumbers);
    }

    private void analyzeDistribution(int uniqueNumbers, double totalAmount) {
        double distributionScore = (double) uniqueNumbers / 100.0;
        String distributionLevel;
        String riskLevel;

        if (distributionScore >= 0.8) {
            distributionLevel = "Phân phối đều (Lý tưởng)";
            riskLevel = "Thấp - An toàn";
        } else if (distributionScore >= 0.6) {
            distributionLevel = "Phân phối tương đối đều";
            riskLevel = "Trung bình";
        } else if (distributionScore >= 0.4) {
            distributionLevel = "Phân phối không đều";
            riskLevel = "Cao";
        } else {
            distributionLevel = "Phân phối rất không đều";
            riskLevel = "Rất cao";
        }

        distributionAnalysisText.setText("Phân phối: " + distributionLevel + " (" + String.format("%.1f", distributionScore * 100) + "%)");
        riskLevelText.setText("Mức độ rủi ro: " + riskLevel);
    }

    private void calculateExpectedProfit(double totalAmount) {
        // Tỷ lệ trả thưởng 1:70, xác suất trúng 1%
        double expectedPayout = totalAmount * 0.01 * 70;
        double expectedProfit = totalAmount - expectedPayout;
        double profitPercentage = (expectedProfit / totalAmount) * 100;

        expectedPayoutText.setText(currencyFormat.format(expectedPayout));
        expectedProfitText.setText(currencyFormat.format(expectedProfit) +
                " (" + String.format("%.1f", profitPercentage) + "%)");
    }

    private void generateRecommendation(int uniqueNumbers, double totalAmount, List<NumberStats> topNumbers) {
        StringBuilder recommendation = new StringBuilder();

        double distributionScore = (double) uniqueNumbers / 100.0;

        if (distributionScore >= 0.8) {
            recommendation.append("✅ Tình hình tốt: Người chơi rải đều, rủi ro thấp.\n\n");
            recommendation.append("• Lợi nhuận ổn định khoảng 30%\n");
            recommendation.append("• Có thể tiếp tục ôm số an toàn\n");
        } else if (distributionScore >= 0.6) {
            recommendation.append("⚠️ Cần chú ý: Phân phối chưa đều.\n\n");
            recommendation.append("• Theo dõi các số được cược nhiều\n");
            recommendation.append("• Cân nhắc điều chỉnh tỷ lệ trả thưởng\n");
        } else {
            recommendation.append("🚨 Cảnh báo: Phân phối rất không đều, rủi ro cao!\n\n");
            recommendation.append("• Xem xét từ chối hoặc giới hạn cược các số hot\n");
            recommendation.append("• Có thể áp dụng tỷ lệ trả thưởng thấp hơn\n");
        }

        // Phân tích số hot
        if (!topNumbers.isEmpty()) {
            NumberStats topNumber = topNumbers.get(0);
            if (topNumber.getTotalAmount() > totalAmount * 0.1) { // Nếu số đầu chiếm >10% tổng tiền
                recommendation.append("• Số ").append(topNumber.getNumber())
                        .append(" có rủi ro cao (")
                        .append(currencyFormat.format(topNumber.getTotalAmount()))
                        .append(")\n");
            }
        }

        // Lời khuyên chung
        recommendation.append("\n📈 Lời khuyên:\n");
        recommendation.append("• Theo dõi xu hướng hàng ngày\n");
        recommendation.append("• Đa dạng hóa để giảm rủi ro\n");
        recommendation.append("• Luôn duy trì tỷ lệ an toàn 30% lợi nhuận");

        recommendationText.setText(recommendation.toString());
    }

    private void displayEmptyState() {
        totalTicketsText.setText("0");
        totalAmountText.setText("0 VNĐ");
        uniqueNumbersText.setText("0/100");
        distributionAnalysisText.setText("Chưa có dữ liệu để phân tích");
        riskLevelText.setText("Không xác định");
        expectedPayoutText.setText("0 VNĐ");
        expectedProfitText.setText("0 VNĐ (0%)");
        recommendationText.setText("Vui lòng thêm dữ liệu cược để có thể phân tích xác suất và đưa ra khuyến nghị.");
    }
}