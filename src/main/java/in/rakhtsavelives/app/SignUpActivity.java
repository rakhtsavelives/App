package in.rakhtsavelives.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etFName,etLName,etEmailSignUp,etPassSignUp,etCPass,etDOB,etAddress1,etAddress2,etPhone;
    Spinner spinState,spinCity,spinBG;
    ArrayAdapter stateAdapter,cityAdapter,bgAdapter;
    String email,pass,dState,dCity,dBG;
    Button btnNext;
    Context context;
    String[] STATE,CITY,BG;
    int day,month,year;
    DatePickerDialog dpd;
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

        STATE=getResources().getStringArray(R.array.states);
        CITY=getResources().getStringArray(R.array.pss);
        BG=getResources().getStringArray(R.array.bg);

        etEmailSignUp=(EditText)findViewById(R.id.etEmailSignUp);
        etPassSignUp=(EditText)findViewById(R.id.etPassSignUp);
        etCPass=(EditText)findViewById(R.id.etCPass);
        etFName=(EditText)findViewById(R.id.etFName);
        etLName=(EditText)findViewById(R.id.etLName);
        etDOB=(EditText)findViewById(R.id.etDOB);
        etAddress1=(EditText)findViewById(R.id.etAddress1);
        etAddress2=(EditText)findViewById(R.id.etAddress2);
        etPhone=(EditText)findViewById(R.id.etPhone);

        /*dpd=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        },year,month,day);

        etDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {

                }
            }
        });*/

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

        dState=spinState.getSelectedItem().toString();
        dCity=spinCity.getSelectedItem().toString();
        dBG=spinBG.getSelectedItem().toString();

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


    private DatePickerDialog.OnDateSetListener dateListener= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = dayOfMonth + "-" + monthOfYear + "-" + year;
            etDOB.setText(date);
        }
    };

    protected boolean checkInputes(String email,String pass,String cpass,String fname,
                                   String lname,String dob,String add1,String add2,
                                   String state,String city,String bg,String phone){
        if(email.isEmpty()|| pass.isEmpty()|| fname.isEmpty()|| lname.isEmpty()
                || dob.isEmpty()|| add1.isEmpty()|| add2.isEmpty()|| phone.isEmpty()
                || state.equals(dState)||city.equals(dCity)||bg.equals(dBG)){
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
                    Log.e("Rakht_ERROR:", e.toString());
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinState) {
            switch (parent.getItemAtPosition(position).toString()) {
                case "Gujarat":
                    CITY = getResources().getStringArray(R.array.guj);
                    break;
                case "Maharashtra":
                    CITY = getResources().getStringArray(R.array.mah);
                    break;
                case "Please Select State":
                    CITY = getResources().getStringArray(R.array.pss);
                    break;
            }
            cityAdapter = new ArrayAdapter(context, R.layout.spinner_item, CITY);
            spinCity.setAdapter(cityAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
