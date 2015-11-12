package com.prezio;

import android.app.Application;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.parse.Parse;
import com.prezio.templates.Config;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by bob on 10/5/15.
 */
public class PrezioApplication extends Application {

    private static final Logger log = LoggerManager.getLogger(PrezioApplication.class);

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        log.d("[onCreate]");

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        /**
         * ERRORS
         * Config.java is not included in project for security reasons. Be sure to make a copy of `templates/ConfigTemplate`
         *  and call it `Config`. Then fill out the information required in the file.
         */
        Parse.initialize(this, Config.API_PARSE_APP_ID, Config.API_PARSE_APP_KEY);

    }

}
