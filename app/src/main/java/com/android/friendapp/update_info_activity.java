package com.android.friendapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

import java.util.Calendar;


public class update_info_activity extends FragmentActivity implements
        DatePickerDialogListener {

    Button saveAndUpdate, uploadPhoto;
    EditText aboutMe, userName;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        //create the buttons and EditTexts
        saveAndUpdate = (Button)findViewById(R.id.updateButton);
        uploadPhoto = (Button)findViewById(R.id.uploadPhotoButton);
        aboutMe = (EditText) findViewById(R.id.uploadAbout);
        userName = (EditText)findViewById(R.id.uploadName);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        user = ParseUser.getCurrentUser();
        saveAndUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("", "aboutMe == " + aboutMe.getText().toString() + " and username is "
                + userName.getText().toString());
                
                if(!(aboutMe.getText().equals(null))) {
                    user.put("AboutMe", aboutMe.getText().toString());
                }

                if(!(userName.getText().equals(null))) {
                    user.put("username", userName.getText().toString());
                }

                user.saveEventually();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        Log.v("Calender", newFragment.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
