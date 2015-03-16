package carnival.gusac.com.gusaccarnival40;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Messi10 on 06-Mar-15.
 */

/*
This is an IntentService which is used for polling the server for fetching updates.
 */

public class PollingService extends IntentService {

    public static final String PREFS_NAME = "MyServiceFile";
    private final String TAG = "Polling Service";
    public static Bundle latestData;
    public static final int STATUS_RUNNING = 0;
    String intentMode;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    public static final String TAG_COUNT = "count";
    public static final String TAG_ARRAY = "items";
    public static final String TAG_AUTHOR = "author";
    public static final String TAG_HEAD = "head";
    public static final String TAG_BODY = "body";
    public static final String TAG_TIMESTAMP = "timestamp";
    JSONParser jsonParser;
    String mode;
    SharedPreferences prefs;
    public static String mPreviousPing;

    public PollingService() {
        super(PollingService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Get the extras from the intent
        mode = intent.getStringExtra("archive");
        final ResultReceiver mReceiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");
        intentMode = intent.getStringExtra("mode");

        //Retrieve the latest timestamp from the prefs for fetching latest data
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mPreviousPing = prefs.getString("latestPing", "0");
        Log.d(TAG, mPreviousPing);
        jsonParser = new JSONParser();
        Log.d(TAG, "Service Started");

        if (!TextUtils.isEmpty(url)) {

            mReceiver.send(STATUS_RUNNING, Bundle.EMPTY);
            try {
                //Fetch the data from the server in Json Format
                // and send it to the calling fragment
                //Also send the latest timestamp so that we can get the updates
                // from that particular time and not the whole data as such.
                JSONObject json = jsonParser.makeHttpGetRequest(url, "GET", mPreviousPing);

                // Parse the json data send it to the feed fragment which implements
                // the ResultReceiver. We also send the status of the service along with it.
                if (json.getInt("count") > 0) {
                    Log.d(TAG, json.toString());
                    Bundle results = parseJsonFeed(json);
                    if (results != null && !(results.isEmpty())) {
                        latestData = results;
                        mReceiver.send(STATUS_FINISHED, results);
                        Log.d(TAG, "Checking mode: " + mode);

                        // If the service started from the BroadcastReceiver fetches some data,
                        // We would want to notify the user.
                        if (intentMode.equals("receiver")) {
                            Intent emptyIntent = new Intent(this, Welcome.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(this)
                                            .setSmallIcon(R.drawable.gc_launcher)
                                            .setContentTitle("Feed Updates Available")
                                            .setContentText("Please check your feed for updates!")
                                            .setContentIntent(pendingIntent);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(1, mBuilder.build());
                        }
                    } else {
                        Bundle error = new Bundle();
                        error.putString("error", "Something's Wrong. Please try again later.");
                        mReceiver.send(STATUS_ERROR, error);
                    }
                } else {
                    //The fetched data is null.
                    //Send the status to the feedFragment specifying null data
                    Log.d(TAG, "Null object");
                    Bundle nullBundle = new Bundle();
                    nullBundle.putString("nullJSON", "Could not fetch data from server.Please try again");
                    mReceiver.send(STATUS_ERROR, nullBundle);
                }
            } catch (Exception js) {
                js.printStackTrace();
            }
        }


    }

    private Bundle parseJsonFeed(JSONObject json) {

        String body[], head[], author[], timestamp[];
        Log.d(TAG, "Parsing Json object");
        try {
            int count = json.getInt(TAG_COUNT);
            body = new String[count];
            head = new String[count];
            author = new String[count];
            timestamp = new String[count];
            JSONArray mItemArray = json.getJSONArray(TAG_ARRAY);
            if (mItemArray.length() > 0) {
                for (int i = 0; i < count; i++) {
                    JSONObject innerObject = mItemArray.getJSONObject(i);
                    author[i] = innerObject.getString(TAG_AUTHOR);
                    body[i] = innerObject.getString(TAG_BODY);
                    head[i] = innerObject.getString(TAG_HEAD);
                    timestamp[i] = innerObject.getString(TAG_TIMESTAMP);

                }
                //Store the parsed data in a bundle and return it back.
                //We also store the latest timestamp fetched rom the server
                //This timestamp is stored in preferences.
                Bundle collection = new Bundle();
                collection.putStringArray(TAG_AUTHOR, author);
                collection.putStringArray(TAG_BODY, body);
                collection.putStringArray(TAG_HEAD, head);
                collection.putStringArray(TAG_TIMESTAMP, timestamp);
                collection.putString("latestPing", timestamp[0]);

                return collection;
            } else {
                return null;
            }
        } catch (JSONException js) {
            Log.d(TAG, "Parsing error");
        }

        return null;
    }
}
