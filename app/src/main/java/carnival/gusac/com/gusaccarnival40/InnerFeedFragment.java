package carnival.gusac.com.gusaccarnival40;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Messi10 on 30-Jan-15.
 */
public class InnerFeedFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_POSITION = "position";

    ListView feedList;
    FeedResultReceiver mReceiver;
    SharedPreferences prefs;
    private int position;
    String pingChecker;
    PendingIntent alarmPendingIntent;


    public static InnerFeedFragment newInstance(int position) {

        InnerFeedFragment ef = new InnerFeedFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_innerfeed, container, false);

        ViewCompat.setElevation(rootView, 50);
        String url;

        long firstMillis = System.currentTimeMillis();
        Intent intent;

        int intervalMillis = 10000;
        prefs = getActivity().getSharedPreferences(PollingService.PREFS_NAME, Context.MODE_PRIVATE);
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        switch (position) {
            case 0:

                url = "http://192.99.151.223:81/latest";
        /* Initialize listView */
                feedList = (ListView) rootView.findViewById(R.id.feed_list);
        /* Starting Download Service */
                mReceiver = new FeedResultReceiver(new Handler());
                mReceiver.setReceiver(new FeedResultReceiver.Receiver() {
                    @Override
                    public void onReceiveResult(int resultCode, Bundle resultData) {
                        switch (resultCode) {
                            case PollingService.STATUS_RUNNING:

                                break;

                            case PollingService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */


                /* Update ListView with result */
                                if (PollingService.latestData.isEmpty()) {
                                    feedList.setAdapter(new MyFeedAdapter(getActivity(), resultData));
                                    String latestPing = resultData.getString("latestPing");
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("latestPing", latestPing);
                                    editor.apply();
                                } else {
                                    feedList.setAdapter(new MyFeedAdapter(getActivity(), PollingService.latestData));
                                    String latestPing = PollingService.latestData.getString("latestPing");
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("latestPing", latestPing);
                                    editor.apply();
                                    PollingService.latestData.clear();
                                }
                                break;

                            case PollingService.STATUS_ERROR:

                                String error = "Cannot fetch Latest feed data from server.Please try again later.";
                                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }, getActivity());

                pingChecker = prefs.getString("latestPing", "0");
                if (pingChecker.equals("0")) {
                    intent = new Intent(getActivity().getApplicationContext(), FeedAlarmReceiver.class);
                    intent.putExtra("receiver", mReceiver);
                    alarmPendingIntent = PendingIntent.getBroadcast(getActivity(), FeedAlarmReceiver.REQUEST_CODE,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);


                    alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, alarmPendingIntent);

                    break;
                }
                intent = new Intent(getActivity(), PollingService.class);
                intent.putExtra("url", url);
                intent.putExtra("receiver", mReceiver);
                intent.putExtra("mode", "latest");

                getActivity().startService(intent);

                break;
            case 1:

                url = "http://192.99.151.223:81/archive";
        /* Initialize listView */
                feedList = (ListView) rootView.findViewById(R.id.feed_list);
        /* Starting Download Service */
                mReceiver = new FeedResultReceiver(new Handler());
                mReceiver.setReceiver(new FeedResultReceiver.Receiver() {
                    @Override
                    public void onReceiveResult(int resultCode, Bundle resultData) {
                        switch (resultCode) {
                            case PollingService.STATUS_RUNNING:

                                break;

                            case PollingService.STATUS_FINISHED:

                                feedList.setAdapter(new MyFeedAdapter(getActivity(), resultData));

                                break;

                            case PollingService.STATUS_ERROR:
                                String error = "Cannot fetch Archive feed data from server.Please try again later.";
                                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

                                break;
                        }
                    }
                }, getActivity());


                intent = new Intent(getActivity(), PollingService.class);
                intent.putExtra("url", url);
                intent.putExtra("receiver", mReceiver);
                intent.putExtra("archive", "true");
                intent.putExtra("mode", "archive");

                getActivity().startService(intent);

                break;

        }
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         }


}
