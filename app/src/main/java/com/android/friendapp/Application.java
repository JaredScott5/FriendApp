package com.android.friendapp;

import com.parse.Parse;

/**
 * Created by Harley on 7/22/2015.
 * using http://stackoverflow.com/questions/31300277/caused-by-java-lang-illegalstateexception-parseplugins-is-already-initialized
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "FE9hqhrX9u82m5rrx2U9G0uRSbsa3RSLMO3N71CQ",
                "Xbtv0h3ETBRz5ahqEqu2b0kDH6mFXWkGXama0dOM");
    }
}