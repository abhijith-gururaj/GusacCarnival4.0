package carnival.gusac.com.gusaccarnival40.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.ResultReceiver;
import android.text.format.Time;

/**
 * Created by Messi10 on 06-Mar-15.
 */

/*
This is the broadcast receiver which gets fired up after each given interval.
We disable this receiver after 10 days of the event as the users will want the app to stop
giving the updates after 10days of the event.
 */
public class FeedAlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12345;
    static String url = "http://192.99.151.223:81/latest";
    Time endTime = new Time(Time.getCurrentTimezone());
    int hour = 01;
    int minute = 01;
    int second = 01;
    int monthDay = 28;
    int month = 2;
    int year = 2015;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        //Start the intent for the IntentService
        Intent i = new Intent(context, PollingService.class);
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        i.putExtra("url", url);
        i.putExtra("receiver", receiver);
        i.putExtra("mode", "receiver");

        /*
        The following code compares the date of the os with the
        Ending date(the date where the broadcast receiver must be disabled)
        If the dates are matched, then the receiver is cancelled.
        */
        endTime.set(second, minute, hour, monthDay, month, year);
        endTime.normalize(true);
        long confMillis = endTime.toMillis(true);
        Time nowTime = new Time(Time.getCurrentTimezone());
        nowTime.setToNow();
        nowTime.normalize(true);
        long nowMillis = nowTime.toMillis(true);
        long milliDiff = confMillis - nowMillis;

        if (milliDiff == 0 || milliDiff < 0)
            cancelAlarm();

        context.startService(i);

    }

    private void cancelAlarm() {
        //Disable the broadcast receiver(i.e. Stop receiving updates)
        ComponentName receiver = new ComponentName(context, FeedAlarmReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }
}