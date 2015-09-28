package carnival.gusac.com.gusaccarnival40;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.util.HashMap;

import carnival.gusac.com.gusaccarnival40.utils.ConnectionDetector;
import carnival.gusac.com.gusaccarnival40.utils.DatabaseHandler;
import carnival.gusac.com.gusaccarnival40.utils.MyFeedAdapter;
import carnival.gusac.com.gusaccarnival40.utils.PollingService;


public class FeedFragment extends android.support.v4.app.Fragment {

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_innerfeed, container, false);
        ((Welcome) getActivity()).setActionbarTitle("Feed");

        ListView listView= (ListView) v.findViewById(R.id.feed_list);
        DatabaseHandler db=new DatabaseHandler(getActivity());
        Bundle feedData=db.getFeedUpdates();
        if(feedData!=null){
            Toast.makeText(getActivity(),"Loading Data",Toast.LENGTH_LONG).show();
            listView.setAdapter(new MyFeedAdapter(getActivity(), feedData));
        }else{
            Toast.makeText(getActivity(),"There are no updates.",Toast.LENGTH_LONG).show();
        }

        return v;
    }

}
