package com.android.friendapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;


public class update_info_activity extends FragmentActivity implements
        DatePickerDialogListener {

    Button saveAndUpdate, uploadPhoto;
    ImageButton uProfileButton2;
    EditText aboutMe, userName, location;
    ParseUser user;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        //create the buttons and EditTexts
        saveAndUpdate = (Button) findViewById(R.id.updateButton);
        uploadPhoto = (Button) findViewById(R.id.uploadPhotoButton);
        uProfileButton2 = (ImageButton) findViewById(R.id.profileButton);
        aboutMe = (EditText) findViewById(R.id.uploadAbout);
        userName = (EditText)findViewById(R.id.uploadName);
        location = (EditText)findViewById(R.id.uploadLocation);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        user = ParseUser.getCurrentUser();
        saveAndUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("", "aboutMe == " + aboutMe.getText().toString() + " and username is "
                + userName.getText().toString() + "and user is " + user.toString());
                
                if(!(aboutMe.getText().equals(null))) {
                    user.put("AboutMe", aboutMe.getText().toString());
                }

                if(!(userName.getText().equals(null))) {
                    user.put("username", userName.getText().toString());
                }

                if(!(userName.getText().equals(null))) {
                    user.put("Location", location.getText().toString());
                }

                user.saveInBackground();
                Toast.makeText(getApplicationContext(),
                        "Your Profile Changes Have Been Saved.",
                        Toast.LENGTH_LONG).show();
            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
                saveImage();
            }
        });

        // Switch back to profile page if user clicks button
        uProfileButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), user_profile.class);
                startActivity(intent);
            }
        });
    }

    public void saveImage() {

        //Store the image inot the database
        //This will prob have to have it's own function
       // Bitmap bImage = ((BitmapDrawable)profileImg.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //bImage.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] byteArray = stream.toByteArray();
        final ParseFile pImg = new ParseFile("profile.png", byteArray);
        pImg.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //success
                    user = ParseUser.getCurrentUser();
                    user.put("profileImg", pImg);
                    user.saveInBackground();
                } else {
                    //failed
                }
            }
        });// end of the saveinbackground
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
