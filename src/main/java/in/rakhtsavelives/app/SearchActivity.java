package in.rakhtsavelives.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ArrayList<ListViewItem> searchList;
    ListViewAdapter searchAdapter;
    ListView searchListView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Searching..\nPlease wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        searchListView = (ListView) findViewById(R.id.listView);
        searchList = new ArrayList<ListViewItem>();
        searchAdapter = new ListViewAdapter(this, searchList);
        searchListView.setAdapter(searchAdapter);
        Intent i = getIntent();
        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        String searchName = i.getStringExtra("name");
        query.whereMatches("First_Name", searchName, "i");
        new Thread() {
            @Override
            public void run() {
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            ListViewItem item;
                            for (int i = 0; i < objects.size(); i++) {
                                item = new ListViewItem
                                        (objects.get(i).getString("First_Name") + " " + objects.get(i).getString("Last_Name"),
                                                "BG: "+objects.get(i).getString("BG"),
                                                objects.get(i).getString("City"),
                                                "Age: "+objects.get(i).getString("Age"));
                                searchList.add(item);
                            }
                            if (objects.size() == 0)
                                searchList.add(new ListViewItem("No Records Found!", "", "", ""));
                            searchAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "No records found!", Toast.LENGTH_LONG).show();
                            searchList.add(new ListViewItem("No Records Found!", "", "", ""));
                            searchAdapter.notifyDataSetChanged();
                            Log.e(InitClass.TAG, e.toString());
                        }
                        dialog.dismiss();
                    }
                });
            }
        }.start();
    }
}
