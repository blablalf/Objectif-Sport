package model.goals;

import java.util.Calendar;

import model.goals.Goal;

public class TimeGoal extends Goal {

    private long time;

    public TimeGoal(String type, Calendar startDate, Calendar endDate, long time) {
        super(type, startDate, endDate);
        this.time = time;
    }
}
