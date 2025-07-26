package com.example.numerology_prm392_group2.controller;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.adapters.BalancedBettingAdapter;
import com.example.numerology_prm392_group2.models.KeepItem;
import com.example.numerology_prm392_group2.models.ForwardItem;

import java.util.ArrayList;


public class BalancedBettingListActivity extends AppCompatActivity {

    private RecyclerView keepListRecyclerView;
    private RecyclerView forwardListRecyclerView;
    private Button exportListsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balanced_betting_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        keepListRecyclerView = findViewById(R.id.keepListRecyclerView);
        forwardListRecyclerView = findViewById(R.id.forwardListRecyclerView);
        exportListsButton = findViewById(R.id.exportListsButton);

        ArrayList<KeepItem> keepItems = (ArrayList<KeepItem>) getIntent().getSerializableExtra("keepList");
        ArrayList<ForwardItem> forwardItems = (ArrayList<ForwardItem>) getIntent().getSerializableExtra("forwardList");

        keepListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        forwardListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        BalancedBettingAdapter keepAdapter = new BalancedBettingAdapter(this, keepItems, BalancedBettingAdapter.TYPE_KEEP);
        BalancedBettingAdapter forwardAdapter = new BalancedBettingAdapter(this, forwardItems, BalancedBettingAdapter.TYPE_FORWARD);

        keepListRecyclerView.setAdapter(keepAdapter);
        forwardListRecyclerView.setAdapter(forwardAdapter);

        exportListsButton.setOnClickListener(v -> {
        });
    }
}