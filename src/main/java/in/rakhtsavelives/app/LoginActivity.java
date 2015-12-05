package in.rakhtsavelives.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.*;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etEmail,etPass;
    TextView tvRegister;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=getApplicationContext();
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPass=(EditText)findViewById(R.id.etPassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        tvRegister=(TextView)findViewById(R.id.tvRegister);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email=etEmail.getText().toString();
                String pass=etPass.getText().toString();
                login(email,pass);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etEmail.getText().toString();
                String pass=etPass.getText().toString();
                Intent i = new Intent(getApplicationContext(),SignUpActivity.class);
                i.putExtra("email",email);
                i.putExtra("pass",pass);
                startActivity(i);
            }
        });
    }

    protected void login(String email,String pass){
        ParseUser.logInInBackground(email, pass,
                new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent intent = new Intent(
                                    LoginActivity.this,
                                    WelcomeActivity.class);
                            intent.putExtra("user",user.getUsername());
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),
                                    "Successfully Logged in",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "No such user exist, please signup",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
