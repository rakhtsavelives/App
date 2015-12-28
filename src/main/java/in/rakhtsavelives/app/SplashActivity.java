package in.rakhtsavelives.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {

    protected static ArrayList STATES, BG;
    Activity SPLASH_ACTIVITY = this;
    Typeface tf;
    TextView tv;
    Context context = this;
    Intent intent;
    ProgressBar pbSplash;
    private int SPLASH_TIME = 3000;
    ParseObject parseObject;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        BG = new ArrayList();
        STATES = new ArrayList();
        tv = (TextView) findViewById(R.id.textView);
        pbSplash = (ProgressBar) findViewById(R.id.pbSplash);
        pbSplash.setIndeterminate(false);
        try {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/gillsansmt.ttf");
            tv.setTypeface(tf);
        } catch (Exception e) {
            Log.e(InitClass.TAG, e.toString());
        }
        if (!InitClass.showNoInternetConnectionAlert(context, SPLASH_ACTIVITY)) {
            new BackGroundProcess().execute();
        }
    }

   /* private void checkUser() {
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
    }*/

    private class BackGroundProcess extends AsyncTask<String, Integer, String> {

        int progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbSplash.setVisibility(View.VISIBLE);
            pbSplash.setProgress(0);
            progress = 0;
        }

        @Override
        protected String doInBackground(String... params) {
            ParseUser.enableAutomaticUser();
            ParseInstallation currInstallation = ParseInstallation.getCurrentInstallation();
            currInstallation.saveInBackground(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        progress += 23;
                        Log.d(InitClass.TAG, "Installation Saved Successfully at " + (progress));
                        publishProgress(progress);
                    } else {
                        Log.e(InitClass.TAG, e.getMessage());
                    }
                }
            });
            ParseACL defaultACL = new ParseACL();
            defaultACL.setPublicReadAccess(true);
            ParseACL.setDefaultACL(defaultACL, true);
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("BloodGroup");
            query.addAscendingOrder("ID")
                    .findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground(objects, new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            progress += 23;
                                            Log.d(InitClass.TAG, "BG Pinned at " + (progress));
                                            publishProgress(progress);
                                        } else {
                                            Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                                        }
                                    }
                                });
                            } else {
                                Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                            }
                        }
                    });
            query = new ParseQuery<ParseObject>("State");
            query.addAscendingOrder("ID")
                    .findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground(objects, new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            progress += 23;
                                            Log.d(InitClass.TAG, "State Pinned at " + (progress));
                                            publishProgress(progress);
                                        } else {
                                            Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                                        }
                                    }
                                });
                            } else {
                                Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                            }
                        }
                    });
            query = new ParseQuery<ParseObject>("City");
            query.addAscendingOrder("ID")
                    .findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground(objects, new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            progress += 23;
                                            Log.d(InitClass.TAG, "City Pinned at " + (progress));
                                            publishProgress(progress);
                                        } else {
                                            Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                                        }
                                    }
                                });
                            } else {
                                Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                            }
                        }
                    });
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
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pbSplash.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            final Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(SPLASH_TIME);
                        if (pbSplash.getProgress() < 92) sleep(1000);
                        BG = InitClass.getBloodGroup(BG);
                        STATES = InitClass.getState(STATES);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Log.e(InitClass.TAG, "Thread Error", e);
                    }
                }
            };
            thread.start();
        }
    }
}

