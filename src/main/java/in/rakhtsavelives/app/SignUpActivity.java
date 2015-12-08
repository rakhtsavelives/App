package in.rakhtsavelives.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.StreamHandler;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etFName,etLName,etEmailSignUp,etPassSignUp,etCPass,etDOB,etAddress1,etAddress2,etPhone;
    Spinner spinState,spinCity,spinBG;
    ArrayAdapter stateAdapter,cityAdapter,bgAdapter;
    String email,pass,dState,dCity,dBG,picturePath=null,TAG="Rakht";
    Button btnNext;
    ImageButton ibChooseProfilePic;
    Context context;
    int day,month,year;
    ArrayList<String> STATE,CITY,BG;
    ParseObject parseObject;
    private static int RESULT_LOAD_IMAGE = 1,RESULT_CROP_IMAGE=2;
    String useremail,userpass,userfname,usercpass,userlname,userdob,useradd1,useradd2,userstate,
            usercity,userbg,userphone;
    ParseFile pf=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent i=getIntent();
        email=i.getStringExtra("email");
        pass=i.getStringExtra("pass");
        context=getApplicationContext();
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1;
        month = Calendar.getInstance().get(Calendar.MONTH)+1;
        year = Calendar.getInstance().get(Calendar.YEAR);
        init(email,pass);
    }
    protected void init(String email,String pass){

        STATE=new ArrayList();
        CITY=new ArrayList();
        BG=new ArrayList();

        etEmailSignUp=(EditText)findViewById(R.id.etEmailSignUp);
        etPassSignUp=(EditText)findViewById(R.id.etPassSignUp);
        etCPass=(EditText)findViewById(R.id.etCPass);
        etFName=(EditText)findViewById(R.id.etFName);
        etLName=(EditText)findViewById(R.id.etLName);
        etDOB=(EditText)findViewById(R.id.etDOB);
        etAddress1=(EditText)findViewById(R.id.etAddress1);
        etAddress2=(EditText)findViewById(R.id.etAddress2);
        etPhone=(EditText)findViewById(R.id.etPhone);

        etEmailSignUp.setText(email);
        etPassSignUp.setText(pass);

        spinState=(Spinner)findViewById(R.id.spinState);
        stateAdapter=new ArrayAdapter(context,R.layout.spinner_item,STATE);

        spinCity=(Spinner)findViewById(R.id.spinCity);
        cityAdapter=new ArrayAdapter(context,R.layout.spinner_item,CITY);

        spinBG=(Spinner)findViewById(R.id.spinBG);
        bgAdapter=new ArrayAdapter(context,R.layout.spinner_item,BG);

        spinState.setAdapter(stateAdapter);
        spinCity.setAdapter(cityAdapter);
        spinBG.setAdapter(bgAdapter);

        spinState.setOnItemSelectedListener(this);
        spinCity.setOnItemSelectedListener(this);

        getState();
        getBloodGroup();

        ibChooseProfilePic=(ImageButton)findViewById(R.id.ibChooseProfilePic);
        ibChooseProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        btnNext=(Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useremail=etEmailSignUp.getText().toString();
                userpass=etPassSignUp.getText().toString();
                usercpass=etCPass.getText().toString();
                userfname=etFName.getText().toString();
                userlname=etLName.getText().toString();
                userdob=etDOB.getText().toString();
                useradd1=etAddress1.getText().toString();
                useradd2=etAddress2.getText().toString();
                userstate=spinState.getSelectedItem().toString();
                usercity=spinCity.getSelectedItem().toString();
                userbg=spinBG.getSelectedItem().toString();
                userphone=etPhone.getText().toString();
                if(checkInputes()) {
                    signUp();
                }
            }
        });
    }
    private void getBloodGroup(){
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("BloodGroup");
        query.addAscendingOrder("ID");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    for(int i=0;i<objects.size();i++){
                        parseObject=objects.get(i);
                        try {
                            parseObject.fetch();
                            BG.add((String) parseObject.get("BG"));
                            if(i==0) dBG=(String) parseObject.get("BG");
                        }
                        catch (Exception ex){
                            Log.e(TAG,ex.getMessage());
                        }
                    }
                    bgAdapter.notifyDataSetChanged();
                }
                else{
                    Log.e(TAG,e.getMessage());
                }
            }
        });
    }
    private void getState(){
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("State");
        query.addAscendingOrder("ID");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    for(int i=0;i<objects.size();i++){
                        parseObject=objects.get(i);
                        try {
                            parseObject.fetch();
                            STATE.add((String) parseObject.get("State"));
                            if(i==0) dState=(String) parseObject.get("State");
                        }
                        catch (Exception ex){
                            Log.e(TAG,ex.getMessage());
                        }
                    }
                    stateAdapter.notifyDataSetChanged();
                }
                else{
                    Log.e(TAG,e.getMessage());
                }
            }
        });
    }
    private void getCity(String state){
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("City");
        query.addAscendingOrder("ID");
        query.whereEqualTo("State", state);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    for(int i=0;i<objects.size();i++){
                        parseObject=objects.get(i);
                        try {
                            parseObject.fetch();
                            CITY.add((String) parseObject.get("City"));
                            if(i==0) dCity=(String) parseObject.get("City");
                        }
                        catch (Exception ex){
                            Log.e(TAG,ex.getMessage());
                        }
                    }
                    cityAdapter.notifyDataSetChanged();
                }
                else{
                    Log.e(TAG,e.getMessage());
                }
            }
        });
    }
    protected boolean checkInputes(){
        if(useremail.isEmpty()|| userpass.isEmpty()|| userfname.isEmpty()|| userlname.isEmpty()
                || userdob.isEmpty()|| useradd1.isEmpty()|| useradd2.isEmpty()|| userphone.isEmpty()
                || userstate.equals(dState)||usercity.equals(dCity)||userbg.equals(dBG)
                || picturePath==null){
            Toast.makeText(context,"Please Fill All Details",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if (userpass.equals(usercpass)) return true;
            else {
                Toast.makeText(context, "Passwords don't Match", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
    protected void signUp() {
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] image = stream.toByteArray();
        pf=new ParseFile(userfname+"_"+userlname+".png",image);
        Log.d(TAG,"Upload Started");
        pf.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Upload Complete");
                    Log.d(TAG, pf.getUrl());
                    saveToParse();
                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {
                Log.d(TAG, percentDone + "% done");
            }
        });

    }
    private void saveToParse(){
        ParseUser user = new ParseUser();
        user.setUsername(useremail);
        user.setPassword(userpass);
        user.put("First_Name", userfname);
        user.put("Last_Name", userlname);
        user.put("DOB",userdob);
        user.put("Address1",useradd1);
        user.put("Address2",useradd2);
        user.put("State",userstate);
        user.put("City",usercity);
        user.put("BG",userbg);
        user.put("Phone",Long.parseLong(userphone));
        user.put("ProfilePic",pf);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context,
                            "Account is Created, please Fill Medical Details.",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(context, MedicalDetailsActivity.class));
                    finish();
                } else {
                    Toast.makeText(context, "Sign up Error: " + e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, e.toString());
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinState) {
            getCity(parent.getItemAtPosition(position).toString());
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
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            ibChooseProfilePic.setImageBitmap(Bitmap.createScaledBitmap(bitmap,110,110,false));

        }
    }
}
