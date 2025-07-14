package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.numerology_prm392_group2.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class RevenueActivity extends AppCompatActivity {

    private TextView titleTextView;
    private ListView revenueListView;

    private ArrayList<String> revenueData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);
        titleTextView = findViewById(R.id.textViewTitle);
        revenueListView = findViewById(R.id.revenueListView);
        loadRevenueData();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, revenueData);
        revenueListView.setAdapter(adapter);


    }

    private void loadRevenueData() {

        revenueData = new ArrayList<>();
        revenueData.add("Tháng 1/2025: 5,000,000 VNĐ");
        revenueData.add("Tháng 2/2025: 6,200,000 VNĐ");
        revenueData.add("Tháng 3/2025: 4,800,000 VNĐ");
        revenueData.add("Tháng 4/2025: 7,100,000 VNĐ");
        revenueData.add("Tháng 5/2025: 5,500,000 VNĐ");
        revenueData.add("Tháng 6/2025: 6,000,000 VNĐ");
    }


}