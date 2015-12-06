package in.rakhtsavelives.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText etFName,etLName,etEmailSignUp,etPassSignUp,etCPass,etDOB,etAddress1,etAddress2;
    Spinner spinState,spinCity;
    ArrayAdapter stateAdapter,cityAdapter;
    String email,pass,InputError;
    Button btnNext;
    Context context;
    String[] STATE,CITY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent i=getIntent();
        email=i.getStringExtra("email");
        pass=i.getStringExtra("pass");
        context=getApplicationContext();
        init(email,pass);
    }
    protected void init(String email,String pass){

        STATE=getResources().getStringArray(R.array.states);
        CITY=getResources().getStringArray(R.array.Please_Select_State);

        etEmailSignUp=(EditText)findViewById(R.id.etEmailSignUp);
        etPassSignUp=(EditText)findViewById(R.id.etPassSignUp);
        etCPass=(EditText)findViewById(R.id.etCPass);
        etFName=(EditText)findViewById(R.id.etFName);
        etLName=(EditText)findViewById(R.id.etLName);
        etDOB=(EditText)findViewById(R.id.etDOB);
        etAddress1=(EditText)findViewById(R.id.etAddress1);
        etAddress2=(EditText)findViewById(R.id.etAddress2);

        spinState=(Spinner)findViewById(R.id.spinState);
        stateAdapter=new ArrayAdapter(context,android.R.layout.simple_spinner_item,STATE);
        spinState.setAdapter(stateAdapter);
        spinState.setOnItemSelectedListener(this);

        spinCity=(Spinner)findViewById(R.id.spinCity);
        cityAdapter=new ArrayAdapter(context,android.R.layout.simple_spinner_item,CITY);
        spinCity.setAdapter(cityAdapter);
        spinCity.setOnItemSelectedListener(this);


        btnNext=(Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputes())
                    startActivity(new Intent(context,MedicalDetailsActivity.class));
                else
                    Toast.makeText(context,InputError,Toast.LENGTH_LONG).show();
            }
        });
        /*
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(email,pass,etFName.getText().toString(),etMName.getText().toString(),etLName.getText().toString());
            }
        });*/
    }
    protected boolean checkInputes(){
        return true;
    }
    protected void signUp(String email,String pass,String fname,String mname,String lname){
        if (email.equals("") && pass.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete the sign up form",
                    Toast.LENGTH_LONG).show();

        } else {
            final ParseQuery pq=new ParseQuery("User");

            ParseUser user = new ParseUser();
            user.setUsername(email);
            user.setPassword(pass);
            user.put("First_Name", fname);
            user.put("Middle_Name",mname);
            user.put("Last_Name",lname);
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Show a simple Toast message upon successful registration
                        Toast.makeText(getApplicationContext(),
                                "Successfully Signed up, please log in.",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Sign up Error: "+e.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent==spinState){
            Toast.makeText(context,"States",Toast.LENGTH_SHORT).show();
        }
        if(parent==spinCity){
            Toast.makeText(context,"Cities",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
