package com.example.objectifsport.model.activities;

import com.example.objectifsport.model.Sport;

import java.util.Calendar;

public class Activity {

    // General part
    private Sport sport;

    private String activityDescription; // something like "skipping rope + pumps" for Boxing sport

    public Activity(Sport sport, String activityDescription) {
        this.sport = sport;
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

    public Sport getSport() {
        return sport;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

}
