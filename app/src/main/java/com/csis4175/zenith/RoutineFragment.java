package com.csis4175.zenith;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RoutineFragment extends Fragment {

    private RecyclerView recyclerView;
    private RoutineAdapter adapter;
    private List<RoutineItem> routineItems;
    private Button addRoutineButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        routineItems = new ArrayList<>();
        //Placeholder Items
        routineItems.add(new RoutineItem("Morning Exercise", "Morning workout to start the day.", "6:00 AM"));
        routineItems.add(new RoutineItem("Breakfast", "Healthy breakfast to fuel the day.", "7:30 AM"));
        routineItems.add(new RoutineItem("Work", "Work on project tasks.", "9:00 AM"));

        adapter = new RoutineAdapter(routineItems);
        recyclerView.setAdapter(adapter);

        addRoutineButton = view.findViewById(R.id.addRoutineButton);
        addRoutineButton.setOnClickListener(v -> showAddRoutineDialog());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private final ColorDrawable deleteBackground = new ColorDrawable(Color.parseColor("#F44336"));
            private final ColorDrawable editBackground = new ColorDrawable(Color.parseColor("#4CAF50"));
            private final Drawable deleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.baseline_delete_sweep_24);
            private final Drawable editIcon = ContextCompat.getDrawable(getContext(), R.drawable.baseline_edit_square_24);

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    adapter.removeItem(position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    showEditRoutineDialog(position);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20; // Offset to prevent corner rounding

                if (dX > 0) { // Swiping to the right
                    editBackground.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                    editBackground.draw(c);

                    int editIconMargin = (itemView.getHeight() - editIcon.getIntrinsicHeight()) / 2;
                    int editIconTop = itemView.getTop() + editIconMargin;
                    int editIconBottom = editIconTop + editIcon.getIntrinsicHeight();
                    int editIconLeft = itemView.getLeft() + editIconMargin;
                    int editIconRight = editIconLeft + editIcon.getIntrinsicWidth();

                    editIcon.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom);
                    editIcon.draw(c);

                } else if (dX < 0) { // Swiping to the left
                    deleteBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    deleteBackground.draw(c);

                    int deleteIconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                    int deleteIconTop = itemView.getTop() + deleteIconMargin;
                    int deleteIconBottom = deleteIconTop + deleteIcon.getIntrinsicHeight();
                    int deleteIconLeft = itemView.getRight() - deleteIconMargin - deleteIcon.getIntrinsicWidth();
                    int deleteIconRight = itemView.getRight() - deleteIconMargin;

                    deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                    deleteIcon.draw(c);
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    /**
     * This is for adding routine
     */
    private void showAddRoutineDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_routine, null);
        builder.setView(dialogView);

        EditText routineTitleEditText = dialogView.findViewById(R.id.routineTitleEditText);
        EditText routineDetailsEditText = dialogView.findViewById(R.id.routineDetailsEditText);
        EditText routineTimeEditText = dialogView.findViewById(R.id.routineTimeEditText);
        Button saveRoutineButton = dialogView.findViewById(R.id.saveRoutineButton);

        routineTimeEditText.setOnClickListener(v -> showCustomTimePickerDialog(routineTimeEditText));

        AlertDialog dialog = builder.create();

        saveRoutineButton.setOnClickListener(v -> {
            String title = routineTitleEditText.getText().toString().trim();
            String details = routineDetailsEditText.getText().toString().trim();
            String time = routineTimeEditText.getText().toString().trim();

            if (title.isEmpty() || details.isEmpty() || time.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                addRoutine(new RoutineItem(title, details, time));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Editing Routine
     * @param position
     */
    private void showEditRoutineDialog(int position) {
        RoutineItem routineItem = routineItems.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_routine, null);
        builder.setView(dialogView);

        EditText routineTitleEditText = dialogView.findViewById(R.id.routineTitleEditText);
        EditText routineDetailsEditText = dialogView.findViewById(R.id.routineDetailsEditText);
        EditText routineTimeEditText = dialogView.findViewById(R.id.routineTimeEditText);
        Button saveRoutineButton = dialogView.findViewById(R.id.saveRoutineButton);

        routineTitleEditText.setText(routineItem.getName());
        routineDetailsEditText.setText(routineItem.getDetails());
        routineTimeEditText.setText(routineItem.getTime());

        routineTimeEditText.setOnClickListener(v -> showCustomTimePickerDialog(routineTimeEditText));

        AlertDialog dialog = builder.create();

        saveRoutineButton.setOnClickListener(v -> {
            String title = routineTitleEditText.getText().toString().trim();
            String details = routineDetailsEditText.getText().toString().trim();
            String time = routineTimeEditText.getText().toString().trim();

            if (title.isEmpty() || details.isEmpty() || time.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                RoutineItem updatedRoutineItem = new RoutineItem(title, details, time);
                adapter.updateItem(position, updatedRoutineItem);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Time picker
     * @param routineTimeEditText
     */
    private void showCustomTimePickerDialog(EditText routineTimeEditText) {
        if (getActivity() == null) {
            return; // Ensure we are in a valid context
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_time_picker, null);
        builder.setView(dialogView);

        Spinner hourSpinner = dialogView.findViewById(R.id.hourSpinner);
        Spinner minuteSpinner = dialogView.findViewById(R.id.minuteSpinner);
        Spinner ampmSpinner = dialogView.findViewById(R.id.ampmSpinner);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

        ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getHourValues());
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);

        ArrayAdapter<String> minuteAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getMinuteValues());
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(minuteAdapter);

        ArrayAdapter<String> ampmAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getAmPmValues());
        ampmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ampmSpinner.setAdapter(ampmAdapter);

        AlertDialog dialog = builder.create();

        confirmButton.setOnClickListener(v -> {
            String selectedHour = hourSpinner.getSelectedItem().toString();
            String selectedMinute = minuteSpinner.getSelectedItem().toString();
            String selectedAmPm = ampmSpinner.getSelectedItem().toString();

            String formattedTime = String.format("%s:%s %s", selectedHour, selectedMinute, selectedAmPm);
            routineTimeEditText.setText(formattedTime);
            dialog.dismiss();
        });

        dialog.show();
    }

    private List<String> getHourValues() {
        List<String> hours = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            hours.add(String.format("%02d", i));
        }
        return hours;
    }

    private List<String> getMinuteValues() {
        List<String> minutes = new ArrayList<>();

        minutes.add(String.format("%02d", 15));
        minutes.add(String.format("%02d", 30));
        minutes.add(String.format("%02d", 45));
        minutes.add(String.format("%02d", 00));
        return minutes;
    }

    private List<String> getAmPmValues() {
        List<String> ampm = new ArrayList<>();
        ampm.add("AM");
        ampm.add("PM");
        return ampm;
    }

    private void addRoutine(RoutineItem routineItem) {
        routineItems.add(routineItem);
        adapter.notifyItemInserted(routineItems.size() - 1);
    }
}