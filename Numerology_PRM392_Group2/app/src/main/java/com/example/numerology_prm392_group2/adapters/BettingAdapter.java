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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BettingAdapter extends RecyclerView.Adapter<BettingAdapter.BettingViewHolder> {

    private List<BettingInfo> bettingList;
    private NumberFormat currencyFormat;

    public BettingAdapter(List<BettingInfo> bettingList) {
        this.bettingList = bettingList;
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
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
        holder.bind(betting);
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

        public BettingViewHolder(@NonNull View itemView) {
            super(itemView);
            bettorNameText = itemView.findViewById(R.id.bettorNameText);
            bettingNumberText = itemView.findViewById(R.id.bettingNumberText);
            bettingAmountText = itemView.findViewById(R.id.bettingAmountText);
            winningAmountText = itemView.findViewById(R.id.winningAmountText);
            winnerIndicator = itemView.findViewById(R.id.winnerIndicator);
        }

        public void bind(BettingInfo betting) {
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
        }
    }
}