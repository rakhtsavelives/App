package in.rakhtsavelives.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Switch;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InitClass extends Application {
    static Object object;
    static ParseObject parseObject;
    static String TAG = "Rakht:", dBG = null, dState = null, dCity = null;
    private String APPLICATION_ID = "AQSUIJWAVYLMoTIxt8TF7pbh5q1lSxapZgIbSrcP";
    private String CLIENT_KEY = "V5E4rRgFmibNvlvvNFeUHX7XHboOojzQ5umaMDjG";

    protected static void updateParseInstallation(ParseUser user) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("userId", user.getObjectId());
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Installation Updated Successfully");
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    protected static boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) object;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    protected static void showNoInternetConnectionAlert(final Context context, final Activity activity) {
        new AlertDialog.Builder(context)
                .setTitle("No Internet Connection")
                .setMessage("Sorry, We cannot connect to our server!\nPlease Try Again!")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(context, SplashActivity.class));
                        activity.finish();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .create()
                .show();
    }

    protected static String getBloodGroup(final ArrayList BG, final ArrayAdapter bgAdapter) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("BloodGroup");
        query.addAscendingOrder("ID");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        parseObject = objects.get(i);
                        try {
                            parseObject.fetch();
                            BG.add((String) parseObject.get("BG"));
                            if (i == 0) dBG = (String) parseObject.get("BG");
                        } catch (Exception ex) {
                            Log.e(InitClass.TAG, ex.getMessage());
                        }
                    }
                    bgAdapter.notifyDataSetChanged();
                } else {
                    Log.e(InitClass.TAG, e.getMessage());
                }
            }
        });
        return dBG;
    }

    protected static String getState(final ArrayList STATE, final ArrayAdapter stateAdapter) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("State");
        query.addAscendingOrder("ID");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        parseObject = objects.get(i);
                        try {
                            parseObject.fetch();
                            STATE.add((String) parseObject.get("State"));
                            if (i == 0) dState = (String) parseObject.get("State");
                        } catch (Exception ex) {
                            Log.e(InitClass.TAG, ex.getMessage());
                        }
                    }
                    stateAdapter.notifyDataSetChanged();
                } else {
                    Log.e(InitClass.TAG, e.getMessage());
                }
            }
        });
        return dState;
    }

    protected static String getCity(String state, final ArrayList CITY, final ArrayAdapter cityAdapter) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("City");
        query.addAscendingOrder("ID");
        query.whereEqualTo("State", state);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    CITY.clear();
                    for (int i = 0; i < objects.size(); i++) {
                        parseObject = objects.get(i);
                        try {
                            parseObject.fetch();
                            CITY.add((String) parseObject.get("City"));
                            if (i == 0) dCity = (String) parseObject.get("City");
                        } catch (Exception ex) {
                            Log.e(InitClass.TAG, ex.getMessage());
                        }
                    }
                    cityAdapter.notifyDataSetChanged();
                } else {
                    Log.e(InitClass.TAG, e.getMessage());
                }
            }
        });
        return dCity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initParse();
        object = getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private void initParse() {
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseInstallation currInstallation = ParseInstallation.getCurrentInstallation();
        currInstallation.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Installation Saved Successfully");
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
    protected static String getBGChannel(String bg){
        switch(bg){
            case "O-": return "O_Neg";
            case "O+": return "O_Pos";
            case "A-": return "A_Neg";
            case "A+": return "A_Pos";
            case "B-": return "B_Neg";
            case "B+": return "B_Pos";
            case "AB-": return "AB_Neg";
            case "AB+": return "AB_Pos";
            default: return null;
        }
    }
    protected static LinkedList findDonners(String bg){
        LinkedList donners=new LinkedList();
        switch (bg){
            case "O-":
                donners.add("O_Neg");
                break;
            case "A-":
                donners.add("O_Neg");
                donners.add("A_Neg");
                break;
            case "B-":
                donners.add("O_Neg");
                donners.add("B_Neg");
                break;
            case "AB-":
                donners.add("O_Neg");
                donners.add("A_Neg");
                donners.add("B_Neg");
                donners.add("AB_Neg");
                break;
            case "O+":
                donners.add("O_Neg");
                donners.add("O_Pos");
                break;
            case "A+":
                donners.add("O_Neg");
                donners.add("A_Neg");
                donners.add("O_Pos");
                donners.add("A_Pos");
                break;
            case "B+":
                donners.add("O_Neg");
                donners.add("B_Neg");
                donners.add("O_Pos");
                donners.add("B_Pos");
                break;
            case "AB+":
                donners.add("O_Neg");
                donners.add("A_Neg");
                donners.add("B_Neg");
                donners.add("AB_Neg");
                donners.add("O_Pos");
                donners.add("A_Pos");
                donners.add("B_Pos");
                donners.add("AB_Pos");
                break;
        }
        return donners;
    }
}
