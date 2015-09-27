package carnival.gusac.com.gusaccarnival40.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import carnival.gusac.com.gusaccarnival40.FeedInfo;
import carnival.gusac.com.gusaccarnival40.R;
import carnival.gusac.com.gusaccarnival40.utils.PollingService;

/**
 * Created by Messi10 on 06-Mar-15.
 */
public class MyFeedAdapter extends BaseAdapter {

    Context context;
    View layout;
    TextView feedHead;
    TextView feedBody;
    TextView feedTS;
    ImageView feedImage;
    ArrayList<FeedInfo> list;
    String[] head;
    String[] body;


    public MyFeedAdapter(Context ctx, Bundle feedData) {
        context = ctx;
        head = feedData.getStringArray(PollingService.TAG_HEAD);
        body = feedData.getStringArray(PollingService.TAG_BODY);

        int img = R.drawable.gc_launcher;
        list = new ArrayList<FeedInfo>();
        for (int i = 0; i < head.length; i++) {
            list.add(new FeedInfo(head[i], body[i], img));
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.feed_list_row, parent, false);

        feedHead = (TextView) layout.findViewById(R.id.feed_head);
        feedBody = (TextView) layout.findViewById(R.id.feed_body);

        feedImage = (ImageView) layout.findViewById(R.id.feed_image);

        FeedInfo fi = list.get(position);
        feedHead.setText(fi.head);
        feedBody.setText(fi.body);

        feedImage.setImageResource(fi.image);

        return layout;
    }
}
