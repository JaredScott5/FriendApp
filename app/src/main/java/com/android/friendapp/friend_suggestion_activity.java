/*referenced http://www.sitepoint.com/handling-displaying-images-android/
* */

package com.android.friendapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class friend_suggestion_activity extends ActionBarActivity{

    private ParseUser user = ParseUser.getCurrentUser();
    private List userList, commonList, comparison;
    private List <ParseUser> similarUsers;
    private Bitmap currentBm = null;
    private List <byte[]> imageArray = new ArrayList <byte[]>();
    private ImageButton currentButton;
    private  List<ImageButton> theViews;

    public void findUsersInCommon(){
        Log.v("findUsersInCommon", "starting...");
        commonList = new ArrayList<>();
        similarUsers = new ArrayList<ParseUser>();

        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereExists("Interests");//.whereEqualTo("Interests", "female");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    Log.v("findUsersInCommon", "query successful...");

                    userList = (List)user.get("Interests");
                    double userSize = userList.size() - 1;
                    Log.v("findUsersInCommon", "size of userSize is " + userSize + "and the size of the objects to compare is " + objects.size());
                    for (ParseUser o : objects)
                    {
                        similarUsers.add(o);
                        Log.v("findUsersInCommon", "something added to similarUsers");
                    }
                    //do this loop for each user in userList
                   for(int i = objects.size() - 1; i > 0; --i){
                        //start with the first item in objects and place it's list into comparison
                        comparison = objects.get(i).getList("Interests");

                        //this is what we use for iterations
                        int iterations = (int) userSize;
                        int percentage = (int) userSize;

                        //see if each item in userList is in comparison
                        Log.v("findUsersInCommon", "beginning first while loop...");

                        while(iterations > 0){
                            if( !comparison.contains(userList.get(iterations)))
                                --percentage;

                            --iterations;
                        }

                        Log.v("findUsersInCommon", "first while loop finished...");

                        //if the percentage is >= 50%, add it to the common list
                        if(percentage/userSize >= .5)
                            commonList.add(comparison);
                            similarUsers.add(objects.get(i));
}//end compairson for loop

                    //now make sure that the list of objects in common is not over 5
                    if(similarUsers.size() > 5){
                        Log.v("findUsersInCommon", "similarUserList size is " + similarUsers.size());

                        while(similarUsers.size() > 5){
                            Random rand = new Random();
                          //  int random = rand.nextInt((commonList.size() - 1) + 1);//it will never choose a number greater than commonLists' size
                            similarUsers.remove(rand.nextInt((similarUsers.size() - 1) + 1));
                        }
                    }
                    addImagesToThegallery();

                } else {
                    // Something went wrong.
                }
            }
        });
    }

    //added by jared
    //modified by Harley 7/25

    public void retrieveImage(Integer a, ImageButton ib) {
        Log.v("retrieveImage", "RetrieveImage function called");
        ParseFile fileObject = (ParseFile) similarUsers.get(a).get("profileImg");
        currentButton = ib;
        if (fileObject != null) {
            fileObject.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Log.v("retrieveImage", "We've got data in data");
                        //Decode the Byte[] into Bitmap
                        currentButton.setImageBitmap(BitmapFactory.decodeByteArray(
                                data, 0, data.length));
                        imageArray.add(data);
                    } else {
                        Log.v("retrieveImage", "There was a problem downloading the image");
                        currentButton.setImageResource(R.drawable.no_image);
                        byte[] ni = new byte[1];
                        imageArray.add(ni);
                    }

                }
            });
        }
        else
        {
            ib.setImageResource(R.drawable.no_image);
            byte[] ni = new byte[1];
            imageArray.add(ni);
        }
    }
    //added by Harley 7/25
    //code from http://www.androidbegin.com/tutorial/android-parse-com-image-download-tutorial/

    public String checkForNull (String t, Integer b){
        if (similarUsers.get(b).get(t) != null)
        {
            if (similarUsers.get(b).get(t).toString() != null)
            {
                return similarUsers.get(b).get(t).toString();
            }
            else return "No Info";
        }
        else return "No Info";
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //added by jared
        findUsersInCommon();



        setContentView(R.layout.activity_friend_suggestion);
        //addImagesToThegallery();
    }

    private void addImagesToThegallery() {
        LinearLayout imageGallery = (LinearLayout) findViewById(R.id.imageGallery);
        Integer k = 0;
        theViews = new ArrayList<ImageButton>();
        Log.v("addImagesToThegallery", "called");
        while (k < similarUsers.size()) {
            Log.v("addImagesToThegallery", "While loop called");
            View thisOne = getImageButton(k);
            imageGallery.addView(thisOne);
            ImageButton c = (ImageButton)thisOne;
            c.setVisibility(View.VISIBLE);
            theViews.add(c);
            ++k;
        }

    }

    private View getImageButton(Integer j){
        ImageButton imageButton = new ImageButton(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 50, 50, 50);
        imageButton.setLayoutParams(lp);
        Log.v("getImageButton", "getting to just before retrieveImage function called");
        retrieveImage(j, imageButton);
        imageButton.setTag(j);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton ib = (ImageButton)v;
                Intent intent = new Intent(getApplicationContext(), friend_info_activity.class);
                Integer tag = (Integer)ib.getTag();
                byte[] res = imageArray.get(tag);
                ParseUser thisOne = similarUsers.get(tag);
                intent.putExtra("imageId", res);
                intent.putExtra("about", checkForNull("AboutMe", tag));
                intent.putExtra("fLink", checkForNull("facebook", tag));
                intent.putExtra("lLink", checkForNull("linkedin", tag));
                intent.putExtra("wLink", checkForNull("whatsapp", tag));
                intent.putExtra("name", thisOne.getUsername());
                intent.putExtra("place", checkForNull("location", tag));
                startActivity(intent);
            }
        });
        return imageButton;
    }

    public void showFriends (View v) {
        int k = 0;
        for (ImageButton d : theViews){
            if (imageArray.get(k).length > 1) {
                d.setImageBitmap(BitmapFactory.decodeByteArray(
                        imageArray.get(k), 0, imageArray.get(k).length));
            }
            else d.setImageResource(R.drawable.no_image);
            d.setVisibility(View.VISIBLE);
            ++k;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_suggestion, menu);
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
