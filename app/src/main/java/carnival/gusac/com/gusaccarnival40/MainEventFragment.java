package carnival.gusac.com.gusaccarnival40;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import carnival.gusac.com.gusaccarnival40.utils.MyEventAdapter;


public class MainEventFragment extends Fragment implements AdapterView.OnItemClickListener {

    Bundle args;
    String type;
    ListView mEventList;

    public MainEventFragment() {
        // Required empty public constructor
    }

    String title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_event, container, false);
        args = getArguments();
        type = args.getString("type");
        assert type != null;
        switch (type) {
            case "home":
                ((Welcome) getActivity()).setActionbarTitle("Events");
                title="Events";
                break;
            case "events":
                ((Welcome) getActivity()).setActionbarTitle("Events");
                title="Events";
                break;
            case "workshops":
                ((Welcome) getActivity()).setActionbarTitle("Workshops");
                title="Workshops";
                break;
            case "sponsors":
                ((Welcome) getActivity()).setActionbarTitle("Sponsors");
                title="Sponsors";
                break;
            case "pronite":
                ((Welcome) getActivity()).setActionbarTitle("Cultural Events");
                title="Cultural Events";
                break;
            case "litfest":
                ((Welcome) getActivity()).setActionbarTitle("Literary Festival");
                title="Literary Festival";
                break;
            case "filmfest":
                ((Welcome) getActivity()).setActionbarTitle("Film Festival");
                title="Film Festival";
                break;
            case "guestlc":
                ((Welcome) getActivity()).setActionbarTitle("Guest Lectures");
                title="Guest Lectures";
                break;

        }
        mEventList = (ListView) rootView.findViewById(R.id.event_main_list);
        mEventList.setAdapter(new MyEventAdapter(getActivity(), type));

        if (type.equals("workshops")||type.equals("events") || type.equals("filmfest") || type.equals("pronite") || type.equals("litfest") || type.equals("guestlc"))
        mEventList.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Welcome)getActivity()).setActionbarTitle(title);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent=new Intent(getActivity(),EventDisplay.class);
        intent.putExtra("tag", type);
        EventInfo s = (EventInfo) parent.getAdapter().getItem(position);
        Log.d("Item clicked: ", s.title);
        intent.putExtra("image", s.image);
        intent.putExtra("title", s.title);
        intent.putExtra("desc", s.description);
        intent.putExtra("eventid",position);
        startActivity(intent);
    }
}
