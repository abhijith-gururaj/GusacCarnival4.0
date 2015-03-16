package carnival.gusac.com.gusaccarnival40;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AboutUs extends Fragment {

//Activity for displaying the about us fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_about_us, container, false);

        ((Welcome) getActivity()).setActionbarTitle("About Us");
        TextView tv = (TextView) rootView.findViewById(R.id.develop);
        tv.setSelected(true);
        return rootView;
    }


}
