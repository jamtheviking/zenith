package com.csis4175.zenith;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder> {

    private List<RoutineItem> routineItemList;

    public RoutineAdapter(List<RoutineItem> routineItemList) {
        this.routineItemList = routineItemList;
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.routine_item, parent, false);
        return new RoutineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        RoutineItem routineItem = routineItemList.get(position);
        holder.nameTextView.setText(routineItem.getName());
        holder.detailsTextView.setText(routineItem.getDetails());
        holder.timeTextView.setText(routineItem.getTime());

        //Add onClickListener to the entire item view
        holder.itemView.setOnClickListener(v -> {
            holder.checkboxRoutine.setChecked(!holder.checkboxRoutine.isChecked());
        });

        holder.checkboxRoutine.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle checkbox state change
        });
    }

    @Override
    public int getItemCount() {
        return routineItemList.size();
    }

    public void removeItem(int position) {
        routineItemList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItem(int position, RoutineItem updatedRoutineItem) {
        routineItemList.set(position, updatedRoutineItem);
        notifyItemChanged(position);
    }

    public static class RoutineViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, detailsTextView, timeTextView;
        public CheckBox checkboxRoutine;

        public RoutineViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            detailsTextView = view.findViewById(R.id.detailsTextView);
            timeTextView = view.findViewById(R.id.timeTextView);
            checkboxRoutine = view.findViewById(R.id.checkboxRoutine);
        }
    }
}



