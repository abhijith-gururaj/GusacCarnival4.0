package carnival.gusac.com.gusaccarnival40.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Abhijith Gururaj and Sanjay Kumar.
 *
 * This is a helper class which is used to perform validations on the user input.
 * This is also used to check whether the device is connected to the internet or not.
 */
public class Validators {

    private Context context;
    private DatabaseHandler db;
    public Validators(Context context){
        this.context=context;
        db=new DatabaseHandler(context);
    }

    /**
     * Validation of email address.
     * @param email: email id entered by the user
     * @return : True if valid, false otherwise.
     */
    public boolean isValidEmailAddress(String email) {

        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Validating user name. This should contain a sequence of character or digits.
     * @param name : user name entered by the user.
     * @return : True if valid, false otherwise.
     */
    public boolean isValidName(String name) {
        return name.matches("[a-zA-Z0-9]*");
    }

    /**
     * To check if the device is connected to the internet.
     * @return : True if online, false otherwise.
     */
    public boolean isOnline() {
        boolean haveWifi = false;
        boolean haveCellular = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for(NetworkInfo ni : netInfo) {
            if(ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if(ni.isConnected()) {
                    haveWifi = true;
                }
            }
            if(ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if(ni.isConnected()) {
                    haveCellular = true;
                }
            }
        }
        return haveWifi || haveCellular;
    }

}
