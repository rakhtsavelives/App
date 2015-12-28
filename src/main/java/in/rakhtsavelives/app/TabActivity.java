package in.rakhtsavelives.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class TabActivity extends ActionBarActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = {"Emergency", "Profile"};
    ParseUser user;
    protected static InterstitialAd interstitialAdExit;
    private String EXIT_AD_UNIT_INTERSTITIAL_ID = "ca-app-pub-1183027429910842/5398253812";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabAdapter(getSupportFragmentManager());
        new Thread(){
            @Override
            public void run() {
                if(InitClass.isFirstRun()) {
                    startActivity(new Intent(TabActivity.this, TutorialActivity.class));
                }
                super.run();
            }
        }.start();
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        user = ParseUser.getCurrentUser();
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        interstitialAdExit = new InterstitialAd(getApplicationContext());
        interstitialAdExit.setAdUnitId(EXIT_AD_UNIT_INTERSTITIAL_ID);
        interstitialAdExit.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                requestNewInterstitial(interstitialAdExit);
            }
        });
        requestNewInterstitial(interstitialAdExit);
    }
    private void requestNewInterstitial(InterstitialAd interstitialAd) {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("79ABECE5D57419ADF9868D2B0EA55594")
                .build();
        interstitialAd.loadAd(adRequest);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_tutorial:
                startActivity(new Intent(TabActivity.this, TutorialActivity.class));
                return true;
            default:
                return false;
        }
    }

    private void logout() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ParseUser.logOutInBackground();
                unsubscribeAll();
            }
        }.start();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    protected void unSubscribe(final String channel) {
        ParsePush.unsubscribeInBackground(channel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(InitClass.TAG, "successfully unsubscribed to from " + channel + " channel.");
                } else {
                    Log.e(InitClass.TAG, "failed to unsubscribe form " + channel, e);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ParseObject.unpinAllInBackground();
        if (interstitialAdExit.isLoaded()) {
            Log.d(InitClass.TAG, "AD Loaded");
            interstitialAdExit.show();
        } else Log.d(InitClass.TAG, "AD Not Loaded");
    }

    protected void unsubscribeAll() {
        unSubscribe("loggedIN");
        unSubscribe("Donner");
        unSubscribe(user.getUsername().replaceAll("[^a-zA-Z0-9]", ""));
        unSubscribe(InitClass.getBGChannel(user.getString("BG")));
    }

}
