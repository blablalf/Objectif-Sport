package com.example.objectifsport.Services.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.objectifsport.model.Sport;
import com.example.objectifsport.model.activities.Activity;

@Database(entities = {Sport.class, Activity.class}, version = 1, exportSchema = false)
public abstract class ObjectifSportDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile ObjectifSportDatabase INSTANCE;

    // --- DAO ---
    public abstract ActivityDao activityDao();
    public abstract SportDao sportDao();

    // --- INSTANCE ---
    public static ObjectifSportDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ObjectifSportDatabase
                    .class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ObjectifSportDatabase.class, "MyDatabase.db")
                            //.addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // for test purpose only
    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 1);
                contentValues.put("username", "Philippe");
                contentValues.put("urlPicture", "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2");

                db.insert("User", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }

}
