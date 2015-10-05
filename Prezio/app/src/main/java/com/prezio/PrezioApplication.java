package com.prezio;

import android.app.Application;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.parse.Parse;

/**
 * Created by bob on 10/5/15.
 */
public class PrezioApplication extends Application {

    private static final Logger log = LoggerManager.getLogger(PrezioApplication.class);

    @Override
    public void onCreate() {
        super.onCreate();

        log.d("[onCreate]");

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // TODO hide these keys
        // Add your initialization code here
        Parse.initialize(this, Config.API_PARSE_APP_ID, Config.API_PARSE_APP_KEY);

    }

}
