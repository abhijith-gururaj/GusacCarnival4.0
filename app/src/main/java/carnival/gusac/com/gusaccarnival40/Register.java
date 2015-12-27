package carnival.gusac.com.gusaccarnival40;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import carnival.gusac.com.gusaccarnival40.utils.AlertDialogManager;
import carnival.gusac.com.gusaccarnival40.utils.DatabaseHandler;
import carnival.gusac.com.gusaccarnival40.utils.JSONParser;
import carnival.gusac.com.gusaccarnival40.utils.Validators;


public class Register extends AppCompatActivity {

    private static final String LOGIN_URL = "https://www.crazyheads.com/carnival_server/contact_me.php";
    private static final String TAG_SUCCESS = "type";
    private static final String TAG_MESSAGE = "message";
    Toolbar toolbar;
    Button mRegister;
    ProgressDialog pDialog;
    JSONParser jsonParser;
    EditText mName, mCollege, mPhone, mEmail, mAddress, mDepartment, mState;
    String name = "", college = "", phone = "", email = "", address = "", department = "", state = "", gender = "";
    RadioGroup radioGroup;
    RadioButton mGender;
    static final int DATE_DIALOG_ID = 999;
    public static String dateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //mName = (EditText) findViewById(R.id.user_name);

        //mID = (EditText) findViewById(R.id.userID);
        mCollege = (EditText) findViewById(R.id.reg_college);
        mPhone = (EditText) findViewById(R.id.reg_phone);
        mEmail = (EditText) findViewById(R.id.reg_email);
        mName = (EditText) findViewById(R.id.reg_name);
        mAddress = (EditText) findViewById(R.id.reg_adress);
        mDepartment = (EditText) findViewById(R.id.reg_department);
        mState = (EditText) findViewById(R.id.reg_state);
        radioGroup = (RadioGroup) findViewById(R.id.gender_group);

        DatabaseHandler db = new DatabaseHandler(this);
        HashMap<String, String> hashMap = db.getUserDetails();
        String username = hashMap.get("user_name");
        String useremail = hashMap.get("user_email");
        String userphone = hashMap.get("user_phone");

        mName.setText(username);
        mEmail.setText(useremail);
        mPhone.setText(userphone);

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

    public void showDatePickerDialog(View view) {
        showDialog(DATE_DIALOG_ID);
    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                // Create a new instance of DatePickerDialog and return it
                return new DatePickerDialog(Register.this, listener, year, month, day);

        }
        return null;
    }

    class CreateUser extends AsyncTask<String, Void, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Contacting Servers...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);


            mGender = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

            college = mCollege.getText().toString();
            email = mEmail.getText().toString();
            phone = mPhone.getText().toString();
            name = mName.getText().toString();
            state = mState.getText().toString();
            address = mAddress.getText().toString();
            college = mCollege.getText().toString();
            department = mDepartment.getText().toString();
            gender = mGender.getText().toString();

            Validators validators = new Validators(Register.this);
            Log.d("Register", "Sending: name: " + name + " college:" + college + " email: " + email +
                    " phone: " + phone + " dob:" +
                    dateOfBirth + " state:" + state + " address:" + address + " dept: " + department +
                    " gender: " + gender);

            if (name.equals("") || college.equals("") || phone.equals("") || address.equals("") ||
                    department.equals("") || email.equals("") || dateOfBirth.equals("") ||
                    state.equals("") || gender.equals("")) {
                Toast.makeText(Register.this, "Please Enter all details", Toast.LENGTH_LONG).show();
                this.cancel(true);
            }

            if (name.length() < 4) {
                Toast.makeText(Register.this, "Please Enter a lengthier name", Toast.LENGTH_LONG).show();
                this.cancel(true);
            }
            if (!validators.isValidEmailAddress(email)) {
                Toast.makeText(Register.this, "Please enter valid email address", Toast.LENGTH_LONG).show();
                this.cancel(true);
            }
            if (phone.length() < 9) {
                Toast.makeText(Register.this, "Please enter valid phone number", Toast.LENGTH_LONG).show();
                this.cancel(true);
            }

            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String type;
            try {
                // Building Parameters


                jsonParser = new JSONParser();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userName", name));
                params.add(new BasicNameValuePair("userCollege", college));
                params.add(new BasicNameValuePair("userEmail", email));
                params.add(new BasicNameValuePair("userPhone", phone));
                params.add(new BasicNameValuePair("dob", dateOfBirth));
                params.add(new BasicNameValuePair("userState", state));
                params.add(new BasicNameValuePair("userAddress", address));
                params.add(new BasicNameValuePair("userDepartment", department));
                params.add(new BasicNameValuePair("userGender", gender));

                Log.d("Register", "Sending: " + name + " " + college + " " + email + " " + phone + " " +
                        dateOfBirth + " " + state + " " + address + " " + department + " " + gender);

                Log.d("Register", "starting  parser");

                JSONObject json = jsonParser.makeHttpPostRequest(LOGIN_URL, "POST", params);

                // full json response
                Log.d("Register", json.toString());
                // json success element
                type = json.getString(TAG_SUCCESS);
                if (type.equals("success")) {
                    status = true;
                    return "Thank you! We will contact you as soon as possible!";
                } else {
                    status = false;

                    return "Oops! Something went wrong. Message from server: " + json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String message) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            String title;
            if (status)
                title = "Registration Successful";
            else
                title = "Registration failed";

            AlertDialogManager manager = new AlertDialogManager();
            manager.showAlertDialog(Register.this, title, message, true);
            mState.setText("");
            TextView tv = (TextView) findViewById(R.id.dob_text);
            tv.setText("");
            mCollege.setText("");
            mDepartment.setText("");
            mAddress.setText("");

            if(!status){
                mName.setText("");
                mEmail.setText("");
                mPhone.setText("");
            }
        }

    }

    private DatePickerDialog.OnDateSetListener listener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int month, int day) {
                    // Do something with the date chosen by the user
                    dateOfBirth = day + "-" + month + "-" + year;
                    TextView tv = (TextView) findViewById(R.id.dob_text);
                    tv.setText(dateOfBirth);
                    Log.d("dob", dateOfBirth);

                }
            };

}
