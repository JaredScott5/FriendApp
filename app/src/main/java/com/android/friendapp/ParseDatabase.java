package com.android.friendapp;

import android.app.Application;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by jaredscott on 7/15/15.
 * Added libs to connect Parse - removed Facebook Authentication
 * due to causing errors.
 */
public class ParseDatabase extends Application {

    private ParseObject retrievedObject;
    private ParseObject newUser;
    private ParseQuery<ParseObject> query;
    @Override
    public void onCreate(){
        super.onCreate();

        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "FE9hqhrX9u82m5rrx2U9G0uRSbsa3RSLMO3N71CQ", "Xbtv0h3ETBRz5ahqEqu2b0kDH6mFXWkGXama0dOM");

        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

    //TODO remind everyone to use this with log-out
    public void logOut(){
        newUser.unpinInBackground();
    }

    //TODO determine if we need to 'put' authData and ACL
    public void createNewuser(String username, String pw, String email){
        newUser = new ParseObject("User");
        newUser.put("username", username);
        newUser.put("password", pw);
        newUser.put("email", email);
        newUser.saveEventually();
    }

    //TODO this needs to be tested or another alternative needs to be found
    public void deleteUser(String userID){
        query = ParseQuery.getQuery("User");
        query.getInBackground(userID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.deleteInBackground();
                }else{
                    System.out.println("Error: deleteUser has failed");
                }
            }
        });
    }

    //if you only want to usdate one value, make the rest null
    public void updateUser(String userID, final String username, final String pw, final String email){
        query = ParseQuery.getQuery("User");
        query.getInBackground(userID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                   if(!username.equals(null))
                       newUser.put("username", username);

                    if(!pw.equals(null))
                        newUser.put("password", pw);

                    if(!email.equals(null))
                        newUser.put("email", email);
                }else{
                    System.out.println("Error: updateUser has failed");
                }
            }
        });
    }

    public void retrieveUser(String objectID){
            query = ParseQuery.getQuery("User");
            query.getInBackground(objectID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if( e == null){
                    //parseObject will be the User
                    retrievedObject = parseObject;
                }else{
                    //this is bad
                    System.out.println("Error in public void retrieveUser. User not found");
                }
            }
        });
    }
}
