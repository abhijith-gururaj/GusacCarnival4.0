package carnival.gusac.com.gusaccarnival40;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;


public class FeedFragment extends android.support.v4.app.Fragment {

    PagerSlidingTabStrip mTabs;
    ViewPager mPager;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        ((Welcome) getActivity()).setActionbarTitle("Feed");


        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            mPager = (ViewPager) v.findViewById(R.id.feed_pager);
            mTabs = (PagerSlidingTabStrip) v.findViewById(R.id.feed_tabs);

            mPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
            mTabs.setViewPager(mPager);
            final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                    .getDisplayMetrics());
            mPager.setPageMargin(pageMargin);
            mTabs.setBackgroundColor(getResources().getColor(R.color.myPrimaryColor));
            mPager.setBackgroundColor(getResources().getColor(R.color.myPrimaryColor));
        } else {
            Toast.makeText(getActivity(),"Unable to connect to the server.Please check your Internet Connection.",Toast.LENGTH_LONG).show();
        }


        return v;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Latest", "Archive"};

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
            return InnerFeedFragment.newInstance(position);
        }
    }


}
