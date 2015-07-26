package com.android.friendapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class friend_info_activity extends ActionBarActivity {

    private TextView txtuser, aboutme, locate;
    private ImageView profileImg;
    private Bitmap image;
    private ImageButton fb;
    private ImageButton li;
    private ImageButton wa;
    private String about, linkedin, facebook, whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);


        // Retrieve current user from Parse.com
        // This is how you get the user from the cache
        // so we don't need to pass the user as a parameter
        //ParseUser currentUser = ParseUser.getCurrentUser();
        // Convert currentUser into String
        //String struser = currentUser.getUsername().toString();
        // Locate TextView in welcome.xml
        txtuser = (TextView) findViewById(R.id.txtuser);
        fb = (ImageButton)findViewById(R.id.faceBook);
        li = (ImageButton)findViewById(R.id.linkedIn);
        wa = (ImageButton)findViewById(R.id.whatsapp);
        locate = (TextView) findViewById(R.id.locationText);
        aboutme = (TextView) findViewById(R.id.descriptionText);
        profileImg = (ImageView) findViewById(R.id.profile_img);

        // get the button for the log out
        Intent myIntent = getIntent();
        // Set the currentUser String into TextView
        txtuser.setText("Profile of " + myIntent.getStringExtra("name"));
        aboutme.setText(myIntent.getStringExtra("about"));
        locate.setText(myIntent.getStringExtra("place"));
        byte[] imageByte = myIntent.getByteArrayExtra("imageId");
        if (imageByte.length > 1) {
            image = BitmapFactory.decodeByteArray(
                    imageByte, 0, imageByte.length);
            profileImg.setImageBitmap(image);
        }
        else profileImg.setImageResource(R.drawable.no_image);

        if (myIntent.getStringExtra("facebook") != null) {
            if (myIntent.getStringExtra("facebook").equals("No Info")) {
                facebook = "http://www.facebook.com";
            } else facebook = myIntent.getStringExtra("facebook");
        }
        else facebook = "http://www.facebook.com";

        if (myIntent.getStringExtra("linkedin") != null) {
            if (myIntent.getStringExtra("linkedin").equals("No Info")) {
                linkedin = "http://www.linkedin.com";
            } else linkedin = myIntent.getStringExtra("linkedin");
        }
        else linkedin = "http://www.linkedin.com";

        if (myIntent.getStringExtra("whatsapp") != null) {
            if (myIntent.getStringExtra("whatsapp").equals("No Info")) {
                whatsapp = "http://www.whatsapp.com";
            } else whatsapp = myIntent.getStringExtra("whatsapp");
        }
        else whatsapp = "http://www.whatsapp.com";

        fb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);

                myWebLink.setData(Uri.parse(facebook));
                startActivity(myWebLink);
            }
        });

        li.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);

                myWebLink.setData(Uri.parse(linkedin));
                startActivity(myWebLink);
            }
        });

        wa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);

                myWebLink.setData(Uri.parse(whatsapp));
                startActivity(myWebLink);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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