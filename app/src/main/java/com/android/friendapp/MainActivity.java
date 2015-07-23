package com.android.friendapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity {

    //private members
    private String email;
    private String password;
    private EditText e_email;
    private EditText e_password;
    private ParseUser currentUser;
    protected Button mLoginButton;
    protected Button mSignupButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = (Button)findViewById(R.id.logIn_B);
        mSignupButton = (Button)findViewById(R.id.signUp_B);
        e_email = (EditText)findViewById(R.id.email_t);
        e_password = (EditText)findViewById(R.id.password_t);

        // Add your initialization code here

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
                            Intent intent = new Intent(MainActivity.this, user_profile.class);//(MainActivity.this, questions_page.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "No such user exists, please signup",
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
