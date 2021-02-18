package com.example.objectifsport.Services.repositories;

import androidx.lifecycle.LiveData;

import com.example.objectifsport.Services.database.ActivityDao;
import com.example.objectifsport.model.activities.Activity;

import java.util.List;

public class ActivitiesDataRepository {

    private final ActivityDao activityDao;

    public ActivitiesDataRepository(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    // --- GET ---

    public LiveData<List<Activity>> getActivities() {
        return activityDao.getActivities();
    }

    public LiveData<List<Activity>> getSportActivities(long sportId) {
        return activityDao.getSportActivities(sportId);
    }

    // --- CREATE ---

    public void createActivity(Activity activity) {
        activityDao.insertActivity(activity);
    }

    // --- DELETE ---
    public void deleteActivity(Activity activity) {
        activityDao.deleteActivity(activity);
    }

    // --- UPDATE ---
    public void updateActivity(Activity activity) {
        activityDao.updateActivity(activity);
    }

}