package in.rakhtsavelives.app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static int RESULT_LOAD_IMAGE = 1;
    EditText etFName, etLName, etEmailSignUp, etPassSignUp,
            etCPass, etDOB, etAddress1, etAddress2, etPhone, etWeight;
    Spinner spinState, spinCity, spinBG;
    ArrayAdapter stateAdapter, cityAdapter, bgAdapter;
    RadioButton rbMale, rbFemale;
    String email, pass, dState, dCity, dBG, picturePath = null;
    Button btnNext;
    ImageButton ibChooseProfilePic;
    Context context = this;
    Boolean fileUploaded = false;
    ArrayList<String> STATE, CITY, BG;
    String useremail, userpass, userfname, usercpass, userlname, userdob, useradd1, useradd2, userstate,
            usercity, userbg, userphone, userweight;
    ParseFile pf = null;
    Bitmap bitmap;
    ProgressDialog dialog;

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent i = getIntent();
        email = i.getStringExtra("email");
        pass = i.getStringExtra("pass");
        init(email, pass);
    }

    protected void init(String email, String pass) {

        STATE = SplashActivity.STATES;
        CITY = new ArrayList();
        BG = SplashActivity.BG;

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Uploading Image and Creating Account.\n Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);

        etEmailSignUp = (EditText) findViewById(R.id.etEmailSignUp);
        etPassSignUp = (EditText) findViewById(R.id.etPassSignUp);
        etCPass = (EditText) findViewById(R.id.etCPass);
        etFName = (EditText) findViewById(R.id.etFName);
        etLName = (EditText) findViewById(R.id.etLName);
        etDOB = (EditText) findViewById(R.id.etDOB);
        etAddress1 = (EditText) findViewById(R.id.etAddress1);
        etAddress2 = (EditText) findViewById(R.id.etAddress2);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etWeight = (EditText) findViewById(R.id.etWeight);

        etEmailSignUp.setText(email);
        etPassSignUp.setText(pass);

        spinState = (Spinner) findViewById(R.id.spinState);
        stateAdapter = new ArrayAdapter(context, R.layout.spinner_item, STATE);

        spinCity = (Spinner) findViewById(R.id.spinCity);
        cityAdapter = new ArrayAdapter(context, R.layout.spinner_item, CITY);

        spinBG = (Spinner) findViewById(R.id.spinBG);
        bgAdapter = new ArrayAdapter(context, R.layout.spinner_item, BG);

        spinState.setAdapter(stateAdapter);
        spinCity.setAdapter(cityAdapter);
        spinBG.setAdapter(bgAdapter);

        ibChooseProfilePic = (ImageButton) findViewById(R.id.ibChooseProfilePic);

        btnNext = (Button) findViewById(R.id.btnNext);

        if (STATE.size() == 0) InitClass.getStateFromParse(STATE, stateAdapter);
        if (BG.size() == 0) InitClass.getBGFromParse(BG, bgAdapter);

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] date = getTodayDate();
                final DecimalFormat decimalFormat = new DecimalFormat("00");
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etDOB.setText(decimalFormat.format(dayOfMonth) + "/"
                                + decimalFormat.format(monthOfYear + 1) + "/"
                                + decimalFormat.format(year));
                        date[0] = year;
                        date[1] = monthOfYear + 1;
                        date[2] = dayOfMonth;
                    }
                }, date[0], date[1], date[2]);
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useremail = etEmailSignUp.getText().toString();
                userpass = etPassSignUp.getText().toString();
                usercpass = etCPass.getText().toString();
                userfname = etFName.getText().toString();
                userlname = etLName.getText().toString();
                userdob = etDOB.getText().toString();
                useradd1 = etAddress1.getText().toString();
                useradd2 = etAddress2.getText().toString();
                userstate = spinState.getSelectedItem().toString();
                // usercity = spinCity.getSelectedItem().toString();
                userbg = spinBG.getSelectedItem().toString();
                userphone = etPhone.getText().toString();
                userweight = etWeight.getText().toString();
                dState = STATE.get(0);
                dBG = BG.get(0);
                if (checkInputes()) {
                    signUp();
                }
            }
        });
        ibChooseProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        spinState.setOnItemSelectedListener(this);

    }

    protected boolean checkInputes() {
        if (useremail.isEmpty() || userpass.isEmpty() || userfname.isEmpty() || userlname.isEmpty()
                || userdob.isEmpty() || useradd1.isEmpty() || userphone.isEmpty()
                || userstate.equals(dState) || userbg.equals(dBG) || userweight.isEmpty()
                || picturePath == null || (rbMale.isChecked() == false && rbFemale.isChecked() == false)) {
            Toast.makeText(context, "Please Fill All Details", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            usercity = spinCity.getSelectedItem().toString();
            if(usercity.equals("Please Select City")){
                Toast.makeText(context, "Please Fill All Details", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (userpass.equals(usercpass)) return true;
            else {
                Toast.makeText(context, "Passwords don't Match", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    protected void signUp() {
        //Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        dialog.show();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] image = stream.toByteArray();
        pf = new ParseFile(userfname + "_" + userlname + ".png", image);
        Log.d(InitClass.TAG, "Upload Started");
        pf.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(InitClass.TAG, "Upload Complete");
                    Log.d(InitClass.TAG, pf.getUrl());
                    fileUploaded = true;
                    saveToParse();
                } else {
                    Log.e(InitClass.TAG, e.toString());
                    Log.e(InitClass.TAG, e.toString(), e);
                    Toast.makeText(getApplicationContext(), "Sorry Image Cannot Be Uploaded!, Please Sign Up WithOut Image", Toast.LENGTH_LONG).show();
                    saveToParse();
                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {
                Log.d(InitClass.TAG, percentDone + "% done");
            }
        });

    }

    private void saveToParse() {
        String gender = rbMale.isChecked() ? "Male" : "Female";
        final ParseUser user = new ParseUser();
        user.setUsername(useremail);
        user.setPassword(userpass);
        user.put("First_Name", userfname);
        user.put("Last_Name", userlname);
        user.put("DOB", userdob);
        user.put("Address1", useradd1);
        user.put("Address2", useradd2);
        user.put("State", userstate);
        user.put("City", usercity);
        user.put("BG", userbg);
        user.put("Phone", Long.parseLong(userphone));
        if (fileUploaded) user.put("ProfilePic", pf);
        user.put("Gender", gender);
        user.put("Age", getAge(userdob));
        user.put("Weight", userweight);
        if (Integer.parseInt(userweight) < 50 || Integer.parseInt(getAge(userdob)) < 17)
            user.put("CanDonate", false);
        else user.put("CanDonate", true);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context,
                            "Account is Created, please Fill Medical Details.",
                            Toast.LENGTH_LONG).show();
                    InitClass.updateParseInstallation(user);
                    startActivity(new Intent(context, MedicalDetailsActivity.class));
                    dialog.dismiss();
                    finish();
                } else {
                    Toast.makeText(context, "Sign up Error: " + e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                    Log.e(InitClass.TAG, e.toString());
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinState) {
            dCity = InitClass.getCity(parent.getItemAtPosition(position).toString(), CITY, cityAdapter);
            if (CITY.size() == 0)
                dCity = InitClass.getCityFromParse(parent.getItemAtPosition(position).toString(), CITY, cityAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                final Matrix matrix = new Matrix();
                bitmap = BitmapFactory.decodeFile(picturePath, options);
                options.inSampleSize = calculateInSampleSize(options, 200, 200);
                try {
                    ExifInterface exif = new ExifInterface(picturePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    Log.d(InitClass.TAG + " EXIF", "Exif: " + orientation);
                    if (orientation == 6) {
                        matrix.postRotate(90);
                    } else if (orientation == 3) {
                        matrix.postRotate(180);
                    } else if (orientation == 8) {
                        matrix.postRotate(270);
                    }
                } catch (Exception e) {
                    Log.e(InitClass.TAG, e.toString() + " at " + e.getCause());
                }

                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(picturePath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                bitmap = Bitmap.createScaledBitmap(bitmap, ibChooseProfilePic.getWidth(), ibChooseProfilePic.getWidth(), false);
                bitmap = getCroppedBitmap(bitmap);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                ibChooseProfilePic.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e(InitClass.TAG, e.toString() + " at " + e.getCause());
                Toast.makeText(context, e.toString() + "\nImage is to big\nPlease choose small Image", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected Bitmap getCroppedBitmap(Bitmap bitmap) {
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

    protected String getAge(String DOB) {
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

    protected int[] getTodayDate() {
        int[] date = new int[3];
        Calendar today = Calendar.getInstance();
        date[2] = today.get(Calendar.DAY_OF_MONTH);
        date[1] = today.get(Calendar.MONTH);
        date[0] = today.get(Calendar.YEAR);
        return date;
    }
}
