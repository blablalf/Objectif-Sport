package com.example.objectifsport.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.activities.Activity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailedActivity extends AppCompatActivity {

    private Activity activity;
    private long startTime, timeToSave;
    private boolean running, started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        activity = DataManager.getActivities().get(getIntent().getIntExtra("position", 0));

        TextView activityDescription = findViewById(R.id.activity_description);
        TextView sportName = findViewById((R.id.sport_name));
        TextView creationDate = findViewById(R.id.creation_date);

        // ne pas oublier de rendre le layout temps et distance gone dans le xml

        if (activity.getSport().getAuthorizedGoals() == 1) // Only time
            setTimeLayout();
        else if (activity.getSport().getAuthorizedGoals() == 2) // Only distance
            setDistanceLayout();
        else {
            setTimeLayout();
            setDistanceLayout();
        }

        activityDescription.setText(activity.getActivityDescription());
        sportName.setText(activity.getSport().getName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        creationDate.setText(formatter.format(activity.getCreationDate()));
    }

    private void setDistanceLayout() {
    }

    private void setTimeLayout() {
        RelativeLayout timePart = findViewById(R.id.time_part);
        Chronometer chronometer = findViewById(R.id.chronometer);
        Button start = findViewById(R.id.start_pause);
        Button reset = findViewById(R.id.reset);
        TextView savedTime = findViewById(R.id.saved_time);

        timePart.setVisibility(View.VISIBLE);

        timeToSave = activity.getActivityTime();
        running = false;
        if (timeToSave == 0) started = false;
        else {
            started = true;
            start.setText(getResources().getString(R.string.resume));
        }

        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
        savedTime.setText((timeToSave == 0) ? getResources().getString(R.string.no_time_recorded) : activity.getFormattedActivityTime());

        start.setOnClickListener(v -> {
            if (running) { // chronometer was running
                chronometer.stop();
                running = false;
                timeToSave += System.currentTimeMillis() - startTime;
                chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
                start.setText(getResources().getString(R.string.resume));
                activity.setActivityTime(timeToSave);
                savedTime.setText(activity.getFormattedActivityTime());
                DataManager.save();
            } else { // chronometer was paused / not started
                if (!started) started = true;
                startTime = System.currentTimeMillis();
                chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
                chronometer.start();
                running = true;
                start.setText(getResources().getString(R.string.stop));
            }
        });

        reset.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.reset_time))
                .setMessage(getResources().getString(R.string.reset_time_message))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Timestamp tempTimestamp = new Timestamp(0);
                    timeToSave = 0;
                    activity.setActivityTime(0);
                    DataManager.save();
                    savedTime.setText(getResources().getString(R.string.no_time_recorded));
                    started = false;
                    running = false;
                    start.setText(getResources().getString(R.string.start));
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());

    }

    public void backToMyActivities(View view) {
        onBackPressed();
    }
}