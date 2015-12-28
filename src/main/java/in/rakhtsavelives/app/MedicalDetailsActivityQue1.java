package in.rakhtsavelives.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class MedicalDetailsActivityQue1 extends AppCompatActivity {

    Button btnNextQue1;
    CheckBox[] que1;
    private int[] QUE_1_IDS =
            {
                    R.id.cbQue1_1,
                    R.id.cbQue1_2,
                    R.id.cbQue1_3,
                    R.id.cbQue1_4,
                    R.id.cbQue1_5,
                    R.id.cbQue1_6,
                    R.id.cbQue1_7,
                    R.id.cbQue1_8,
                    R.id.cbQue1_9,
                    R.id.cbQue1_10,
            };
    ParseUser user;
    Intent i;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_details_que1);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        user = ParseUser.getCurrentUser();
        que1 = new CheckBox[QUE_1_IDS.length];
        for (int i = 0; i < QUE_1_IDS.length; i++) {
            que1[i] = (CheckBox) findViewById(QUE_1_IDS[i]);
        }
        btnNextQue1 = (Button) findViewById(R.id.btnNextQue1);
        btnNextQue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                ArrayList que1SelectedOptions=InitClass.getChecked(que1);
                if(que1SelectedOptions.size()==0){
                    que1SelectedOptions.add("FALSE");
                    i=new Intent(getApplicationContext(),MedicalDetailsActivityQue2.class);
                }
                else {
                    i=new Intent(getApplicationContext(),TabActivity.class);
                }
                user.put("QUE1", que1SelectedOptions);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null) {
                            startActivity(i);
                            finish();
                            dialog.dismiss();
                        }
                        else {
                            Log.e(InitClass.TAG,"Medical Details 1",e);
                        }
                    }
                });
            }
        });
    }
}
