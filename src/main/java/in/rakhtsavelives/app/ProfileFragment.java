package in.rakhtsavelives.app;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;

public class ProfileFragment extends Fragment {
    TextView tvName, tvBG;
    EditText etDOB, etPhone, etAdd1, etAdd2, etCity, etState, etGender, etWeight;
    ImageButton ibProfile, ibEdit;
    Button btnProfileUpdate;
    String name, bg, dob, phone, add1, add2, city, state, email, gender, age, weight;
    int heightOfBitmap, widthOfBitmap;
    View rootView;
    ProgressDialog dialog;
    ParseUser user;
    ParseInstallation installation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Log.d(InitClass.TAG, "User Associated with Installation");
                else
                    Log.e(InitClass.TAG, e.getMessage());
            }
        });

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Updating\n Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        tvBG = (TextView) rootView.findViewById(R.id.tvBloodGroup);

        etDOB = (EditText) rootView.findViewById(R.id.etProfileDOB);
        etPhone = (EditText) rootView.findViewById(R.id.etProfilePhone);
        etAdd1 = (EditText) rootView.findViewById(R.id.etProfileAddress1);
        etAdd2 = (EditText) rootView.findViewById(R.id.etProfileAddress2);
        etCity = (EditText) rootView.findViewById(R.id.etProfileCity);
        etState = (EditText) rootView.findViewById(R.id.etProfileState);
        etGender = (EditText) rootView.findViewById(R.id.etProfileGender);
        etWeight = (EditText) rootView.findViewById(R.id.etProfileWeight);

        ibProfile = (ImageButton) rootView.findViewById(R.id.ibProfile);
        ibEdit = (ImageButton) rootView.findViewById(R.id.ibEdit);

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                widthOfBitmap = heightOfBitmap = 200;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                widthOfBitmap = heightOfBitmap = 100;
                break;
        }

        if (heightOfBitmap <= 0 || widthOfBitmap <= 0) {
            heightOfBitmap = 100;
            widthOfBitmap = 100;
        }

        btnProfileUpdate = (Button) rootView.findViewById(R.id.btnProfileUpdate);

        user = ParseUser.getCurrentUser();
        email = user.getUsername();
        name = user.get("First_Name") + " " + user.get("Last_Name");
        bg = (String) user.get("BG");
        dob = (String) user.get("DOB");
        phone = "" + user.get("Phone");
        add1 = (String) user.get("Address1");
        add2 = (String) user.get("Address2");
        city = (String) user.get("City");
        state = (String) user.get("State");
        gender = (String) user.get("Gender");
        weight = (String) user.get("Weight");
        age = getAge(dob);
        user.put("Age", age);
        if (Integer.parseInt(age) > 17 && Integer.parseInt(weight) >= 50)
            user.put("CanDonate", true);
        else
            user.put("CanDonate", false);
        user.saveInBackground();
        try {
            ParseFile pf = user.getParseFile("ProfilePic");
            pf.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Log.d(InitClass.TAG, "We've got data in data.");
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        bmp = Bitmap.createScaledBitmap(bmp, widthOfBitmap, heightOfBitmap, false);
                        bmp = getCroppedBitmap(bmp);
                        ibProfile.setImageBitmap(bmp);
                    } else {
                        Log.d(InitClass.TAG, "There was a problem downloading the data.");
                    }
                }
            });
        } catch (Exception e) {
            Log.e(InitClass.TAG,"No image found");
        }
        tvName.setText(name);
        tvBG.setText(bg);
        etDOB.setText(dob + " (" + age + " Years)");
        etPhone.setText(phone);
        etAdd1.setText(add1);
        etAdd2.setText(add2);
        etCity.setText(city);
        etState.setText(state);
        etGender.setText(gender);
        etWeight.setText(weight);
        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPhone.setEnabled(true);
                etAdd1.setEnabled(true);
                etAdd2.setEnabled(true);
                etCity.setEnabled(true);
                etState.setEnabled(true);
                etWeight.setEnabled(true);
                btnProfileUpdate.setVisibility(View.VISIBLE);
                ibProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });

        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                user.put("Address1", etAdd1.getText().toString());
                user.put("Address2", etAdd2.getText().toString());
                user.put("State", etState.getText().toString());
                user.put("City", etCity.getText().toString());
                user.put("Phone", Long.parseLong(etPhone.getText().toString()));
                user.put("Weight", etWeight.getText().toString());
                if (Integer.parseInt(age) > 17 && Integer.parseInt(etWeight.getText().toString()) >= 50)
                    user.put("CanDonate", true);
                else
                    user.put("CanDonate", false);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "Update Sucessful", Toast.LENGTH_SHORT).show();
                            etPhone.setEnabled(false);
                            etAdd1.setEnabled(false);
                            etAdd2.setEnabled(false);
                            etCity.setEnabled(false);
                            etState.setEnabled(false);
                            etWeight.setEnabled(false);
                            btnProfileUpdate.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                        } else {
                            Log.e(InitClass.TAG, e.getMessage());
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return rootView;
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public String getAge(String DOB) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        int year, month, day;
        day = Integer.parseInt(DOB.substring(0, 2));
        month = Integer.parseInt(DOB.substring(3, 5));
        year = Integer.parseInt(DOB.substring(6, 10));
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age + "";
    }
}
