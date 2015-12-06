package in.rakhtsavelives.app;

import android.app.Application;
import android.util.Log;

import com.parse.*;

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
            //Parse.initialize(this, "jqr2hzu7oRoJL4l3VP09HxcDl3R7kAhP0Cx4D1xx", "eBQOktkPcO42mvgUqdMP0gYlbuGSirxGkFL0EaiB");
            Parse.initialize(this, "AQSUIJWAVYLMoTIxt8TF7pbh5q1lSxapZgIbSrcP", "V5E4rRgFmibNvlvvNFeUHX7XHboOojzQ5umaMDjG");
            ParseUser.enableAutomaticUser();
            ParseInstallation.getCurrentInstallation().saveInBackground();
            ParseACL defaultACL = new ParseACL();
            defaultACL.setPublicReadAccess(true);
            ParseACL.setDefaultACL(defaultACL, true);
    }
    public static void updateParseInstallation(ParseUser user) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("userId", user.getObjectId());
        installation.saveInBackground();
    }
}
