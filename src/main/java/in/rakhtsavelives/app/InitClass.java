package in.rakhtsavelives.app;

import android.app.Application;
import android.util.Log;


import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.ParseException;

/**
 * Created by Dhrumesh on 06-12-2015.
 */
public class InitClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initParse();
    }
    private void initParse(){
        Parse.initialize(this, "AQSUIJWAVYLMoTIxt8TF7pbh5q1lSxapZgIbSrcP", "V5E4rRgFmibNvlvvNFeUHX7XHboOojzQ5umaMDjG");
        ParseUser.enableAutomaticUser();
        ParseInstallation currInstallation=ParseInstallation.getCurrentInstallation();
        currInstallation.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Log.d("Rakht", "Insta" +
                            "llation Saved Successfully");
                } else {
                    Log.e("Rakht", e.getMessage());
                }
            }
        });
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
    public static void updateParseInstallation(ParseUser user) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("userId", user.getObjectId());
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if(e==null){
                    Log.d("Rakht","Installation Updated Successfully");
                }
                else{
                    Log.e("Rakht",e.getMessage());
                }
            }
        });
    }
}
