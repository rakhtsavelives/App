package in.rakhtsavelives.app;
import android.content.Intent;
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

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ProfileFragment extends Fragment {
    TextView tvName,tvBG;
    EditText etDOB,etPhone,etAdd1,etAdd2,etCity,etState;
    ImageButton ibProfile,ibEdit;
    Button btnProfileUpdate;
    String name,bg,dob,phone,add1,add2,city,state,email;
    int heightOfBitmap,widthOfBitmap;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;


        tvName=(TextView)rootView.findViewById(R.id.tvName);
        tvBG=(TextView)rootView.findViewById(R.id.tvBloodGroup);

        etDOB=(EditText)rootView.findViewById(R.id.etProfileDOB);
        etPhone=(EditText)rootView.findViewById(R.id.etProfilePhone);
        etAdd1=(EditText)rootView.findViewById(R.id.etProfileAddress1);
        etAdd2=(EditText)rootView.findViewById(R.id.etProfileAddress2);
        etCity=(EditText)rootView.findViewById(R.id.etProfileCity);
        etState=(EditText)rootView.findViewById(R.id.etProfileState);

        ibProfile=(ImageButton)rootView.findViewById(R.id.ibProfile);
        ibEdit=(ImageButton)rootView.findViewById(R.id.ibEdit);

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                widthOfBitmap=heightOfBitmap=200;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                widthOfBitmap=heightOfBitmap=100;
                break;
        }

        if(heightOfBitmap <= 0 || widthOfBitmap<=0 ){
            heightOfBitmap = 100;
            widthOfBitmap=100;
        }

        btnProfileUpdate=(Button)rootView.findViewById(R.id.btnProfileUpdate);

        ParseUser user=ParseUser.getCurrentUser();

        email=user.getUsername();
        name=user.get("First_Name")+" "+user.get("Last_Name");
        bg=(String)user.get("BG");
        dob=(String)user.get("DOB");
        phone=""+user.get("Phone");
        add1=(String)user.get("Address1");
        add2=(String)user.get("Address2");
        city=(String)user.get("City");
        state=(String)user.get("State");
        ParseFile pf=user.getParseFile("ProfilePic");
        pf.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    Log.d("test", "We've got data in data.");
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    bmp = Bitmap.createScaledBitmap(bmp, widthOfBitmap, heightOfBitmap, false);
                    bmp = getCroppedBitmap(bmp);
                    ibProfile.setImageBitmap(bmp);
                } else {
                    Log.d("test", "There was a problem downloading the data.");
                }
            }
        });



        tvName.setText(name);
        tvBG.setText(bg);
        etDOB.setText(dob);
        etPhone.setText(phone);
        etAdd1.setText(add1);
        etAdd2.setText(add2);
        etCity.setText(city);
        etState.setText(state);

        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDOB.setEnabled(true);
                etPhone.setEnabled(true);
                etAdd1.setEnabled(true);
                etAdd2.setEnabled(true);
                etCity.setEnabled(true);
                etState.setEnabled(true);
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
}
