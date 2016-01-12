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

public class MedicalDetailsActivityQue3 extends AppCompatActivity {

    Button btnNextQue3;
    CheckBox[] que3;
    private int[] QUE_3_IDS =
            {
                    R.id.cbQue3_1,
                    R.id.cbQue3_2,
                    R.id.cbQue3_3,
                    R.id.cbQue3_4,
                    R.id.cbQue3_5,
                    R.id.cbQue3_6,
                    R.id.cbQue3_7,
                    R.id.cbQue3_8,
                    R.id.cbQue3_9
            };
    ParseUser user;
    Intent i;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_details_que3);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        user = ParseUser.getCurrentUser();
        que3 = new CheckBox[QUE_3_IDS.length];
        for (int i = 0; i < QUE_3_IDS.length; i++) {
            que3[i] = (CheckBox) findViewById(QUE_3_IDS[i]);
        }
        btnNextQue3 = (Button) findViewById(R.id.btnNextQue3);
        btnNextQue3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                ArrayList que3SelectedOptions = InitClass.getChecked(que3);
                if (que3SelectedOptions.size() == 0) {
                    que3SelectedOptions.add("FALSE");
                    i = new Intent(getApplicationContext(), MedicalDetailsActivityQue4.class);
                } else {
                    i = new Intent(getApplicationContext(), TabActivity.class);
                }
                if (que3SelectedOptions.get(0) != "FALSE") {
                    user.put("CANT_DONATE_FOR_6_MON", true);
                    user.put("CANT_DONATE_FOR_3_MONTHS", true);
                }
                else
                    user.put("CANT_DONATE_FOR_6_MON", false);
                user.put("QUE3", que3SelectedOptions);
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
