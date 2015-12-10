package in.rakhtsavelives.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.opengl.EGLExt;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

public class ProfileActivity extends AppCompatActivity {

    TextView tvName,tvBG;
    EditText etDOB,etPhone,etAdd1,etAdd2,etCity,etState;
    ImageButton ibProfile,ibLogout,ibEdit;
    Button btnProfileUpdate;
    String name,bg,dob,phone,add1,add2,city,state,email;
    int heightOfBitmap,widthOfBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tvName=(TextView)findViewById(R.id.tvName);
        tvBG=(TextView)findViewById(R.id.tvBloodGroup);

        etDOB=(EditText)findViewById(R.id.etProfileDOB);
        etPhone=(EditText)findViewById(R.id.etProfilePhone);
        etAdd1=(EditText)findViewById(R.id.etProfileAddress1);
        etAdd2=(EditText)findViewById(R.id.etProfileAddress2);
        etCity=(EditText)findViewById(R.id.etProfileCity);
        etState=(EditText)findViewById(R.id.etProfileState);

        ibLogout=(ImageButton)findViewById(R.id.ibLogout);
        ibProfile=(ImageButton)findViewById(R.id.ibProfile);
        ibEdit=(ImageButton)findViewById(R.id.ibEdit);

        heightOfBitmap=ibProfile.getHeight();
        widthOfBitmap=ibProfile.getWidth();
        if(heightOfBitmap <= 0 || widthOfBitmap<=0 ){
            heightOfBitmap = 100;
            widthOfBitmap=100;
        }

        btnProfileUpdate=(Button)findViewById(R.id.btnProfileUpdate);

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
                    bmp=Bitmap.createScaledBitmap(bmp, widthOfBitmap, heightOfBitmap, false);
                    bmp=getCroppedBitmap(bmp);
                    ibProfile.setImageBitmap(bmp);
                } else {
                    Log.d("test", "There was a problem downloading the data.");
                }
            }
        });

        Snackbar.make(findViewById(android.R.id.content), "Logged in as " +email,Snackbar.LENGTH_SHORT).show();

        tvName.setText(name);
        tvBG.setText(bg);
        etDOB.setText(dob);
        etPhone.setText(phone);
        etAdd1.setText(add1);
        etAdd2.setText(add2);
        etCity.setText(city);
        etState.setText(state);

        ibLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground();
                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                unSubscribe();
                startActivity(i1);
                finish();
            }
        });

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
