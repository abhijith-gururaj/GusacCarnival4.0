package carnival.gusac.com.gusaccarnival40.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

import carnival.gusac.com.gusaccarnival40.R;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final int DB_VERSION = 8;

    public static final String DB_NAME = "gusac_carnival";
    public static final String TABLE_EVENT_CATEGORY = "event_category";
    public static final String TABLE_USER = "users";
    public static final String TABLE_EVENTS_DATA = "events";
    public static final String TABLE_FEED_UPDATES = "table_feed_updates";

    //Event Category Table Details
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_EVENT_NAME = "event_name";
    //

    //Events table details
    public static final String EVENT_NAME = "event_name";
    public static final String EVENT_SHORT_DESC = "event_short_desc";
    public static final String EVENT_LONG_DESC = "event_long_desc";
    public static final String EVENT_PROBLEM_STATEMENT = "event_problem_statement";
    public static final String EVENT_RULES = "event_rules";
    public static final String EVENTS_JUDGING_CRITERIA = "event_judging_criteria";
    public static final String EVENT_ORGANIZERS = "event_organizers";
    //

    //Users table details
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PHONE = "user_phone";
    //

    //Feed Table Details
    public static final String FEED_ID = "feed_id";
    public static final String FEED_MESSAGE = "feed_message";
    public static final String FEED_TIMESTAMP = "feed_timestamp";
    //

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategoryTable = "CREATE TABLE " + TABLE_EVENT_CATEGORY + " (" +
                CATEGORY_NAME + " TEXT , " +
                CATEGORY_EVENT_NAME + " TEXT )";


        String createEventsTable = "CREATE TABLE " + TABLE_EVENTS_DATA + " (" +
                EVENT_NAME + " TEXT UNIQUE, " +
                EVENT_SHORT_DESC + " TEXT, " +
                EVENT_LONG_DESC + " TEXT, " +
                EVENT_PROBLEM_STATEMENT + " TEXT, " +
                EVENT_RULES + " TEXT, " +
                EVENTS_JUDGING_CRITERIA + " TEXT, " +
                EVENT_ORGANIZERS + " TEXT " + " )";

        String createUsersTable = "CREATE TABLE " + TABLE_USER + " (" +
                USER_NAME + " TEXT UNIQUE, " +
                USER_EMAIL + " TEXT UNIQUE, " +
                USER_PHONE + " TEXT )";

        String createFeedTable = "CREATE TABLE " + TABLE_FEED_UPDATES + " (" +
                FEED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                FEED_MESSAGE + " TEXT, " +
                FEED_TIMESTAMP + " TEXT )";

        db.execSQL(createCategoryTable);
        db.execSQL(createEventsTable);
        db.execSQL(createUsersTable);
        db.execSQL(createFeedTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB", "upgrade");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEED_UPDATES);

        // Create tables again
        onCreate(db);
    }

    public void addCategoryEvent(String categoryName, String eventName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("GCM", "Adding Event: " + categoryName + " " + eventName);
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, categoryName);
        contentValues.put(CATEGORY_EVENT_NAME, eventName);
        db.insert(TABLE_EVENT_CATEGORY, null, contentValues);
        db.close();
    }

    public void addEventDetails(String eventName, String shortDesc, String longDesc,
                                String probStatement, String rules, String criteria,
                                String organizers) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("GCM","Adding Event: "+shortDesc+" "+eventName);
        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, eventName);
        values.put(EVENT_SHORT_DESC, shortDesc);
        values.put(EVENT_LONG_DESC, longDesc);
        values.put(EVENT_PROBLEM_STATEMENT, probStatement);
        values.put(EVENT_RULES, rules);
        values.put(EVENTS_JUDGING_CRITERIA, criteria);
        values.put(EVENT_ORGANIZERS, organizers);
        db.insert(TABLE_EVENTS_DATA, null, values);
        db.close();
    }

    public void deleteEventDetails(String eventName){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="DELETE FROM "+TABLE_EVENTS_DATA+" WHERE "+
                EVENT_NAME+" = "+eventName;

        db.execSQL(query);
        db.close();
    }

    public void addUserDetails(String userName, String userEmail, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, userName);
        values.put(USER_EMAIL, userEmail);
        values.put(USER_PHONE, phone);

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public HashMap<String, String> getUserDetails() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USER;
        HashMap<String, String> hashMap = new HashMap<>();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            hashMap.put("user_name", cursor.getString(cursor.getColumnIndex(USER_NAME)));
            hashMap.put("user_email", cursor.getString(cursor.getColumnIndex(USER_EMAIL)));
            hashMap.put("user_phone", cursor.getString(cursor.getColumnIndex(USER_PHONE)));
        }
        cursor.close();
        db.close();

        return hashMap;
    }

    public void addFeedUpdate(String feedMessage, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FEED_MESSAGE, feedMessage);
        values.put(FEED_TIMESTAMP, timestamp);

        db.insert(TABLE_FEED_UPDATES, null, values);
        db.close();
    }

    public HashMap<String, String> getEventShortDesc(String mainEvent) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + EVENT_NAME + " , " + EVENT_SHORT_DESC +
                " FROM " + TABLE_EVENTS_DATA + " WHERE " + EVENT_NAME + " IN " + "( " +
                "SELECT " + EVENT_NAME + " FROM " + TABLE_EVENT_CATEGORY + " WHERE " +
                CATEGORY_NAME + "= '" + mainEvent + "')";

        Cursor cursor = db.rawQuery(query, null);
        HashMap<String, String> hashMap = new HashMap<>();
        Log.d("Fetch Short Descs: ", query + "cursor count: " + cursor.getCount());
        cursor.moveToFirst();
        int counter = 0;
        while (!cursor.isAfterLast()) {
            hashMap.put(String.valueOf(counter++), cursor.getString(cursor.getColumnIndex(EVENT_NAME))
                    + "!@#!@#" + cursor.getString(cursor.getColumnIndex(EVENT_SHORT_DESC)));
            Log.d("Inserted: ", hashMap.get(String.valueOf(counter - 1)));
            cursor.moveToNext();
        }
        Log.d("FetchShort Descs : ", "Hashmap insertion done");
        cursor.close();
        db.close();
        return hashMap;
    }

    public HashMap<String, String> getEventDetails(String eventName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_EVENTS_DATA + " WHERE " + EVENT_NAME +
                "= '" + eventName + "'";
        Log.d("Fetch Event Details; ", query);
        HashMap<String, String> hashMap = new HashMap<>();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            hashMap.put("event_name", cursor.getString(0));
            hashMap.put("short_desc", cursor.getString(1));
            hashMap.put("long_desc", cursor.getString(2));
            hashMap.put("problem_statement", cursor.getString(3));
            hashMap.put("rules", cursor.getString(4));
            hashMap.put("criteria", cursor.getString(5));
            hashMap.put("organizers", cursor.getString(6));
        }
        cursor.close();
        db.close();

        return hashMap;
    }

    public void initDetails(Context context) {
        String[] categories = {"Events", "Literary Fest", "Cultural Night", "Guest Lectures"};
        String[] events = context.getResources().getStringArray(R.array.events);
        String[] litfest = context.getResources().getStringArray(R.array.litfest);
        String[] pronite = context.getResources().getStringArray(R.array.pronite);
        String[] guestlcs = context.getResources().getStringArray(R.array.guestLectures);
        String[][] mainEvents = {events, litfest, pronite, guestlcs};
        int index = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        for (String category : categories) {
            values = new ContentValues();
            String[] eventArray = mainEvents[index++];
            for (String event : eventArray) {
                values.put(CATEGORY_NAME, category);
                values.put(CATEGORY_EVENT_NAME, event);

                long i = db.insert(TABLE_EVENT_CATEGORY, null, values);
                Log.d("Inserting Category: ", String.valueOf(i) + "Cat Name: " + category + " event name: " + event);
            }
        }
        initEventDetails(context, db, mainEvents);
    }

    private void initEventDetails(Context context, SQLiteDatabase db, String[][] mainEvents) {
        Resources resources = context.getResources();
        String[] eventShortDescs = resources.getStringArray(R.array.events_desc);
        String[] eventLongDescs = resources.getStringArray(R.array.events_main_desc);
        String[] litfestShortDescs = resources.getStringArray(R.array.litfest_desc);
        String[] litfestLongDescs = resources.getStringArray(R.array.litfest_desc_display);
        String[] proniteShortDescs = resources.getStringArray(R.array.pronite_desc);
        String[] proniteLongDescs = resources.getStringArray(R.array.pronite_display_desc);
        String[] guestlcShortDescs = resources.getStringArray(R.array.guestlc_shortDesc);
        String[] guestlcLongDescs = resources.getStringArray(R.array.guestlc_longDesc);
        String[] problemStatement = resources.getStringArray(R.array.problem_statements);
        String[] rules = resources.getStringArray(R.array.rules);
        String[] criteria = resources.getStringArray(R.array.criteria);
        String[] organizers = resources.getStringArray(R.array.organizers);
        String[][] shortDescs = new String[][]{eventShortDescs, litfestShortDescs, proniteShortDescs, guestlcShortDescs};
        String[][] longDescs = new String[][]{eventLongDescs, litfestLongDescs, proniteLongDescs, guestlcLongDescs};

        for (int i = 0; i < mainEvents.length; i++) {
            for (int j = 0; j < mainEvents[i].length; j++) {
                Log.d("Inserting: ", mainEvents[i][j] + " : " + shortDescs[i][j] + " : " + longDescs[i][j]);
                ContentValues values = new ContentValues();
                values.put(EVENT_NAME, mainEvents[i][j]);
                values.put(EVENT_SHORT_DESC, shortDescs[i][j]);
                values.put(EVENT_LONG_DESC, longDescs[i][j]);
                values.put(EVENT_RULES, rules[i]);
                values.put(EVENTS_JUDGING_CRITERIA, criteria[i]);
                values.put(EVENT_PROBLEM_STATEMENT, problemStatement[i]);
                values.put(EVENT_ORGANIZERS, organizers[i]);

                long i1 = db.insert(TABLE_EVENTS_DATA, null, values);
                Log.d("isInserted? : ", String.valueOf(i1));
            }
        }
    }

    public void updateEventData(String eventName, String shortDesc, String longDesc,
                                String probStatement, String rules, String criteria,
                                String organizers) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, eventName);
        values.put(EVENT_SHORT_DESC, shortDesc);
        values.put(EVENT_LONG_DESC, longDesc);
        values.put(EVENT_PROBLEM_STATEMENT, probStatement);
        values.put(EVENT_RULES, rules);
        values.put(EVENTS_JUDGING_CRITERIA, criteria);
        values.put(EVENT_ORGANIZERS, organizers);

        String where = EVENT_NAME + "=?";
        String[] whereArgs = new String[]{eventName};
        db.update(TABLE_EVENTS_DATA, values, where, whereArgs);
        db.close();
    }

    public Bundle getFeedUpdates(){

        SQLiteDatabase db=this.getReadableDatabase();
        Bundle args=new Bundle();
        String query="SELECT * FROM " + TABLE_FEED_UPDATES;

        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();

        Log.d("Feed Count: ", String.valueOf(cursor.getCount()));
        if(cursor.getCount()==0)
            return null;

        int counter=0;
        String[] messages=new String[cursor.getCount()];
        String[] timestamps=new String[cursor.getCount()];

        while(!cursor.isAfterLast()){
            messages[counter]=cursor.getString(cursor.getColumnIndex(FEED_MESSAGE));
            timestamps[counter]=cursor.getString(cursor.getColumnIndex(FEED_TIMESTAMP));
            Log.d("Inserted feed: ", String.valueOf(counter++));
            cursor.moveToNext();
        }
        args.putStringArray("messages",messages);
        args.putStringArray("timestamps", timestamps);
        cursor.close();
        db.close();

        return args;
    }
}