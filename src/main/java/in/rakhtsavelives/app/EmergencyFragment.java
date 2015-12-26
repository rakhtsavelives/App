package in.rakhtsavelives.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONObject;

import java.util.ArrayList;

public class EmergencyFragment extends Fragment implements View.OnClickListener {
    FloatingActionButton fabEmergency, fabAboutUs, fabFAQ, fabSearch, fabDonate;
    boolean canDonate;
    int NOTIFICATION_EXPIRE_TIME_SECONDS = 60 * 60;
    ParseUser user;
    JSONObject data;
    ArrayList BG;
    ArrayAdapter bgAdapter;
    String name, phone, bg, requestBG;
    ;
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);

    public EmergencyFragment() {
        BG = SplashActivity.BG;
        user = ParseUser.getCurrentUser();
        name = user.getString("First_Name") + " " + user.getString("Last_Name");
        phone = user.getLong("Phone") + "";
        bg = user.getString("BG");
        data = new JSONObject();
    }

    protected void getData(String bg) {
        try {
            data.put("Name", name);
            data.put("Phone", phone);
            data.put("alert", "Blood is Required of " + bg + "ve Blood Group");
        } catch (Exception e) {
            Log.e(InitClass.TAG, e.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bgAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, BG);
        View rootView = inflater.inflate(R.layout.fragment_emergency, container, false);
        canDonate = user.getBoolean("CanDonate");
        subscribe(InitClass.getBGChannel(bg));
        if (canDonate) {
            subscribe("Donner");
        } else unsubscribe("Donner", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Log.d(InitClass.TAG, "successfully unsubscribed to the broadcast channel.");
                else
                    Log.e(InitClass.TAG, e.getMessage());
            }
        });
        subscribe(user.getUsername());
        fabEmergency = (FloatingActionButton) rootView.findViewById(R.id.fabEmergency);
        fabAboutUs = (FloatingActionButton) rootView.findViewById(R.id.fabAboutUs);
        fabSearch = (FloatingActionButton) rootView.findViewById(R.id.fabSearch);
        fabFAQ = (FloatingActionButton) rootView.findViewById(R.id.fabFAQ);
        fabDonate = (FloatingActionButton) rootView.findViewById(R.id.fabDonate);

        fabEmergency.setOnClickListener(this);
        fabFAQ.setOnClickListener(this);
        fabSearch.setOnClickListener(this);
        fabAboutUs.setOnClickListener(this);
        fabDonate.setOnClickListener(this);



        final Paint colorTitle = new Paint();
        final Paint colorText = new Paint();
        colorTitle.setColor(getResources().getColor(R.color.showCaseTitle));
        colorText.setColor(getResources().getColor(R.color.showCaseText));
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == fabEmergency) {
            unsubscribe("Donner", new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d(InitClass.TAG, "successfully unsubscribed to the broadcast channel.");
                        final Spinner spinnerBG = new Spinner(getContext());
                        spinnerBG.setLayoutParams(lp);
                        spinnerBG.setAdapter(bgAdapter);
                        spinnerBG.setSelection(BG.indexOf(bg));
                        new AlertDialog.Builder(getContext())
                                .setTitle("Request for Blood")
                                .setMessage("Please Select Blood Group :")
                                .setView(spinnerBG)
                                .setCancelable(false)
                                .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestBG = (String) spinnerBG.getSelectedItem();
                                        ParseQuery userQuery = ParseUser.getQuery();
                                        userQuery.whereEqualTo("City", user.get("City"));
                                        ParseQuery pushQuery = ParseInstallation.getQuery();
                                        pushQuery.whereMatchesQuery("user", userQuery);
                                        pushQuery.whereEqualTo("channels", "Donner");
                                        ParsePush pushReq = new ParsePush();
                                        pushReq.setQuery(pushQuery);
                                        pushReq.setChannels(InitClass.findDonners(requestBG));
                                        getData(requestBG);
                                        pushReq.setData(data);
                                        pushReq.setExpirationTimeInterval(NOTIFICATION_EXPIRE_TIME_SECONDS);
                                        pushReq.sendInBackground(new SendCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    subscribe("Blood");
                                                    Log.d(InitClass.TAG, "Notification is sent");
                                                    for (int i = 0; i < InitClass.findDonners(requestBG).size(); i++) {
                                                        Log.d(InitClass.TAG, "Donner :" + InitClass.findDonners(requestBG).get(i));
                                                    }
                                                    subscribe("Donner");
                                                    Toast.makeText(getContext(), "Blood Request has been sent..\nPlease wait..", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.e(InitClass.TAG, "failed to send push", e);
                                                    Toast.makeText(getContext(), "Sorry...Request cannot be sent.\n Please try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        subscribe("Donner");
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        Log.e(InitClass.TAG, "failed to unsubscribe for push", e);
                        Toast.makeText(getContext(), "Sorry...Request cannot be sent.\n Please try again...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (v == fabAboutUs) {
            Toast.makeText(getContext(), "About Us Clicked", Toast.LENGTH_SHORT).show();
        } else if (v == fabSearch) {
            final EditText input = new EditText(getContext());
            input.setLayoutParams(lp);
            new AlertDialog.Builder(getContext())
                    .setTitle("Search")
                    .setMessage("Enter Name of Person to Search:")
                    .setView(input)
                    .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getContext(), SearchActivity.class);
                            i.putExtra("name", input.getText().toString());
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create()
                    .show();
        } else if (v == fabFAQ) {
            Toast.makeText(getContext(), "FAQ Clicked", Toast.LENGTH_SHORT).show();
        } else if (v == fabDonate) {
            Toast.makeText(getContext(), "Donate Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    private void subscribe(final String channel) {
        ParsePush.subscribeInBackground(channel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(InitClass.TAG, "successfully subscribed to the " + channel + " broadcast channel.");
                } else {
                    Log.e(InitClass.TAG, "failed to subscribe for push", e);
                }
            }
        });
    }

    private void unsubscribe(String channel, SaveCallback saveCallback) {
        ParsePush.unsubscribeInBackground(channel, saveCallback);
    }
}
