package com.prezio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 11/6/15.
 */
public class BluetoothDeviceList {

    private List<String> mDiscoveredDevices = new ArrayList<String>();

    public void add(String name){
        if(! exists(name)) {
            mDiscoveredDevices.add(name);
        }
    }

    public boolean exists(String name){
        for(String device : mDiscoveredDevices){
            if(name == device){
                return true;
            }
        }
        return false;
    }



}
