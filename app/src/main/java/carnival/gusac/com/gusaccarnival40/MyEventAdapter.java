package carnival.gusac.com.gusaccarnival40;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Messi10 on 31-Jan-15.
 */

/*
This is a custom adapter for displaying the event's title, description and its image.
 */
public class MyEventAdapter extends BaseAdapter {

    ArrayList<EventInfo> list;
    Context context;
    String type;
    View layout;
    TextView tv;
    TextView desc;
    ImageView iv;
    TypedArray imgs;

/*
Based on the type parameter, we initialize the respective
titles, descriptions and the images and inflate them so that they can be displayed.
 */
    MyEventAdapter(Context context, String type) {
        this.type = type;
        String titles[] = null, desc[] = null;
        list = new ArrayList<EventInfo>();
        int[] drawables = null;
        this.context = context;

        Resources res = context.getResources();
        switch (type) {
            case "event_technical":

                titles = res.getStringArray(R.array.events);
                desc = res.getStringArray(R.array.events_desc);
                imgs = context.getResources().obtainTypedArray(R.array.drawables);
                int tempDrawables[] = new int[imgs.length()];
                for (int i = 0; i < imgs.length(); i++)
                    tempDrawables[i] = imgs.getResourceId(i, -1);
                drawables = tempDrawables;
                break;

            case "event_nontech":
                titles = res.getStringArray(R.array.events_nontech);
                desc = res.getStringArray(R.array.events_nontech_desc);
                drawables = new int[]{R.drawable.nontek, R.drawable.nontek, R.drawable.nontek, R.drawable.nontek, R.drawable.nontek, R.drawable.nontek};
                break;


            case "pronite":
                titles = res.getStringArray(R.array.pronite);
                desc = res.getStringArray(R.array.pronite_desc);
                drawables = new int[]{R.drawable.cult1, R.drawable.cult2, R.drawable.cult3, R.drawable.cult4};
                break;

            case "litfest":
                titles = res.getStringArray(R.array.litfest);
                desc = res.getStringArray(R.array.litfest_desc);
                drawables = new int[]{R.drawable.blue_black, R.drawable.blue_black, R.drawable.blue_black, R.drawable.blue_black, R.drawable.blue_black};
                break;

            case "filmfest":
                titles = res.getStringArray(R.array.filmfest_titles);
                desc = res.getStringArray(R.array.filmfest_desc);
                drawables = new int[]{R.drawable.red, R.drawable.red, R.drawable.red, R.drawable.red, R.drawable.red};
                break;


        }


        for (int i = 0; i < titles.length; i++) {
            list.add(new EventInfo(titles[i], desc[i], drawables[i]));

        }

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Inflate the layout
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);




        layout = inflater.inflate(R.layout.event_list_row, parent, false);

            tv = (TextView) layout.findViewById(R.id.event_name);
            desc = (TextView) layout.findViewById(R.id.event_desc);
            iv = (ImageView) layout.findViewById(R.id.event_img);

            EventInfo temp = list.get(position);
            tv.setText(temp.title);
            desc.setText(temp.description);
            iv.setImageResource(temp.image);



        return layout;
    }
}