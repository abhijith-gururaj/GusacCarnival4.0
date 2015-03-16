package carnival.gusac.com.gusaccarnival40;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.melnykov.fab.FloatingActionButton;
import com.todddavies.components.progressbar.ProgressWheel;

/**
 * Created by Messi10 on 31-Jan-15.
 */

//The first fragment whcih will be displayed to the user.
public class HomeFragment extends Fragment {

    FloatingActionButton fab;
    private ProgressWheel mDaysWheel;
    private ProgressWheel mHoursWheel;
    private ProgressWheel mMinutesWheel;
    private ProgressWheel mSecondsWheel;
    Time conferenceTime = new Time(Time.getCurrentTimezone());
    int hour = 01;
    int minute = 01;
    int second = 01;
    int monthDay = 18;
    int month = 2;
    int year = 2015;
    private int mDisplayDays;
    private int mDisplayHours;
    private int mDisplayMinutes;
    private int mDisplaySeconds;

    public HomeFragment() {
        super();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);
        ((Welcome) getActivity()).setActionbarTitle("Home");

       //Start the register activity when the user clicks the fab
        fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Register.class));

            }
        });


        mDaysWheel = (ProgressWheel) rootview.findViewById(R.id.activity_countdown_timer_days);
        mHoursWheel = (ProgressWheel) rootview.findViewById(R.id.activity_countdown_timer_hours);
        mMinutesWheel = (ProgressWheel) rootview.findViewById(R.id.activity_countdown_timer_minutes);
        mSecondsWheel = (ProgressWheel) rootview.findViewById(R.id.activity_countdown_timer_seconds);

        configureConferenceDate();

        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    /*
    This method displays a countdown timer for the fest.
    When the timer is elapsed, We will display a another fragment which will
    display the menu in the map.
     */

    private void configureConferenceDate() {
        conferenceTime.set(second, minute, hour, monthDay, month, year);
        conferenceTime.normalize(true);
        long confMillis = conferenceTime.toMillis(true);

        Time nowTime = new Time(Time.getCurrentTimezone());
        nowTime.setToNow();
        nowTime.normalize(true);


        long nowMillis = nowTime.toMillis(true);

        long milliDiff = confMillis - nowMillis;

        new CountDownTimer(milliDiff, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                mDisplayDays = (int) ((millisUntilFinished / 1000) / 86400);
                mDisplayHours = (int) (((millisUntilFinished / 1000) - (mDisplayDays * 86400)) / 3600);
                mDisplayMinutes = (int) (((millisUntilFinished / 1000) - ((mDisplayDays * 86400) + (mDisplayHours * 3600))) / 60);
                mDisplaySeconds = (int) ((millisUntilFinished / 1000) % 60);

                mDaysWheel.setText(String.valueOf(mDisplayDays));
                mDaysWheel.setProgress(mDisplayDays);
                mHoursWheel.setText(String.valueOf(mDisplayHours));
                mHoursWheel.setProgress(mDisplayHours * 15);

                mMinutesWheel.setText(String.valueOf(mDisplayMinutes));
                mMinutesWheel.setProgress(mDisplayMinutes * 6);

                Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
                an.setFillAfter(true);

                mSecondsWheel.setText(String.valueOf(mDisplaySeconds));
                mSecondsWheel.setProgress(mDisplaySeconds * 6);

            }

            @Override
            public void onFinish() {
                ((Welcome) getActivity()).replaceFragment();
            }
        }.start();
    }


}
