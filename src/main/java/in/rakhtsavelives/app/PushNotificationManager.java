package in.rakhtsavelives.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Dhrumesh on 12-12-2015.
 */
public class PushNotificationManager extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return NotificationActivity.class;
    }
}
