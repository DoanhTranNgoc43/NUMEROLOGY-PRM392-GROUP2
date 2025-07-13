package com.example.numerology_prm392_group2;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BetHistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bet_history);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<BettingHistoryManager.SavedBettingList> history = BettingHistoryManager.getInstance(this).getBettingHistory();
        historyAdapter = new HistoryAdapter(history, this);
        historyRecyclerView.setAdapter(historyAdapter);
    }
}