package com.example.numerology_prm392_group2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numerology_prm392_group2.models.BettingInfo;
import com.example.numerology_prm392_group2.R;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BettingAdapter extends RecyclerView.Adapter<BettingAdapter.BettingViewHolder> {

    private List<BettingInfo> bettingList;
    private NumberFormat currencyFormat;
    private OnItemClickListener listener;
    private final boolean readOnly;

    public interface OnItemClickListener {
        void onEditClick(BettingInfo bettingInfo, int position);
        void onDeleteClick(BettingInfo bettingInfo, int position);
    }

    // Constructor for normal mode (with edit/delete)
    public BettingAdapter(List<BettingInfo> bettingList, OnItemClickListener listener) {
        this.bettingList = bettingList;
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        this.listener = listener;
        this.readOnly = false;
    }

    // Constructor for read-only mode (no edit/delete)
    public BettingAdapter(List<BettingInfo> bettingList, boolean readOnly) {
        this.bettingList = bettingList;
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        this.listener = null;
        this.readOnly = readOnly;
    }

    @NonNull
    @Override
    public BettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_betting, parent, false);
        return new BettingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BettingViewHolder holder, int position) {
        BettingInfo betting = bettingList.get(position);
        holder.bind(betting, position);
    }

    @Override
    public int getItemCount() {
        return bettingList.size();
    }

    public void updateList(List<BettingInfo> newList) {
        this.bettingList = newList;
        notifyDataSetChanged();
    }

    class BettingViewHolder extends RecyclerView.ViewHolder {
        private TextView bettorNameText;
        private TextView bettingNumberText;
        private TextView bettingAmountText;
        private TextView winningAmountText;
        private ImageView winnerIndicator;
        private MaterialButton editButton;
        private MaterialButton deleteButton;

        public BettingViewHolder(@NonNull View itemView) {
            super(itemView);
            bettorNameText = itemView.findViewById(R.id.bettorNameText);
            bettingNumberText = itemView.findViewById(R.id.bettingNumberText);
            bettingAmountText = itemView.findViewById(R.id.bettingAmountText);
            winningAmountText = itemView.findViewById(R.id.winningAmountText);
            winnerIndicator = itemView.findViewById(R.id.winnerIndicator);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(BettingInfo betting, int position) {
            bettorNameText.setText(betting.getBettorName());
            bettingNumberText.setText(betting.getBettingNumber());
            bettingAmountText.setText(currencyFormat.format(betting.getBettingAmount()) + " VNĐ");

            if (betting.isWinner()) {
                itemView.setSelected(true);
                winnerIndicator.setVisibility(View.VISIBLE);
                winningAmountText.setVisibility(View.VISIBLE);
                winningAmountText.setText("Thắng: " + currencyFormat.format(betting.getWinningAmount()) + " VNĐ");
            } else {
                itemView.setSelected(false);
                winnerIndicator.setVisibility(View.GONE);
                winningAmountText.setVisibility(View.GONE);
            }

            if (readOnly) {
                // Hide edit and delete buttons in read-only mode
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            } else {
                // Show buttons and set click listeners in normal mode
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                if (listener != null) {
                    editButton.setOnClickListener(v -> listener.onEditClick(betting, position));
                    deleteButton.setOnClickListener(v -> listener.onDeleteClick(betting, position));
                }
            }
        }
    }
}