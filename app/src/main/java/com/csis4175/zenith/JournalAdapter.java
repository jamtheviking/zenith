package com.csis4175.zenith;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private List<JournalEntry> journalEntries;

    public JournalAdapter(List<JournalEntry> journalEntries) {
        this.journalEntries = journalEntries;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_journal, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        JournalEntry entry = journalEntries.get(position);
        holder.textViewDescription.setText(entry.getDescription());
        holder.textViewTimestamp.setText(entry.getTimestamp());

        int moodIcon;
        switch (entry.getMood()) {
            case "Amazing":
                moodIcon = R.drawable.amazing;
                break;
            case "Great":
                moodIcon = R.drawable.good;
                break;
            case "Good":
                moodIcon = R.drawable.meh;
                break;
            case "Bad":
                moodIcon = R.drawable.bad;
                break;
            case "Awful":
                moodIcon = R.drawable.angry;
                break;
            default:
                moodIcon = R.drawable.meh;
                break;
        }
        holder.imageViewMood.setImageResource(moodIcon);
    }

    @Override
    public int getItemCount() {
        return journalEntries.size();
    }

    static class JournalViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMood;
        TextView textViewDescription;
        TextView textViewTimestamp;

        JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMood = itemView.findViewById(R.id.imageViewMood);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}