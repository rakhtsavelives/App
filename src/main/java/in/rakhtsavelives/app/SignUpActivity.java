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
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private static int RESULT_LOAD_IMAGE = 1;
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
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        btnNext=(Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,pass,cpass,fname,lname,dob,add1,add2,state,city,bg,phone;
                email=etEmailSignUp.getText().toString();
                pass=etPassSignUp.getText().toString();
                cpass=etCPass.getText().toString();
                fname=etFName.getText().toString();
                lname=etLName.getText().toString();
                dob=etDOB.getText().toString();
                add1=etAddress1.getText().toString();
                add2=etAddress2.getText().toString();
                state=spinState.getSelectedItem().toString();
                city=spinCity.getSelectedItem().toString();
                bg=spinBG.getSelectedItem().toString();
                phone=etPhone.getText().toString();
                if(checkInputes(email,pass,cpass,fname,lname,dob,add1,add2,state,city,bg,phone)) {
                    signUp(email,pass,fname,lname,dob,add1,add2,state,city,bg,phone);
                    finish();
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
        query.whereEqualTo("State",state);
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
    protected boolean checkInputes(String email,String pass,String cpass,String fname,
                                   String lname,String dob,String add1,String add2,
                                   String state,String city,String bg,String phone){
        if(email.isEmpty()|| pass.isEmpty()|| fname.isEmpty()|| lname.isEmpty()
                || dob.isEmpty()|| add1.isEmpty()|| add2.isEmpty()|| phone.isEmpty()
                || state.equals(dState)||city.equals(dCity)||bg.equals(dBG)
                || picturePath==null){
            Toast.makeText(context,"Please Fill All Details",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if (pass.equals(cpass)) return true;
            else {
                Toast.makeText(context, "Passwords don't Match", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
    protected void signUp(String email,String pass,String fname,
                          String lname,String dob,String add1,
                          String add2,String state,String city,
                          String bg, String phone) {
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        ParseFile pf=new ParseFile(fname+"_"+lname+".png",image);
        pf.saveInBackground();
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(pass);
        user.put("First_Name", fname);
        user.put("Last_Name", lname);
        user.put("DOB",dob);
        user.put("Address1",add1);
        user.put("Address2",add2);
        user.put("State",state);
        user.put("City",city);
        user.put("BG",bg);
        user.put("Phone",Long.parseLong(phone));
        user.put("ProfilePic",pf);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context,
                            "Account is Created, please Fill Medical Details.",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(context, MedicalDetailsActivity.class));
                } else {
                    Toast.makeText(context,
                            "Sign up Error: " + e.getMessage(), Toast.LENGTH_LONG)
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

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            ibChooseProfilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}
