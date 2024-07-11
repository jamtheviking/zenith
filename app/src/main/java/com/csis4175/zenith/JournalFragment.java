package com.csis4175.zenith;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JournalFragment extends Fragment {

    private RecyclerView recyclerView;
    private JournalAdapter journalAdapter;
    private List<JournalEntry> journalEntries;
    private ImageButton lastSelectedButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewJournal);
        journalEntries = new ArrayList<>();
        journalAdapter = new JournalAdapter(journalEntries);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(journalAdapter);

        // Initialize FloatingActionButton
        FloatingActionButton fabAddEntry = view.findViewById(R.id.fabAddEntry);
        fabAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEntryDialog();
            }
        });

        return view;
    }

    private void showAddEntryDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_entry_journal, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        // Initialize dialog views
        final ImageButton buttonMoodAmazing = dialogView.findViewById(R.id.buttonMoodAmazing);
        final ImageButton buttonMoodGreat = dialogView.findViewById(R.id.buttonMoodGreat);
        final ImageButton buttonMoodGood = dialogView.findViewById(R.id.buttonMoodGood);
        final ImageButton buttonMoodBad = dialogView.findViewById(R.id.buttonMoodBad);
        final ImageButton buttonMoodAwful = dialogView.findViewById(R.id.buttonMoodAwful);
        final EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        final Button buttonSaveEntry = dialogView.findViewById(R.id.buttonSaveEntry);

        final String[] selectedMood = new String[1];

        View.OnClickListener moodClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the previous selection
                if (lastSelectedButton != null) {
                    lastSelectedButton.setSelected(false);
                }
                // Set the new selection
                v.setSelected(true);
                lastSelectedButton = (ImageButton) v;

                switch (v.getId()) {
                    case R.id.buttonMoodAmazing:
                        selectedMood[0] = "Amazing";
                        break;
                    case R.id.buttonMoodGreat:
                        selectedMood[0] = "Great";
                        break;
                    case R.id.buttonMoodGood:
                        selectedMood[0] = "Good";
                        break;
                    case R.id.buttonMoodBad:
                        selectedMood[0] = "Bad";
                        break;
                    case R.id.buttonMoodAwful:
                        selectedMood[0] = "Awful";
                        break;
                }
                Toast.makeText(getContext(), "Mood selected: " + selectedMood[0], Toast.LENGTH_SHORT).show();
            }
        };

        buttonMoodAmazing.setOnClickListener(moodClickListener);
        buttonMoodGreat.setOnClickListener(moodClickListener);
        buttonMoodGood.setOnClickListener(moodClickListener);
        buttonMoodBad.setOnClickListener(moodClickListener);
        buttonMoodAwful.setOnClickListener(moodClickListener);

        //Create dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        //Save journal
        buttonSaveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = editTextDescription.getText().toString().trim();
                if (selectedMood[0] == null || selectedMood[0].isEmpty() || description.isEmpty()) {
                    Toast.makeText(getContext(), "Please select a mood and enter a description.", Toast.LENGTH_SHORT).show();
                    return;
                }


                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                journalEntries.add(new JournalEntry(selectedMood[0], description, timestamp));
                journalAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
    }
}