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

public class MedicalDetailsActivityQue4 extends AppCompatActivity {

    Button btnNextQue4;
    CheckBox[] que4;
    private int[] QUE_4_IDS =
            {
                    R.id.cbQue4_1,
                    R.id.cbQue4_2,
                    R.id.cbQue4_3,
                    R.id.cbQue4_4,
            };
    ParseUser user;
    Intent i;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_details_que4);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        user = ParseUser.getCurrentUser();
        que4 = new CheckBox[QUE_4_IDS.length];
        for (int i = 0; i < QUE_4_IDS.length; i++) {
            que4[i] = (CheckBox) findViewById(QUE_4_IDS[i]);
        }
        btnNextQue4 = (Button) findViewById(R.id.btnNextQue4);
        btnNextQue4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                ArrayList que4SelectedOptions = InitClass.getChecked(que4);
                if (que4SelectedOptions.size() == 0) {
                    que4SelectedOptions.add("FALSE");
                }
                i = new Intent(getApplicationContext(), TabActivity.class);
                user.put("QUE4", que4SelectedOptions);
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
