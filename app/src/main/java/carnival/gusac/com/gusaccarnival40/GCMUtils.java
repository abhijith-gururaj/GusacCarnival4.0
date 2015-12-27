package carnival.gusac.com.gusaccarnival40;

import android.content.Context;
import android.content.Intent;

public class GCMUtils {

    // give your server registration url here
    //static final String SERVER_URL = "http://192.168.0.10/carnival_gcm_server/register.php";
    static final String SERVER_URL = "https://www.crazyheads.com/carnival_server/register.php";
    // Google project id
    static final String SENDER_ID = "510398650672";


    static final String TAG = "GCM";

    static final String DISPLAY_MESSAGE_ACTION =
            "carnival.gusac.com.gusaccarnival40.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";


    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
