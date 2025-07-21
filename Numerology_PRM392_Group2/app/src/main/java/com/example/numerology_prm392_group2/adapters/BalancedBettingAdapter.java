package com.example.numerology_prm392_group2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.models.KeepItem;
import com.example.numerology_prm392_group2.models.ForwardItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BalancedBettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_KEEP = 0;
    public static final int TYPE_FORWARD = 1;
    private Context context;
    private List<?> items;
    private int itemType;
    private NumberFormat currencyFormat;

    public BalancedBettingAdapter(Context context, List<?> items, int itemType) {
        this.context = context;
        this.items = items;
        this.itemType = itemType;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }

    @Override
    public int getItemViewType(int position) {
        return itemType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_KEEP) {
            View view = inflater.inflate(R.layout.item_keep_list, parent, false);
            return new KeepViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_forward_list, parent, false);
            return new ForwardViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (itemType == TYPE_KEEP) {
            KeepItem item = (KeepItem) items.get(position);
            KeepViewHolder keepHolder = (KeepViewHolder) holder;
            keepHolder.numberText.setText(item.getNumber());
            keepHolder.selfBetAmountText.setText(currencyFormat.format(item.getSelfBetAmount()) + " (" +
                    String.format("%.2f%%", item.getSelfBetPercentage() * 100) + ")");
            keepHolder.profitText.setText("Nếu không trúng: " + currencyFormat.format(item.getProfitIfNotWin()) + "\n" +
                    "Nếu trúng: " + currencyFormat.format(item.getProfitIfWin()));
        } else {
            ForwardItem item = (ForwardItem) items.get(position);
            ForwardViewHolder forwardHolder = (ForwardViewHolder) holder;
            forwardHolder.numberText.setText(item.getNumber());
            forwardHolder.forwardAmountText.setText(currencyFormat.format(item.getForwardAmount()) + " (" +
                    String.format("%.2f%%", item.getForwardPercentage() * 100) + ")");
            forwardHolder.commissionText.setText(currencyFormat.format(item.getCommission()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class KeepViewHolder extends RecyclerView.ViewHolder {
        TextView numberText, selfBetAmountText, profitText;

        KeepViewHolder(View itemView) {
            super(itemView);
            numberText = itemView.findViewById(R.id.numberText);
            selfBetAmountText = itemView.findViewById(R.id.selfBetAmountText);
            profitText = itemView.findViewById(R.id.profitText);
        }
    }

    static class ForwardViewHolder extends RecyclerView.ViewHolder {
        TextView numberText, forwardAmountText, commissionText;

        ForwardViewHolder(View itemView) {
            super(itemView);
            numberText = itemView.findViewById(R.id.numberText);
            forwardAmountText = itemView.findViewById(R.id.forwardAmountText);
            commissionText = itemView.findViewById(R.id.commissionText);
        }
    }
}