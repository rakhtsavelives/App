package in.rakhtsavelives.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONObject;

public class NotificationActivity extends Activity {

    String msgToUser = "Hello", name, phone, username, reqName, reqPhone, reqUserName, donnerName, donnerPhone, channel;
    ParseUser user;
    JSONObject sendData, receiveData;
    Intent intent;
    Typeface tf;
    TextView tv;
    Context context = this;

    public NotificationActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv = (TextView) findViewById(R.id.textView);
        try {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/gillsansmt.ttf");
            tv.setTypeface(tf);
        } catch (Exception e) {
            Log.e(InitClass.TAG, e.toString(),e);
        }
        intent = getIntent();
        user = ParseUser.getCurrentUser();
        username = user.getUsername();
        Log.d(InitClass.TAG, "UserName : " + username);
        Bundle bundle = intent.getExtras();
        try {
            channel = bundle.getString("com.parse.Channel");
            Log.d(InitClass.TAG, "Channel: " + channel);
        } catch (Exception e) {
            Log.e(InitClass.TAG, e.toString());
        }
        if (channel.equals(username.replaceAll("[^a-zA-Z0-9]", ""))) bloodReqAccepted();
        else {
            getData();
            bloodReqCame();
        }
    }

    protected void getData() {
        name = user.getString("First_Name") + " " + user.getString("Last_Name");
        phone = user.getLong("Phone") + "";
        sendData = new JSONObject();
        try {
            sendData.put("Username", username);
            sendData.put("Phone", phone);
            sendData.put("Name", name);
            sendData.put("alert", name + " is available to donate blood");
            receiveData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            reqName = receiveData.getString("Name");
            reqPhone = receiveData.getString("Phone");
            reqUserName = receiveData.getString("Username");
        } catch (Exception e) {
            Log.e(InitClass.TAG, e.toString());
        }
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

    private void reqAccepted() {
        new AlertDialog.Builder(this)
                .setMessage(msgToUser)
                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParsePush pushMsg = new ParsePush();
                        pushMsg.setChannel(reqUserName.replaceAll("[^a-zA-Z0-9]", ""));
                        pushMsg.setData(sendData);
                        pushMsg.sendInBackground(new SendCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.d(InitClass.TAG, "Response Send");
                                    showContactDialog();
                                } else {
                                    Log.e(InitClass.TAG, e.toString());
                                    startActivity(new Intent(NotificationActivity.this, SplashActivity.class));
                                    finish();
                                }

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

    private void showContactDialog() {
        new AlertDialog.Builder(this)
                .setMessage(reqName + " has requested for the blood.\n" +
                        "You can contact him/her on " + reqPhone)
                .setTitle("Thanks!")
                .setPositiveButton("Copy To Clipboard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Phone", donnerPhone);
                        clipboard.setPrimaryClip(clipData);
                        dialog.dismiss();
                        startActivity(new Intent(NotificationActivity.this, SplashActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(NotificationActivity.this, SplashActivity.class));
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
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(NotificationActivity.this, SplashActivity.class));
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
