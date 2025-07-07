package com.example.numerology_prm392_group2;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class list extends AppCompatActivity {

    private RecyclerView bettingListRecyclerView;
    private BettingAdapter adapter;
    private MaterialButton probabilityAnalysisButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bildingViews();
        setupRecyclerView();
        setupClickListeners();
        loadBettingData();
    }

    private void bildingViews() {
        bettingListRecyclerView = findViewById(R.id.bettingListRecyclerView);
        probabilityAnalysisButton = findViewById(R.id.probabilityAnalysisButton);
    }

    private void setupRecyclerView() {
        bettingListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        adapter = new BettingAdapter(bettingList);
        bettingListRecyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        probabilityAnalysisButton.setOnClickListener(v -> openProbabilityAnalysis());
    }

    private void openProbabilityAnalysis() {
        Intent intent = new Intent(this, ProbabilityAnalysisActivity.class);
        startActivity(intent);
    }

    private void loadBettingData() {
        List<BettingInfo> bettingList = BettingManager.getInstance().getBettingList();
        adapter.updateList(bettingList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        loadBettingData();
    }
}