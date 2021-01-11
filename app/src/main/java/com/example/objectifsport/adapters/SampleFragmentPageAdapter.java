package com.example.objectifsport.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.objectifsport.fragments.MySportsFragment;

public class SampleFragmentPageAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "My Sports", "Tab2", "Tab3" };
    private final int PAGE_COUNT = 1;

    public SampleFragmentPageAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int pagePosition) {
        return MySportsFragment.newInstance(pagePosition + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
