package com.prezio;

import com.parse.ParseObject;

/**
 * Created by bobtimm on 11/4/2015.
 */
public class UserModel {

    private static final String PO_USERNAME = "username";
    private static final String PO_NAME = "Name";
    private static final String PO_PROFILE_PICTURE_URL = "profilePictureUrl";

    private ParseObject mParseObject;

    public UserModel(ParseObject po){
        mParseObject = po;
    }

    public String getUsername(){
        return mParseObject.getString(PO_USERNAME);
    }

    public String getName(){
        return mParseObject.getString(PO_NAME);
    }

    public ParseObject getParseObject(){
        return mParseObject;
    }

    public String getProfilePictureUrl(){
        return mParseObject.getString(PO_PROFILE_PICTURE_URL);
    }

}
