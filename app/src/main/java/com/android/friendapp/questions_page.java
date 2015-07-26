package com.android.friendapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseQuery;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * Created by jaredscott on 7/17/15.
 * http://www.mkyong.com/android/android-listview-example/
 */
public class questions_page extends Activity {
    private ListView listView;
    private ParseUser user = ParseUser.getCurrentUser();
    private ImageButton uSaveButton;
    private ImageButton uProfileButton;
    private Switch uMusicSwitch;
    private Switch uFriendSwitch;
    private Switch uMuseumSwitch;
    private Switch uMoviesSwitch;
    private Switch uTravelSwitch;
    private Switch uExerciseSwitch;
    private Switch uGamesSwitch;
    private Switch uSportsSwitch;
    private Switch uPoliticsSwitch;
    private Switch uHoroscopeSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        uSaveButton = (ImageButton) findViewById(R.id.saveButton);
        uProfileButton = (ImageButton) findViewById(R.id.profileButton);

        uMusicSwitch = (Switch) findViewById(R.id.musicSwitch);
        uFriendSwitch = (Switch) findViewById(R.id.friendSwitch);
        uMuseumSwitch = (Switch) findViewById(R.id.museumSwitch);
        uMoviesSwitch = (Switch) findViewById(R.id.movieSwitch);
        uTravelSwitch = (Switch) findViewById(R.id.travelSwitch);
        uExerciseSwitch = (Switch) findViewById(R.id.exerciseSwitch);
        uGamesSwitch = (Switch) findViewById(R.id.gameSwitch);
        uSportsSwitch = (Switch) findViewById(R.id.sportsSwitch);
        uPoliticsSwitch = (Switch) findViewById(R.id.politicsSwitch);
        uHoroscopeSwitch = (Switch) findViewById(R.id.horoscopeSwitch);

        //load user's interests and toggle switches to match
        //added by Jamie

       List interestList = user.getList("Interests");

                   if (interestList.contains("Music")) {
                       uMusicSwitch.toggle();
                   }
                    if (interestList.contains("Making Friends")) {
                        uFriendSwitch.toggle();
                    }
                    if (interestList.contains("Museums")) {
                        uMuseumSwitch.toggle();
                    }
                    if (interestList.contains("Movies")) {
                        uMoviesSwitch.toggle();
                    }
                    if (interestList.contains("Travel")) {
                        uTravelSwitch.toggle();
                    }
                    if (interestList.contains("Exercise")) {
                        uExerciseSwitch.toggle();
                    }
                    if (interestList.contains("Board Games")) {
                        uGamesSwitch.toggle();
                    }
                    if (interestList.contains("Sports")) {
                        uSportsSwitch.toggle();
                    }
                    if (interestList.contains("Politics")) {
                        uPoliticsSwitch.toggle();
                    }
                    if (interestList.contains("Horoscope")) {
                        uHoroscopeSwitch.toggle();
                    }


        //when save button is clicked, remove old interests, and save new ones to database
        //depending on answers - added by Jamie
        uSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.remove("Interests");

                if (uMusicSwitch.isChecked()) {
                    user.addUnique("Interests", "Music");
                    user.saveEventually();
                }
                if (uFriendSwitch.isChecked()) {
                    user.addUnique("Interests", "Making Friends");
                    user.saveEventually();
                }
                if (uMuseumSwitch.isChecked()) {
                    user.addUnique("Interests", "Museums");
                    user.saveEventually();
                }
                if (uMoviesSwitch.isChecked()) {
                    user.addUnique("Interests", "Movies");
                    user.saveEventually();
                }
                if (uTravelSwitch.isChecked()) {
                    user.addUnique("Interests", "Travel");
                    user.saveEventually();
                }
                if (uExerciseSwitch.isChecked()) {
                    user.addUnique("Interests", "Exercise");
                    user.saveEventually();
                }
                if (uGamesSwitch.isChecked()) {
                    user.addUnique("Interests", "Board Games");
                    user.saveEventually();
                }
                if (uSportsSwitch.isChecked()) {
                    user.addUnique("Interests", "Sports");
                    user.saveEventually();
                }
                if (uPoliticsSwitch.isChecked()) {
                    user.addUnique("Interests", "Politics");
                    user.saveEventually();
                }
                if (uHoroscopeSwitch.isChecked()) {
                    user.addUnique("Interests", "Horoscope");
                    user.saveEventually();
                }

                Toast.makeText(getApplicationContext(),
                        "Your Interests Have Been Saved.",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Switch back to profile page if user clicks button
        uProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), user_profile.class);
                startActivity(intent);
            }
        });



    }
}
