package carnival.gusac.com.gusaccarnival40;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import carnival.gusac.com.gusaccarnival40.utils.DatabaseHandler;
import static carnival.gusac.com.gusaccarnival40.GCMUtils.SENDER_ID;
import static carnival.gusac.com.gusaccarnival40.GCMUtils.displayMessage;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.gc_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);

        String title = context.getString(R.string.app_name);

        Intent notificationIntent = new Intent(context, Welcome.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
        Log.d("NAME", Welcome.name);
        Log.d("IntentService", "Registering in server");
        ServerUtilities.register(context, Welcome.name, Welcome.email, registrationId, Welcome.phone);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");

        String eventCategory = "", eventName = "", problemStatement = "", rules = "",
                organizers = "", criteria = "", shortDesc = "", longDesc = "";
        DatabaseHandler db = new DatabaseHandler(context);

        String data = intent.getExtras().getString("data");
        Log.d(TAG, data);

        String type = "";
        try {
            JSONObject json = new JSONObject(data);
            String isRegistered=json.getString("isRegistered");
            if(isRegistered!=null) {
                Log.d(TAG,"Server registration: "+isRegistered);
                Boolean containsPrevEvents=json.getBoolean("containsPrevEvents");
                Boolean containsPrevFeed=json.getBoolean("containsPrevFeed");
                Log.d(TAG,"ContainsPrevEvents: "+containsPrevEvents);
                Log.d(TAG,"ContainsPrevFeed: "+containsPrevFeed);

                if(containsPrevEvents) {
                    JSONArray prevEvents=json.getJSONArray("prevEvents");
                    for (int i = 0; i < prevEvents.length(); i++) {
                        JSONObject jobj=prevEvents.getJSONObject(i);
                        String mode=jobj.getString("mode");
                        eventCategory = jobj.getString("event_category");
                        eventName = jobj.getString("event_name");
                        problemStatement = jobj.getString("problem_statement");
                        shortDesc = jobj.getString("event_shortDesc");
                        longDesc = jobj.getString("event_longDesc");
                        criteria = jobj.getString("judging_criteria");
                        rules = jobj.getString("event_rules");
                        organizers = jobj.getString("event_organizers");

                        Log.d(TAG,"Performing: "+mode+" : "+eventCategory+" : "+ eventName);

                        performUpdateDB(db,mode,eventCategory,eventName,shortDesc,longDesc,
                                problemStatement,rules,criteria,organizers);
                    }
                }

                if(containsPrevFeed){
                    JSONArray prevFeed=json.getJSONArray("prevFeed");
                    for(int i=0;i < prevFeed.length();i++) {
                        JSONObject jobj=prevFeed.getJSONObject(i);
                        String message=jobj.getString("message");
                        String timestamp=jobj.getString("timestamp");
                        generateNotification(context, message);
                        db.addFeedUpdate(message,timestamp);
                    }
                }
            }
            else {
                type = json.getString("type");
                Log.d(TAG, "type: " + type);

                JSONObject jsonData = json.getJSONObject("0");

                //String message = intent.getExtras().getString("message");
                //Log.d(TAG, message);
                //String[] msgs=intent.getExtras().getStringArray("data");
                // notifies user

                if (type.equals("notify")) {
                    String message = jsonData.getString("message");
                    String timestamp = jsonData.getString("timestamp");
                    generateNotification(context, message);
                    db.addFeedUpdate(message, timestamp);
                } else {
                    eventCategory = jsonData.getString("eventCategory");
                    eventName = jsonData.getString("eventName");
                    problemStatement = jsonData.getString("problemStatement");
                    shortDesc = jsonData.getString("shortDesc");
                    longDesc = jsonData.getString("longDesc");
                    criteria = jsonData.getString("criteria");
                    rules = jsonData.getString("rules");
                    organizers = jsonData.getString("organizers");

                    performUpdateDB(db,type,eventCategory,eventName,shortDesc,longDesc,
                            problemStatement,rules,criteria,organizers);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void performUpdateDB(DatabaseHandler db,String type,String eventCategory,String eventName,
                                String shortDesc,String longDesc,String problemStatement,
                                String rules,String criteria,String organizers){


        switch (type) {
            case "add":
                db.addCategoryEvent(eventCategory, eventName);
                db.addEventDetails(eventName, shortDesc, longDesc,
                        problemStatement, rules, criteria, organizers);
                break;
            case "update":
                db.updateEventData(eventName, shortDesc, longDesc,
                        problemStatement, rules, criteria, organizers);
                break;
            case "delete":
                db.deleteEventDetails(eventName);
                break;
        }
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

}
