package com.prezio;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by bob on 10/5/15.
 */
public class PrezioApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // TODO hide these keys
        // Add your initialization code here
        Parse.initialize(this, Config.API_PARSE_APP_ID, Config.API_PARSE_APP_KEY);

    }

}
