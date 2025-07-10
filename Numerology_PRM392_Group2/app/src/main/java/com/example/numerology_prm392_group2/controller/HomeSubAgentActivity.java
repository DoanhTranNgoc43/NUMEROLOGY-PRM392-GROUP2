package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageView;

import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.utils.ApiService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HomeSubAgentActivity extends AppCompatActivity {

    private static final String TAG = "HomeSubAgentActivity";

    // UI Components
    private TextView textViewGreeting;
    private AppCompatButton btnNewBet;
    private AppCompatButton btnCustomerList;
    private AppCompatButton btnPersonalInfo;
    private AppCompatButton btnBetHistory;
    private AppCompatButton btnDailySummary;
    private AppCompatButton btnContactAgent;
    private AppCompatButton btnNotification;
    private AppCompatButton btnRevenue;

    private AppCompatButton btnBack;
    private ImageView imageViewAvatar;

    // Data
    private String userName;
    private String userId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_sub_agent);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        loadUserData();
        setupClickListeners();
    }

    private void initializeComponents() {
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewGreeting = findViewById(R.id.textViewGreeting);
        btnNewBet = findViewById(R.id.btnNewBet);
        btnCustomerList = findViewById(R.id.btnCustomerList);
        btnPersonalInfo = findViewById(R.id.btnPersonalInfo);
        btnBetHistory = findViewById(R.id.btnBetHistory);
        btnDailySummary = findViewById(R.id.btnDailySummary);
        btnContactAgent = findViewById(R.id.btnContactAgent);
        btnNotification = findViewById(R.id.btnNotification);
        btnBack = findViewById(R.id.btnBack);
        btnRevenue = findViewById(R.id.btnRevenue);
        apiService = ApiService.getInstance(this);
    }

    private void loadUserData() {
        // Load user data from ApiService
        userName = apiService.getUserName();
        userId = apiService.getUserId();

        Log.d(TAG, "Loading user data - Name: " + userName + ", ID: " + userId);

        // Update greeting text
        if (userName != null && !userName.isEmpty()) {
            textViewGreeting.setText("Xin chào, " + userName);
        } else {
            textViewGreeting.setText("Xin chào, SubAgent");
        }
    }

    private void setupClickListeners() {
        imageViewAvatar.setOnClickListener(v -> {
            Log.d(TAG, "Avatar clicked");
            showAvatarOptionsDialog();
        });

        btnNewBet.setOnClickListener(v -> {
            Intent intent = new Intent(HomeSubAgentActivity.this, subAgentActivity.class);
            startActivity(intent);
        });


        btnCustomerList.setOnClickListener(v -> {
            showFeatureDialog("Danh sách khách hàng", "Chức năng danh sách khách hàng đang được phát triển");
            // TODO: Navigate to CustomerListActivity
            // Intent intent = new Intent(HomeSubAgentActivity.this, CustomerListActivity.class);
            // startActivity(intent);
        });


        btnPersonalInfo.setOnClickListener(v -> {
             Intent intent = new Intent(HomeSubAgentActivity.this, PersonalInfoActivity.class);
             startActivity(intent);
        });

        btnBetHistory.setOnClickListener(v -> {
            showFeatureDialog("Lịch sử", "Chức năng lịch sử ghi đề đang được phát triển");
            // TODO: Navigate to BetHistoryActivity
            // Intent intent = new Intent(HomeSubAgentActivity.this, BetHistoryActivity.class);
            // startActivity(intent);
        });


        btnDailySummary.setOnClickListener(v -> {
            Log.d(TAG, "Daily Summary button clicked");
            showFeatureDialog("Tổng kết ngày", "Chức năng tổng kết ngày đang được phát triển");
            // TODO: Navigate to DailySummaryActivity
            // Intent intent = new Intent(HomeSubAgentActivity.this, DailySummaryActivity.class);
            // startActivity(intent);
        });


        btnContactAgent.setOnClickListener(v -> {
             Intent intent = new Intent(HomeSubAgentActivity.this, ContactAgentActivity.class);
             startActivity(intent);
        });
        btnNotification.setOnClickListener(v -> {
             Intent intent = new Intent(HomeSubAgentActivity.this, NotificationActivity.class);
             startActivity(intent);
        });
        btnRevenue.setOnClickListener(v -> {
             Intent intent = new Intent(HomeSubAgentActivity.this, RevenueActivity.class);
             startActivity(intent);
        });

    }

    private void showAvatarOptionsDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Tùy chọn")
                .setItems(new String[]{"Cài đặt", "Đăng xuất"}, (dialog, which) -> {
                    if (which == 0) {
                         Intent intent = new Intent(HomeSubAgentActivity.this, SettingsActivity.class);
                         startActivity(intent);
                    }if(which == 1) {
                        showLogoutConfirmation();
                    }
                })
                .show();
    }

    private void showFeatureDialog(String title, String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showLogoutConfirmation() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void performLogout() {
        try {
            // Clear login data using ApiService
            apiService.clearLoginData();

            // Show logout message
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

            // Navigate back to login
            Intent intent = new Intent(HomeSubAgentActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            Log.d(TAG, "Logout completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error during logout", e);
            Toast.makeText(this, "Lỗi khi đăng xuất", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if user is still logged in
        if (apiService != null && !apiService.isLoggedIn()) {
            Log.d(TAG, "User not logged in, redirecting to login");
            Intent intent = new Intent(HomeSubAgentActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "HomeSubAgentActivity destroyed");
    }

    @Override
    public void onBackPressed() {
        // Override back button to show logout confirmation
        showLogoutConfirmation();
    }
}