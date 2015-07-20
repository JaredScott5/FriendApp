package com.android.friendapp;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by jaredscott on 7/15/15.
 * Added libs to connect Parse - removed Facebook Authentication
 * due to causing errors.
 */
public class ParseDatabase extends ActionBarActivity {

    private ParseQuery<ParseObject> query;
    private List<ParseObject> userData;

    //the following variables are for testing
    private Button upload, play;
    private EditText songPath, songNumber;
    private ParseUser testUser;
    private String songURL, trackNumber;

    //this is for testng purposes
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_test_parse_database);

       // upload = (Button)findViewById(R.id.uploadSong);
       // play = (Button)findViewById(R.id.playSongButton);
        songPath = (EditText) findViewById(R.id.uploadText);
        songNumber = (EditText)findViewById(R.id.passText);
        testUser = new ParseUser();

        //make it store the song
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //songURL = songPath.getText().toString();
                songURL = "/Users/jaredscott/Downloads/win.mp3";
                storeSong("Jared", songURL);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //songURL = songPath.getText().toString();
                trackNumber = "song" + songNumber.toString() + ".mp3";
                retriveSong("Jared", "1");
            }
        });
    }

    //TODO remind everyone to use this with log-out
    public void logOut(ParseUser user){
        user.unpinInBackground();
    }

    //search for a user object. Once we have the object, we have access to all of its values
    /**
     * @param email
     * @return
     */
    public List<ParseObject> findUser(String email){
        query = ParseQuery.getQuery("User");
        query.whereEqualTo("email", email);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                if (e == null) {
                    Log.d("User", "Retrieved" + list.toString());
                    userData = list;
                } else {
                    Log.d("User", "Error: " + e.getMessage());
                }
            }
        });
        return userData;
    }

    //this creates a new music entry in Music database after checking that there is room
    public void storeSong(String email, String pathToSong){

        //first see if the user already has 3 songs
        int size = 0;
        try {
            query = ParseQuery.getQuery("Music");
            query.whereEqualTo("userEmail", email);
            query.orderByAscending("songNumber");
             size = query.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //if == 3, then the user need to delete a song, otherwise, keep going
        if(size < 3) {
            String songNumber = String.valueOf(size);
            FileInputStream fileInputStream = null;
            File fileObj = new File(pathToSong);
            byte[] data = new byte[(int) fileObj.length()];
            ParseFile file = new ParseFile("song.mp3", data);

            file.saveInBackground();

            ParseObject newMusicObj = new ParseObject("Music");

            newMusicObj.put("userEmail", email);
            newMusicObj.put("song" + songNumber, file);

           try {
                //convert file into array of bytes
                fileInputStream = new FileInputStream(fileObj);
                fileInputStream.read(data);
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ParseFile pfile = new ParseFile(pathToSong.toString(), data);
            pfile.saveInBackground();
        }else{
            Log.d("Music", "Error: There are already 3 songs in the database for this user: " + email);
        }
    }

    public List<ParseObject> retriveSong(String email, String songNumber){
        query = ParseQuery.getQuery("Music");
        query.whereEqualTo("userEmail", email);
        query.whereEqualTo("songNumber", songNumber);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                if (e == null) {
                    Log.d("Music", "Music Track Retrieved" + list.toString());
                    userData = list;
                } else {
                    Log.d("User", "Error: " + e.getMessage());
                }
            }
        });
        return userData;
    }

    //TODO go over this one
    public void addPicture(String imageNumber, FileInputStream image, ParseUser user, ParseUser photo){
    //read the bytes from File inot a byte[] and put it into the json
        InputStream is;
        byte[] imageData;
        ParseFile file;

        try {
            is = new BufferedInputStream(new FileInputStream(image.toString()));
            imageData = IOUtils.toByteArray(is);
            file = new ParseFile("image" + ".jpg", imageData);
            file.saveInBackground();

            //now that it has been saved, we need to associate this file with the User object
            photo.put("picture", file);
            photo.put("User", user);
            photo.put("imageNumber", imageNumber);
            photo.saveEventually();
           // newUser.put("image" + imageNumber, file);
            //newUser.saveEventually();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //retrive image from a specified user
    //https://www.parse.com/questions/retrieve-image-from-parse
  /*  public Bitmap retriveImage(final String imageNumber, String userId){

        final Bitmap[] image = new Bitmap[1];
        ParseQuery query = new ParseQuery("ImageTester");
        query.getInBackground(userId, new GetCallback() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("test", "The object was not found...");
                } else {
                    Log.d("test", "Retrieved the object.");
                    ParseFile fileObject = (ParseFile) object.get("image" + imageNumber);

                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
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
    }*/
}
