package com.example.objectifsport.model.goals;

import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.Sport;

import org.threeten.bp.Duration;

import java.util.Calendar;
import java.util.UUID;

public class Goal {

    private Calendar creationDate, deadlineDate;
    private final UUID sportId;
    private final String description;
    private int authorizedGoal;
    private boolean achieved;

    // time part
    private Duration duration;

    // distance part
    private double distance;

    public Goal(Sport sport, String goalDescription, Calendar deadlineDate, Duration durationGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = Calendar.getInstance();
        this.deadlineDate = deadlineDate;
        achieved = false;
        duration = durationGoal;
        authorizedGoal = 1;
    }

    public Goal(Sport sport, String goalDescription, Calendar deadlineDate, double distanceGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = Calendar.getInstance();
        this.deadlineDate = deadlineDate;
        achieved = false;
        distance = distanceGoal;
        authorizedGoal = 2;
    }

    public Goal(Sport sport, String goalDescription, Calendar deadlineDate, Duration durationGoal, double distanceGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = Calendar.getInstance();
        this.deadlineDate = deadlineDate;
        achieved = false;
        duration = durationGoal;
        distance = distanceGoal;
        authorizedGoal = 0;
    }

    public Goal(Sport sport, String goalDescription, Duration durationGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = Calendar.getInstance();
        achieved = false;
        duration = durationGoal;
        authorizedGoal = 1;
    }

    public Goal(Sport sport, String goalDescription, double distanceGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = Calendar.getInstance();
        achieved = false;
        distance = distanceGoal;
        authorizedGoal = 2;
    }

    public Goal(Sport sport, String goalDescription, Duration durationGoal, double distanceGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = Calendar.getInstance();
        achieved = false;
        duration = durationGoal;
        distance = distanceGoal;
        authorizedGoal = 0;
    }

    public boolean hasDeadlineDate(){
        return deadlineDate != null;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public Calendar getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Calendar deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public int getAuthorizedGoal() {
        return authorizedGoal;
    }

    public void setAuthorizedGoal(int authorizedGoal) {
        this.authorizedGoal = authorizedGoal;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Sport getSport(){
        return DataManager.getSport(sportId);
    }
}
