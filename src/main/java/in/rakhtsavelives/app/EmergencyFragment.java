package in.rakhtsavelives.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
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
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class EmergencyFragment extends Fragment implements View.OnClickListener {
    FloatingActionButton fabEmergency, fabAboutUs, fabFAQ, fabSearch, fabDonate;
    boolean canDonate;
    int NOTIFICATION_EXPIRE_TIME_SECONDS = 60 * 60;
    ParseUser user;
    JSONObject data;
    ArrayList BG;
    ArrayAdapter bgAdapter;
    protected static InterstitialAd interstitialAd;
    private String AD_UNIT_INTERSTITIAL_ID = "ca-app-pub-1183027429910842/6853137412";
    Paint colorTitle = new Paint();
    Paint colorText = new Paint();

    String name, phone, bg, requestBG, username;
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      /*  colorTitle.setColor(getResources().getColor(R.color.showCaseTitle));
        colorText.setColor(getResources().getColor(R.color.showCaseText));
        ShowcaseView mainTutorial=new ShowcaseView.Builder(getActivity())
                .withHoloShowcase()
                .singleShot(new Random().nextLong())
                .setTarget(new ViewTarget(fabEmergency))
                .setContentTitlePaint(new TextPaint(colorTitle))
                .setContentTextPaint(new TextPaint(colorText))
                .setContentTitle("Emergency Button")
                .setContentText("This is Emergency button!")
                .build();*/

    }

    public EmergencyFragment() {
        BG = new ArrayList();
        BG = SplashActivity.BG;
        user = ParseUser.getCurrentUser();
        name = user.getString("First_Name") + " " + user.getString("Last_Name");
        phone = user.getLong("Phone") + "";
        bg = user.getString("BG");
        username = user.getUsername();
        data = new JSONObject();
    }

    protected void getData(String bg) {
        try {
            data.put("Username", username);
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
        if (BG.size() == 0) InitClass.getBGFromParse(BG, bgAdapter);
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
        subscribe(username.replaceAll("[^a-zA-Z0-9]", ""));
        Log.d(InitClass.TAG, username.replaceAll("[^a-zA-Z0-9]", ""));
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

        interstitialAd = new InterstitialAd(getContext());
        interstitialAd.setAdUnitId(AD_UNIT_INTERSTITIAL_ID);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        return rootView;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("79ABECE5D57419ADF9868D2B0EA55594")
                .build();
        interstitialAd.loadAd(adRequest);
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
                                        userQuery.whereContainedIn("BG", InitClass.findDonnersInBGColumn(requestBG));
                                        ParseQuery pushQuery = ParseInstallation.getQuery();
                                        pushQuery.whereMatchesQuery("user", userQuery);
                                        pushQuery.whereNotEqualTo("channels", username.replaceAll("[^a-zA-Z0-9]", ""));
                                        pushQuery.whereEqualTo("channels", "Donner");
                                        ParsePush pushReq = new ParsePush();
                                        pushReq.setQuery(pushQuery);
                                        // pushReq.setChannels(InitClass.findDonners(requestBG));
                                        getData(requestBG);
                                        pushReq.setData(data);
                                        pushReq.setExpirationTimeInterval(NOTIFICATION_EXPIRE_TIME_SECONDS);
                                        pushReq.sendInBackground(new SendCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Log.d(InitClass.TAG, "Notification is sent");
                                                    for (int i = 0; i < InitClass.findDonners(requestBG).size(); i++) {
                                                        Log.d(InitClass.TAG, "Donner :" + InitClass.findDonners(requestBG).get(i));
                                                    }
                                                    subscribe("Donner");
                                                    Toast.makeText(getContext(), "Blood Request has been sent..\nPlease wait..", Toast.LENGTH_SHORT).show();
                                                    if (interstitialAd.isLoaded()) {
                                                        Log.d(InitClass.TAG, "AD Loaded");
                                                        interstitialAd.show();
                                                    } else Log.d(InitClass.TAG, "AD Not Loaded");
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
