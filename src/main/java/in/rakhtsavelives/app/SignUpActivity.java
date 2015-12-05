package in.rakhtsavelives.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    EditText etFName,etMName,etLName;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent i=getIntent();
        final String email=i.getStringExtra("email");
        final String pass=i.getStringExtra("pass");
        etFName=(EditText)findViewById(R.id.etFName);
        etMName=(EditText)findViewById(R.id.etMName);
        etLName=(EditText)findViewById(R.id.etLName);
        btnSignUp=(Button)findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(email,pass,etFName.getText().toString(),etMName.getText().toString(),etLName.getText().toString());
            }
        });
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
}
