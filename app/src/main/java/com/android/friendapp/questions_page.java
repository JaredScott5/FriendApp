package com.android.friendapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseUser;

import static android.widget.Toast.*;

/**
 * Created by jaredscott on 7/17/15.
 * http://www.mkyong.com/android/android-listview-example/
 */
public class questions_page extends Activity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_questions);

        // Get ListView object from xml
        ParseUser user = ParseUser.getCurrentUser();
        listView = (ListView) findViewById(R.id.list);
        // Defined Array values to show in ListView
        String[] values = new String[] {
                "Music",
                "Making Friends",
                "Museums",
                "Movies",
                "Travel",
                "Exercise",
                "Board Games",
                "Sports",
                "Politics",
                "Horoscope"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String  itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                makeText(getApplicationContext(), "Position :" + itemPosition + "  ListItem : "
                        + itemValue, LENGTH_LONG).show();

                //store answer in the database
            }

        });
    }
}
