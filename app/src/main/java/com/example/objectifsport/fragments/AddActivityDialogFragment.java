package com.example.objectifsport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.Sport;
import com.example.objectifsport.model.activities.Activity;

import java.util.ArrayList;
import java.util.Objects;

public class AddActivityDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{

    private EditText activityDescription;
    private static DataManager dataManager;
    private int selectedSport;

    public AddActivityDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddActivityDialogFragment newInstance() {
        return new AddActivityDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (dataManager == null) dataManager = DataManager.getInstance();
        return inflater.inflate(R.layout.add_activity_fragment, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        selectedSport = 0; // default value

        activityDescription = view.findViewById(R.id.activity_description);
        Spinner sportSelect = view.findViewById(R.id.sport_select);
        Button cancel = view.findViewById(R.id.cancel);
        Button addActivity = view.findViewById(R.id.add_activity);

        Objects.requireNonNull(getDialog()).setTitle("Add Activity");
        ArrayList<String> sportNames = new ArrayList<>();
        for (Sport sport : dataManager.getSports()) sportNames.add(sport.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, sportNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSelect.setAdapter(adapter);

        activityDescription.requestFocus(); // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        addActivity.setOnClickListener(v -> {
            // Add activity
            Activity activity = new Activity(dataManager.getSports().get(selectedSport),
                    activityDescription.getText().toString());
            dataManager.addActivity(activity);
            //dataManager.save();

            // close dialog
            dismiss();
        });

        cancel.setOnClickListener(v -> {
            // close dialog
            dismiss();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSport = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
