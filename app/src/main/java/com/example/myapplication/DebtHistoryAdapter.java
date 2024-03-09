package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DebtHistoryAdapter extends RecyclerView.Adapter<DebtHistoryAdapter.DebtHistoryViewHolder> {

    private List<DebtEntry> debtHistory;

    public DebtHistoryAdapter(List<DebtEntry> debtHistory) {
        this.debtHistory = debtHistory;
    }

    @NonNull
    @Override
    public DebtHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.debt_history_item, parent, false);
        return new DebtHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtHistoryViewHolder holder, int position) {
        DebtEntry entry = debtHistory.get(position);
        holder.textViewTitle.setText(entry.getTitle());
        holder.textViewDate.setText(entry.getFormattedDate().toString());  // Format as needed
        holder.textViewAmount.setText(String.format("%.2f zł", entry.getAmount()));

        if (entry.getAmount() >= 0) {
            holder.textViewAmount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.positiveColor));
        } else {
            holder.textViewAmount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.negativeColor));
        }
    }

    @Override
    public int getItemCount() {
        return debtHistory.size();
    }

    public static class DebtHistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTitle;
        public TextView textViewDate;
        public TextView textViewAmount;
        Button btnRemove;

        public DebtHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
        }
    }
}
