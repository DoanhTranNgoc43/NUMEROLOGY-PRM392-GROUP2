package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.numerology_prm392_group2.R;
import com.google.android.material.snackbar.Snackbar;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView fullNameText, emailText;
    private Button btnBackTo;
    private Button btnSettings;
    private Button btnList;
    private Button btnPersonalInfo;
    private Button btnNotifications;

    private Button btnrevenue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.imageView2);
        fullNameText = findViewById(R.id.textView);
        emailText = findViewById(R.id.textView2);
        btnBackTo = findViewById(R.id.btnBackTo);
        btnSettings = findViewById(R.id.btnSettings);
        btnList = findViewById(R.id.btnList);
        btnPersonalInfo = findViewById(R.id.btnPersonalInfo);
        btnNotifications = findViewById(R.id.btnNoti);
        btnrevenue = findViewById(R.id.btnReve);

        loadUserInfo();

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);

            });
        }

        if (btnList!= null) {
            btnList.setOnClickListener(v -> {
//                Intent intent = new Intent(HistoryListActivity.this, PersonalInfoActivity.class);
//                startActivity(intent);
                Snackbar snackbar = Snackbar.make(v, "Chức năng đang bảo trì", Snackbar.LENGTH_SHORT);
                View snackbarView = snackbar.getView();
                ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
                if (params instanceof FrameLayout.LayoutParams) {
                    FrameLayout.LayoutParams frameParams = (FrameLayout.LayoutParams) params;
                    frameParams.gravity = Gravity.TOP;
                    snackbarView.setLayoutParams(frameParams);
                }
                snackbar.show();
            });
        }

        if (btnPersonalInfo != null) {
            btnPersonalInfo.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
            });
        }
        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, NotificationActivity.class);
                startActivity(intent);
            });
        }
        if (btnrevenue != null) {
            btnrevenue.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, RevenueActivity.class);
                startActivity(intent);
            });
        }
        if (btnBackTo != null) {
            btnBackTo.setOnClickListener(v -> navigateBack());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }

    private void loadUserInfo() {
        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String fullName = prefs.getString("full_name", "Chưa cập nhật tên");
        String email = prefs.getString("user_email", "Không có email");
        String avatarUrl = prefs.getString("avatarUrl", "");

        fullNameText.setText(fullName);
        emailText.setText(email);

        if (!avatarUrl.isEmpty()) {
            Glide.with(this).load(avatarUrl).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.profile);
        }
    }

    private void navigateBack() {
        finish();
    }
}