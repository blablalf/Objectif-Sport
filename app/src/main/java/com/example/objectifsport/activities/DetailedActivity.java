package com.example.objectifsport.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.activities.Activity;

import org.osmdroid.views.MapView;

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

        // System.out.println("WTF"+activity.getSport().getAuthorizedGoals()); PROBLEM

        // ne pas oublier de rendre le layout temps et distance gone dans le xml

        /*if (activity.getSport().getAuthorizedGoals() == 1) // Only time PROBLEM
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
        creationDate.setText(formatter.format(activity.getCreationDate()));*/
    }

    private void setDistanceLayout() {
        // checkOSMPermission
        checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, 0);

        findViewById(R.id.distance_part).setVisibility(View.VISIBLE); // set the view visible
        MapView myOpenMapView = findViewById(R.id.mapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapView.setClickable(true);
        myOpenMapView.getController().setZoom(15);
    }

    private void setTimeLayout() {
        findViewById(R.id.time_part).setVisibility(View.VISIBLE);
        Chronometer chronometer = findViewById(R.id.chronometer);
        Button start = findViewById(R.id.start_pause);
        Button reset = findViewById(R.id.reset);
        TextView savedTime = findViewById(R.id.saved_time);

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

    public void checkPermission(String permission, int requestCode){
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            this,
                            new String[] { permission },
                            requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (grantResults.length == 0
                || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                // Checking whether user granted the permission or not.
                case 0:
                    checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, 0);
                    break;
                case 1:
                    checkPermission(Manifest.permission.INTERNET, 1);
                    break;
                case 2:
                    checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 2);
                    break;
                case 3:
                    checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 3);
                    break;
            }
        } else {
            switch (requestCode) {
                case 0:
                    checkPermission(Manifest.permission.INTERNET, 1);
                    break;
                case 1:
                    checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 2);
                    break;
                case 2:
                    checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, 3);
                    break;
            }
        }
    }

    public void backToMyActivities(View view) {
        onBackPressed();
    }
}