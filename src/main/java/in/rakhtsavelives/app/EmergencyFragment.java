package in.rakhtsavelives.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

public class EmergencyFragment extends Fragment {
    ImageButton ibEmergency;
    boolean canDonate;
    int NOTIFICATION_EXPIRE_TIME_SECONDS=60*5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_emergency, container, false);

        ParseUser user = ParseUser.getCurrentUser();
        canDonate = user.getBoolean("CanDonate");
        if (canDonate) {
            subscribe("Donner");
        }
        ibEmergency = (ImageButton) rootView.findViewById(R.id.ibEmergency);
        ibEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unsubscribe("Donner", new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("Rakht_DEBUG:", "successfully unsubscribed to the broadcast channel.");
                            ParsePush pushReq = new ParsePush();
                            pushReq.setChannel("Donner");
                            pushReq.setMessage("Blood is Required of X+ Blood Group");
                            pushReq.setExpirationTimeInterval(NOTIFICATION_EXPIRE_TIME_SECONDS);
                            pushReq.sendInBackground(new SendCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("Rakht", "Notification is sent");
                                        Toast.makeText(getContext(), "Blood Request has been sent..\nPlease wait..", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("Rakht_ERROR:", "failed to send push", e);
                                        Toast.makeText(getContext(), "Sorry...Request cannot be sent.\n Please try again...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Log.e("Rakht_ERROR:", "failed to unsubscribe for push", e);
                            Toast.makeText(getContext(), "Sorry...Request cannot be sent.\n Please try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return rootView;
    }

    private void subscribe(final String channel) {
        ParsePush.subscribeInBackground(channel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Rakht_DEBUG:", "successfully subscribed to the " + channel + " broadcast channel.");
                } else {
                    Log.e("Rakht_ERROR:", "failed to subscribe for push", e);
                }
            }
        });
    }

    private void unsubscribe(String channel, SaveCallback saveCallback) {
        ParsePush.unsubscribeInBackground(channel, saveCallback);
    }
}
