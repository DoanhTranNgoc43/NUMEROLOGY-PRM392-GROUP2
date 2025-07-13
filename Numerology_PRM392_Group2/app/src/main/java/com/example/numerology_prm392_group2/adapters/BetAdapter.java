package com.example.numerology_prm392_group2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.models.BetResponse;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BetAdapter extends RecyclerView.Adapter<BetAdapter.BetViewHolder> {

    private List<BetResponse.Bet> betList;
    private Context context;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat dateFormat;

    public BetAdapter(List<BetResponse.Bet> betList, Context context) {
        this.betList = betList;
        this.context = context;
        this.decimalFormat = new DecimalFormat("#,###");
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public BetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bet, parent, false);
        return new BetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BetViewHolder holder, int position) {
        BetResponse.Bet bet = betList.get(position);

        holder.bettorNameText.setText(bet.getBettorName());
        holder.numberText.setText(String.valueOf(bet.getNumber()));
        holder.amountText.setText(decimalFormat.format(bet.getAmount()) + " VNĐ");


        try {
            holder.dateText.setText(bet.getCreatedDate());
        } catch (Exception e) {
            holder.dateText.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return betList != null ? betList.size() : 0;
    }

    public static class BetViewHolder extends RecyclerView.ViewHolder {
        TextView bettorNameText;
        TextView numberText;
        TextView amountText;
        TextView dateText;

        public BetViewHolder(@NonNull View itemView) {
            super(itemView);
            bettorNameText = itemView.findViewById(R.id.bettorNameText);
            numberText = itemView.findViewById(R.id.numberText);
            amountText = itemView.findViewById(R.id.amountText);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }
}