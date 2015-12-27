package carnival.gusac.com.gusaccarnival40;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import carnival.gusac.com.gusaccarnival40.utils.AlertDialogManager;
import carnival.gusac.com.gusaccarnival40.utils.ConnectionDetector;
import carnival.gusac.com.gusaccarnival40.utils.DatabaseHandler;
import carnival.gusac.com.gusaccarnival40.utils.Validators;

import static carnival.gusac.com.gusaccarnival40.GCMUtils.SENDER_ID;
import static carnival.gusac.com.gusaccarnival40.GCMUtils.SERVER_URL;

public class GetStarted extends AppCompatActivity {

    EditText txtName, txtEmail, txtPhone;
    AlertDialogManager alert = new AlertDialogManager();
    ConnectionDetector cd;
    private SharedPreferences settings;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        settings = getSharedPreferences("registerPrefs", Context.MODE_PRIVATE);
        if (settings.getBoolean("isRegistered", false)) {
            startActivity(new Intent(getApplicationContext(), Welcome.class));
            finish();
        }

        DatabaseHandler db = new DatabaseHandler(this);
        db.initDetails(this);
        db.close();

        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(GetStarted.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // Check if GCM configuration is set
        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
                || SENDER_ID.length() == 0) {
            // GCM sernder id / server url is missing
            alert.showAlertDialog(GetStarted.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);
            // stop executing code by return
            return;
        }

        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        btnRegister = (Button) findViewById(R.id.btnRegister);

		/*
         * Click event on Register button
		 * */
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Read EditText dat
                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                String phone = txtPhone.getText().toString();
                // Check if user filled the form
                Validators validators = new Validators(GetStarted.this);
                if (validators.isOnline()) {
                    if (name.trim().length() > 0 && email.trim().length() > 0 && phone.trim().length() > 0) {
                        // Launch Main Activity

                        if (validators.isValidEmailAddress(email)) {
                            Intent i = new Intent(getApplicationContext(), Welcome.class);
                            Log.d("Register", "Starting Main");
                            // Registering user on our server
                            // Sending registraiton details to MainActivity

                            DatabaseHandler db = new DatabaseHandler(GetStarted.this);
                            db.addUserDetails(name, email, phone);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(GetStarted.this, "Please enter valid email address.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // user doen't filled that data
                        // ask him to fill the form
                        alert.showAlertDialog(GetStarted.this, "Registration Error!", "Please enter your details", false);
                    }
                } else {
                    Toast.makeText(GetStarted.this, "Cannot connect to the net. Please make sure your device is online!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_started, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
