package com.example.objectifsport.model.goals;

import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.Sport;
import com.example.objectifsport.model.activities.Activity;

import org.threeten.bp.Duration;

import java.util.Date;
import java.util.UUID;

public class Goal {

    private final Date creationDate;
    private Date deadlineDate;
    private final UUID sportId;
    private final String description;
    private final int authorizedGoal;
    private boolean achieved;

    // time part
    private Duration duration;

    // distance part
    private double distance;

    public Goal(Sport sport, String goalDescription, Date deadlineDate, Duration durationGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = new Date();
        this.deadlineDate = deadlineDate;
        achieved = false;
        duration = durationGoal;
        authorizedGoal = 1;
    }

    public Goal(Sport sport, String goalDescription, Date deadlineDate, double distanceGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = new Date();
        this.deadlineDate = deadlineDate;
        achieved = false;
        distance = distanceGoal;
        authorizedGoal = 2;
    }

    public Goal(Sport sport, String goalDescription, Date deadlineDate, Duration durationGoal, double distanceGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = new Date();
        this.deadlineDate = deadlineDate;
        achieved = false;
        duration = durationGoal;
        distance = distanceGoal;
        authorizedGoal = 0;
    }

    public Goal(Sport sport, String goalDescription, Duration durationGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = new Date();
        achieved = false;
        duration = durationGoal;
        authorizedGoal = 1;
    }

    public Goal(Sport sport, String goalDescription, double distanceGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = new Date();
        achieved = false;
        distance = distanceGoal;
        authorizedGoal = 2;
    }

    public Goal(Sport sport, String goalDescription, Duration durationGoal, double distanceGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = new Date();
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

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public int getAuthorizedGoal() {
        return authorizedGoal;
    }

    public Duration getDuration() {
        return duration;
    }

    public double getDistance() {
        return distance;
    }

    public Sport getSport(){
        return DataManager.getSport(sportId);
    }

    public long getTimeProgress() {
        long totalTimeProgress = 0;
        for (Activity activity : DataManager.getActivities())
            if (activity.getCreationDate().after(creationDate) && activity.getSport() == getSport())
                totalTimeProgress += activity.getActivityTime();
        return totalTimeProgress;
    }

    public double getDistanceProgress() {
        double totalDistanceProgress = 0;
        for (Activity activity : DataManager.getActivities())
            if (activity.getCreationDate().after(creationDate) && activity.getSport() == getSport())
                totalDistanceProgress += activity.getCompletedDistance();
        return totalDistanceProgress;
    }

    public boolean verify(){
        if (((hasDeadlineDate() && deadlineDate.after(new Date())) || !hasDeadlineDate()) && !isAchieved()) {
            switch (authorizedGoal) {
                case 1 :
                    if (getTimeProgress() >= duration.toMillis()) {
                        achieved = true;
                        DataManager.save();
                        return true;
                    }
                    break;
                case 2 :
                    if (getDistanceProgress() >= distance) {
                        achieved = true;
                        DataManager.save();
                        return true;
                    }
                    break;
                default :
                    if (getTimeProgress() >= duration.toMillis() && getDistanceProgress() >= distance) {
                        achieved = true;
                        DataManager.save();
                        return true;
                    }
            }
        }
        return false;
    }
}
