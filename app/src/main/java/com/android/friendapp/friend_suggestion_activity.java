/*referenced http://www.sitepoint.com/handling-displaying-images-android/
* */

package com.android.friendapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class friend_suggestion_activity extends ActionBarActivity{

    private Integer images[] = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,
    R.drawable.pic4, R.drawable.pic5};
    private ParseUser user = ParseUser.getCurrentUser();
    private List userList, commonList, comparison;

    public void findUsersInCommon(){
        Log.v("findUsersInCommon", "starting...");
        commonList = new ArrayList<>();

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
                            commonList.add(objects.get(i));
                    }//end compairson for loop

                    //now make sure that the list of objects in common is not over 5
                    if(commonList.size() > 5){
                        Log.v("findUsersInCommon", "commonList size is " + commonList.size());

                        while(commonList.size() > 5){
                            Random rand = new Random();
                          //  int random = rand.nextInt((commonList.size() - 1) + 1);//it will never choose a number greater than commonLists' size
                            commonList.remove(rand.nextInt((commonList.size() - 1) + 1));
                        }
                    }

                } else {
                    // Something went wrong.
                }
            }
        });
    }

    //added by jared

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //added by jared
        findUsersInCommon();

        setContentView(R.layout.activity_friend_suggestion);
        addImagesToThegallery();
    }

    private void addImagesToThegallery() {
        LinearLayout imageGallery = (LinearLayout) findViewById(R.id.imageGallery);
        for (Integer image : images) {
            imageGallery.addView(getImageButton(image));
        }
    }

    private View getImageButton(Integer image) {
        ImageButton imageButton = new ImageButton(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 10, 0);
        imageButton.setLayoutParams(lp);
        imageButton.setImageResource(image);
        imageButton.setTag(image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton ib = (ImageButton)v;
                Intent intent = new Intent(getApplicationContext(), friend_info_activity.class);
                Integer res = (Integer)ib.getTag();
                intent.putExtra("imageId", res);
                startActivity(intent);
            }
        });
        return imageButton;
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