package carnival.gusac.com.gusaccarnival40.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Messi10 on 06-Mar-15.
 */
public class FeedResultReceiver extends ResultReceiver {
    Context context;
    private Receiver mReceiver;

    public FeedResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver, Context context) {
        mReceiver = receiver;
        this.context = context;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }


    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }
}
