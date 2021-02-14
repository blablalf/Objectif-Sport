package com.example.objectifsport.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.objectifsport.model.activities.Activity;
import com.example.objectifsport.model.goals.Goal;

public class Sport {

    private ArrayList<Goal> goals;
    private ArrayList<Activity> activities;
    private String name;

    public Sport(String name) {
        this.name = name;
        this.activities = new ArrayList<Activity>();
        this.goals = new ArrayList<Goal>();
    }

    public ArrayList<Goal> getGoals() {
        return goals;
    }

    public void setGoals(ArrayList<Goal> goals) {
        this.goals = goals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public void addActivity(Activity activity){
        this.activities.add(activity);
    }

    public void removeActivity(Activity activity){
        this.activities.remove(activity);
    }

    public ArrayList<Goal> getAchievedGoals() {
        ArrayList<Goal> achievedGoals = new ArrayList();
        for (Goal goal : goals) {
            if (goal.isAchieved()) achievedGoals.add(goal);
        }

        return achievedGoals;
    }

}
