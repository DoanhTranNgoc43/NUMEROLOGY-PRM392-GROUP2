package com.example.numerology_prm392_group2.controller;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FrequencyAnalysisActivity extends AppCompatActivity {

    private TextView hotNumbersText;
    private TextView coldNumbersText;
    private TextView dateRangeText;
    private RecyclerView frequencyRecyclerView;
    private RecyclerView recommendationRecyclerView;
    private Button selectDateRangeButton;
    private BarChart frequencyChart;
    private TopNumbersAdapter frequencyAdapter;
    //    private RecommendationAdapter recommendationAdapter;
    private LotteryService lotteryService;
    private Calendar startDate;
    private Calendar endDate;
    private SimpleDateFormat sdf;
    private SimpleDateFormat displaySdf;

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
        setupDateRange();
        fetchAndAnalyzeFrequency();
    }

    private void initViews() {
        hotNumbersText = findViewById(R.id.hotNumbersText);
        coldNumbersText = findViewById(R.id.coldNumbersText);
        dateRangeText = findViewById(R.id.dateRangeText);
        frequencyRecyclerView = findViewById(R.id.frequencyRecyclerView);
//        recommendationRecyclerView = findViewById(R.id.recommendationRecyclerView);
        selectDateRangeButton = findViewById(R.id.selectDateRangeButton);
        frequencyChart = findViewById(R.id.frequencyChart);
        lotteryService = LotteryService.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        displaySdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    private void setupRecyclerViews() {
        frequencyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recommendationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupDateRange() {
        // Default: One year from July 22, 2024, to July 22, 2025
        startDate = Calendar.getInstance();
        startDate.set(2024, Calendar.JULY, 22);
        endDate = Calendar.getInstance();
        endDate.set(2025, Calendar.JULY, 22);

        // Display default date range
        updateDateRangeText();

        selectDateRangeButton.setOnClickListener(v -> showDateRangePicker());
    }

    private void updateDateRangeText() {
        String dateRange = String.format("Từ ngày %s đến ngày %s",
                displaySdf.format(startDate.getTime()),
                displaySdf.format(endDate.getTime()));
        dateRangeText.setText(dateRange);
    }

    private void showDateRangePicker() {
        // Start date picker
        DatePickerDialog startDatePicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    startDate.set(year, month, dayOfMonth);
                    // End date picker
                    DatePickerDialog endDatePicker = new DatePickerDialog(
                            this,
                            (view2, year2, month2, dayOfMonth2) -> {
                                endDate.set(year2, month2, dayOfMonth2);
                                // Validate end date is not before start date
                                if (endDate.before(startDate)) {
                                    dateRangeText.setText("Lỗi: Ngày kết thúc phải sau ngày bắt đầu");
                                    return;
                                }
                                updateDateRangeText();
                                fetchAndAnalyzeFrequency();
                            },
                            endDate.get(Calendar.YEAR),
                            endDate.get(Calendar.MONTH),
                            endDate.get(Calendar.DAY_OF_MONTH)
                    );
                    // Restrict end date to be within one year
                    endDatePicker.getDatePicker().setMaxDate(startDate.getTimeInMillis() + 365L * 24 * 60 * 60 * 1000);
                    endDatePicker.getDatePicker().setMinDate(startDate.getTimeInMillis());
                    endDatePicker.show();
                },
                startDate.get(Calendar.YEAR),
                startDate.get(Calendar.MONTH),
                startDate.get(Calendar.DAY_OF_MONTH)
        );
        // Restrict start date to July 22, 2024, to July 22, 2025
        Calendar minDate = Calendar.getInstance();
        minDate.set(2024, Calendar.JULY, 22);
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(2025, Calendar.JULY, 22);
        startDatePicker.getDatePicker().setMinDate(minDate.getTimeInMillis());
        startDatePicker.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        startDatePicker.show();
    }

    private void fetchAndAnalyzeFrequency() {
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        new Thread(() -> {
            try {
                String csvContent = fetchCsvContent();
                // Phân tích tần suất cho danh sách cược
                List<NumberStats> bettingFrequencyStats = analyzeBettingFrequency(csvContent);
                // Phân tích tần suất cho tất cả số từ 00-99 dựa trên cột đầu tiên
                Map<String, Integer> allNumbersFrequency = analyzeAllNumbersFrequency(csvContent);
                runOnUiThread(() -> {
                    displayFrequencyAnalysis(bettingFrequencyStats);
                    displayFrequencyChart(allNumbersFrequency);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    hotNumbersText.setText("Lỗi khi phân tích dữ liệu: " + e.getMessage());
                    coldNumbersText.setText("Lỗi khi phân tích dữ liệu: " + e.getMessage());
                    dateRangeText.setText("Lỗi khi tải dữ liệu");
                    frequencyChart.setData(null);
                    frequencyChart.invalidate();
                });
            }
        }).start();
    }

    private String fetchCsvContent() throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(null) // Disable cache
                .build();
        Request request = new Request.Builder()
                .url("https://raw.githubusercontent.com/khiemdoan/vietnam-lottery-xsmb-analysis/main/data/xsmb-2-digits.csv")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Lỗi server: " + response.code());
            String csvContent = response.body().string();
            Log.d("FrequencyAnalysis", "CSV Content: " + csvContent);
            return csvContent;
        }
    }

    private List<NumberStats> analyzeBettingFrequency(String csvContent) {
        // Get betting numbers from BettingManager
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        Set<String> bettingNumbers = new HashSet<>();
        for (BettingInfo bet : bettingList) {
            bettingNumbers.add(bet.getBettingNumber());
        }

        // If no betting numbers, return empty list
        if (bettingNumbers.isEmpty()) {
            return new ArrayList<>();
        }

        // Count frequency of betting numbers based on the first prize column
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String number : bettingNumbers) {
            frequencyMap.put(number, 0); // Initialize frequency to 0
        }

        String[] lines = csvContent.split("\n");
        if (lines.length < 2) return new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            String[] columns = lines[i].trim().split(",", -1);
            if (columns.length < 2) continue;

            try {
                Date drawDate = sdf.parse(columns[0]);
                if (drawDate == null || drawDate.before(startDate.getTime()) || drawDate.after(endDate.getTime())) continue;

                // Only check the first prize column (index 1)
                String number = columns[1]; // Cột thứ hai là giải đặc biệt
                if (!number.isEmpty() && number.matches("\\d{2}") && bettingNumbers.contains(number)) {
                    frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
                    Log.d("FrequencyAnalysis", "Betting number " + number + " found on " + columns[0] + " in column 1");
                }
            } catch (Exception e) {
                Log.e("FrequencyAnalysis", "Error parsing CSV line " + i + ": " + e.getMessage());
            }
        }

        List<NumberStats> frequencyStats = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            frequencyStats.add(new NumberStats(entry.getKey(), 0, entry.getValue()));
        }

        // Sort by frequency descending
        Collections.sort(frequencyStats, (o1, o2) -> Integer.compare(o2.getTicketCount(), o1.getTicketCount()));
        return frequencyStats;
    }

    private Map<String, Integer> analyzeAllNumbersFrequency(String csvContent) {
        // Initialize frequency map for all numbers from 00 to 99
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (int i = 0; i <= 99; i++) {
            String number = String.format("%02d", i);
            frequencyMap.put(number, 0);
        }

        String[] lines = csvContent.split("\n");
        if (lines.length < 2) return frequencyMap;

        for (int i = 1; i < lines.length; i++) {
            String[] columns = lines[i].trim().split(",", -1);
            if (columns.length < 2) continue;

            try {
                Date drawDate = sdf.parse(columns[0]);
                if (drawDate == null || drawDate.before(startDate.getTime()) || drawDate.after(endDate.getTime())) continue;

                // Only check the first prize column (index 1)
                String number = columns[1]; // Cột thứ hai là giải đặc biệt
                if (!number.isEmpty() && number.matches("\\d{2}")) {
                    frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
                    Log.d("FrequencyAnalysis", "Number " + number + " found on " + columns[0] + " in column 1");
                }
            } catch (Exception e) {
                Log.e("FrequencyAnalysis", "Error parsing CSV line " + i + ": " + e.getMessage());
            }
        }

        return frequencyMap;
    }

    private void displayFrequencyAnalysis(List<NumberStats> frequencyStats) {
        if (frequencyStats.isEmpty()) {
            hotNumbersText.setText("Không có dữ liệu số nóng từ danh sách cược.");
            coldNumbersText.setText("Không có dữ liệu số lạnh từ danh sách cược.");
            recommendationRecyclerView.setAdapter(new RecommendationAdapter(new ArrayList<>()));
            frequencyRecyclerView.setAdapter(new TopNumbersAdapter(new ArrayList<>()));
            return;
        }

        StringBuilder hotNumbers = new StringBuilder();
        StringBuilder coldNumbers = new StringBuilder();

        // Separate hot and cold numbers based on frequency > 7
        for (NumberStats stat : frequencyStats) {
            if (stat.getTicketCount() >= 7) {
                hotNumbers.append("• ").append(stat.getNumber()).append(": ").append(stat.getTicketCount()).append(" lần\n");
            } else {
                coldNumbers.append("• ").append(stat.getNumber()).append(": ").append(stat.getTicketCount()).append(" lần\n");
            }
        }

        hotNumbersText.setText(hotNumbers.length() > 0 ? hotNumbers.toString() : "Không có số nóng.");
        coldNumbersText.setText(coldNumbers.length() > 0 ? coldNumbers.toString() : "Không có số lạnh.");

        // Sort back to descending for RecyclerView
        Collections.sort(frequencyStats, (o1, o2) -> Integer.compare(o2.getTicketCount(), o1.getTicketCount()));
        frequencyAdapter = new TopNumbersAdapter(frequencyStats);
        frequencyRecyclerView.setAdapter(frequencyAdapter);

//        generateRecommendations(frequencyStats);
    }

    private void displayFrequencyChart(Map<String, Integer> frequencyMap) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        // Create data for the chart
        for (int i = 0; i <= 99; i++) {
            String number = String.format("%02d", i);
            int frequency = frequencyMap.getOrDefault(number, 0);
            entries.add(new BarEntry(i, frequency));
            labels.add(number);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Tần suất xuất hiện (22/07/2024 - 22/07/2025)");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        frequencyChart.setData(barData);
        frequencyChart.setFitBars(true);

        // Customize X-axis
        XAxis xAxis = frequencyChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(10); // Display 10 labels to avoid clutter
        xAxis.setLabelRotationAngle(45f);

        // Customize chart
        frequencyChart.getDescription().setEnabled(false);
        frequencyChart.getLegend().setEnabled(true);
        frequencyChart.setPinchZoom(true);
        frequencyChart.setDragEnabled(true);
        frequencyChart.setScaleEnabled(true);

        // Add listener for value selection
        frequencyChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, com.github.mikephil.charting.highlight.Highlight h) {
                int index = (int) e.getX();
                String number = String.format("%02d", index);
                int frequency = (int) e.getY();
                Toast.makeText(FrequencyAnalysisActivity.this,
                        "Số: " + number + "\nTần suất: " + frequency + " lần",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
                // Do nothing when no value is selected
            }
        });

        frequencyChart.invalidate(); // Refresh chart
    }

//    private void generateRecommendations(List<NumberStats> frequencyStats) {
//        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
//        List<Recommendation> recommendations = new ArrayList<>();
//
//        if (bettingList.isEmpty()) {
//            recommendations.add(new Recommendation("", "NEUTRAL", 0, "Không có dữ liệu cược để phân tích."));
//            recommendationAdapter = new RecommendationAdapter(recommendations);
//            recommendationRecyclerView.setAdapter(recommendationAdapter);
//            return;
//        }
//
//        int totalFrequency = frequencyStats.stream().mapToInt(NumberStats::getTicketCount).sum();
//        double avgFrequency = frequencyStats.isEmpty() ? 0 : totalFrequency / (double) frequencyStats.size();
//
//        for (BettingInfo bet : bettingList) {
//            String number = bet.getBettingNumber();
//            int frequency = getFrequency(number, frequencyStats);
//            String type;
//            String message;
//
//            if (frequency > avgFrequency) {
//                type = "KEEP";
//                message = String.format("NÓNG: Trúng %d lần - Nên ÔM để tối ưu lợi nhuận.", frequency);
//            } else if (frequency < avgFrequency && frequency > 0) {
//                type = "FORWARD";
//                message = String.format("LẠNH: Trúng %d lần - Nên GỬI ĐI để giảm rủi ro.", frequency);
//            } else {
//                type = "NEUTRAL";
//                message = String.format("TRUNG BÌNH: Trúng %d lần - Cân nhắc ôm với tỷ lệ thấp.", frequency);
//            }
//
//            recommendations.add(new Recommendation(number, type, frequency, message));
//        }
//        recommendationAdapter = new RecommendationAdapter(recommendations);
//        recommendationRecyclerView.setAdapter(recommendationAdapter);
//    }

    private int getFrequency(String number, List<NumberStats> frequencyStats) {
        for (NumberStats stat : frequencyStats) {
            if (stat.getNumber().equals(number)) {
                return stat.getTicketCount();
            }
        }
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAndAnalyzeFrequency();
    }
}