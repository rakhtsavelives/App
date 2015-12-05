package in.rakhtsavelives.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.parse.*;
public class MainActivity extends Activity{

    private int SPLASH_TIME=2000;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv=(TextView)findViewById(R.id.textView);
        try {
            Typeface tf = Typeface.createFromAsset(this.getAssets(),"fonts/gillsansmt.ttf");
            tv.setTypeface(tf);
        }
        catch (Exception e){
            Log.e("ERROR",e.toString());
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initParse();
            }
        },SPLASH_TIME);
    }
    private void initParse(){
        Parse.initialize(this, "jqr2hzu7oRoJL4l3VP09HxcDl3R7kAhP0Cx4D1xx", "eBQOktkPcO42mvgUqdMP0gYlbuGSirxGkFL0EaiB");
        ParseUser.enableAutomaticUser();
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        checkUser();
    }
    private void checkUser(){
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            Intent intent = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();

        } else {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                intent.putExtra("user", currentUser.getUsername());
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}

