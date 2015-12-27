package carnival.gusac.com.gusaccarnival40;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import carnival.gusac.com.gusaccarnival40.utils.DatabaseHandler;
import carnival.gusac.com.gusaccarnival40.utils.MyFeedAdapter;


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
