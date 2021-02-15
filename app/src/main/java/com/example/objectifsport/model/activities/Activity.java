package com.example.objectifsport.model.activities;

import java.util.Calendar;

public class Activity {

    // General part
    private int type; // Could be time (1), distance (2) or all (0)
    private String sportName;

    private String activityDescription; // something like "skipping rope + pumps" for Boxing sport

    public Activity(String sportName, String activityDescription, int type) {
        this.type = type;
        this.sportName = sportName;
        this.activityDescription = activityDescription;
    }

    // Period part
    private Calendar startDate;
    private Calendar endDate;


    // Time part
    //

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

    public int getType() {
        return type;
    }

    public String getSportName() {
        return sportName;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

}
