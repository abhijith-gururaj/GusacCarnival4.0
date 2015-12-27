package carnival.gusac.com.gusaccarnival40;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import carnival.gusac.com.gusaccarnival40.utils.DatabaseHandler;

/**
 * Created by Abhijith Gururaj.
 */

public class EventDisplay extends AppCompatActivity {

    String tag;
    int position;
    TextView desc_event;
    String desc[] = null;
    String head[]=null;
    Resources res;
    Toolbar toolbar;
    TextView longDesc, rules, criteria, organizers, problemStatement;
    TextView event_rules, event_prob, event_organizers, event_criteria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);

        //Set the toolbar and its title.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        Intent i = getIntent();
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(i.getStringExtra("title"));
        ImageView image = (ImageView) findViewById(R.id.image);
        Drawable d = getResources().getDrawable(i.getIntExtra("image", R.drawable.cosmic));
        image.setImageDrawable(d);
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int primaryDark = getResources().getColor(R.color.myPrimaryDarkColor);
                int primary = getResources().getColor(R.color.myPrimaryColor);
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(primaryDark));
            }
        });

        problemStatement = (TextView) findViewById(R.id.problem_statement);
        desc_event = (TextView) findViewById(R.id.desc_event);
        longDesc = (TextView) findViewById(R.id.long_desc_event);
        rules = (TextView) findViewById(R.id.rules);
        criteria = (TextView) findViewById(R.id.criteria);
        organizers = (TextView) findViewById(R.id.organizers);
        event_rules = (TextView) findViewById(R.id.event_rules);
        event_prob = (TextView) findViewById(R.id.event_problemStatement);
        event_criteria = (TextView) findViewById(R.id.event_criteria);
        event_organizers = (TextView) findViewById(R.id.event_organizers);

        tag = i.getStringExtra("tag");

        if (tag.equals("guestlc")||tag.equals("workshops")) {

            problemStatement.setVisibility(View.GONE);
            event_prob.setVisibility(View.GONE);
            rules.setVisibility(View.GONE);
            event_rules.setVisibility(View.GONE);
            criteria.setVisibility(View.GONE);
            event_criteria.setVisibility(View.GONE);
            organizers.setVisibility(View.GONE);
            event_organizers.setVisibility(View.GONE);
        }
        position = i.getIntExtra("eventid", 0);
        res = getResources();
            if(!tag.equals("workshops")) {
                HashMap<String, String> hashMap = new DatabaseHandler(this).getEventDetails(i.getStringExtra("title"));

                desc_event.setText(hashMap.get("short_desc"));
                problemStatement.setText(hashMap.get("problem_statement"));
                longDesc.setText(hashMap.get("long_desc"));
                rules.setText(hashMap.get("rules"));
                criteria.setText(hashMap.get("criteria"));
                organizers.setText(hashMap.get("organizers"));
            }else {
                String[] descs=res.getStringArray(R.array.workshops_longDescs);
                desc_event.setText(i.getStringExtra("desc"));
                longDesc.setText(descs[position]);

            }
        }
        //displayDescription(tag, position);
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
            case "guestlc":
                head = res.getStringArray(R.array.guestlc_shortDesc);
                desc = res.getStringArray(R.array.guestlc_longDesc);
        }

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