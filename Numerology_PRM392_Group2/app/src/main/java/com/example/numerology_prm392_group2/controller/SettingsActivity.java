package com.example.numerology_prm392_group2.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.numerology_prm392_group2.R;
import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications;
    private Switch switchDarkMode;
    private Button btnSaveSettings;
    private Button btnBack;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize UI elements
        switchNotifications = findViewById(R.id.switchNotifications);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);
        btnBack = findViewById(R.id.btnBack);

        // Initialize SharedPreferences
        prefs = getSharedPreferences("SettingsPrefs", MODE_PRIVATE);

        // Load saved settings
        loadSettings();

        // Set listeners
        btnSaveSettings.setOnClickListener(v -> saveSettings());
        btnBack.setOnClickListener(v -> navigateBack());
    }

    private void loadSettings() {
        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        boolean darkModeEnabled = prefs.getBoolean("dark_mode_enabled", false);

        switchNotifications.setChecked(notificationsEnabled);
        switchDarkMode.setChecked(darkModeEnabled);
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("notifications_enabled", switchNotifications.isChecked());
        editor.putBoolean("dark_mode_enabled", switchDarkMode.isChecked());
        editor.apply();

        // Show confirmation snackbar
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Cài đặt đã được lưu", Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
        if (params instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams frameParams = (FrameLayout.LayoutParams) params;
            frameParams.gravity = Gravity.TOP;
            snackbarView.setLayoutParams(frameParams);
        }
        snackbar.show();
    }

    private void navigateBack() {
        finish(); // Close current activity and return to previous one
    }
}