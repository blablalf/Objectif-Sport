package com.example.objectifsport.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.adapters.MainFragmentPageAdapter;
import com.example.objectifsport.model.goals.Goal;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";
    private static final CharSequence CHANNEL_NAME = "CHANNEL_NAME";

    private MainFragmentPageAdapter mainFragmentPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataManager.load(this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        mainFragmentPageAdapter = new MainFragmentPageAdapter(getSupportFragmentManager(),
                R.layout.activity_main);
        viewPager.setAdapter(mainFragmentPageAdapter);

        // If we com from a goal achieved notification
        if (getIntent().getBooleanExtra("notification", false))
            viewPager.setCurrentItem(2);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        createNotificationChannel();

    }

    public MainFragmentPageAdapter getMainFragmentPageAdapter() {
        return mainFragmentPageAdapter;
    }


    // Useful when activity is updated into DetailedActivity
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        for (Goal goal : DataManager.getGoals()) {
            if (goal.verify()) {
                // notify
                Intent backIntent = new Intent(this, MainActivity.class);
                backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                backIntent.putExtra("notification", true);

                Intent intent = new Intent(MainActivity.this, DetailedGoalActivity.class);
                intent.putExtra("position", DataManager.getGoals().indexOf(goal));

                PendingIntent pendingIntent = PendingIntent.getActivities(MainActivity.this,
                        0, new Intent[] {backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);

                String notificationMessage = getResources()
                        .getString(R.string.congratulation) + " " + goal.getDescription() + " " +
                        getResources().getString(R.string.goal_achieved_notification_msg);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                        .setContentTitle(getResources().getString(R.string.goal_achieved_notification_title))
                        .setContentText(notificationMessage)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

                // notificationId is a unique int for each notification that you must define
                notificationManagerCompat.notify(0, builder.build());
            }
        }

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}