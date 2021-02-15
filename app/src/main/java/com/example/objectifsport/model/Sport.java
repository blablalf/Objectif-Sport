package com.example.objectifsport.model;

import java.util.ArrayList;

import com.example.objectifsport.model.activities.Activity;
import com.example.objectifsport.model.goals.Goal;

public class Sport {

    private ArrayList<Goal> goals;
    private ArrayList<Activity> activities;
    private String name;
    private int authorizedGoals; // 0 for all goals | 1 for time goal | 2 for distance goal

    public Sport(String name) {
        this.name = name;
        this.activities = new ArrayList<>();
        this.goals = new ArrayList<>();
        authorizedGoals = 0;
    }

    public Sport(String name, int authorizedGoals) {
        this.name = name;
        this.activities = new ArrayList<>();
        this.goals = new ArrayList<>();
        this.authorizedGoals = authorizedGoals;
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
