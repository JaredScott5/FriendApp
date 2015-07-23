/*referenced http://www.sitepoint.com/handling-displaying-images-android/
* */

package com.android.friendapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class friend_suggestion_activity extends ActionBarActivity{

    private Integer images[] = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,
    R.drawable.pic4, R.drawable.pic5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
