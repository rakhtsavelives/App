package in.rakhtsavelives.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONObject;

public class NotificationActivity extends Activity {

    String msgToUser = "Hello", name, phone, reqName, reqPhone, donnerName, donnerPhone, channel;
    ParseUser user;
    JSONObject sendData, receiveData;
    Intent intent;

    public NotificationActivity() {

    }

    protected void getData() {
        user = ParseUser.getCurrentUser();
        name = user.getString("First_Name") + " " + user.getString("Last_Name");
        phone = user.getLong("Phone") + "";
        sendData = new JSONObject();
        try {
            sendData.put("Phone", phone);
            sendData.put("Name", name);
            sendData.put("alert", name + " is available to donate blood");
            receiveData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            reqName = receiveData.getString("Name");
            reqPhone = receiveData.getString("Phone");
        } catch (Exception e) {
            Log.e(InitClass.TAG, e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            channel = bundle.getString("com.parse.Channel");
        } catch (Exception e) {
            Log.e(InitClass.TAG, e.toString());
        }
        if (channel.equals("Blood")) bloodReqAccepted();
        else {
            getData();
            bloodReqCame();
        }
    }

    private void reqAccepted() {
        unsubscribe("Blood", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(InitClass.TAG, "successfully unsubscribed to the broadcast channel.");
                } else
                    Log.e(InitClass.TAG, e.toString());
            }
        });
        Toast.makeText(NotificationActivity.this, "Yes clicked", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this)
                .setMessage(msgToUser)
                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParsePush pushMsg = new ParsePush();
                        pushMsg.setChannel("Blood");
                        pushMsg.setData(sendData);
                        pushMsg.sendInBackground(new SendCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.d(InitClass.TAG, "Response Send");
                                    subscribe("Blood");
                                } else
                                    Log.e(InitClass.TAG, e.toString());
                                finish();
                            }
                        });
                    }
                })
                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create()
                .show();
    }

    private void reqDenied() {
        Toast.makeText(NotificationActivity.this, "No Clicked", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void subscribe(final String channel) {
        ParsePush.subscribeInBackground(channel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(InitClass.TAG, "successfully subscribed to the " + channel + " broadcast channel.");
                } else {
                    Log.e(InitClass.TAG, "failed to subscribe for push", e);
                }
            }
        });
    }

    private void unsubscribe(String channel, SaveCallback saveCallback) {
        ParsePush.unsubscribeInBackground(channel, saveCallback);
    }

    protected void bloodReqCame() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon_notification_32)
                .setMessage("Can you donate blood right now?")
                .setTitle("Rakht : Save Lives")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reqAccepted();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reqDenied();
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    protected void bloodReqAccepted() {
        try {
            receiveData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            donnerName = receiveData.getString("Name");
            donnerPhone = receiveData.getString("Phone");
            Log.d(InitClass.TAG, donnerName + "(" + donnerPhone + ") is donating blood");
            new AlertDialog.Builder(this)
                    .setMessage(donnerName + " is ready to donate blood\n" +
                            "You can contact him/her on\n" + donnerPhone + "\n" +
                            "You can call him/her by clicking button below.")
                    .setTitle("Donner Found!")
                    .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          /*
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("Phone", donnerPhone);
                            clipboard.setPrimaryClip(clipData);
                            dialog.dismiss();
                            finish();
                            */
                            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + donnerPhone));
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create()
                    .show();

        } catch (Exception e) {
            Log.e(InitClass.TAG, e.toString());
        }

    }
}
