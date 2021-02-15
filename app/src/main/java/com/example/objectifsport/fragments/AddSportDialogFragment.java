package com.example.objectifsport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.Sport;

public class AddSportDialogFragment extends DialogFragment {

    private EditText sportName;
    private CheckBox timeGoal;
    private CheckBox distanceGoal;
    private Button cancel;
    private Button addSport;
    private static DataManager dataManager;

    public AddSportDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddSportDialogFragment newInstance(String title) {
        AddSportDialogFragment frag = new AddSportDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        dataManager = DataManager.getInstance();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_sport_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sportName = view.findViewById(R.id.sport_name);
        timeGoal = view.findViewById(R.id.time_goal);
        distanceGoal = view.findViewById(R.id.distance_goal);
        cancel = view.findViewById(R.id.cancel);
        addSport = view.findViewById(R.id.add_sport);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        sportName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        timeGoal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            addSport.setEnabled(isChecked || distanceGoal.isChecked());
        });

        distanceGoal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            addSport.setEnabled(isChecked || timeGoal.isChecked());
        });

        addSport.setOnClickListener(v -> {
            // learn what type of goals are needed
            int authorizedGoals;
            if (timeGoal.isChecked() && !distanceGoal.isChecked()) authorizedGoals = 1;
            else if (distanceGoal.isChecked() && !timeGoal.isChecked())  authorizedGoals = 2;
            else authorizedGoals = 0;

            // create the new sport
            dataManager.getSports().add(new Sport(sportName.getText().toString(), authorizedGoals));
            dataManager.save();

            // close dialog
            dismiss();
        });

        cancel.setOnClickListener(v -> {
            // close dialog
            dismiss();
        });
    }

}
