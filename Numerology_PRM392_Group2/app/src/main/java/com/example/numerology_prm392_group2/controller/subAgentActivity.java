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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

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
        agentList.append("=== DANH SÁCH ĐẠI LÝ CHÍNH ===\n\n");

        for (GeneralAgent agent : agents) {
            agentList.append("👤 ").append(agent.getAgentName()).append("\n");
            agentList.append("📞 ").append(agent.getPhoneNumber()).append("\n");
            agentList.append("📧 ").append(agent.getEmail()).append("\n");
            agentList.append("📍 ").append(agent.getAddress()).append("\n");
            agentList.append("💰 Hoa hồng: ").append(String.format("%.1f", agent.getCommissionRate() * 100)).append("%\n");
            agentList.append("👥 Số đại lý phụ: ").append(agent.getSubAgentIds().size()).append("\n");
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


}