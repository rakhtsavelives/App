package in.rakhtsavelives.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class MedicalDetailsActivityQue2 extends AppCompatActivity {

    Button btnNextQue2;
    CheckBox[] que2;
    private int[] QUE_2_IDS =
            {
                    R.id.cbQue2_1,
                    R.id.cbQue2_2,
                    R.id.cbQue2_3,
                    R.id.cbQue2_4,
                    R.id.cbQue2_5,
                    R.id.cbQue2_6,
                    R.id.cbQue2_7,
            };
    ParseUser user;
    Intent i;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_details_que2);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        user = ParseUser.getCurrentUser();
        que2 = new CheckBox[QUE_2_IDS.length];
        for (int i = 0; i < QUE_2_IDS.length; i++) {
            que2[i] = (CheckBox) findViewById(QUE_2_IDS[i]);
        }
        btnNextQue2 = (Button) findViewById(R.id.btnNextQue2);
        btnNextQue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                ArrayList que2SelectedOptions = InitClass.getChecked(que2);
                if (que2SelectedOptions.size() == 0) {
                    que2SelectedOptions.add("FALSE");
                    i = new Intent(getApplicationContext(), MedicalDetailsActivityQue3.class);
                } else {
                    i = new Intent(getApplicationContext(), TabActivity.class);
                }
                if (que2SelectedOptions.get(0) != "FALSE") {
                    user.put("CANT_DONATE_FOR_1_YEAR", true);
                    user.put("CANT_DONATE_FOR_6_MON", true);
                    user.put("CANT_DONATE_FOR_3_MONTHS", true);
                }
                else
                    user.put("CANT_DONATE_FOR_1_YEAR", false);
                user.put("QUE2", que2SelectedOptions);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        startActivity(i);
                        finish();
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
