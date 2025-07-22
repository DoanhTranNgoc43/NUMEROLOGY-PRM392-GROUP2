package com.example.numerology_prm392_group2.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numerology_prm392_group2.adapters.BettingAdapter;
import com.example.numerology_prm392_group2.models.BettingInfo;
import com.example.numerology_prm392_group2.manager.BettingManager;
import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.manager.BettingHistoryManager;
import com.example.numerology_prm392_group2.models.LotteryResult;
import com.example.numerology_prm392_group2.service.LotteryService;
import com.example.numerology_prm392_group2.service.PayoutCalculator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class list extends AppCompatActivity {

    private RecyclerView bettingListRecyclerView;
    private BettingAdapter adapter;
    private MaterialButton probabilityAnalysisButton;
    private MaterialButton checkResultsButton;
    private MaterialButton exportResultsButton;
    private MaterialButton saveListButton;
    private LotteryService lotteryService;
    private PayoutCalculator payoutCalculator;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private LotteryResult lastLotteryResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        BettingManager.getInstance().init(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initServices();
        setupRecyclerView();
        setupClickListeners();
        loadBettingData();
    }

    private void initViews() {
        bettingListRecyclerView = findViewById(R.id.bettingListRecyclerView);
        probabilityAnalysisButton = findViewById(R.id.probabilityAnalysisButton);
        checkResultsButton = findViewById(R.id.checkResultsButton);
        exportResultsButton = findViewById(R.id.exportResultsButton);
        saveListButton = findViewById(R.id.saveListButton);
    }

    private void initServices() {
        lotteryService = LotteryService.getInstance();
        payoutCalculator = PayoutCalculator.getInstance();
    }

    private void setupRecyclerView() {
        bettingListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        adapter = new BettingAdapter(bettingList, new BettingAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(BettingInfo bettingInfo, int position) {
                showEditDialog(bettingInfo, position);
            }

            @Override
            public void onDeleteClick(BettingInfo bettingInfo, int position) {
                showDeleteConfirmationDialog(bettingInfo, position);
            }
        });
        bettingListRecyclerView.setAdapter(adapter);
    }
    private void showEditDialog(BettingInfo bettingInfo, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_betting, null);
        builder.setView(dialogView);

        TextInputEditText nameInput = dialogView.findViewById(R.id.editBettorNameInput);
        TextInputEditText numberInput = dialogView.findViewById(R.id.editBettingNumberInput);
        TextInputEditText amountInput = dialogView.findViewById(R.id.editBettingAmountInput);
        TextInputLayout nameLayout = dialogView.findViewById(R.id.editBettorNameLayout);
        TextInputLayout numberLayout = dialogView.findViewById(R.id.editBettingNumberLayout);
        TextInputLayout amountLayout = dialogView.findViewById(R.id.editBettingAmountLayout);

        nameInput.setText(bettingInfo.getBettorName());
        numberInput.setText(bettingInfo.getBettingNumber());
        amountInput.setText(String.valueOf(bettingInfo.getBettingAmount()));

        builder.setTitle("Chỉnh sửa thông tin cược")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String number = numberInput.getText().toString().trim();
                    String amount = amountInput.getText().toString().trim();

                    // Validation
                    boolean isValid = true;
                    if (name.isEmpty()) {
                        nameLayout.setError("Vui lòng nhập tên người cược");
                        isValid = false;
                    } else {
                        nameLayout.setError(null);
                    }
                    if (!number.matches("\\d{2}")) {
                        numberLayout.setError("Số cược phải là 2 chữ số");
                        isValid = false;
                    } else {
                        numberLayout.setError(null);
                    }
                    if (amount.isEmpty() || !amount.matches("\\d+\\.?\\d*")) {
                        amountLayout.setError("Vui lòng nhập số tiền hợp lệ");
                        isValid = false;
                    } else {
                        amountLayout.setError(null);
                    }

                    if (isValid) {
                        bettingInfo.setBettorName(name);
                        bettingInfo.setBettingNumber(number);
                        bettingInfo.setBettingAmount(Double.parseDouble(amount));
                        bettingInfo.resetWinnerStatus();
                        BettingManager.getInstance().updateBettingList(BettingManager.getInstance().getBettingList());
                        adapter.notifyItemChanged(position);

                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
    private void showDeleteConfirmationDialog(BettingInfo bettingInfo, int position) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa thông tin cược của " + bettingInfo.getBettorName() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    BettingManager.getInstance().removeBetting(position);
                    List<BettingInfo> updatedList = BettingManager.getInstance().getBettingList();
                    adapter.updateList(updatedList);
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void setupClickListeners() {
        probabilityAnalysisButton.setOnClickListener(v -> openProbabilityAnalysis());
        checkResultsButton.setOnClickListener(v -> checkLotteryResults());
        exportResultsButton.setOnClickListener(v -> showExportOptions());
        saveListButton.setOnClickListener(v -> saveBettingList());
    }

    private void saveBettingList() {
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        if (bettingList.isEmpty()) {
            showDialog("Thông báo", "Danh sách cược trống! Vui lòng thêm thông tin cược trước khi lưu.");
            return;
        }
        BettingHistoryManager.getInstance(this).saveBettingList(bettingList);
        showDialog("Thành công", "Danh sách cược đã được lưu vào lịch sử!");
    }

    private void openProbabilityAnalysis() {
        Intent intent = new Intent(this, ProbabilityAnalysisActivity.class);
        startActivity(intent);
    }

    private void checkLotteryResults() {
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();

        if (bettingList.isEmpty()) {
            showDialog("Thông báo", "Chưa có thông tin cược nào để kiểm tra!");
            return;
        }

        AlertDialog loadingDialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Đang kiểm tra...")
                .setMessage("Vui lòng đợi...")
                .setCancelable(false)
                .show();
//        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//        lotteryService.getResultByDate(today, new LotteryService.LotteryResultCallback(){
        lotteryService.getResultByDate("2025-07-21", new LotteryService.LotteryResultCallback(){
            @Override
            public void onSuccess(LotteryResult result) {
                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    if (result == null || result.getSpecialPrize() == null) {
                        showDialog(" Chưa có kết quả,", "Kết quả xổ số được cập nhật vào lúc 18:30 hàng ngày.");
                        return;
                    }

                    lastLotteryResult = result;
                    Toast.makeText(list.this,
                            "Giải đặc biệt: " + result.getSpecialPrize(),
                            Toast.LENGTH_LONG).show();

                    processResults(result);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    showDialog("Lỗi", error);
                });
            }
        });
    }

    private void showExportOptions() {
        if (lastLotteryResult == null) {
            showDialog("Lỗi", "Vui lòng kiểm tra kết quả xổ số trước khi xuất!");
            return;
        }

        String[] options = {"Xuất file CSV", "Xuất file PDF"};
        new MaterialAlertDialogBuilder(this)
                .setTitle("Chọn định dạng xuất file")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        exportToCSV();
                    } else {
                        exportToPDF();
                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void exportToCSV() {
        if (!checkStoragePermission()) {
            requestStoragePermission();
            return;
        }

        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Tên người cược,Số cược,Số tiền cược,Trúng,Kết quả thắng\n");

        for (BettingInfo betting : bettingList) {
            String winnerStatus = betting.isWinner() ? "Có" : "Không";
            String winningAmount = betting.isWinner() ? currencyFormat.format(betting.getWinningAmount()) : "0";
            csvContent.append(String.format("%s,%s,%s,%s,%s\n",
                    betting.getBettorName(),
                    betting.getBettingNumber(),
                    currencyFormat.format(betting.getBettingAmount()),
                    winnerStatus,
                    winningAmount));
        }

        try {
            String fileName = "Lottery_Results_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".csv";
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(csvContent.toString().getBytes());
            fos.close();
            showDialog("Thành công", "File CSV đã được lưu tại: " + file.getAbsolutePath());
        } catch (IOException e) {
            showDialog("Lỗi", "Không thể xuất file CSV: " + e.getMessage());
        }
    }

    private void exportToPDF() {
        if (!checkStoragePermission()) {
            requestStoragePermission();
            return;
        }

        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(12);

        int y = 50;
        canvas.drawText("Kết Quả Xổ Số - Ngày: 2025-07-22", 50, y, paint);
        y += 20;
        canvas.drawText("Giải Đặc Biệt: " + lastLotteryResult.getSpecialPrize(), 50, y, paint);
        y += 30;

        // Draw table header
        canvas.drawText("Tên", 50, y, paint);
        canvas.drawText("Số cược", 150, y, paint);
        canvas.drawText("Tiền cược", 250, y, paint);
        canvas.drawText("Trúng", 350, y, paint);
        canvas.drawText("Tiền thắng", 450, y, paint);
        y += 10;
        canvas.drawLine(50, y, 545, y, paint);
        y += 10;

        // Draw table content
        for (BettingInfo betting : bettingList) {
            String winnerStatus = betting.isWinner() ? "Có" : "Không";
            String winningAmount = betting.isWinner() ? currencyFormat.format(betting.getWinningAmount()) : "0";
            canvas.drawText(betting.getBettorName(), 50, y, paint);
            canvas.drawText(betting.getBettingNumber(), 150, y, paint);
            canvas.drawText(currencyFormat.format(betting.getBettingAmount()), 250, y, paint);
            canvas.drawText(winnerStatus, 350, y, paint);
            canvas.drawText(winningAmount, 450, y, paint);
            y += 20;
            if (y > 800) {
                document.finishPage(page);
                page = document.startPage(pageInfo);
                canvas = page.getCanvas();
                y = 50;
            }
        }

        document.finishPage(page);
        try {
            String fileName = "Lottery_Results_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".pdf";
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            fos.close();
            document.close();
            showDialog("Thành công", "File PDF đã được lưu tại: " + file.getAbsolutePath());
        } catch (IOException e) {
            document.close();
            showDialog("Lỗi", "Không thể xuất file PDF: " + e.getMessage());
        }
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showExportOptions();
            } else {
                showDialog("Lỗi", "Quyền truy cập bộ nhớ bị từ chối. Không thể xuất file.");
            }
        }
    }

    private void processResults(LotteryResult lotteryResult) {
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        Map<String, PayoutCalculator.PayoutResult> payoutResults =
                payoutCalculator.calculateAllPayouts(bettingList, lotteryResult);

        for (BettingInfo betting : bettingList) {
            String key = betting.getBettorName() + "_" + betting.getBettingNumber();
            PayoutCalculator.PayoutResult result = payoutResults.get(key);
            if (result != null) {
                betting.setWinner(result.isWinner());
                betting.setWinningAmount(result.getPayoutAmount());
                betting.setWinningPrize(result.isWinner() ? "ĐB" : "");
            }
        }

        adapter.updateList(bettingList);

        double totalBetAmount = payoutCalculator.calculateTotalBetAmount(bettingList);
        double totalPayout = payoutCalculator.calculateTotalPayout(bettingList, lotteryResult);
        double netProfit = payoutCalculator.calculateNetProfit(bettingList, lotteryResult);

        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("📌 *Lưu ý:*\n");
        resultMessage.append("🔹 Trúng **đề**: chỉ xét 2 số cuối của **Giải Đặc Biệt (ĐB)**\n");
        resultMessage.append("🔹 Trúng **lô**: xét 2 số cuối của **tất cả các giải từ ĐB đến G7**\n\n");
        resultMessage.append("=== KẾT QUẢ XỔ SỐ ===\n");
        resultMessage.append("🎯 ĐB: ").append(lotteryResult.getSpecialPrize()).append("\n");
        resultMessage.append("🥇 G1: ").append(lotteryResult.getFirstPrize()).append("\n");
        resultMessage.append("🥈 G2: ").append(String.join(", ", lotteryResult.getSecondPrizes())).append("\n");
        resultMessage.append("🥉 G3: ").append(String.join(", ", lotteryResult.getThirdPrizes())).append("\n");
        resultMessage.append("🎖️  G4: ").append(String.join(", ", lotteryResult.getFourthPrizes())).append("\n");
        resultMessage.append("🏅 G5: ").append(String.join(", ", lotteryResult.getFifthPrizes())).append("\n");
        resultMessage.append("🏵️ G6: ").append(String.join(", ", lotteryResult.getSixthPrizes())).append("\n");
        resultMessage.append("🎫 G7: ").append(String.join(", ", lotteryResult.getSeventhPrizes())).append("\n\n");

        resultMessage.append("=== KẾT QUẢ CƯỢC ===\n");
        int winnerCount = 0;
        for (PayoutCalculator.PayoutResult result : payoutResults.values()) {
            if (result.isWinner()) {
                winnerCount++;
                resultMessage.append("✅ ").append(result.getBettorName())
                        .append(" - Số: ").append(result.getWinningNumber())
                        .append(" - Thắng: ").append(String.format("%.0f", result.getPayoutAmount()))
                        .append(" VNĐ\n");
            }
        }

        if (winnerCount == 0) {
            resultMessage.append("❌ Không có số nào trúng!\n");
        }

        resultMessage.append("\n=== TỔNG KẾT ===\n");
        resultMessage.append("Tổng tiền cược: ").append(String.format("%.0f", totalBetAmount)).append(" VNĐ\n");
        resultMessage.append("Tổng tiền trả: ").append(String.format("%.0f", totalPayout)).append(" VNĐ\n");
        resultMessage.append("Lợi nhuận ròng: ").append(String.format("%.0f", netProfit)).append(" VNĐ\n");

        new MaterialAlertDialogBuilder(this)
                .setTitle("Kết quả XSMB")
                .setMessage(resultMessage.toString())
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setNeutralButton("Xem chi tiết", (dialog, which) -> {
                    showDetailedResults(payoutResults, lotteryResult);
                })
                .show();
    }

    private void showDetailedResults(Map<String, PayoutCalculator.PayoutResult> payoutResults, LotteryResult lotteryResult) {
        StringBuilder detailMessage = new StringBuilder();
        detailMessage.append("=== TẤT CẢ CÁC SỐ TRÚNG ===\n");
        detailMessage.append("🎯 ĐB: ").append(lotteryResult.getSpecialPrize()).append("\n");
        detailMessage.append("🥇 G1: ").append(lotteryResult.getFirstPrize()).append("\n");
        detailMessage.append("🥈 G2: ").append(String.join(", ", lotteryResult.getSecondPrizes())).append("\n");
        detailMessage.append("🥉 G3: ").append(String.join(", ", lotteryResult.getThirdPrizes())).append("\n");
        detailMessage.append("🎖️ G4: ").append(String.join(", ", lotteryResult.getFourthPrizes())).append("\n");
        detailMessage.append("🏅 G5: ").append(String.join(", ", lotteryResult.getFifthPrizes())).append("\n");
        detailMessage.append("🏵️ G6: ").append(String.join(", ", lotteryResult.getSixthPrizes())).append("\n");
        detailMessage.append("🎫 G7: ").append(String.join(", ", lotteryResult.getSeventhPrizes())).append("\n\n");

        detailMessage.append("=== CHI TIẾT TỪNG CƯỢC ===\n");

        for (PayoutCalculator.PayoutResult result : payoutResults.values()) {
            String status = result.isWinner() ? "✅ TRÚNG" : "❌ KHÔNG TRÚNG";
            detailMessage.append(result.getBettorName())
                    .append(" - Số: ").append(result.getWinningNumber() != null ? result.getWinningNumber() : "N/A")
                    .append(" - Cược: ").append(String.format("%.0f", result.getBetAmount()))
                    .append(" - ").append(status);

            if (result.isWinner()) {
                detailMessage.append(" - Thắng: ").append(String.format("%.0f", result.getPayoutAmount())).append(" VNĐ");
            }
            detailMessage.append("\n");
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle("Chi tiết kết quả")
                .setMessage(detailMessage.toString())
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showDialog(String title, String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void loadBettingData() {
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        adapter.updateList(bettingList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBettingData();
    }
}