package com.android.friendapp;

import android.content.Intent;
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

    private TextView txtuser;
    private ImageView profileImg;
    private Integer imageId;
    private ImageButton fb;
    private ImageButton li;
    private ImageButton wa;

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
        // Set the currentUser String into TextView
        txtuser.setText("FriendName's profile");
        // get the button for the log out
        Intent myIntent = getIntent();
        imageId = myIntent.getIntExtra("imageId", 0);
        profileImg = (ImageView) findViewById(R.id.profile_img);
        profileImg.setImageResource(imageId);

        fb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);

                myWebLink.setData(Uri.parse("http://www.facebook.com"));
                startActivity(myWebLink);
            }
        });

        li.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);

                myWebLink.setData(Uri.parse("http://www.linkedin.com"));
                startActivity(myWebLink);
            }
        });

        wa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);

                myWebLink.setData(Uri.parse("http://www.whatsapp.com"));
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