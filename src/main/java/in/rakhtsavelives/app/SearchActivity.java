package in.rakhtsavelives.app;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchListView = (ListView) findViewById(R.id.listView);
        searchList = new ArrayList<ListViewItem>();
        searchAdapter = new ListViewAdapter(this, searchList);
        searchListView.setAdapter(searchAdapter);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        Intent i = getIntent();
        String searchName = i.getStringExtra("name");
        query.whereContains("First_Name", searchName);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    ListViewItem item;
                    for (int i = 0; i < objects.size(); i++) {
                        item = new ListViewItem
                                (objects.get(i).getString("First_Name") + " " + objects.get(i).getString("Last_Name"),
                                        objects.get(i).getString("BG"),
                                        objects.get(i).getString("City"));
                        searchList.add(item);
                    }
                    searchAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "No records found!", Toast.LENGTH_LONG).show();
                    Log.e(InitClass.TAG, e.toString());
                }
            }
        });
    }
}
