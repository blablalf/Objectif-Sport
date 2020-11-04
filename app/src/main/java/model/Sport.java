package model;

import java.util.ArrayList;

import model.activities.Activity;
import model.goals.Goal;

public class Sport {

    private ArrayList<Goal> goals;
    private ArrayList<Activity> activities;
    private String name;

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

    public Sport(String name) {
        this.name = name;
    }

}
