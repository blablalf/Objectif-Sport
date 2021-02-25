package com.example.objectifsport.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.fragments.ActivityMapFragment;
import com.example.objectifsport.model.activities.Activity;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class DetailedActivityActivity extends AppCompatActivity implements PermissionsListener {

    private Activity activity;

    // distance part
    private PermissionsManager permissionsManager;
    private ActivityMapFragment activityMapFragment;

    // time part views
    private Button resetTimeButton;
    private Button startTimeButton;
    private long startTime, timeToSave;
    private boolean timeRunning, timeStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_activity);

        activity = DataManager.getActivities().get(getIntent().getIntExtra("position", 0));

        TextView activityDescription = findViewById(R.id.activity_description);
        TextView sportName = findViewById((R.id.sport_name));
        TextView creationDate = findViewById(R.id.creation_date);

        if (activity.getSport().getAuthorizedGoals() == 1) // Only time
            setTimeLayout();
        else if (activity.getSport().getAuthorizedGoals() == 2) // Only distance
            setDistanceLayout();
        else { // all
            setTimeLayout();
            setDistanceLayout();
        }

        Button completeUncompleteButton = findViewById(R.id.complete_uncomplete);
        completeUncompleteButton.setText((activity.isAchieved())?
                getResources().getString(R.string.activity_unfinished) :
                getResources().getString(R.string.activity_complete));

        if (resetTimeButton != null) {
            resetTimeButton.setEnabled(!activity.isAchieved());
            startTimeButton.setEnabled(!activity.isAchieved());
        }

        completeUncompleteButton.setOnClickListener(v -> {
            activity.setAchieved(!activity.isAchieved());
            DataManager.save();

            completeUncompleteButton.setText((activity.isAchieved())?
                    getResources().getString(R.string.activity_unfinished) :
                    getResources().getString(R.string.activity_complete));

            if (resetTimeButton != null) {
                resetTimeButton.setEnabled(!activity.isAchieved());
                startTimeButton.setEnabled(!activity.isAchieved());
            }

            if (activity.getSport().getAuthorizedGoals() != 1)
                activityMapFragment.enablingDisablingTrackingButtons();

        });

        activityDescription.setText(activity.getActivityDescription());
        sportName.setText(activity.getSport().getName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        creationDate.setText(formatter.format(activity.getCreationDate()));
    }

    private void setTimeLayout() {
        View timePart = findViewById(R.id.time_part);
        Chronometer chronometer = findViewById(R.id.chronometer);
        startTimeButton = findViewById(R.id.start_pause);
        resetTimeButton = findViewById(R.id.reset);
        TextView savedTime = findViewById(R.id.saved_time);

        timePart.setVisibility(View.VISIBLE);

        timeToSave = activity.getActivityTime();
        timeRunning = false;
        if (timeToSave == 0) timeStarted = false;
        else {
            timeStarted = true;
            startTimeButton.setText(getResources().getString(R.string.resume));
        }

        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
        savedTime.setText((timeToSave == 0) ? getResources().getString(R.string.no_time_recorded)
                : activity.getFormattedActivityTime());

        startTimeButton.setOnClickListener(v -> {
            if (timeRunning) { // chronometer was running
                chronometer.stop();
                timeRunning = false;
                timeToSave += System.currentTimeMillis() - startTime;
                chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
                startTimeButton.setText(getResources().getString(R.string.resume));
                activity.setActivityTime(timeToSave);
                savedTime.setText(activity.getFormattedActivityTime());
                DataManager.save();
            } else { // chronometer was paused / not started
                if (!timeStarted) timeStarted = true;
                startTime = System.currentTimeMillis();
                chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
                chronometer.start();
                timeRunning = true;
                startTimeButton.setText(getResources().getString(R.string.stop));
            }
        });

        resetTimeButton.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.reset_time))
                .setMessage(getResources().getString(R.string.reset_time_message))
                .setPositiveButton(R.string.reset, (dialog, which) -> {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    timeToSave = 0;
                    activity.setActivityTime(0);
                    DataManager.save();
                    savedTime.setText(getResources().getString(R.string.no_time_recorded));
                    startTimeButton.setText(getResources().getString(R.string.start));
                    timeStarted = false;
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
    }

    private void setDistanceLayout() {
        findViewById(R.id.distance_part).setVisibility(View.VISIBLE); // set the view visible
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        activityMapFragment = new ActivityMapFragment();
        ft.replace(R.id.distance_part, activityMapFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        if (!PermissionsManager.areLocationPermissionsGranted(this)) {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    public void backToMyActivities(View view) {
        onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Distance part
        if (activity.getSport().getAuthorizedGoals() != 1) activityMapFragment.getMapView().onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Distance part
        if (activity.getSport().getAuthorizedGoals() != 1) activityMapFragment.getMapView().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Distance part
        if (activity.getSport().getAuthorizedGoals() != 1) activityMapFragment.getMapView().onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Distance part
        if (activity.getSport().getAuthorizedGoals() != 1) activityMapFragment.getMapView().onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Distance part
        if (activity.getSport().getAuthorizedGoals() != 1)
            activityMapFragment.getMapView().onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Distance part
        if (activity.getSport().getAuthorizedGoals() != 1){
            activityMapFragment.preventLeak();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        // Distance part
        if (activity.getSport().getAuthorizedGoals() != 1) activityMapFragment.getMapView().onLowMemory();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        activityMapFragment.treatOnExplanationNeeded();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        activityMapFragment.treatOnPermissionResult(granted);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}