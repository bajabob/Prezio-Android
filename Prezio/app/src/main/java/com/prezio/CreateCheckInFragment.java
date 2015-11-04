package com.prezio;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;


/**
 * Created by bobtimm on 10/28/2015.
 */
public class CreateCheckInFragment extends PrezioFragment{

    private static final Logger log = LoggerManager.getLogger(CreateCheckInFragment.class);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_check_in, container, false);



        return v;
    }


}
