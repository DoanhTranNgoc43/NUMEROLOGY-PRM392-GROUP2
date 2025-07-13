
package com.example.numerology_prm392_group2.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.adapters.BetAdapter;
import com.example.numerology_prm392_group2.models.BetListResponse;
import com.example.numerology_prm392_group2.models.BetResponse;
import com.example.numerology_prm392_group2.utils.ApiService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BetListActivity extends AppCompatActivity {

    private static final String TAG = "BetListActivity";

    private RecyclerView recyclerView;
    private BetAdapter betAdapter;
    private ProgressBar progressBar;
    private TextView emptyView;
    private TextView totalAmountView;
    private TextView totalCountView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ApiService apiService;
    private List<BetResponse.Bet> betList;
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet_list);

        initViews();
        setupRecyclerView();
        setupSwipeRefresh();

        apiService = ApiService.getInstance(this);
        betList = new ArrayList<>();
        decimalFormat = new DecimalFormat("#,###");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Danh Sách Cược");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadBetList();
    }

    @SuppressLint("WrongViewCast")
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewBets);
        progressBar = findViewById(R.id.progressBar);
        emptyView = findViewById(R.id.emptyView);
        totalAmountView = findViewById(R.id.totalAmountView);
        totalCountView = findViewById(R.id.totalCountView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    private void setupRecyclerView() {
        betAdapter = new BetAdapter(betList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(betAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadBetList);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }

    private void loadBetList() {
        if (!apiService.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem danh sách cược", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showLoading(true);

        Call<BetListResponse> call = apiService.getApiInterface().getUserBets();
        call.enqueue(new Callback<BetListResponse>() {
            @Override
            public void onResponse(Call<BetListResponse> call, Response<BetListResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    BetListResponse betListResponse = response.body();

                    if (betListResponse.isSuccess()) {
                        updateBetList(betListResponse);
                    } else {
                        showError("Lỗi: " + betListResponse.getMessage());
                    }
                } else {
                    String errorMessage = "Có lỗi xảy ra khi tải danh sách";
                    if (response.code() == 401) {
                        errorMessage = "Phiên đăng nhập đã hết hạn";
                    } else if (response.code() == 404) {
                        errorMessage = "Không tìm thấy dữ liệu";
                    }
                    showError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<BetListResponse> call, Throwable t) {
                showLoading(false);
                showError("Lỗi kết nối: " + t.getMessage());
                Log.e(TAG, "Load bet list network error", t);
            }
        });
    }

    private void updateBetList(BetListResponse response) {
        betList.clear();

        if (response.getBets() != null && !response.getBets().isEmpty()) {
            betList.addAll(response.getBets());
            betAdapter.notifyDataSetChanged();

            updateStatistics(response.getTotalAmount(), response.getTotalCount());

            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            updateStatistics(0, 0);
        }
    }

    private void updateStatistics(double totalAmount, int totalCount) {
        totalAmountView.setText("Tổng tiền: " + decimalFormat.format(totalAmount) + " VNĐ");
        totalCountView.setText("Tổng số cược: " + totalCount);
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, message);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBetList();
    }
}

