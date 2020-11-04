package model.goals;

import java.util.Calendar;

public abstract class Goal {

    private String type;

    private Calendar startDate, endDate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Goal(String type, Calendar startDate, Calendar endDate) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
