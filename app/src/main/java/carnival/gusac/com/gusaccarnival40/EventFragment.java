package carnival.gusac.com.gusaccarnival40;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Messi10 on 31-Jan-15.
 */

/*   This is the ViewPager Fragment.
This fragment displays the events' titles and descriptions using a ListView.
The OnItemClickListener for the listview is used to start another activity to display
all the details of the event.
 */

public class EventFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_POSITION = "position";

    String tag;
    ListView eventList;

    private int position;

    public static EventFragment newInstance(int position) {
        EventFragment ef = new EventFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        ef.setArguments(b);
        return ef;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        ViewCompat.setElevation(rootView, 50);
        eventList = (ListView) rootView.findViewById(R.id.event_list);

        switch (position) {
            case 0:
                tag = "event_technical";
                //Setting the custom adapter for the listview
                eventList.setAdapter(new MyEventAdapter(getActivity(), "event_technical"));
                //Setting the item click listener
                eventList.setOnItemClickListener(this);
                break;
            case 1:
                tag = "event_nontech";
                //Setting the custom adapter for the listview
                eventList.setAdapter(new MyEventAdapter(getActivity(), "event_nontech"));
                //Setting the item click listener
                eventList.setOnItemClickListener(this);
                break;
        }
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Start another activity.Put the tag and the position of the item as extras.
        Intent event_intent = new Intent(getActivity(), EventDisplay.class);
        event_intent.putExtra("tag", tag);
        event_intent.putExtra("eventid", position);
        startActivity(event_intent);
    }


}
