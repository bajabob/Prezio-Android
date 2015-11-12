package com.prezio;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.parse.ParseObject;

/**
 * Created by bobtimm on 11/4/2015.
 */
public class CheckinModel {

    private static final Logger log = LoggerManager.getLogger(CheckinModel.class);

    private static final String PO_BLUETOOTH_ID = "bluetoothId";
    private static final String PO_NAME = "name";
    private static final String PO_CHECKIN_EXPIRES_UTC = "checkinExpiresUtc";
    private static final String PO_CREATOR = "creator";
    private static final String PO_EXPECTED = "expected";

    private ParseObject mParseObject;

    public CheckinModel(ParseObject po){
        mParseObject = po;
    }

    public String getName(){
        return mParseObject.getString(PO_NAME);
    }

    public String getBluetoothId(){
        return mParseObject.getString(PO_BLUETOOTH_ID);
    }

    public long getCheckinExpiresUtc(){
        return mParseObject.getLong(PO_CHECKIN_EXPIRES_UTC);
    }

    public ParseObject getParseObject(){
        return mParseObject;
    }

    public ParseObject getParseObjectPointer(){
        return ParseObject.createWithoutData("User", mParseObject.getObjectId());
    }


}
