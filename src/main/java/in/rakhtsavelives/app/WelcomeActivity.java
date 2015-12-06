package in.rakhtsavelives.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class WelcomeActivity extends AppCompatActivity {

    TextView user;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user=(TextView)findViewById(R.id.user);
        logout=(Button)findViewById(R.id.logout);
        Intent i=getIntent();
        user.setText(i.getStringExtra("user").toString());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground();
                Intent i1=new Intent(getApplicationContext(),LoginActivity.class);
                unSubscribe();
                startActivity(i1);
                finish();
            }
        });
    }
    protected void unSubscribe(){
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
