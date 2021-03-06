package in.rakhtsavelives.app;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class TabActivity extends ActionBarActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = { "Emergency", "Profile"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_logout:
                logout();
                return true;
            default:
                return false;
        }
    }
    private void logout(){
        ParseUser.logOutInBackground();
        Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
        unSubscribe();
        startActivity(i1);
        finish();
    }

    protected void unSubscribe(){
        ParsePush.unsubscribeInBackground("Donner", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Rakht_DEBUG:", "successfully unsubscribed to the broadcast channel.");
                } else {
                    Log.e("Rakht_ERROR:", "failed to unsubscribe for push", e);
                }
            }
        });
        ParsePush.unsubscribeInBackground("loggedIN", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Rakht_DEBUG:", "successfully unsubscribed to the broadcast channel.");
                } else {
                    Log.e("Rakht_ERROR:", "failed to unsubscribe for push", e);
                }
            }
        });
    }
}
