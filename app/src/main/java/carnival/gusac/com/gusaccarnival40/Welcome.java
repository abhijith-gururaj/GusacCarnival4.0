package carnival.gusac.com.gusaccarnival40;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import java.util.HashMap;

import carnival.gusac.com.gusaccarnival40.utils.AlertDialogManager;
import carnival.gusac.com.gusaccarnival40.utils.ConnectionDetector;
import carnival.gusac.com.gusaccarnival40.utils.DatabaseHandler;

import static carnival.gusac.com.gusaccarnival40.GCMUtils.DISPLAY_MESSAGE_ACTION;
import static carnival.gusac.com.gusaccarnival40.GCMUtils.EXTRA_MESSAGE;
import static carnival.gusac.com.gusaccarnival40.GCMUtils.SENDER_ID;


public class Welcome extends AppCompatActivity

{
    final static String HASHTAG = "Be sure to attend Gusac Carnival 2015. Venue: Gitam University." +
            "Download the app : https://play.google.com/store/apps/details?id=carnival.gusac.com.gusaccarnival40&hl=en";
    public static String name;
    public static String email;
    public static String phone;
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
            //lblMessage.append(newMessage + "\n");
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            WakeLocker.release();
        }
    };
    Toolbar toolbar;
    FrameLayout mFrameLayout;
    private DrawerLayout drawerLayout;
    private AsyncTask<Void, Void, Void> mRegisterTask;
    private AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        SharedPreferences settings = getSharedPreferences("registerPrefs", Context.MODE_PRIVATE);
        ConnectionDetector cd;
        mFrameLayout = (FrameLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Home");


        if (!settings.getBoolean("isRegistered", false)) {
            Log.d("WebViewActivity", "isRegistered: " + settings.getBoolean("isRegistered", false));
            Log.d("Main Activity", "Started Main");
            cd = new ConnectionDetector(getApplicationContext());

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                // Internet Connection is not present
                alert.showAlertDialog(Welcome.this,
                        "Internet Connection Error",
                        "Please connect to working Internet connection", false);

            }

            // Getting name, email from intent

            DatabaseHandler db = new DatabaseHandler(this);
            HashMap<String, String> userDetails = db.getUserDetails();
            name = userDetails.get("user_name");
            email = userDetails.get("user_email");
            phone = userDetails.get("user_phone");

            Toast.makeText(this, "Name: " + name + " email: " + email, Toast.LENGTH_LONG).show();
            // Make sure the device has the proper dependencies.
            GCMRegistrar.checkDevice(this);

            // Make sure the manifest was properly set - comment out this line
            // while developing the app, then uncomment it when it's ready.
            GCMRegistrar.checkManifest(this);
            Log.d("Main", "Checked");

            registerReceiver(mHandleMessageReceiver, new IntentFilter(
                    DISPLAY_MESSAGE_ACTION));

            // Get GCM registration id
            final String regId = GCMRegistrar.getRegistrationId(this);
            Log.d("RegId", regId);
            // Check if regid already presents
            if (regId.equals("")) {
                // Registration is not present, register now with GCM
                GCMRegistrar.register(this, SENDER_ID);
            } else {
                // Device is already registered on GCM
                if (GCMRegistrar.isRegisteredOnServer(this)) {
                    // Skips registration.
                    Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
                } else {
                    // Try to register again, but not in the UI thread.
                    // It's also necessary to cancel the thread onDestroy(),
                    // hence the use of AsyncTask instead of a raw thread.
                    final Context context = this;
                    mRegisterTask = new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            // Register on our server
                            // On server creates a new user
                            Log.d("Register", "Trying to regsiter");
                            ServerUtilities.register(context, name, email, regId, phone);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            Log.d("Register", "Registration Complete");

                            mRegisterTask = null;
                        }

                    };
                    mRegisterTask.execute();
                    Log.d("Main", "Ended Main");
                }
            }
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("isRegistered", true);
            editor.putString("name", name);
            editor.putString("email", email);
            editor.apply();
        } else {
            DatabaseHandler db=new DatabaseHandler(this);
            HashMap<String,String> hashMap = db.getUserDetails();
            name = hashMap.get("user_name");
            email = hashMap.get("user_email");
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View v = navigationView.inflateHeaderView(R.layout.navheader);

        TextView nametv = (TextView) v.findViewById(R.id.username);
        TextView emailtv = (TextView) v.findViewById(R.id.email);
        nametv.setText(name);
        emailtv.setText(email);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            FragmentManager fm = getSupportFragmentManager();
            Fragment mFragment = null;
            Bundle args = new Bundle();
            String mTag = null;

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                if (!getSupportActionBar().isShowing())
                    getSupportActionBar().show();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.drawer_home:
                        mFragment = new HomeFragment();
                        args.putString("type", "Home");
                        mTag = "Home";
                        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fm.beginTransaction().replace(R.id.container, mFragment, mTag).commit();
                        break;

                    case R.id.drawer_feed:
                        mFragment = new FeedFragment();
                        mTag = "Feed";
                        break;

                    case R.id.drawer_events:
                        args.putString("type", "events");
                        mFragment = new MainEventFragment();
                        mFragment = new MainEventFragment();
                        mFragment.setArguments(args);
                        mTag = "events";
                        break;

                    case R.id.drawer_cult_events:
                        args.putString("type", "pronite");
                        mFragment = new MainEventFragment();
                        mFragment.setArguments(args);
                        mTag = "pronite";
                        break;

                    case R.id.drawer_litfest:
                        args.putString("type", "litfest");
                        mFragment = new MainEventFragment();
                        mFragment.setArguments(args);
                        mTag = "litfest";
                        break;

                    case R.id.drawer_filmfest:
                        args.putString("type", "filmfest");
                        mFragment = new MainEventFragment();
                        mFragment.setArguments(args);
                        mTag = "filmfest";
                        Toast.makeText(getApplicationContext(), "Please wait. Loading website", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.drawer_workshops:
                        args.putString("type","workshops");
                        mFragment=new MainEventFragment();
                        mFragment.setArguments(args);
                        mTag="workshops";
                        break;

                    case R.id.drawer_sponsors:
                        args.putString("type", "sponsors");
                        mFragment = new MainEventFragment();
                        mFragment.setArguments(args);
                        mTag = "sponsors";
                        break;

                    case R.id.drawer_about:
                        args.putString("type", "about");
                        mFragment = new AboutUs();
                        mFragment.setArguments(args);
                        mTag = "about";
                        break;

                    case R.id.drawer_guestLectures:
                        args.putString("type", "guestlc");
                        mFragment = new MainEventFragment();
                        mFragment.setArguments(args);
                        mTag = "guestlc";
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
                if (mFragment != null) {
                    if (!mTag.equals("Home")) {
                        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fm.beginTransaction().replace(R.id.container, mFragment, mTag)
                                .addToBackStack(mTag)
                                .commit();
                    }
                }
                return true;
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        Boolean notifClicked=getIntent().getBooleanExtra("notifClicked", false);
        Log.d("NotifClicked?", String.valueOf(notifClicked));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .addToBackStack("Home")
                    .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.share) {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, HASHTAG);
            startActivity(sendIntent);
        }

        if(id==R.id.register){
            Intent intent=new Intent(this,Register.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionbarTitle(String title) {

        toolbar.setTitle(title);
    }

    public void replaceFragment() {
        String mapTag = "HomeMap";
        Fragment map = new MapFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, map, mapTag).commit();
    }
}
