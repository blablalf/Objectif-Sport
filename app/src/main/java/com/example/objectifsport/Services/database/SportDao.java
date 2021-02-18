package com.example.objectifsport.Services.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.objectifsport.model.Sport;

import java.util.List;

@Dao
public interface SportDao {

    @Query("SELECT * FROM Sport WHERE id = :sportId")
    LiveData<Sport> getSport(long sportId);

    @Query("SELECT * FROM Sport")
    LiveData<List<Sport>> getSports();

    @Insert
    void insertSport(Sport sport);

    @Update
    int updateSport(Sport sport);

    @Delete
    int deleteSport(Sport sport);

}
