package com.example.objectifsport.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.objectifsport.fragments.MyActivitiesFragment;
import com.example.objectifsport.fragments.MyGoalsFragment;
import com.example.objectifsport.fragments.MySportsFragment;

public class MainFragmentPageAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "My Sports", "My Activities", "My Goals" };
    private final int PAGE_COUNT = 3;

    public MainFragmentPageAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int pagePosition) {
        switch (tabTitles[pagePosition]){
            case "My Activities" :
                return MyActivitiesFragment.newInstance();
            case "My Objectives" :
                return MyGoalsFragment.newInstance();
            default: return MySportsFragment.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
