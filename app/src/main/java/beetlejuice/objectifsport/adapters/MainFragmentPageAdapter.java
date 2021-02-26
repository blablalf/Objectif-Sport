package beetlejuice.objectifsport.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import beetlejuice.objectifsport.fragments.MyActivitiesFragment;
import beetlejuice.objectifsport.fragments.MyGoalsFragment;
import beetlejuice.objectifsport.fragments.MySportsFragment;

/**
 * The type Main fragment page adapter.
 */
public class MainFragmentPageAdapter extends FragmentPagerAdapter {

    private final String[] tabTitles = new String[] { "My Sports", "My Activities", "My Goals" };
    private MyActivitiesFragment myActivitiesFragment;
    private MyGoalsFragment myGoalsFragment;
    private MySportsFragment mySportsFragment;

    /**
     * Instantiates a new Main fragment page adapter.
     *
     * @param fm       the fm
     * @param behavior the behavior
     */
    public MainFragmentPageAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public Fragment getItem(int pagePosition) {
        switch (tabTitles[pagePosition]){
            case "My Activities" :
                myActivitiesFragment = MyActivitiesFragment.newInstance();
                return myActivitiesFragment;
            case "My Goals" :
                myGoalsFragment = MyGoalsFragment.newInstance();
                return myGoalsFragment;
            default: mySportsFragment = MySportsFragment.newInstance();
                return mySportsFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    /**
     * Gets my activities fragment.
     *
     * @return the my activities fragment
     */
    public MyActivitiesFragment getMyActivitiesFragment() {
        return myActivitiesFragment;
    }


    /**
     * Gets my goals fragment.
     *
     * @return the my goals fragment
     */
    public beetlejuice.objectifsport.fragments.MyGoalsFragment getMyGoalsFragment() {
        return myGoalsFragment;
    }

    /**
     * Gets my sports fragment.
     *
     * @return the my sports fragment
     */
    public beetlejuice.objectifsport.fragments.MySportsFragment getMySportsFragment() {
        return mySportsFragment;
    }

}
