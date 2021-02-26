package beetlejuice.objectifsport.model.goals;

import beetlejuice.objectifsport.Services.DataManager;
import beetlejuice.objectifsport.model.Sport;
import beetlejuice.objectifsport.model.activities.Activity;

import org.threeten.bp.Duration;

import java.util.Date;
import java.util.UUID;

/**
 * The type Goal.
 */
public class Goal {

    private final Date creationDate; // Goal creation date
    private Date deadlineDate; // If user decide to use a deadline date
    private final UUID sportId; // To restore the good instances from shared instance we need to store id rather than a sport instance
    private final String description; // Goal title
    private final int authorizedGoal; // Type of goal 1 for time goal, 2 for distance and 0 for both
    private boolean achieved; // Is the goal actually achieved

    // time part
    private Duration duration;

    // distance part
    private double distance;

    /**
     * Instantiates a new Goal.
     *
     * @param sport           the sport
     * @param goalDescription the goal description
     * @param deadlineDate    the deadline date
     * @param durationGoal    the duration goal
     */
    public Goal(Sport sport, String goalDescription, Date deadlineDate, Duration durationGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = new Date();
        this.deadlineDate = deadlineDate;
        achieved = false;
        duration = durationGoal;
        authorizedGoal = 1;
    }

    /**
     * Instantiates a new Goal.
     *
     * @param sport           the sport
     * @param goalDescription the goal description
     * @param deadlineDate    the deadline date
     * @param distanceGoal    the distance goal
     */
    public Goal(Sport sport, String goalDescription, Date deadlineDate, double distanceGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = new Date();
        this.deadlineDate = deadlineDate;
        achieved = false;
        distance = distanceGoal;
        authorizedGoal = 2;
    }

    /**
     * Instantiates a new Goal.
     *
     * @param sport           the sport
     * @param goalDescription the goal description
     * @param deadlineDate    the deadline date
     * @param durationGoal    the duration goal
     * @param distanceGoal    the distance goal
     */
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

    /**
     * Instantiates a new Goal.
     *
     * @param sport           the sport
     * @param goalDescription the goal description
     * @param durationGoal    the duration goal
     */
    public Goal(Sport sport, String goalDescription, Duration durationGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = new Date();
        achieved = false;
        duration = durationGoal;
        authorizedGoal = 1;
    }

    /**
     * Instantiates a new Goal.
     *
     * @param sport           the sport
     * @param goalDescription the goal description
     * @param distanceGoal    the distance goal
     */
    public Goal(Sport sport, String goalDescription, double distanceGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = new Date();
        achieved = false;
        distance = distanceGoal;
        authorizedGoal = 2;
    }

    /**
     * Instantiates a new Goal.
     *
     * @param sport           the sport
     * @param goalDescription the goal description
     * @param durationGoal    the duration goal
     * @param distanceGoal    the distance goal
     */
    public Goal(Sport sport, String goalDescription, Duration durationGoal, double distanceGoal) {
        sportId = sport.getId();
        description = goalDescription;
        creationDate = new Date();
        achieved = false;
        duration = durationGoal;
        distance = distanceGoal;
        authorizedGoal = 0;
    }

    /**
     * Has deadline date boolean.
     *
     * @return the boolean
     */
    public boolean hasDeadlineDate(){
        return deadlineDate != null;
    }

    /**
     * Is achieved boolean.
     *
     * @return the boolean
     */
    public boolean isAchieved() {
        return achieved;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Gets deadline date.
     *
     * @return the deadline date
     */
    public Date getDeadlineDate() {
        return deadlineDate;
    }

    /**
     * Gets authorized goal.
     *
     * @return the authorized goal
     */
    public int getAuthorizedGoal() {
        return authorizedGoal;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Gets distance.
     *
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Get sport sport.
     *
     * @return the sport
     */
    public Sport getSport(){
        return DataManager.getSport(sportId);
    }

    /**
     * Gets time progress.
     *
     * @return the time progress
     */
    public long getTimeProgress() {
        long totalTimeProgress = 0;
        for (Activity activity : DataManager.getActivities())
            if (activity.getCreationDate().after(creationDate) && activity.getSport() == getSport())
                totalTimeProgress += activity.getActivityTime();
        return totalTimeProgress;
    }

    /**
     * Gets distance progress.
     *
     * @return the distance progress
     */
    public double getDistanceProgress() {
        double totalDistanceProgress = 0;
        for (Activity activity : DataManager.getActivities())
            if (activity.getCreationDate().after(creationDate) && activity.getSport() == getSport())
                totalDistanceProgress += activity.getCompletedDistance();
        return totalDistanceProgress;
    }

    /**
     * Verify boolean.
     *
     * @return the boolean
     */
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
