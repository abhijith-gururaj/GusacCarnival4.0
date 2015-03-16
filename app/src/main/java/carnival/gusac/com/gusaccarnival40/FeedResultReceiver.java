package carnival.gusac.com.gusaccarnival40;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Messi10 on 06-Mar-15.
 */
public class FeedResultReceiver extends ResultReceiver {
    private Receiver mReceiver;
    Context context;

    public FeedResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver, Context context) {
        mReceiver = receiver;
        this.context = context;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }


    }
}
