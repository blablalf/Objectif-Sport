package com.example.objectifsport.model.goals;

import com.example.objectifsport.model.Sport;

import java.util.Calendar;

public class DistanceGoal extends Goal {

    private double distance, completedDistance;

    public DistanceGoal(Sport sport, Calendar startDate, Calendar endDate, double distance) {
        super(sport, startDate, endDate);
        this.distance = distance;
    }
}
