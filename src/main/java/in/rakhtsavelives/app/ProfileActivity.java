package in.rakhtsavelives.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ProfileActivity extends AppCompatActivity {

    TextView user,tvName,tvBG;
    ImageButton ibProfile;
    Button logout;
    String name,bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user=(TextView)findViewById(R.id.user);
        logout=(Button)findViewById(R.id.logout);
        tvName=(TextView)findViewById(R.id.tvName);
        tvBG=(TextView)findViewById(R.id.tvBloodGroup);
        ibProfile=(ImageButton)findViewById(R.id.ibProfile);
        ParseUser user=ParseUser.getCurrentUser();
        name=user.get("First_Name")+" "+user.get("Last_Name");
        bg=(String)user.get("BG");
        ParseFile pf=user.getParseFile("ProfilePic");
        pf.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    Log.d("test","We've got data in data.");
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,data.length);
                    ibProfile.setImageBitmap(bmp);
                } else {
                    Log.d("test", "There was a problem downloading the data.");
                }
            }
        });
        tvName.setText(name);
        tvBG.setText(bg);
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
