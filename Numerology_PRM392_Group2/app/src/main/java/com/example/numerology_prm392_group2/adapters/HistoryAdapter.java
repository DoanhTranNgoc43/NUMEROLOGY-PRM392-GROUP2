package com.example.numerology_prm392_group2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.manager.BettingHistoryManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<BettingHistoryManager.SavedBettingList> historyList;
    private final BettingHistoryManager historyManager;
    private final Context context;

    public HistoryAdapter(List<BettingHistoryManager.SavedBettingList> historyList, Context context) {
        this.historyList = historyList;
        this.historyManager = BettingHistoryManager.getInstance(context);
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        BettingHistoryManager.SavedBettingList savedList = historyList.get(position);
        holder.timestampTextView.setText(savedList.getTimestamp());
        holder.bettingRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

        // Use BettingAdapter in read-only mode (no edit/delete)
        BettingAdapter bettingAdapter = new BettingAdapter(savedList.getBettingList(), true);
        holder.bettingRecyclerView.setAdapter(bettingAdapter);

        holder.deleteButton.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá danh sách cược này không?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        historyManager.deleteBettingList(savedList.getTimestamp());
                        historyList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, historyList.size());
                    })
                    .setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView timestampTextView;
        RecyclerView bettingRecyclerView;
        MaterialButton deleteButton;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            bettingRecyclerView = itemView.findViewById(R.id.bettingRecyclerView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
