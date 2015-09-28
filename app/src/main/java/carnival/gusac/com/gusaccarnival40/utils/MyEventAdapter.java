package carnival.gusac.com.gusaccarnival40.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import carnival.gusac.com.gusaccarnival40.EventInfo;
import carnival.gusac.com.gusaccarnival40.R;

/**
 * Created by Abhijith Gururaj.
 * Custom Event Adapter.
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
public MyEventAdapter(Context context, String type) {
        this.type = type;
        String titles[] = null, desc[] = null;
        list = new ArrayList<>();
        int[] drawables = null;
        this.context = context;
    DatabaseHandler handler = new DatabaseHandler(context);
    HashMap<String, String> hashMap;
        Resources res = context.getResources();
        switch (type) {
            case "events":
                Log.d("Adapter", "case Technical");
                hashMap = handler.getEventShortDesc("Events");
                titles = new String[hashMap.size()];
                desc = new String[hashMap.size()];
                for (int i = 0; i < hashMap.size(); i++) {
                    String[] strings = hashMap.get(String.valueOf(i)).split("!@#!@#");
                    titles[i] = strings[0];
                    desc[i] = strings[1];
                }
                imgs = context.getResources().obtainTypedArray(R.array.drawables);
                int tempDrawables[] = new int[imgs.length()];
                for (int i = 0; i < imgs.length(); i++)
                    tempDrawables[i] = imgs.getResourceId(i, -1);
                drawables = tempDrawables;
                break;

            case "pronite":
                hashMap = handler.getEventShortDesc("Cultural Night");
                titles = new String[hashMap.size()];
                desc = new String[hashMap.size()];
                for (int i = 0; i < hashMap.size(); i++) {
                    String[] strings = hashMap.get(String.valueOf(i)).split("!@#!@#");
                    titles[i] = strings[0];
                    desc[i] = strings[1];
                }
                drawables = new int[]{R.drawable.cult1, R.drawable.cult2, R.drawable.cult3, R.drawable.cult4};
                break;

            case "litfest":
                hashMap = handler.getEventShortDesc("Literary Fest");
                titles = new String[hashMap.size()];
                desc = new String[hashMap.size()];
                for (int i = 0; i < hashMap.size(); i++) {
                    String[] strings = hashMap.get(String.valueOf(i)).split("!@#!@#");
                    titles[i] = strings[0];
                    Log.d("LitFest: Binding: ", titles[i]);
                    desc[i] = strings[1];
                    Log.d("LitFest: Binding: ", desc[i]);
                }
                drawables = new int[]{R.drawable.blue_black, R.drawable.blue_black, R.drawable.blue_black, R.drawable.blue_black, R.drawable.blue_black};
                break;

            case "filmfest":
                titles = res.getStringArray(R.array.filmfest_titles);
                desc = res.getStringArray(R.array.filmfest_desc);
                drawables = new int[]{R.drawable.red, R.drawable.red, R.drawable.red, R.drawable.red, R.drawable.red};
                break;

            case "guestlc":
                hashMap = handler.getEventShortDesc("Guest Lectures");
                titles = new String[hashMap.size()];
                desc = new String[hashMap.size()];
                for (int i = 0; i < hashMap.size(); i++) {
                    String[] strings = hashMap.get(String.valueOf(i)).split("!@#!@#");
                    titles[i] = strings[0];
                    Log.d("Guest Lc: Binding: ", titles[i]);
                    desc[i] = strings[1];
                    Log.d("Guest Lc: Binding: ", desc[i]);
                }
                drawables = new int[]{R.drawable.guestlect1, R.drawable.guestlect3, R.drawable.guestlect2};
                break;

            case "sponsors":
                titles = new String[]{"Title Sponsor", "Co Sponsor", "Associate Sponsor"};
                desc = new String[]{"Sponsor Description 1", "Sponsor Description 2", "Sponsor Description 3"};
                drawables = new int[]{R.drawable.red, R.drawable.red, R.drawable.red};
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (type.equals("guestlc") || type.equals("sponsors")) {
            layout = inflater.inflate(R.layout.sponsors_list_row, parent, false);
            tv = (TextView) layout.findViewById(R.id.sponsor_name);
            desc = (TextView) layout.findViewById(R.id.sponsor_desc);
            iv = (ImageView) layout.findViewById(R.id.imageView);
        } else {
            layout = inflater.inflate(R.layout.event_list_row, parent, false);

            tv = (TextView) layout.findViewById(R.id.event_name);
            desc = (TextView) layout.findViewById(R.id.event_desc);
            iv = (ImageView) layout.findViewById(R.id.event_img);
        }
            EventInfo temp = list.get(position);
            tv.setText(temp.title);
            desc.setText(temp.description);
            iv.setImageResource(temp.image);

        return layout;
    }
}