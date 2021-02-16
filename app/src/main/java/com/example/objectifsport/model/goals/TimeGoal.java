package com.example.objectifsport.model.goals;

import com.example.objectifsport.model.Sport;

import java.util.Calendar;

public class TimeGoal extends Goal {

    private long time, completedTime;

    public TimeGoal(Sport sport, Calendar startDate, Calendar endDate, long time) {
        super(sport, startDate, endDate);
        this.time = time;
    }
}
