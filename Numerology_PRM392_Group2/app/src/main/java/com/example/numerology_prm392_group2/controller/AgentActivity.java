package com.example.numerology_prm392_group2.controller;//package com.example.numerology_prm392_group2.controller;
//
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.numerology_prm392_group2.R;
//import com.example.numerology_prm392_group2.adapters.PlayerListAdapter;
//import com.google.android.material.appbar.MaterialToolbar;
//
//import java.util.List;
//
//public class AgentActivity extends AppCompatActivity {
//
//    private RecyclerView playerListRecyclerView;
//    private PlayerListAdapter adapter;
//    private MaterialToolbar toolbar;
//    private AgentService agentService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_agent);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        initViews();
//        setupToolbar();
//        setupRecyclerView();
//        loadPlayerData();
//    }
//
//    private void initViews() {
//        playerListRecyclerView = findViewById(R.id.playerListRecyclerView);
//        toolbar = findViewById(R.id.toolbar);
//        agentService = new AgentService();
//    }
//
//    private void setupToolbar() {
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("Danh Sách Người Chơi");
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//    }
//
//    private void setupRecyclerView() {
//        playerListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        // Khởi tạo adapter với danh sách rỗng
//        adapter = new PlayerListAdapter(this);
//        playerListRecyclerView.setAdapter(adapter);
//    }
//
//    private void loadPlayerData() {
//        // Lấy danh sách người chơi từ các SubAgent
//        agentService.getPlayersFromSubAgents(new AgentService.PlayersCallback() {
//            @Override
//            public void onSuccess(List<Player> players) {
//                runOnUiThread(() -> {
//                    if (players != null && !players.isEmpty()) {
//                        adapter.updatePlayerList(players);
//                    } else {
//                        Toast.makeText(AgentActivity.this,
//                                "Không có dữ liệu người chơi", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String error) {
//                runOnUiThread(() -> {
//                    Toast.makeText(AgentActivity.this,
//                            "Lỗi tải dữ liệu: " + error, Toast.LENGTH_SHORT).show();
//                });
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Refresh data khi quay lại activity
//        loadPlayerData();
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//}