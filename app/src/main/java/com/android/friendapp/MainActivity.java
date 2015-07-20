package com.android.friendapp;

import com.android.friendapp.ParseDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends ActionBarActivity {

    //private members
    private String email;
    private String password;
    private EditText e_email;
    private EditText e_password;
    private ParseUser currentUser;
    protected Button mLoginButton;
    protected Button mSignupButton;

    //NOTE - The Parse Database uses ParseUser datatype to interact with the user
    //data table it's no different than what the ParseObject is like except
    //it uses a few different functions if we use ParseObject as our
    //data collector for this we will just create another table
    //TODO ask Jared which option he would like
    //TODO ask Jared what how he would like to tackle the email versus username issue
    //Possible solution add the email to both fields (since we cannot delete either field)
    //TODO Userid function call


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = (Button)findViewById(R.id.logIn_B);
        mSignupButton = (Button)findViewById(R.id.signUp_B);
        e_email = (EditText)findViewById(R.id.email_t);
        e_password = (EditText)findViewById(R.id.password_t);

        // Add your initialization code here
        Parse.initialize(this, "FE9hqhrX9u82m5rrx2U9G0uRSbsa3RSLMO3N71CQ",
                "Xbtv0h3ETBRz5ahqEqu2b0kDH6mFXWkGXama0dOM");

        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        //initialize this after we have initilized everything else
        //otherwise it throws a runtime error
        currentUser = new ParseUser();

        //I can now create users and add them to the database next I will retrive them
        //and cross reference them to the information they are providing
        //this is temp solution just for us to start pushing data into database and
        // tieing data together

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), sign_up.class);
                startActivity(intent);

            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
              // Check to see if they provided proper credentials to log into the
              // Account they are claiming to be once they do send them to their profile
              // view so they can see their information

                email = e_email.getText().toString();
                password = e_password.getText().toString();

                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            // If user exist and authenticated, send user to user_profile.class
                            // start the intent and sign them in
                            // Finish allows you to kill an activity if it is no longer needed
                            Intent intent = new Intent(MainActivity.this, user_profile.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "No such user exist, please signup",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
