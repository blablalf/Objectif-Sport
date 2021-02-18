package com.example.objectifsport.Services.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.objectifsport.model.activities.Activity;

import java.util.List;

@Dao
public interface ActivityDao {

    @Query("SELECT * FROM Activity WHERE sportId = :sportId")
    LiveData<List<Activity>> getSportActivities(long sportId);

    @Query("SELECT * FROM Activity")
    LiveData<List<Activity>> getActivities();

    @Insert
    void insertActivity(Activity activity);

    @Delete
    void deleteActivity(Activity activity);

    @Update
    void updateActivity(Activity activity);

    @Query("DELETE FROM Activity WHERE sportId = :sportId")
    int deleteSportActivies(long sportId);

    @Query("SELECT * FROM Activity WHERE id = :activityId")
    LiveData<Activity> getActivity(long activityId);
}
