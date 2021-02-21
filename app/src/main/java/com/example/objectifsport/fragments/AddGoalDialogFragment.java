package com.example.objectifsport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class AddGoalDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{

    private EditText timeGoalInput;
    private EditText distanceGoalInput;
    private EditText deadlineGoalInput;
    CheckBox timeGoal;
    CheckBox distanceGoal;
    CheckBox deadline;

    public AddGoalDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddGoalDialogFragment newInstance() {
        return new AddGoalDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_goal_fragment, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        EditText goalDescription = view.findViewById(R.id.activity_description);
        Spinner sportSelect = view.findViewById(R.id.sport_select);
        Button cancel = view.findViewById(R.id.cancel);
        Button addGoal = view.findViewById(R.id.add_goal);
        timeGoal = view.findViewById(R.id.time_goal);
        distanceGoal = view.findViewById(R.id.distance_goal);
        deadline = view.findViewById(R.id.deadline);
        timeGoalInput = view.findViewById(R.id.time_goal_input);
        distanceGoalInput = view.findViewById(R.id.distance_goal_input);
        deadlineGoalInput = view.findViewById(R.id.deadline_goal_input);
        RelativeLayout timeContainer = view.findViewById(R.id.time_container);
        RelativeLayout distanceContainer = view.findViewById(R.id.distance_container);
        RelativeLayout deadlineContainer = view.findViewById(R.id.deadline_container);

        Objects.requireNonNull(getDialog()).setTitle("Add Activity");
        ArrayList<String> sportNames = new ArrayList<>();
        for (Sport sport : DataManager.getSports()) sportNames.add(sport.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, sportNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSelect.setAdapter(adapter);

        goalDescription.requestFocus(); // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        addGoal.setEnabled(false); // disabled by default

        sportSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                Sport selectedSport = DataManager.getSports().get(sportSelect.getSelectedItemPosition());
                if (selectedSport.getAuthorizedGoals() == 1) {
                    distanceContainer.setVisibility(View.GONE);
                    timeContainer.setVisibility(View.VISIBLE);
                    addGoal.setEnabled(timeGoal.isChecked());
                    timeGoal.setOnCheckedChangeListener((buttonView, isChecked) ->{
                        inputVisibiliytyManager();
                        addGoal.setEnabled(isChecked);
                    });
                    deadline.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        inputVisibiliytyManager();
                    });
                } else if (selectedSport.getAuthorizedGoals() == 2) {
                    timeContainer.setVisibility(View.GONE);
                    distanceContainer.setVisibility(View.VISIBLE);
                    addGoal.setEnabled(distanceGoal.isChecked());
                    distanceGoal.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        inputVisibiliytyManager();
                        addGoal.setEnabled(isChecked);
                    });
                    deadline.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        inputVisibiliytyManager();
                    });
                } else {
                    timeContainer.setVisibility(View.VISIBLE);
                    distanceContainer.setVisibility(View.VISIBLE);
                    timeGoal.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        inputVisibiliytyManager();
                        addGoal.setEnabled(isChecked || distanceGoal.isChecked());
                    });
                    distanceGoal.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        inputVisibiliytyManager();
                        addGoal.setEnabled(isChecked || timeGoal.isChecked());
                    });
                    deadline.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        inputVisibiliytyManager();
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });


        addGoal.setOnClickListener(v -> {
            // Add activity
            Activity activity = new Activity(DataManager.getSports().get(sportSelect.getSelectedItemPosition()),
                    goalDescription.getText().toString());
            DataManager.addActivity(activity);
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
        System.out.println("Selected_is_"+position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void inputVisibiliytyManager(){
        if (timeGoal.isChecked()) timeGoalInput.setVisibility(View.VISIBLE);
        else timeGoalInput.setVisibility(View.GONE);
        if (distanceGoal.isChecked()) distanceGoalInput.setVisibility(View.VISIBLE);
        else distanceGoalInput.setVisibility(View.GONE);
        if (deadline.isChecked()) deadlineGoalInput.setVisibility(View.VISIBLE);
        else deadlineGoalInput.setVisibility(View.GONE);
    }

}
