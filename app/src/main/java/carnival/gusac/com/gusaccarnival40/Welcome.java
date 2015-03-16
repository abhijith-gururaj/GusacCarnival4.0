package carnival.gusac.com.gusaccarnival40;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;

import carnival.gusac.com.gusaccarnival40.navdrawer.NavigationDrawerCallbacks;
import carnival.gusac.com.gusaccarnival40.navdrawer.NavigationDrawerFragment;


public class Welcome extends ActionBarActivity implements NavigationDrawerCallbacks

{

    NavigationDrawerFragment mNavigationDrawerFragment;
    Toolbar toolbar;
    FrameLayout mFrameLayout;

    final static String HASHTAG = "Be sure to attend Gusac Carnival 4.0 #GC4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        mFrameLayout =
                (FrameLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Home");


        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), toolbar);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);

        return true;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment mFragment = null;
        Bundle args = new Bundle();
        String mTag = null;
        switch (position) {
            case 1:

                mFragment = new HomeFragment();
                mTag = "Home";
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().replace(R.id.container, mFragment, mTag).commit();
                break;
            case 2:
                //Toast.makeText(this,"Inside Activity, Calling Fragment",Toast.LENGTH_SHORT).show();
                mFragment = new FeedFragment();
                mTag = "Feed";
                //fm.beginTransaction().replace(R.id.container,mFragment,mTag).commit();
                break;


            case 5:
                args.putString("type", "litfest");
                mFragment = new MainEventFragment();
                mFragment.setArguments(args);
                mTag = "litfest";
                break;

            case 6:
                args.putString("type", "filmfest");
                mFragment = new MainEventFragment();
                mFragment.setArguments(args);
                mTag = "filmfest";
                break;

            case 3:
                mFragment = new Events();
                mTag = "events";
                break;


            case 4:
                args.putString("type", "pronite");
                mFragment = new MainEventFragment();
                mFragment.setArguments(args);
                mTag = "pronite";
                break;
            case 7:
                args.putString("type","about");
                mFragment = new AboutUs();
                mFragment.setArguments(args);
                mTag="about";
                break;

            default:
                break;
        }
        if (mFragment != null) {
            if (!mTag.equals("Home")) {
                fm.beginTransaction().replace(R.id.container, mFragment, mTag)
                        .addToBackStack(mTag)
                        .commit();
            }
        }


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
