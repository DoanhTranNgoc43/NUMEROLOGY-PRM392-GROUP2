package com.example.numerology_prm392_group2.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.models.NumberStats;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TopNumbersAdapter extends RecyclerView.Adapter<TopNumbersAdapter.TopNumberViewHolder> {

    private List<NumberStats> topNumbers;

    private NumberFormat currencyFormat;

    public TopNumbersAdapter(List<NumberStats> topNumbers) {
        this.topNumbers = topNumbers;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }

    @NonNull
    @Override
    public TopNumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_number, parent, false);
        return new TopNumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopNumberViewHolder holder, int position) {
        NumberStats numberStats = topNumbers.get(position);
        holder.bind(numberStats, position + 1);
    }

    @Override
    public int getItemCount() {
        return Math.min(topNumbers.size(), 5); // Chỉ hiển thị top 5
    }

    class TopNumberViewHolder extends RecyclerView.ViewHolder {
        private TextView rankText;
        private TextView numberText;
        private TextView totalAmountText;
        private TextView ticketCountText;
        private TextView riskText;

        public TopNumberViewHolder(@NonNull View itemView) {
            super(itemView);
            rankText = itemView.findViewById(R.id.rankText);
            numberText = itemView.findViewById(R.id.numberText);
            totalAmountText = itemView.findViewById(R.id.totalAmountText);
            ticketCountText = itemView.findViewById(R.id.ticketCountText);
            riskText = itemView.findViewById(R.id.riskText);
        }

        public void bind(NumberStats numberStats, int rank) {
            rankText.setText(rank + ".");
            numberText.setText(numberStats.getNumber());
            totalAmountText.setText(currencyFormat.format(numberStats.getTotalAmount()));
            ticketCountText.setText(numberStats.getTicketCount() + " vé");
            riskText.setText(numberStats.getRiskLevel());
            riskText.setTextColor(Color.parseColor(numberStats.getRiskColor()));
        }
    }
}