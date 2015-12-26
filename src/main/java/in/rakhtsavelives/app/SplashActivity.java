package in.rakhtsavelives.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

import java.util.ArrayList;

public class SplashActivity extends Activity {

    protected static ArrayList STATES, BG;
    Activity SPLASH_ACTIVITY = this;
    Typeface tf;
    TextView tv;
    Context context = this;
    Intent intent;
    private int SPLASH_TIME = 3000;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        BG = new ArrayList();
        STATES = new ArrayList();
        tv = (TextView) findViewById(R.id.textView);
        try {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/gillsansmt.ttf");
            tv.setTypeface(tf);
        } catch (Exception e) {
            Log.e(InitClass.TAG, e.toString());
        }
        if (!InitClass.showNoInternetConnectionAlert(context, SPLASH_ACTIVITY)) {
            //  new Handler().postDelayed(new Runnable() {
            //     @Override
            //   public void run() {
            //   }
            // }, SPLASH_TIME);
            //if(InitClass.isFirstRun()) InitClass.restartIt(context,SPLASH_ACTIVITY);
            new BackGroundProcess().execute();
        }
    }
    private class  BackGroundProcess extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(SPLASH_TIME);
            }
            catch (Exception e){
                Log.e(InitClass.TAG,e.toString());
            }
            checkUser();
            InitClass.getState(STATES);
            InitClass.getBloodGroup(BG);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            startActivity(intent);
            finish();
        }
    }
    private void checkUser() {
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            intent = new Intent(SplashActivity.this,
                    LoginActivity.class);

        } else {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                intent = new Intent(SplashActivity.this, TabActivity.class);
                intent.putExtra("user", currentUser.getUsername());
            } else {
                intent = new Intent(SplashActivity.this,
                        LoginActivity.class);
            }
        }
    }
}

