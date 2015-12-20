package in.rakhtsavelives.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class SplashActivity extends Activity {

    Activity SPLASH_ACTIVITY = this;
    private int SPLASH_TIME = 2000;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!InitClass.haveNetworkConnection()) {
            InitClass.showNoInternetConnectionAlert(this, SPLASH_ACTIVITY);
        } else {
            TextView tv = (TextView) findViewById(R.id.textView);
            try {
                Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/gillsansmt.ttf");
                tv.setTypeface(tf);
            } catch (Exception e) {
                Log.e(InitClass.TAG, e.toString());
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkUser();
                }
            }, SPLASH_TIME);
        }
    }

    private void checkUser() {
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            Intent intent = new Intent(SplashActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();

        } else {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(SplashActivity.this, TabActivity.class);
                intent.putExtra("user", currentUser.getUsername());
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}

