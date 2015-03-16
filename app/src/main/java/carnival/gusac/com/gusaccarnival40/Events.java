package carnival.gusac.com.gusaccarnival40;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by Messi10 on 03-Jan-15.
 */

/*
This fragment inflates the Sliding Tabs and their respective ViewPagers.
The ViewPagers require another fragment to display the data.
Therefore, we have to use another fragment(EventFragment) which returns the data to be displayed.
 */
public class Events extends Fragment {

    PagerSlidingTabStrip mTabs;
    ViewPager mPager;

    //Fragment empty constructor
    public Events() {

    }

    //Set up the sliding tabs and the viewpagers
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_events, container, false);

        ((Welcome) getActivity()).setActionbarTitle("Events");
        mPager = (ViewPager) v.findViewById(R.id.dashboard_pager);
        mTabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);

        //Set the adapter for the pager
        mPager.setAdapter(new PagerAdapter(getChildFragmentManager()));

        //set the viewpagers for the tabs
        mTabs.setViewPager(mPager);

        //Set the margin between the consecutive ViewPagers
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mPager.setPageMargin(pageMargin);


        mTabs.setBackgroundColor(getResources().getColor(R.color.myPrimaryColor));
        mPager.setBackgroundColor(getResources().getColor(R.color.myPrimaryColor));
        return v;
    }


    class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Technical", "Non-Technical"};

        PagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return EventFragment.newInstance(position);
        }
    }


}
