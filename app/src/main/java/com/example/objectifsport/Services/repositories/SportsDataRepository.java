package com.example.objectifsport.Services.repositories;

import androidx.lifecycle.LiveData;

import com.example.objectifsport.Services.database.SportDao;
import com.example.objectifsport.model.Sport;

import java.util.List;

public class SportsDataRepository {

    private final SportDao sportDao;

    public SportsDataRepository(SportDao sportDao) {
        this.sportDao = sportDao;
    }

    // --- GET ---

    public LiveData<List<Sport>> getSports() {
        return sportDao.getSports();
    }

    // --- CREATE ---

    public void createSport(Sport sport) {
        sportDao.insertSport(sport);
    }

    // --- DELETE ---
    public void deleteSport(Sport sport) {
        sportDao.deleteSport(sport);
    }

    // --- UPDATE ---
    public void updateSport(Sport sport) {
        sportDao.updateSport(sport);
    }

}
