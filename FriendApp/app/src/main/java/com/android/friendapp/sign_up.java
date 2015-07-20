package com.android.friendapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class sign_up extends ActionBarActivity {

    private ParseUser user;
    private String email;
    private String password;
    private String v_password;
    private EditText t_email;
    private EditText t_password;
    private EditText verify_pass;
    protected Button mSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        t_email = (EditText)findViewById(R.id.uploadText);
        t_password = (EditText)findViewById(R.id.passText);
        verify_pass = (EditText)findViewById(R.id.passVText);
        mSignupButton = (Button)findViewById(R.id.SignUpB);

        user = new ParseUser();


        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = t_email.getText().toString();
                password = t_password.getText().toString();
                v_password = verify_pass.getText().toString();

                if(password.equals(v_password)) {
                    user.setUsername(email);
                    user.setPassword(password);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {

                            if (e == null) {
                                // let them into the app
                                // If user exist and authenticated, send user to Welcome.class
                                Intent intent = new Intent(sign_up.this, user_profile.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Signup didn't succeed look at error to figure out whats up
                            }
                        }

                    });

                }else {
                    Toast.makeText(getApplicationContext(), "Passwords were different ", Toast.LENGTH_LONG).show();
                }


            }

        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
