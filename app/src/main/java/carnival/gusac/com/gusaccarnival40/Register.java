package carnival.gusac.com.gusaccarnival40;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import carnival.gusac.com.gusaccarnival40.utils.JSONParser;


public class Register extends ActionBarActivity {

    private static final String LOGIN_URL = "http://gusaccarnival.org/mobile_register.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    Toolbar toolbar;
    Button mRegister;
    ProgressDialog pDialog;
    JSONParser jsonParser;
    EditText mName, mID, mCollege, mPhone, mEmail;
    String name,userid, college, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mName = (EditText) findViewById(R.id.user_name);

        mID = (EditText) findViewById(R.id.userID);
        mCollege = (EditText) findViewById(R.id.college);
        mPhone = (EditText) findViewById(R.id.phone_number);
        mEmail = (EditText) findViewById(R.id.email);
        mRegister = (Button) findViewById(R.id.register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CreateUser().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.share_register) {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, Welcome.HASHTAG);
            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    class CreateUser extends AsyncTask<String, Void, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            name = mName.getText().toString();

            userid = mID.getText().toString();
            college = mCollege.getText().toString();
            email = mEmail.getText().toString();
            phone = mPhone.getText().toString();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {
                // Building Parameters


                jsonParser = new JSONParser();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("mobile", "true"));
                params.add(new BasicNameValuePair("id", userid));
                params.add(new BasicNameValuePair("collorg", college));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("number", phone));


                Log.d("Register", "starting  parser");

                JSONObject json = jsonParser.makeHttpPostRequest(LOGIN_URL,"POST",params);

                // full json response
                Log.d("Register", json.toString());
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Toast.makeText(getApplicationContext(),"Registered Successfully!We will send you a mail as soon as possible!",Toast.LENGTH_LONG).show();
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Register", json.getString(TAG_MESSAGE));
                    Toast.makeText(getApplicationContext(),"Oops! Something went wrong. Please try again later",Toast.LENGTH_LONG).show();

                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

}
