package com.example.objectifsport.database;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.objectifsport.Services.database.ObjectifSportDatabase;
import com.example.objectifsport.model.Sport;
import com.example.objectifsport.model.activities.Activity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DaoTest {

    // FOR DATA
    private ObjectifSportDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                ObjectifSportDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        database.close();
    }

    private static final Sport SPORT1 = new Sport("Boxing", 1);
    private static final Sport SPORT2 = new Sport("Running", 0);
    private static final Sport SPORT3 = new Sport("Hiking", 2);
    private static final Activity ACTIVITY1 = new Activity("Session with JLB");
    private static final Activity ACTIVITY2 = new Activity("Session at La Rochelle");
    private static final Activity ACTIVITY3 = new Activity("Going to be hard...");
    private static final Activity ACTIVITY4 = new Activity("Going to be easy...");

    @Test
    public void getActivitiesWhenNoSportInserted() throws InterruptedException {
        // TEST
        List<Activity> activities = LiveDataTestUtil.getValue(this.database.activityDao().getSportActivities(0));
        assertTrue(activities.isEmpty());
    }

    @Test
    public void insertAndGetSport() throws InterruptedException {
        // BEFORE : Adding demo sport
        database.sportDao().insertSport(SPORT1);

        // TEST
        List<Sport> sports = LiveDataTestUtil.getValue(database.sportDao().getSports());
        Sport sport = LiveDataTestUtil.getValue(database.sportDao().getSport(sports.get(0).getId()));
        assertEquals(sport.getId(), sports.get(0).getId());
    }

    @Test
    public void insertAndGetActivity() throws InterruptedException {
        // BEFORE : Adding demo sport
        database.sportDao().insertSport(SPORT1);

        // BEFORE : Adding demo activity
        ACTIVITY1.setSportId(LiveDataTestUtil.getValue(database.sportDao().getSports()).get(0).getId());
        database.activityDao().insertActivity(ACTIVITY1);

        // TEST
        List<Activity> activities = LiveDataTestUtil.getValue(database.activityDao().getActivities());
        Activity activity = LiveDataTestUtil.getValue(database.activityDao().getActivity(activities.get(0).getId()));
        assertEquals(activities.get(0).getId(), activity.getId());
    }

    @Test
    public void insertAndGetSports() throws InterruptedException {
        // BEFORE : Adding demo sport
        database.sportDao().insertSport(SPORT1);
        database.sportDao().insertSport(SPORT2);
        database.sportDao().insertSport(SPORT3);

        // TEST
        List<Sport> sports = LiveDataTestUtil.getValue(this.database.sportDao().getSports());
        assertEquals(3, sports.size());
    }

    @Test
    public void insertAndGetActivities() throws InterruptedException {
        // BEFORE : Adding demo sports & activities
        database.sportDao().insertSport(SPORT1);
        database.sportDao().insertSport(SPORT2);
        database.sportDao().insertSport(SPORT3);

        long sport1Id = LiveDataTestUtil.getValue(database.sportDao().getSports()).get(0).getId();
        long sport2Id = LiveDataTestUtil.getValue(database.sportDao().getSports()).get(0).getId();
        long sport3Id = LiveDataTestUtil.getValue(database.sportDao().getSports()).get(0).getId();

        ACTIVITY1.setSportId(sport1Id);
        ACTIVITY2.setSportId(sport2Id);
        ACTIVITY3.setSportId(sport3Id);
        ACTIVITY4.setSportId(sport3Id);

        database.activityDao().insertActivity(ACTIVITY1);
        database.activityDao().insertActivity(ACTIVITY2);
        database.activityDao().insertActivity(ACTIVITY3);
        database.activityDao().insertActivity(ACTIVITY4);

        // TEST
        List<Activity> activities = LiveDataTestUtil.getValue(database.activityDao().getActivities());
        assertEquals(4, activities.size());
    }

    @Test
    public void insertAndUpdateActivity() throws InterruptedException {
        // BEFORE : Adding demo sport & activity
        database.sportDao().insertSport(SPORT1);
        long sport1Id = LiveDataTestUtil.getValue(database.sportDao().getSports()).get(0).getId();
        ACTIVITY1.setSportId(sport1Id);
        database.activityDao().insertActivity(ACTIVITY1);
        Activity activityAdded = LiveDataTestUtil.getValue(database.activityDao().getActivity(sport1Id));
        activityAdded.setActivityTime(15);
        database.activityDao().updateActivity(activityAdded);

        //TEST
        List<Activity> activities = LiveDataTestUtil.getValue(this.database.activityDao().getSportActivities(sport1Id));
        assertEquals(15, activities.get(0).getActivityTime());
    }

    @Test
    public void insertAndDeleteActivity() throws InterruptedException {
        // BEFORE : Adding demo sport & activities
        database.sportDao().insertSport(SPORT3);
        long sport3Id = LiveDataTestUtil.getValue(database.sportDao().getSports()).get(0).getId();
        ACTIVITY3.setSportId(sport3Id);
        database.activityDao().insertActivity(ACTIVITY3);
        database.activityDao().deleteActivity(LiveDataTestUtil.getValue(database.activityDao().getActivities()).get(0));

        //TEST
        List<Activity> activities = LiveDataTestUtil.getValue(database.activityDao().getActivities());
        assertEquals(0, activities.size());
    }
}