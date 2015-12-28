package in.rakhtsavelives.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ArrayAdapter;

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
    static ParseObject parseObject, cities, state, bg;
    static String TAG = "Rakht", dBG = null, dState = null, dCity = null;
    static SharedPreferences pref;
    private static String APPLICATION_ID = "AQSUIJWAVYLMoTIxt8TF7pbh5q1lSxapZgIbSrcP";
    private static String CLIENT_KEY = "V5E4rRgFmibNvlvvNFeUHX7XHboOojzQ5umaMDjG";

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

    protected static boolean showNoInternetConnectionAlert(final Context context, final Activity activity) {
        if (!haveNetworkConnection()) {
            new AlertDialog.Builder(context)
                    .setTitle("No Internet Connection")
                    .setMessage("Sorry, We cannot connect to our server!\nPlease Try Again!")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restartIt(context, activity);
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
            return true;
        }
        return false;
    }

    protected static void restartIt(final Context context, final Activity activity) {
        context.startActivity(new Intent(context, context.getClass()));
        activity.finish();
    }

    protected static ArrayList getBloodGroup(final ArrayList BG) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("BloodGroup");
        query.addAscendingOrder("ID")
                .fromLocalDatastore()
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (int i = 0; i < objects.size(); i++) {
                                parseObject = objects.get(i);
                                try {
                                    // parseObject.fetch();
                                    BG.add(parseObject.getString("BG"));
                                } catch (Exception ex) {
                                    Log.e(InitClass.TAG, ex.getMessage() + ex.getStackTrace());
                                }
                            }
                        } else {
                            Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                        }
                    }
                });
        return BG;
    }

    protected static ArrayList getState(final ArrayList STATES) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("State");
        query.addAscendingOrder("ID")
                .fromLocalDatastore()
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (int i = 0; i < objects.size(); i++) {
                                parseObject = objects.get(i);
                                try {
                                    //parseObject.fetch();
                                    STATES.add(parseObject.getString("State"));
                                } catch (Exception ex) {
                                    Log.e(InitClass.TAG, ex.getMessage() + ex.getStackTrace());
                                }
                            }
                        } else {
                            Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                        }
                    }
                });
        return STATES;
    }

    protected static String getCity(String state, final ArrayList CITY, final ArrayAdapter cityAdapter) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("City");
        query.addAscendingOrder("ID")
                .whereEqualTo("State", state)
                .fromLocalDatastore()
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            CITY.clear();
                            for (int i = 0; i < objects.size(); i++) {
                                parseObject = objects.get(i);
                                try {
                                    // parseObject.fetch();
                                    CITY.add(parseObject.getString("City"));
                                    if (i == 0) dCity = parseObject.getString("City");
                                } catch (Exception ex) {
                                    Log.e(InitClass.TAG, ex.toString());
                                }
                            }
                            cityAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                        }
                    }
                });
        return dCity;
    }

    protected static String getBGChannel(String bg) {
        switch (bg) {
            case "O-":
                return "O_Neg";
            case "O+":
                return "O_Pos";
            case "A-":
                return "A_Neg";
            case "A+":
                return "A_Pos";
            case "B-":
                return "B_Neg";
            case "B+":
                return "B_Pos";
            case "AB-":
                return "AB_Neg";
            case "AB+":
                return "AB_Pos";
            case "HH (Bombay)":
                return "HH_Bombay";
            default:
                return null;
        }
    }

    protected static LinkedList findDonners(String bg) {
        LinkedList donners = new LinkedList();
        switch (bg) {
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
            case "HH (Bombay)":
                donners.add("HH_Bombay");
            case "Example":
                donners.add("Example");
                break;
        }
        return donners;
    }

    protected static LinkedList findDonnersInBGColumn(String bg) {
        LinkedList donners = new LinkedList();
        switch (bg) {
            case "O-":
                donners.add("O-");
                break;
            case "A-":
                donners.add("O-");
                donners.add("A-");
                break;
            case "B-":
                donners.add("O-");
                donners.add("B-");
                break;
            case "AB-":
                donners.add("O-");
                donners.add("A-");
                donners.add("B-");
                donners.add("AB-");
                break;
            case "O+":
                donners.add("O-");
                donners.add("O+");
                break;
            case "A+":
                donners.add("O-");
                donners.add("A-");
                donners.add("O+");
                donners.add("A+");
                break;
            case "B+":
                donners.add("O-");
                donners.add("B-");
                donners.add("O+");
                donners.add("B+");
                break;
            case "AB+":
                donners.add("O-");
                donners.add("A-");
                donners.add("B-");
                donners.add("AB-");
                donners.add("O+");
                donners.add("A+");
                donners.add("B+");
                donners.add("AB+");
                break;
            case "HH (Bombay)":
                donners.add("HH (Bombay)");
                break;
            case "Example":
                donners.add("Example");
                break;
        }
        return donners;
    }


    protected static void initParse() {
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
                                        Log.d(InitClass.TAG, "BG Pinned");
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
                                        Log.d(InitClass.TAG, "State Pinned");
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
                                        Log.d(InitClass.TAG, "City Pinned");
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
    }

    protected static boolean isFirstRun() {
        Log.d(TAG, "FirstRun:" + pref.getBoolean("FirstRun", true));
        if (pref.getBoolean("FirstRun", true)) {
            pref.edit().putBoolean("FirstRun", false).commit();
            return true;
        }
        return false;
    }

    protected static String getCityFromParse(String state, final ArrayList CITY, final ArrayAdapter cityAdapter) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("City");
        query.addAscendingOrder("ID")
                .whereEqualTo("State", state)
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            CITY.clear();
                            for (int i = 0; i < objects.size(); i++) {
                                parseObject = objects.get(i);
                                try {
                                    CITY.add(parseObject.getString("City"));
                                    if (i == 0) dCity = parseObject.getString("City");
                                } catch (Exception ex) {
                                    Log.e(InitClass.TAG, ex.toString());
                                }
                            }
                            cityAdapter.notifyDataSetChanged();
                            Log.d(InitClass.TAG, "CiTy Fetched From Parse!");
                        } else {
                            Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                        }
                    }
                });
        return dCity;
    }

    protected static String getStateFromParse(final ArrayList STATE, final ArrayAdapter stateAdapter) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("State");
        query.addAscendingOrder("ID")
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            STATE.clear();
                            for (int i = 0; i < objects.size(); i++) {
                                parseObject = objects.get(i);
                                try {
                                    STATE.add(parseObject.getString("State"));
                                    if (i == 0) dState = parseObject.getString("State");
                                } catch (Exception ex) {
                                    Log.e(InitClass.TAG, ex.toString());
                                }
                            }
                            stateAdapter.notifyDataSetChanged();
                            Log.d(InitClass.TAG, "State Fetched From Parse!");
                        } else {
                            Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                        }
                    }
                });
        return dState;
    }

    protected static String getBGFromParse(final ArrayList BG, final ArrayAdapter bgAdapter) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("BloodGroup");
        query.addAscendingOrder("ID")
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            BG.clear();
                            for (int i = 0; i < objects.size(); i++) {
                                parseObject = objects.get(i);
                                try {
                                    BG.add(parseObject.getString("BG"));
                                    if (i == 0) dBG = parseObject.getString("BG");
                                } catch (Exception ex) {
                                    Log.e(InitClass.TAG, ex.toString());
                                }
                            }
                            Log.d(InitClass.TAG, "BG Fetched From Parse!");
                            bgAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(InitClass.TAG, e.getMessage() + "\n" + e.getStackTrace());
                        }
                    }
                });
        return dBG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        object = getSystemService(Context.CONNECTIVITY_SERVICE);
        state = new ParseObject("State");
        bg = new ParseObject("BG");
        cities = new ParseObject("City");
        pref = getSharedPreferences("Data", MODE_PRIVATE);
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(getApplicationContext(), APPLICATION_ID, CLIENT_KEY);
     /*   new Thread(){
            @Override
            public void run() {
                initParse();
            }
        }.start();*/
    }

}
