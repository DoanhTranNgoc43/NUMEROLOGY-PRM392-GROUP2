package com.example.numerology_prm392_group2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.models.Recommendation;
import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {

    private List<Recommendation> recommendations;
    private Context context;

    public RecommendationAdapter(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommendation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recommendation recommendation = recommendations.get(position);

        holder.numberText.setText(recommendation.getNumber());
        holder.messageText.setText(recommendation.getMessage());

        switch (recommendation.getType()) {
            case "KEEP":
                holder.icon.setImageResource(R.drawable.ic_keep);
                holder.numberText.setTextColor(ContextCompat.getColor(context, R.color.keep_color));
                break;
            case "FORWARD":
                holder.icon.setImageResource(R.drawable.ic_forward);
                holder.numberText.setTextColor(ContextCompat.getColor(context, R.color.forward_color));
                break;
            case "NEUTRAL":
                holder.icon.setImageResource(R.drawable.ic_neutral);
                holder.numberText.setTextColor(ContextCompat.getColor(context, R.color.neutral_color));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return recommendations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView numberText;
        TextView messageText;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.recommendationIcon);
            numberText = itemView.findViewById(R.id.recommendationNumber);
            messageText = itemView.findViewById(R.id.recommendationMessage);
        }
    }
}