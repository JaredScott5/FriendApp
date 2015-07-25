package com.android.friendapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.GetCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;


//acts as user's homepage
public class user_profile extends ActionBarActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private Button logout;
    private TextView txtuser;
    private ParseImageView profileImg;
    private String picturePath;
    private ImageButton uQuizButton;
    private ImageButton uUpdateButton;
    private ImageButton uFindButton;
    private TextView aboutMe;
    private TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        // Retrieve current user from Parse.com
        // This is how you get the user from the cache
        // so we don't need to pass the user as a parameter
        ParseUser currentUser = ParseUser.getCurrentUser();
        // Convert currentUser into String
        String struser = currentUser.getUsername().toString();



        // Locate TextView in welcome.xml
        txtuser = (TextView) findViewById(R.id.txtuser);

        // Set the currentUser String into TextView
        txtuser.setText(struser);
        // get the button for the log out
        logout = (Button) findViewById(R.id.logOutB);
        uQuizButton = (ImageButton) findViewById(R.id.quizButton);
        uUpdateButton = (ImageButton) findViewById(R.id.updateButton);
        uFindButton = (ImageButton) findViewById(R.id.findButton);
        profileImg = (ParseImageView) findViewById(R.id.profile_img);
        retrieveImage();
        getUserInfo();

        uQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), questions_page.class);
                startActivity(intent);

            }
        });

        uUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), update_info_activity.class);
                startActivity(intent);

            }
        });

        uFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), friend_suggestion_activity.class);
                startActivity(intent);

            }
        });


        //allow the user to select an image from his gallery
        profileImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
                saveImage();
            }
        });






        // Logout Button Click Listener
        logout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                //kills activity
                finish();
            }
        });

    }

    // query current user and load about me and location data to activity.
    public void getUserInfo() {

        ParseQuery query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    for (ParseObject  ob: objects) {

                        aboutMe = (TextView) findViewById(R.id.aboutText);
                        aboutMe.setText(ob.getString("AboutMe"));
                        //aboutMe.setText("This is a test of the emergency Broadcast system, this is only a test");
                        location = (TextView) findViewById(R.id.locationText);
                        location.setText(ob.getString("location"));
                        //location.setText("Jacksonville, FL");
                    }

                } else {
                    // Something went wrong. Look at the ParseException to see what's up.
                }
            }
        });


    }



    public void retrieveImage() {
        ParseQuery query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    // Locate the column named "ImageName" and set
                    // the string
                    for (ParseObject  ob: objects) {
                        ParseFile fileObject = (ParseFile) ob.getParseFile("profileImg");
                        if(fileObject != null) {
                            profileImg.setParseFile(fileObject);
                            profileImg.loadInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    //nothing to be done
                                }
                            });
                        }
                    }

                } else {
                    // Something went wrong. Look at the ParseException to see what's up.
                }
            }
        });



    }

    public void saveImage() {

        //Store the image inot the database
        //This will prob have to have it's own function
        Bitmap bImage = ((BitmapDrawable)profileImg.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bImage.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] byteArray = stream.toByteArray();
        final ParseFile pImg = new ParseFile("profile.png", byteArray);
        pImg.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //success
                    ParseUser tempUser = ParseUser.getCurrentUser();
                    tempUser.put("profileImg", pImg);
                    tempUser.saveInBackground();
                } else {
                    //failed
                }
            }
        });// end of the saveinbackground
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {
                    MediaStore.Images.Media.DATA
            };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            // String picturePath contains the path of selected Image
            ImageView imageView = (ImageView) findViewById(R.id.profile_img);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
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
