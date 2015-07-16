package com.android.friendapp;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jaredscott on 7/15/15.
 * Added libs to connect Parse - removed Facebook Authentication
 * due to causing errors.
 */
public class ParseDatabase extends Application {

    private ParseObject retrievedObject,
            newUser,
            photo,
            music;
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
                } else {
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
                if (e == null) {
                    if (!username.equals(null))
                        newUser.put("username", username);

                    if (!pw.equals(null))
                        newUser.put("password", pw);

                    if (!email.equals(null))
                        newUser.put("email", email);
                } else {
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
                    if (e == null) {
                        //parseObject will be the User
                        retrievedObject = parseObject;
                    } else {
                        //this is bad
                        System.out.println("Error in public void retrieveUser. User not found");
                    }
                }
            });
    }

    //delete user
    //user must first be logged in
    public void deleteUser(){
        newUser.deleteInBackground();
    }

    public void addTrack(String song){

    }

    public void addPicture(String imageNumber, FileInputStream image){
    //read the bytes from File inot a byte[] and put it into the json
        InputStream is;
        byte[] imageData;
        ParseFile file;

        try {
            is = new BufferedInputStream(new FileInputStream(image.toString()));
            imageData = IOUtils.toByteArray(is);
            file = new ParseFile("image" + imageNumber + ".jpg", imageData);
            file.saveInBackground();

            //now that it has been saved, we need to associate this file with the User object
            newUser.put("image" + imageNumber, file);
            newUser.saveEventually();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //retrive image from a specified user
    //https://www.parse.com/questions/retrieve-image-from-parse
    public Bitmap retriveImage(final String imageNumber, String userId){

        final Bitmap[] image = new Bitmap[1];
        ParseQuery query = new ParseQuery("ImageTester");
        query.getInBackground(userId, new GetCallback() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("test", "The object was not found...");
                } else {
                    Log.d("test", "Retrieved the object.");
                    ParseFile fileObject = (ParseFile) object.get("image" + imageNumber);

                    fileObject.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                Log.d("test", "We've got data in data.");
                                // use data for something
                                image[0] = BitmapFactory.decodeByteArray(data, 0, data.length);
                            } else {
                                Log.d("test", "There was a problem downloading the data.");
                            }
                        }
                    });
                }
            }
        });
        return image[0];
    }
}
