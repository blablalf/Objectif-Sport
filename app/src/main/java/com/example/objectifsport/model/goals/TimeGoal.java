package com.example.objectifsport.model.goals;

import java.util.Calendar;

public class TimeGoal extends Goal {

    private long time, completedTime;

    public TimeGoal(String type, Calendar startDate, Calendar endDate, long time) {
        super(type, startDate, endDate);
        this.time = time;
    }
}
