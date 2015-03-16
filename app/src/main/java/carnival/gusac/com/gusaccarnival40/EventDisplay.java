package carnival.gusac.com.gusaccarnival40;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Messi10 on 31-Jan-15.
 */

public class EventDisplay extends ActionBarActivity {

    String tag;
    int position;
    TextView desc_event;
    String desc[] = null;
    String head[]=null;
    Resources res;
    Toolbar toolbar;
    TextView head_event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);

        //Set the toolbar and its title.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Description");

        head_event= (TextView) findViewById(R.id.event_heading);
        desc_event = (TextView) findViewById(R.id.desc_event);
        tag = getIntent().getStringExtra("tag");
        position = getIntent().getIntExtra("eventid", 0);

        //Get the resources to initialize the various
        // string arrays present in strings.xml
        res = getResources();

        displayDescription(tag, position);

    }
/*
This method extracts the titles and descriptions of the respective events
using a switch case. Based on the user's click, the method initializes the arrays
and displays them in the textviews.
 */
    private void displayDescription(String tag, int position) {
        switch (tag) {
            case "event_technical":
                head=res.getStringArray(R.array.events);
                desc = res.getStringArray(R.array.events_main_desc);
                 break;
            case "event_nontech":
                head=res.getStringArray(R.array.events_nontech);
                desc = res.getStringArray(R.array.event_desc_nontech);
                break;
            case "litfest":
                head=res.getStringArray(R.array.litfest);
                desc = res.getStringArray(R.array.litfest_desc_display);
                break;
            case "filmfest":
                head=res.getStringArray(R.array.filmfest_titles);
                desc = res.getStringArray(R.array.filmfest_display_desc);
                break;
            case "pronite":
                head=res.getStringArray(R.array.pronite);
                desc = res.getStringArray(R.array.pronite_display_desc);
                break;
        }
        head_event.setText(head[position]);
        desc_event.setText(desc[position]);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        //Create a share intent
        if (id == R.id.share_event) {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, Welcome.HASHTAG);
            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }


}