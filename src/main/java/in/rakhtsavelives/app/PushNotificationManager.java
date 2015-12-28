package in.rakhtsavelives.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseUser;

import org.json.JSONObject;

public class PushNotificationManager extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return NotificationActivity.class;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    /*    try {
            JSONObject receiveData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            String username = receiveData.getString("Username");
            if (!ParseUser.getCurrentUser().getUsername().equals(username))*/
        super.onReceive(context, intent);
   /*         else Log.d(InitClass.TAG,"Push from same device neglected");
        } catch (Exception e) {
            Log.e(InitClass.TAG, "Error on Receive", e);
        }*/
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        try {
            JSONObject receiveData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            String username = null;
            username = receiveData.getString("Username");
            if (!ParseUser.getCurrentUser().getUsername().equals(username))
                super.onPushReceive(context, intent);
            else Log.d(InitClass.TAG, "Push from same device neglected");
        } catch (Exception e) {
            Log.e(InitClass.TAG, "Error on Receive", e);
            super.onPushReceive(context,intent);
        }
    }

    @Override
    protected int getSmallIconId(Context context, Intent intent) {
        return R.mipmap.ic_launcher;
    }

    @Override
    protected Bitmap getLargeIcon(Context context, Intent intent) {
        Bitmap largeIcon = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher);
        return largeIcon;
    }
}
