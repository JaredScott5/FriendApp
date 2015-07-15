package com.android.friendapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;

/**
 * Created by jaredscott on 7/15/15.
 */
public class ParseDatabase extends Application {

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
}
