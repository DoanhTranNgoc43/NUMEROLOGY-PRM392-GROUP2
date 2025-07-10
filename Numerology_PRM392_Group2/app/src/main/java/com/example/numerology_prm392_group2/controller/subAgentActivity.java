package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numerology_prm392_group2.BettingInfo;
import com.example.numerology_prm392_group2.BettingManager;
import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.list;
import com.example.numerology_prm392_group2.utils.ApiService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class subAgentActivity extends AppCompatActivity {

    private static final String TAG = "SubAgentActivity";
    private TextInputEditText bettorNameInput;
    private TextInputEditText bettingNumberInput;
    private TextInputEditText bettingAmountInput;
    private AppCompatButton submitButton;
    private AppCompatButton showListButton;
    private ApiService apiService;

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
    }

    private void initializeComponents() {
        bettorNameInput = findViewById(R.id.bettorNameInput);
        bettingNumberInput = findViewById(R.id.bettingNumberInput);
        bettingAmountInput = findViewById(R.id.bettingAmountInput);
        submitButton = findViewById(R.id.submitButton);
        showListButton = findViewById(R.id.showListButton);
        apiService = ApiService.getInstance(this);
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
    }

    private void submitBet() {
        String bettorName = bettorNameInput.getText().toString().trim();
        String bettingNumber = bettingNumberInput.getText().toString().trim();
        String bettingAmountStr = bettingAmountInput.getText().toString().trim();

        // Validate inputs
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
            double bettingAmount = Double.parseDouble(bettingAmountStr

            );

            if (bettingAmount <= 1000) {
                bettingAmountInput.setError("Số tiền cược phải lớn hơn 1000 VNĐ");
                bettingAmountInput.requestFocus();
                return;
            }

            // Create betting info object and add to manager
            BettingInfo bettingInfo = new BettingInfo(bettorName, bettingNumber, bettingAmount);
            BettingManager.getInstance().addBetting(bettingInfo);

            showFeatureDialog("Thành công", "Thêm thông tin cược thành công!");

            // Clear input fields
            clearInputs();

        } catch (NumberFormatException e) {
            bettingAmountInput.setError("Số tiền không hợp lệ");
            bettingAmountInput.requestFocus();
        }
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
}


//    private void setupClickListeners() {
//        submitButton.setOnClickListener(v -> {
//            if (validateInputs()) {
//                createBet();
//            }
//        });
//
//        showListButton.setOnClickListener(v -> {
//
//            showBetList();
//        });
//    }
//
//    private boolean validateInputs() {
//        String bettorName = bettorNameInput.getText().toString().trim();
//        String bettingNumberStr = bettingNumberInput.getText().toString().trim();
//        String bettingAmountStr = bettingAmountInput.getText().toString().trim();
//
//        // Validate bettor name
//        if (TextUtils.isEmpty(bettorName)) {
//            bettorNameInput.setError("Vui lòng nhập tên người cược");
//            bettorNameInput.requestFocus();
//            return false;
//        }
//
//        // Validate betting number
//        if (TextUtils.isEmpty(bettingNumberStr)) {
//            bettingNumberInput.setError("Vui lòng nhập số cược");
//            bettingNumberInput.requestFocus();
//            return false;
//        }
//
//        try {
//            int number = Integer.parseInt(bettingNumberStr);
//            if (number < 0 || number > 99) {
//                bettingNumberInput.setError("Số cược phải từ 0 đến 99");
//                bettingNumberInput.requestFocus();
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            bettingNumberInput.setError("Số cược không hợp lệ");
//            bettingNumberInput.requestFocus();
//            return false;
//        }
//
//        // Validate betting amount
//        if (TextUtils.isEmpty(bettingAmountStr)) {
//            bettingAmountInput.setError("Vui lòng nhập số tiền cược");
//            bettingAmountInput.requestFocus();
//            return false;
//        }
//
//        try {
//            double amount = Double.parseDouble(bettingAmountStr);
//            if (amount <= 0) {
//                bettingAmountInput.setError("Số tiền cược phải lớn hơn 0");
//                bettingAmountInput.requestFocus();
//                return false;
//            }
//            if (amount < 1000) {
//                bettingAmountInput.setError("Số tiền cược tối thiểu là 1,000 VNĐ");
//                bettingAmountInput.requestFocus();
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            bettingAmountInput.setError("Số tiền cược không hợp lệ");
//            bettingAmountInput.requestFocus();
//            return false;
//        }
//
//        return true;
//    }
//
//    private void createBet() {
//        // Check if user is logged in
//        if (!apiService.isLoggedIn()) {
//            Toast.makeText(this, "Vui lòng đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Get input values
//        String bettorName = bettorNameInput.getText().toString().trim();
//        int number = Integer.parseInt(bettingNumberInput.getText().toString().trim());
//        double amount = Double.parseDouble(bettingAmountInput.getText().toString().trim());
//        String userId = apiService.getUserId();
//
//        // Create bet request
//        BetRequest betRequest = new BetRequest(bettorName, number, amount, userId);
//
//        // Show loading state
//        submitButton.setEnabled(false);
//        submitButton.setText("Đang xử lý...");
//
//        // Make API call
//        Call<BetResponse> call = apiService.getApiInterface().createBet(betRequest);
//        call.enqueue(new Callback<BetResponse>() {
//            @Override
//            public void onResponse(Call<BetResponse> call, Response<BetResponse> response) {
//                // Reset button state
//                submitButton.setEnabled(true);
//                submitButton.setText("Xác Nhận");
//
//                if (response.isSuccessful() && response.body() != null) {
//                    BetResponse betResponse = response.body();
//
//                    if (betResponse.isSuccess()) {
//                        Toast.makeText(subAgentActivity.this,
//                                "Tạo cược thành công!", Toast.LENGTH_SHORT).show();
//
//                        // Clear inputs
//                        clearInputs();
//
//                        Log.d(TAG, "Bet created successfully: " + betResponse.getMessage());
//                    } else {
//                        Toast.makeText(subAgentActivity.this,
//                                "Lỗi: " + betResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                        Log.e(TAG, "Create bet failed: " + betResponse.getMessage());
//                    }
//                } else {
//                    String errorMessage = "Có lỗi xảy ra khi tạo cược";
//                    if (response.code() == 401) {
//                        errorMessage = "Phiên đăng nhập đã hết hạn";
//                        // Optionally redirect to login
//                    } else if (response.code() == 400) {
//                        errorMessage = "Dữ liệu không hợp lệ";
//                    } else if (response.code() == 500) {
//                        errorMessage = "Lỗi server, vui lòng thử lại sau";
//                    }
//
//                    Toast.makeText(subAgentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "Create bet error: " + response.code() + " - " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BetResponse> call, Throwable t) {
//                // Reset button state
//                submitButton.setEnabled(true);
//                submitButton.setText("Xác Nhận");
//
//                Toast.makeText(subAgentActivity.this,
//                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "Create bet network error", t);
//            }
//        });
//    }
//
//    private void clearInputs() {
//        bettorNameInput.setText("");
//        bettingNumberInput.setText("");
//        bettingAmountInput.setText("");
//        bettorNameInput.clearFocus();
//        bettingNumberInput.clearFocus();
//        bettingAmountInput.clearFocus();
//    }
//
//    private void showBetList() {
//        // Create intent to show bet list activity
//        // You'll need to create this activity
//        Intent intent = new Intent(this, BetListActivity.class);
//        startActivity(intent);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // Check if apiService is initialized
//        if (apiService == null) {
//            apiService = ApiService.getInstance(this);
//        }
//
//        // Check login status when activity resumes
//        if (!apiService.isLoggedIn()) {
//            Log.d(TAG, "User not logged in, redirecting to login");
//
//            Intent intent = new Intent(this, Login.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }
//    }


