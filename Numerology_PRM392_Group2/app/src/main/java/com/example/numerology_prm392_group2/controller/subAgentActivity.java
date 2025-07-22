package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.numerology_prm392_group2.models.BettingInfo;
import com.example.numerology_prm392_group2.manager.BettingManager;
import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.manager.GeneralAgentManager;
import com.example.numerology_prm392_group2.models.GeneralAgent;
import com.example.numerology_prm392_group2.service.LotteryService;
import com.example.numerology_prm392_group2.service.PayoutCalculator;
import com.example.numerology_prm392_group2.utils.ApiService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Random;

public class subAgentActivity extends AppCompatActivity {

    private static final String TAG = "SubAgentActivity";
    private TextInputEditText bettorNameInput;
    private TextInputEditText bettingNumberInput;
    private TextInputEditText bettingAmountInput;
    private AppCompatButton submitButton;
    private AppCompatButton showListButton;
    private AppCompatButton checkResultsButton;
    private AppCompatButton showAgentsButton;
    private ApiService apiService;
    private LotteryService lotteryService;
    private PayoutCalculator payoutCalculator;
    private GeneralAgentManager agentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sub_agent);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        setupClickListeners();
        updateStatistics();
    }

    private void initializeComponents() {
        bettorNameInput = findViewById(R.id.bettorNameInput);
        bettingNumberInput = findViewById(R.id.bettingNumberInput);
        bettingAmountInput = findViewById(R.id.bettingAmountInput);
        submitButton = findViewById(R.id.submitButton);
        showListButton = findViewById(R.id.showListButton);
        checkResultsButton = findViewById(R.id.checkResultsButton);
        showAgentsButton = findViewById(R.id.showAgentsButton);
        AppCompatButton batchInputButton = findViewById(R.id.batchInputButton);
        BettingManager.getInstance().init(this);
        apiService = ApiService.getInstance(this);
        lotteryService = LotteryService.getInstance();
        payoutCalculator = PayoutCalculator.getInstance();
        agentManager = GeneralAgentManager.getInstance();
    }

    private void setupClickListeners() {
        submitButton.setOnClickListener(v -> {
            Log.d(TAG, "Submit button clicked");
            submitBet();
        });

        showListButton.setOnClickListener(v -> {
            Log.d(TAG, "Show List button clicked");
            showBetList();
        });


        showAgentsButton.setOnClickListener(v -> {
            Log.d(TAG, "Show Agents button clicked");
            showGeneralAgents();
        });

        AppCompatButton batchInputButton = findViewById(R.id.batchInputButton);
        batchInputButton.setOnClickListener(v -> {
            Log.d(TAG, "Batch Input button clicked");
            showBatchInputDialog();
        });
    }

    private void submitBet() {
        String bettorName = bettorNameInput.getText().toString().trim();
        String bettingNumber = bettingNumberInput.getText().toString().trim();
        String bettingAmountStr = bettingAmountInput.getText().toString().trim();
        if (TextUtils.isEmpty(bettorName)) {
            bettorNameInput.setError("Vui lòng nhập tên người cược");
            bettorNameInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(bettingNumber)) {
            bettingNumberInput.setError("Vui lòng nhập số cược");
            bettingNumberInput.requestFocus();
            return;
        }

        if (!bettingNumber.matches("\\d+")) {
            bettingNumberInput.setError("Số cược phải là số nguyên");
            bettingNumberInput.requestFocus();
            return;
        }

        int number;
        try {
            number = Integer.parseInt(bettingNumber);
            if (number > 99 || number < 0) {
                bettingNumberInput.setError("Số cược phải là số từ 0 đến 99");
                bettingNumberInput.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            bettingNumberInput.setError("Vui lòng nhập một số hợp lệ");
            bettingNumberInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(bettingAmountStr)) {
            bettingAmountInput.setError("Vui lòng nhập số tiền cược");
            bettingAmountInput.requestFocus();
            return;
        }

        try {
            double bettingAmount = Double.parseDouble(bettingAmountStr);

            if (bettingAmount <= 1000) {
                bettingAmountInput.setError("Số tiền cược phải lớn hơn 1000 VNĐ");
                bettingAmountInput.requestFocus();
                return;
            }
            BettingInfo bettingInfo = new BettingInfo(bettorName, bettingNumber, bettingAmount);
            BettingManager.getInstance().addBetting(bettingInfo);
            showFeatureDialog("Thành công", "Thêm thông tin cược thành công!");
            clearInputs();
            updateStatistics();

        } catch (NumberFormatException e) {
            bettingAmountInput.setError("Số tiền không hợp lệ");
            bettingAmountInput.requestFocus();
        }
    }


    private void showGeneralAgents() {
        List<GeneralAgent> agents = agentManager.getAllAgents();

        if (agents.isEmpty()) {
            showFeatureDialog("Thông báo", "Chưa có đại lý nào trong hệ thống!");
            return;
        }

        StringBuilder agentList = new StringBuilder();

        for (GeneralAgent agent : agents) {
            agentList.append("👤 ").append(agent.getAgentName()).append("\n");
            agentList.append("📞 ").append(agent.getPhoneNumber()).append("\n");
            agentList.append("📧 ").append(agent.getEmail()).append("\n");
            agentList.append("📍 ").append(agent.getAddress()).append("\n");
            agentList.append("💰 Hoa hồng: ").append(String.format("%.1f", agent.getCommissionRate() * 100)).append("%\n");
            agentList.append("🔄 Trạng thái: ").append(agent.isActive() ? "Hoạt động" : "Tạm dừng").append("\n");
            agentList.append("─────────────────────\n");
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle("Danh sách đại lý")
                .setMessage(agentList.toString())
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setNeutralButton("Thêm đại lý", (dialog, which) -> {
                    showAddAgentDialog();
                })
                .show();
    }

    private void showAddAgentDialog() {
        android.view.LayoutInflater inflater = getLayoutInflater();
        android.view.View dialogView = inflater.inflate(R.layout.dialog_add_agent, null);

        TextInputEditText nameInput = dialogView.findViewById(R.id.agentNameInput);
        TextInputEditText phoneInput = dialogView.findViewById(R.id.agentPhoneInput);
        TextInputEditText emailInput = dialogView.findViewById(R.id.agentEmailInput);
        TextInputEditText addressInput = dialogView.findViewById(R.id.agentAddressInput);
        TextInputEditText commissionInput = dialogView.findViewById(R.id.agentCommissionInput);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Thêm đại lý mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String phone = phoneInput.getText().toString().trim();
                    String email = emailInput.getText().toString().trim();
                    String address = addressInput.getText().toString().trim();
                    String commissionStr = commissionInput.getText().toString().trim();

                    if (validateAgentInput(name, phone, email, address, commissionStr)) {
                        double commission = Double.parseDouble(commissionStr) / 100.0;
                        GeneralAgent newAgent = new GeneralAgent(name, phone, email, address, commission);
                        agentManager.addAgent(newAgent);

                        showFeatureDialog("Thành công", "Đã thêm đại lý mới: " + name);
                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private boolean validateAgentInput(String name, String phone, String email, String address, String commissionStr) {
        if (TextUtils.isEmpty(name)) {
            showFeatureDialog("Lỗi", "Vui lòng nhập tên đại lý!");
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            showFeatureDialog("Lỗi", "Vui lòng nhập số điện thoại!");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            showFeatureDialog("Lỗi", "Vui lòng nhập email!");
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            showFeatureDialog("Lỗi", "Vui lòng nhập địa chỉ!");
            return false;
        }

        if (TextUtils.isEmpty(commissionStr)) {
            showFeatureDialog("Lỗi", "Vui lòng nhập tỷ lệ hoa hồng!");
            return false;
        }

        try {
            double commission = Double.parseDouble(commissionStr);
            if (commission < 0 || commission > 100) {
                showFeatureDialog("Lỗi", "Tỷ lệ hoa hồng phải từ 0 đến 100%!");
                return false;
            }
        } catch (NumberFormatException e) {
            showFeatureDialog("Lỗi", "Tỷ lệ hoa hồng không hợp lệ!");
            return false;
        }

        if (agentManager.findAgentByPhone(phone) != null) {
            showFeatureDialog("Lỗi", "Số điện thoại đã tồn tại!");
            return false;
        }

        return true;
    }

    private void showFeatureDialog(String title, String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showBetList() {
        Intent intent = new Intent(subAgentActivity.this, list.class);
        startActivity(intent);
    }

    private void clearInputs() {
        bettorNameInput.setText("");
        bettingNumberInput.setText("");
        bettingAmountInput.setText("");
        bettorNameInput.clearFocus();
        bettingNumberInput.clearFocus();
        bettingAmountInput.clearFocus();
    }

    private void updateStatistics() {
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        TextView totalBetsText = findViewById(R.id.totalBetsText);
        TextView totalAmountText = findViewById(R.id.totalAmountText);
        totalBetsText.setText(String.valueOf(bettingList.size()));
        totalAmountText.setText(String.format("%.0f", payoutCalculator.calculateTotalBetAmount(bettingList)));
    }
    private void showBatchInputDialog() {
        android.view.LayoutInflater inflater = getLayoutInflater();
        android.view.View dialogView = inflater.inflate(R.layout.dialog_batch_input, null);

        TextInputEditText batchInput = dialogView.findViewById(R.id.batchInputEditText);
        MaterialButton sampleDataButton = dialogView.findViewById(R.id.sampleDataButton);

        // Set initial hint with example
        String exampleText = "Nguyễn Văn A,12,50000\n" +
                "Trần Thị B,45,75000\n" +
                "Lê Văn C,78,100000\n" +
                "Phạm Thị D,23,60000\n" +
                "Hoàng Văn E,89,80000";
        batchInput.setHint("Nhập theo định dạng: Tên,Số cược,Tiền cược\nMỗi dòng là một cược\n\nVí dụ:\n" + exampleText);

        // Tạo dialog với cả neutral button
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle("Nhập hàng loạt")
                .setView(dialogView)
                .setPositiveButton("Thêm tất cả", (dialogInterface, which) -> {
                    String inputText = batchInput.getText().toString().trim();
                    if (!inputText.isEmpty()) {
                        processBatchInput(inputText);
                    } else {
                        showFeatureDialog("Lỗi", "Vui lòng nhập dữ liệu trước khi thêm!");
                    }
                })
                .setNegativeButton("Hủy", (dialogInterface, which) -> dialogInterface.dismiss())
                .setNeutralButton("Tạo dữ liệu mẫu", null); // Set null trước, sẽ override sau

        androidx.appcompat.app.AlertDialog dialog = builder.create();

        // Override neutral button click để không dismiss dialog
        dialog.setOnShowListener(dialogInterface -> {
            android.widget.Button neutralButton = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(v -> {
                Log.d(TAG, "Neutral button clicked");
                String sampleData = generateSampleData();
                batchInput.setText(sampleData);
                batchInput.setSelection(0); // Scroll to top
            });
        });

        // Set listener cho button trong layout (backup option)
        if (sampleDataButton != null) {
            sampleDataButton.setOnClickListener(v -> {
                Log.d(TAG, "Layout button clicked");
                String sampleData = generateSampleData();
                batchInput.setText(sampleData);
                batchInput.setSelection(0); // Scroll to top
            });
        }

        dialog.show();
    }

    private void processBatchInput(String inputText) {
        String[] lines = inputText.split("\n");
        int successCount = 0;
        int errorCount = 0;
        StringBuilder errorMessages = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length != 3) {
                errorCount++;
                errorMessages.append("Dòng ").append(i + 1).append(": Sai định dạng\n");
                continue;
            }

            String name = parts[0].trim();
            String number = parts[1].trim();
            String amountStr = parts[2].trim();

            // Validate input
            if (name.isEmpty()) {
                errorCount++;
                errorMessages.append("Dòng ").append(i + 1).append(": Tên trống\n");
                continue;
            }

            if (!number.matches("\\d+")) {
                errorCount++;
                errorMessages.append("Dòng ").append(i + 1).append(": Số cược không hợp lệ\n");
                continue;
            }

            try {
                int numberInt = Integer.parseInt(number);
                if (numberInt < 0 || numberInt > 99) {
                    errorCount++;
                    errorMessages.append("Dòng ").append(i + 1).append(": Số cược phải từ 0-99\n");
                    continue;
                }
            } catch (NumberFormatException e) {
                errorCount++;
                errorMessages.append("Dòng ").append(i + 1).append(": Số cược không hợp lệ\n");
                continue;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 1000) {
                    errorCount++;
                    errorMessages.append("Dòng ").append(i + 1).append(": Số tiền phải > 1000\n");
                    continue;
                }

                // Format number to 2 digits
                String formattedNumber = String.format("%02d", Integer.parseInt(number));

                // Add to betting list
                BettingInfo bettingInfo = new BettingInfo(name, formattedNumber, amount);
                BettingManager.getInstance().addBetting(bettingInfo);
                successCount++;

            } catch (NumberFormatException e) {
                errorCount++;
                errorMessages.append("Dòng ").append(i + 1).append(": Số tiền không hợp lệ\n");
            }
        }

        // Show result
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("Kết quả nhập hàng loạt:\n");
        resultMessage.append("✅ Thành công: ").append(successCount).append(" cược\n");
        if (errorCount > 0) {
            resultMessage.append("❌ Lỗi: ").append(errorCount).append(" cược\n\n");
            resultMessage.append("Chi tiết lỗi:\n").append(errorMessages.toString());
        }

        showFeatureDialog("Kết quả nhập hàng loạt", resultMessage.toString());

        if (successCount > 0) {
            clearInputs();
            updateStatistics();
        }
    }

    private String generateSampleData() {
        String[] sampleNames = {
                "Nguyễn Văn A", "Trần Thị B", "Lê Văn C", "Phạm Thị D", "Hoàng Văn E",
                "Vũ Thị F", "Đỗ Văn G", "Bùi Thị H", "Dương Văn I", "Mai Thị K",
                "Tạ Văn L", "Lý Thị M", "Đặng Văn N", "Cao Thị O", "Phan Văn P",
                "Từ Thị Q", "Đinh Văn R", "Võ Thị S", "Lâm Văn T", "Chu Thị U"
        };

        StringBuilder sampleData = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < Math.min(20, sampleNames.length); i++) {
            String name = sampleNames[i];
            int number = random.nextInt(100); // 0-99
            int amount = (random.nextInt(20) + 1) * 5000; // 5000-100000, multiples of 5000

            sampleData.append(name).append(",").append(String.format("%02d", number))
                    .append(",").append(amount);
            if (i < Math.min(19, sampleNames.length - 1)) sampleData.append("\n");
        }

        return sampleData.toString();
    }

}