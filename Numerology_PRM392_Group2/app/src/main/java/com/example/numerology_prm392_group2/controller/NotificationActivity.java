package com.example.numerology_prm392_group2.controller;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.adapters.NotificationAdapter;
import com.example.numerology_prm392_group2.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        setupRecyclerView();
    }
    private void setupRecyclerView() {
        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(new Notification("Cập Nhật Hệ Thống", "Hệ thống sẽ bảo trì vào 10:00 ngày 10/07/2025.", "2025-07-09 12:47"));
        notificationList.add(new Notification("Khuyến Mãi Mới", "Giảm giá 20% cho khách hàng mới từ 15/07/2025.", "2025-07-08 15:30"));
        notificationList.add(new Notification("Thông Báo Quan Trọng", "Vui lòng cập nhật thông tin cá nhân trước 20/07/2025.", "2025-07-07 09:00"));
        NotificationAdapter adapter = new NotificationAdapter(notificationList);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotifications.setAdapter(adapter);
    }
}