package com.example.objectifsport.model.goals;

import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.Sport;
import com.example.objectifsport.model.activities.Activity;

import org.threeten.bp.Duration;

import java.util.Date;
import java.util.UUID;

public class Goal {

    private Date creationDate, deadlineDate;
    private final UUID sportId;
    private final String description;
    private int authorizedGoal;
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

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
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

    public void verify(){
        if (hasDeadlineDate() && deadlineDate.before(new Date())) {
            System.out.println("YEAH");
            switch (authorizedGoal) {
                case 1 :
                    if (getTimeProgress() >= duration.toMillis())
                        achieved = true;
                    break;
                case 2 :
                    if (getDistanceProgress() >= distance)
                        achieved = true;
                    break;
                default :
                    if (getTimeProgress() >= duration.toMillis() && getDistanceProgress() >= distance)
                        achieved = true;
                    break;
            }
        }
    }
}
