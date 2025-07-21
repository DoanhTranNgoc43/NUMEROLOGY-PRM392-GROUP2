package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
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
import com.example.numerology_prm392_group2.models.KeepItem;
import com.example.numerology_prm392_group2.models.ForwardItem;
import com.google.android.material.button.MaterialButton;

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
    private TextView balancedBettingText;
    private RecyclerView topNumbersRecyclerView;
    private MaterialButton showBalancedListsButton;

    private TopNumbersAdapter topNumbersAdapter;
    private NumberFormat currencyFormat;
    private static final double DEFAULT_SELF_BET_RATIO = 0.001; // 0.1% baseline
    private static final double HIGH_RISK_SELF_BET_RATIO = 0.0005; // 0.05% for high-risk numbers
    private static final double LOW_RISK_SELF_BET_RATIO = 0.0012; // 0.12% for low-risk numbers
    private static final double COMMISSION_RATE = 0.1; // 10% commission
    private static final double PAYOUT_RATIO = 70.0; // 1:70 payout ratio
    private static final double HIGH_RISK_THRESHOLD = 0.1; // 10% of total amount
    private static final double LOW_RISK_THRESHOLD = 0.05; // 5% of total amount

    private List<KeepItem> keepItems;
    private List<ForwardItem> forwardItems;

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
        balancedBettingText = findViewById(R.id.balancedBettingText);
        topNumbersRecyclerView = findViewById(R.id.topNumbersRecyclerView);
        showBalancedListsButton = findViewById(R.id.showBalancedListsButton);
        showBalancedListsButton = findViewById(R.id.showBalancedListsButton);
        MaterialButton showFrequencyAnalysisButton = findViewById(R.id.showFrequencyAnalysisButton);

        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        showBalancedListsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProbabilityAnalysisActivity.this, BalancedBettingListActivity.class);
            intent.putExtra("keepList", new ArrayList<>(keepItems));
            intent.putExtra("forwardList", new ArrayList<>(forwardItems));
            startActivity(intent);
        });
        showFrequencyAnalysisButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProbabilityAnalysisActivity.this, FrequencyAnalysisActivity.class);
            startActivity(intent);
        });
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
        performAdvancedRiskAnalysis(topNumbers, totalAmount);
        calculateSpecificScenarios(topNumbers, totalAmount);
        calculateBalancedBetting(topNumbers, totalAmount);
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
        double expectedPayout = totalAmount * 0.01 * PAYOUT_RATIO;
        double expectedProfit = totalAmount - expectedPayout;
        double profitPercentage = (expectedProfit / totalAmount) * 100;

        expectedPayoutText.setText(currencyFormat.format(expectedPayout));
        expectedProfitText.setText(currencyFormat.format(expectedProfit) +
                " (" + String.format("%.1f", profitPercentage) + "%)");
    }

    private void generateRecommendation(int uniqueNumbers, double totalAmount, List<NumberStats> topNumbers) {
        StringBuilder recommendation = new StringBuilder();
        double distributionScore = (double) uniqueNumbers / 100.0;

        recommendation.append("📈 PHÂN TÍCH NHANH:\n");

        if (distributionScore >= 0.8) {
            recommendation.append("✅ Tình hình tốt: Phân tán đều, rủi ro thấp\n");
        } else if (distributionScore >= 0.6) {
            recommendation.append("⚠️ Cần chú ý: Phân phối chưa đều\n");
        } else {
            recommendation.append("🚨 Cảnh báo: Phân phối không đều, rủi ro cao\n");
        }

        if (!topNumbers.isEmpty()) {
            NumberStats topNumber = topNumbers.get(0);
            double topAmount = topNumber.getTotalAmount();
            double maxRisk = topAmount * 0.01 * PAYOUT_RATIO;
            double riskPercent = (maxRisk / totalAmount) * 100;

            recommendation.append("• Số rủi ro cao nhất: ").append(topNumber.getNumber())
                    .append(" - ").append(currencyFormat.format(topAmount)).append("\n");
            recommendation.append("• Tổn thất tiềm ẩn: ").append(currencyFormat.format(maxRisk))
                    .append(" (").append(String.format("%.1f", riskPercent)).append("% vốn)\n");

            if (riskPercent > 50.0) {
                recommendation.append("🚨 RỦI RO VƯỢT NGƯỠNG - Từ chối số này!\n");
            } else if (riskPercent > 25.0) {
                recommendation.append("⚠️ Rủi ro cao - Cần giới hạn\n");
            } else {
                recommendation.append("✅ Rủi ro chấp nhận được\n");
            }
        }

        recommendationText.setText(recommendation.toString());
    }

    private void performAdvancedRiskAnalysis(List<NumberStats> topNumbers, double totalAmount) {
        StringBuilder advancedAnalysis = new StringBuilder();

        advancedAnalysis.append("📊 PHÂN TÍCH RỦI RO CHI TIẾT:\n\n");

        analyzeNumberCoverage(topNumbers, totalAmount, advancedAnalysis);
        analyzeConcentrationRisk(topNumbers, totalAmount, advancedAnalysis);
        calculateMaximumRisk(topNumbers, totalAmount, advancedAnalysis);
        generateDetailedRecommendations(topNumbers, totalAmount, advancedAnalysis);

        displayAdvancedAnalysis(advancedAnalysis.toString(), false);
    }

    private void analyzeNumberCoverage(List<NumberStats> topNumbers, double totalAmount, StringBuilder analysis) {
        int totalNumbers = topNumbers.size();
        double coveragePercentage = (double) totalNumbers / 100.0;

        analysis.append("🎯 Độ phủ số: ").append(totalNumbers).append("/100 (")
                .append(String.format("%.1f", coveragePercentage * 100)).append("%)\n");

        if (coveragePercentage >= 0.8) {
            analysis.append("✅ Phân tán tốt: Rủi ro thấp, lợi nhuận ổn định\n");
        } else if (coveragePercentage >= 0.6) {
            analysis.append("⚠️ Phân tán trung bình: Cần theo dõi\n");
        } else if (coveragePercentage >= 0.4) {
            analysis.append("🔶 Tập trung cao: Rủi ro đáng kể\n");
        } else {
            analysis.append("🚨 Rất tập trung: Rủi ro cao, cần hành động\n");
        }
        analysis.append("\n");
    }

    private void analyzeConcentrationRisk(List<NumberStats> topNumbers, double totalAmount, StringBuilder analysis) {
        analysis.append("💰 PHÂN TÍCH TẬP TRUNG RỦI RO:\n");

        double top5Amount = 0;
        int top5Count = Math.min(5, topNumbers.size());

        for (int i = 0; i < top5Count; i++) {
            top5Amount += topNumbers.get(i).getTotalAmount();
        }

        double top5Percentage = (top5Amount / totalAmount) * 100;
        analysis.append("• Top 5 số chiếm: ").append(String.format("%.1f", top5Percentage)).append("% tổng tiền\n");

        if (top5Percentage > 70) {
            analysis.append("🚨 Cực kỳ nguy hiểm: Quá tập trung vào ít số\n");
        } else if (top5Percentage > 50) {
            analysis.append("⚠️ Nguy hiểm: Tập trung cao\n");
        } else if (top5Percentage > 30) {
            analysis.append("🔶 Cần chú ý: Tập trung trung bình\n");
        } else {
            analysis.append("✅ An toàn: Phân tán tốt\n");
        }

        for (int i = 0; i < Math.min(3, topNumbers.size()); i++) {
            NumberStats number = topNumbers.get(i);
            double percentage = (number.getTotalAmount() / totalAmount) * 100;
            double riskIfWin = number.getTotalAmount() * 0.01 * PAYOUT_RATIO;

            analysis.append("• Số ").append(number.getNumber()).append(": ")
                    .append(String.format("%.1f", percentage)).append("% - Rủi ro: ")
                    .append(formatCurrency(riskIfWin)).append("\n");
        }
        analysis.append("\n");
    }

    private void calculateMaximumRisk(List<NumberStats> topNumbers, double totalAmount, StringBuilder analysis) {
        analysis.append("⚡ TÌNH HUỐNG RỦI RO TỐI ĐA:\n");

        if (!topNumbers.isEmpty()) {
            NumberStats topNumber = topNumbers.get(0);
            double maxPayout = topNumber.getTotalAmount() * 0.01 * PAYOUT_RATIO;
            double lossPercentage = (maxPayout / totalAmount) * 100;

            analysis.append("• Nếu số ").append(topNumber.getNumber()).append(" trúng:\n");
            analysis.append("  - Phải trả: ").append(formatCurrency(maxPayout)).append("\n");
            analysis.append("  - Tỷ lệ thua: ").append(String.format("%.1f", lossPercentage)).append("% vốn\n");

            if (lossPercentage > 100) {
                analysis.append("  🚨 CẢNH BÁO: Thua lỗ nghiêm trọng!\n");
            } else if (lossPercentage > 50) {
                analysis.append("  ⚠️ RỦI RO CAO: Thua nặng\n");
            } else if (lossPercentage > 20) {
                analysis.append("  🔶 RỦI RO TRUNG BÌNH: Cần cân nhắc\n");
            } else {
                analysis.append("  ✅ RỦI RO THẤP: Có thể chấp nhận\n");
            }
        }

        double worstCaseRisk = 0;
        for (int i = 0; i < Math.min(3, topNumbers.size()); i++) {
            worstCaseRisk += topNumbers.get(i).getTotalAmount() * 0.01 * PAYOUT_RATIO;
        }

        analysis.append("• Trường hợp xấu nhất (3 số top trúng): ")
                .append(formatCurrency(worstCaseRisk)).append("\n");
        analysis.append("• Tỷ lệ rủi ro: ").append(String.format("%.1f", (worstCaseRisk / totalAmount) * 100)).append("%\n\n");
    }

    private void generateDetailedRecommendations(List<NumberStats> topNumbers, double totalAmount, StringBuilder analysis) {
        analysis.append("📋 KHUYẾN NGHỊ CHI TIẾT:\n");

        if (!topNumbers.isEmpty()) {
            NumberStats topNumber = topNumbers.get(0);
            double topRisk = (topNumber.getTotalAmount() * 0.01 * PAYOUT_RATIO / totalAmount) * 100;

            if (topRisk > 50) {
                analysis.append("🚨 HÀNH ĐỘNG NGAY:\n");
                analysis.append("• Từ chối hoặc giới hạn số ").append(topNumber.getNumber()).append("\n");
                analysis.append("• Giảm tỷ lệ trả thưởng xuống 1:50 hoặc thấp hơn\n");
                analysis.append("• Tạm ngừng nhận cược cho số này\n");
            } else if (topRisk > 25) {
                analysis.append("⚠️ CẦN HÀNH ĐỘNG:\n");
                analysis.append("• Giới hạn tiền cược tối đa cho số ").append(topNumber.getNumber()).append("\n");
                analysis.append("• Theo dõi sát sao trong ngày\n");
                analysis.append("• Cân nhắc điều chỉnh tỷ lệ trả thưởng\n");
            } else {
                analysis.append("✅ TRẠNG THÁI AN TOÀN:\n");
                analysis.append("• Tiếp tục theo dõi bình thường\n");
                analysis.append("• Duy trì tỷ lệ trả thưởng hiện tại\n");
            }
        }

        analysis.append("\n💡 CHIẾN LƯỢC TỔNG THỂ:\n");
        analysis.append("• Đặt limit tối đa cho mỗi số: ").append(formatCurrency(totalAmount * 0.05)).append("\n");
        analysis.append("• Theo dõi tỷ lệ phân phối hàng giờ\n");
        analysis.append("• Chuẩn bị phương án dự phòng cho các số hot\n");
        analysis.append("• Duy trì tỷ lệ an toàn: 70-80% tổng vốn\n");
    }

    private void calculateSpecificScenarios(List<NumberStats> topNumbers, double totalAmount) {
        StringBuilder scenarios = new StringBuilder();
        scenarios.append("📊 TÌNH HUỐNG CỤ THỂ:\n\n");

        int uniqueNumbers = topNumbers.size();
        double coverageRate = (double) uniqueNumbers / 100.0;

        if (coverageRate >= 0.8) {
            scenarios.append("🎯 Trường hợp lý tưởng:\n");
            scenarios.append("• Phân phối đều, rủi ro thấp\n");
            scenarios.append("• Lợi nhuận kỳ vọng: 30% (").append(formatCurrency(totalAmount * 0.3)).append(")\n");
            scenarios.append("• Khuyến nghị: Duy trì tỷ lệ hiện tại\n\n");
        }

        for (NumberStats number : topNumbers) {
            double riskPercentage = (number.getTotalAmount() / totalAmount) * 100;
            if (riskPercentage > 10) {
                scenarios.append("⚠️ Số ").append(number.getNumber()).append(" - Rủi ro cao:\n");
                scenarios.append("• Chiếm ").append(String.format("%.1f", riskPercentage)).append("% tổng tiền\n");
                scenarios.append("• Rủi ro nếu trúng: ").append(formatCurrency(number.getTotalAmount() * 0.01 * PAYOUT_RATIO)).append("\n");
                scenarios.append("• Khuyến nghị: Giới hạn hoặc từ chối\n\n");
            }
        }

        double top3Amount = 0;
        for (int i = 0; i < Math.min(3, topNumbers.size()); i++) {
            top3Amount += topNumbers.get(i).getTotalAmount();
        }
        double top3Percentage = (top3Amount / totalAmount) * 100;

        if (top3Percentage > 60) {
            scenarios.append("🔥 Trường hợp số nóng:\n");
            scenarios.append("• Top 3 số chiếm ").append(String.format("%.1f", top3Percentage)).append("% tổng tiền\n");
            scenarios.append("• Rủi ro tập trung cao\n");
            scenarios.append("• Khuyến nghị: Điều chỉnh tỷ lệ trả thưởng\n\n");
        }

        displayAdvancedAnalysis(scenarios.toString(), false);
    }

    private double calculateSelfBetRatio(NumberStats number, double totalAmount) {
        double betAmount = number.getTotalAmount();
        double percentageOfTotal = betAmount / totalAmount;

        if (percentageOfTotal > HIGH_RISK_THRESHOLD) {
            return HIGH_RISK_SELF_BET_RATIO; // 0.05% for high-risk numbers
        } else if (percentageOfTotal < LOW_RISK_THRESHOLD) {
            return LOW_RISK_SELF_BET_RATIO; // 0.12% for low-risk numbers
        } else {
            return DEFAULT_SELF_BET_RATIO; // 0.1% for medium-risk numbers
        }
    }

    private void calculateBalancedBetting(List<NumberStats> topNumbers, double totalAmount) {
        StringBuilder balancedAnalysis = new StringBuilder();
        balancedAnalysis.append("⚖️ CHIẾN LƯỢC CÂN BẰNG ÔM ĐỀ:\n\n");

        keepItems = new ArrayList<>();
        forwardItems = new ArrayList<>();

        // Calculate data for Keep and Forward lists
        for (NumberStats number : topNumbers) {
            double betAmount = number.getTotalAmount();
            double selfBetRatio = calculateSelfBetRatio(number, totalAmount);
            double selfBetAmount = betAmount * selfBetRatio;
            double forwardBetAmount = betAmount * (1 - selfBetRatio);
            double commission = forwardBetAmount * COMMISSION_RATE;

            // Trường hợp số không trúng
            double profitIfNotWin = selfBetAmount + commission;
            // Trường hợp số trúng
            double profitIfWin = selfBetAmount - (selfBetAmount * PAYOUT_RATIO) + commission;

            // Add to Keep List
            keepItems.add(new KeepItem(
                    number.getNumber(),
                    selfBetAmount,
                    selfBetRatio,
                    profitIfNotWin,
                    profitIfWin
            ));

            // Add to Forward List
            forwardItems.add(new ForwardItem(
                    number.getNumber(),
                    forwardBetAmount,
                    1 - selfBetRatio,
                    commission
            ));
        }

        // Display summary in balancedBettingText
        balancedAnalysis.append("📍 DANH SÁCH GIỮ LẠI (TỰ ÔM):\n");
        for (KeepItem item : keepItems) {
            balancedAnalysis.append("• Số ").append(item.getNumber()).append(":\n");
            balancedAnalysis.append("  - Tiền tự ôm: ").append(formatCurrency(item.getSelfBetAmount())).append(" (")
                    .append(String.format("%.2f", item.getSelfBetPercentage() * 100)).append("%)\n");
            balancedAnalysis.append("  - Lợi nhuận nếu không trúng: ").append(formatCurrency(item.getProfitIfNotWin())).append("\n");
            balancedAnalysis.append("  - Lợi nhuận nếu trúng: ").append(formatCurrency(item.getProfitIfWin())).append("\n\n");
        }

        balancedAnalysis.append("📤 DANH SÁCH GỬI ĐI (CHO ĐẠI LÝ):\n");
        for (ForwardItem item : forwardItems) {
            balancedAnalysis.append("• Số ").append(item.getNumber()).append(":\n");
            balancedAnalysis.append("  - Tiền gửi đi: ").append(formatCurrency(item.getForwardAmount())).append(" (")
                    .append(String.format("%.2f", item.getForwardPercentage() * 100)).append("%)\n");
            balancedAnalysis.append("  - Hoa hồng nhận được: ").append(formatCurrency(item.getCommission())).append("\n\n");
        }

        // Khuyến nghị chung
        balancedAnalysis.append("💡 KHUYẾN NGHỊ CÂN BẰNG:\n");
        balancedAnalysis.append("• Tỷ lệ tự ôm được điều chỉnh theo rủi ro: 0.05% cho số rủi ro cao (>10% tổng tiền), 0.12% cho số rủi ro thấp (<5%), và 0.1% cho số trung bình\n");
        balancedAnalysis.append("• Nhấn 'Xem Danh Sách Cân Bằng' để xem chi tiết\n");
        balancedAnalysis.append("• Đảm bảo vốn dự phòng để chi trả khi số hot trúng\n");

        balancedBettingText.setText(balancedAnalysis.toString());
    }

    private void displayAdvancedAnalysis(String analysis, boolean isBalancedBetting) {
        if (isBalancedBetting) {
            balancedBettingText.setText(analysis);
        } else {
            String currentText = recommendationText.getText().toString();
            recommendationText.setText(currentText + "\n\n" + analysis);
        }
    }

    private String formatCurrency(double amount) {
        return currencyFormat.format(amount);
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
        balancedBettingText.setText("Chưa có dữ liệu để phân tích chiến lược cân bằng.");
        keepItems = new ArrayList<>();
        forwardItems = new ArrayList<>();
    }
}