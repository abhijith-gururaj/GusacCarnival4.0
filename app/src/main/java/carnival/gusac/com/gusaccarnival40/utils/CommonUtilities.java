package carnival.gusac.com.gusaccarnival40.utils;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

    public static final String SERVER_URL = "http://192.168.0.10/carnival_gcm_server/register.php";

    // Google project id
    public static final String SENDER_ID = "510398650672";
    public static final String DISPLAY_MESSAGE_ACTION =
            "carnival.gusac.com.gusaccarnival40.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";
    static final String TAG = "GCM";

    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
