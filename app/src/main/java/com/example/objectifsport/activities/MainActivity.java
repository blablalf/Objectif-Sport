package com.example.objectifsport.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.adapters.MainFragmentPageAdapter;
import com.example.objectifsport.model.goals.Goal;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private MainFragmentPageAdapter mainFragmentPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataManager.load(this);

        // FOR TESTING PURPOSES ONLY
        //DataManager.generateFakeSports();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        mainFragmentPageAdapter = new MainFragmentPageAdapter(getSupportFragmentManager(),
                R.layout.activity_main);
        viewPager.setAdapter(mainFragmentPageAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public MainFragmentPageAdapter getMainFragmentPageAdapter() {
        return mainFragmentPageAdapter;
    }


    // Useful when activity is updated into DetailedActivity
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        for (Goal goal : DataManager.getGoals()) {
            goal.verify();
        }

        if (getMainFragmentPageAdapter().getMyActivitiesFragment() != null) {
            getMainFragmentPageAdapter()
                    .getMyActivitiesFragment()
                    .getActivityAdapter()
                    .notifyDataSetChanged();

            getMainFragmentPageAdapter()
                    .getMyGoalsFragment()
                    .getGoalAdapter()
                    .notifyDataSetChanged();
        }

    }
}