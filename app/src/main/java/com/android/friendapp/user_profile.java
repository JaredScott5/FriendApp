package com.android.friendapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.MediaStore;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.parse.ParseUser;

//acts as user's homepage
public class user_profile extends ActionBarActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private Button logout;
    private TextView txtuser;
    private ImageView profileImg;

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
        txtuser.setText("You are logged in as " + struser);
        // get the button for the log out
        logout = (Button) findViewById(R.id.logOutB);
        profileImg = (ImageView) findViewById(R.id.profile_img);

        profileImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
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
