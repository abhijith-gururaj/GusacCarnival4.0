package carnival.gusac.com.gusaccarnival40;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class MainEventFragment extends Fragment implements AdapterView.OnItemClickListener {

    Bundle args;
    String type;
    ListView mEventList;

    public MainEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_event, container, false);
        args = getArguments();
        type = args.getString("type");
        switch (type) {
            case "workshops":
                ((Welcome) getActivity()).setActionbarTitle("Workshops");
                break;
            case "sponsors":
                ((Welcome) getActivity()).setActionbarTitle("Sponsors");
                break;
            case "celebs":
                ((Welcome) getActivity()).setActionbarTitle("Celebrities");
                break;
            case "startups":
                ((Welcome) getActivity()).setActionbarTitle("Startup Camps");
                break;
            case "pronite":
                ((Welcome) getActivity()).setActionbarTitle("Cultural Events");
                break;
            case "litfest":
                ((Welcome) getActivity()).setActionbarTitle("Literary Festival");
                break;
            case "expos":
                ((Welcome) getActivity()).setActionbarTitle("Expos");
                break;
            case "filmfest":
                ((Welcome) getActivity()).setActionbarTitle("Film Festival");
                break;
            case "guestlc":
                ((Welcome) getActivity()).setActionbarTitle("Guest Lectures");
                break;

        }
        mEventList = (ListView) rootView.findViewById(R.id.event_main_list);
        mEventList.setAdapter(new MyEventAdapter(getActivity(), type));

        if(type.equals("workshops")||type.equals("filmfest")||type.equals("pronite")||type.equals("litfest"))
        mEventList.setOnItemClickListener(this);

        return rootView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent=new Intent(getActivity(),EventDisplay.class);
        intent.putExtra("tag",type);
        intent.putExtra("eventid",position);
        startActivity(intent);
    }
}
