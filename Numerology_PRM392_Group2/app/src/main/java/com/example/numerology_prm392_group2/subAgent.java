package com.example.numerology_prm392_group2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numerology_prm392_group2.model.XSMBResult;
import com.example.numerology_prm392_group2.service.NumerologyService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class subAgent extends AppCompatActivity {

    private TextInputEditText bettorNameInput;
    private TextInputEditText bettingNumberInput;
    private TextInputEditText bettingAmountInput;
    private MaterialButton submitButton;
    private MaterialButton showListButton;

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

        initViews();
        setupClickListeners();
        callApiAndConsoleData();
    }
    private void callApiAndConsoleData() {
        Log.d("API", "Starting API call...");
        NumerologyService.getAllXSMBResults()
                .thenAccept(response -> {
                    runOnUiThread(() -> {
                        Log.d("API", "=== RESPONSE ===");
                        Log.d("API", "Status: " + response.getStatus());
                        Log.d("API", "Message: " + response.getMessage());
                        if (response.getData() != null) {
                            List<XSMBResult> data = response.getData();
                            Log.d("API", "Data size: " + data.size());
                            System.out.println(data.size());
                            // Console từng item
                            for (int i = 0; i < data.size(); i++) {
                                Log.d("DATA", "Item " + i + ": " + data.get(i).toString());
                            }
                        } else {
                            Log.d("API", "Data is null");
                        }
                        Log.d("API", "=== END ===");
                    });
                })
                .exceptionally(error -> {
                    runOnUiThread(() -> {
                        Log.e("API", "Error: " + error.getMessage());
                        error.printStackTrace();
                    });
                    return null;
                });
    }

    private void initViews() {
        bettorNameInput = findViewById(R.id.bettorNameInput);
        bettingNumberInput = findViewById(R.id.bettingNumberInput);
        bettingAmountInput = findViewById(R.id.bettingAmountInput);
        submitButton = findViewById(R.id.submitButton);
        showListButton = findViewById(R.id.showListButton);
    }

    private void setupClickListeners() {
        submitButton.setOnClickListener(v -> handleSubmit());
        showListButton.setOnClickListener(v -> showBettingList());
    }

    private void handleSubmit() {
        String bettorName = bettorNameInput.getText().toString().trim();
        String bettingNumber = bettingNumberInput.getText().toString().trim();
        String bettingAmountStr = bettingAmountInput.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(bettorName)) {
            bettorNameInput.setError("Vui lòng nhập tên người cược");
            bettorNameInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(bettingNumber)  ) {
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

            // Create betting info object and add to manager
            BettingInfo bettingInfo = new BettingInfo(bettorName, bettingNumber, bettingAmount);
            BettingManager.getInstance().addBetting(bettingInfo);

            // Show success message
            Toast.makeText(this, "Đã thêm thông tin cược thành công!", Toast.LENGTH_SHORT).show();

            // Clear input fields
            clearInputs();

        } catch (NumberFormatException e) {
            bettingAmountInput.setError("Số tiền không hợp lệ");
            bettingAmountInput.requestFocus();
        }
    }

    private void showBettingList() {
        Intent intent = new Intent(this, list.class);
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