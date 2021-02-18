package com.example.objectifsport.model.activities;

import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.Sport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Activity {

    // General part
    private final UUID sportId;
    private final Date creationDate;
    private final String activityDescription; // something like "skipping rope + pumps" for Boxing sport
    private boolean isAchieved;

    public Activity(Sport sport, String activityDescription) {
        sportId = sport.getId();
        this.activityDescription = activityDescription;
        creationDate = new Date();
        isAchieved = false;
        activityTime = 0;
    }

    // Time part
    private long activityTime;

    public String getFormattedActivityTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss.SSS", Locale.getDefault());
        return formatter.format(activityTime);
    }

    public long getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(long timestamp) {
        this.activityTime = timestamp;
    }

    // Distance part
    private double startCoordinateX, startCoordinateY, endCoordinateX, endCoordinateY, completedDistance;

    public double getStartCoordinateX() {
        return startCoordinateX;
    }

    public double getStartCoordinateY() {
        return startCoordinateY;
    }

    public double getEndCoordinateX() {
        return endCoordinateX;
    }

    public double getEndCoordinateY() {
        return endCoordinateY;
    }

    public void setStartCoordinate(double startCoordinateX, double startCoordinateY) {
        this.startCoordinateX = startCoordinateX;
        this.startCoordinateY = startCoordinateY;
    }

    public void setEndCoordinate(double endCoordinateX, double endCoordinateY) {
        this.endCoordinateX = endCoordinateX;
        this.endCoordinateY = endCoordinateY;
    }

    public double getCompletedDistance() {
        return completedDistance;
    }

    public void setCompletedDistance(double completedDistance) {
        this.completedDistance += completedDistance;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isAchieved() {
        return isAchieved;
    }

    public void setAchieved(boolean achieved) {
        isAchieved = achieved;
    }

    public Sport getSport() {
        return DataManager.getSport(sportId);
    }

}
