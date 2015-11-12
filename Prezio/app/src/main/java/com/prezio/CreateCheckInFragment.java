package com.prezio;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.parse.ParseObject;

import java.math.BigInteger;
import java.security.SecureRandom;


/**
 * Created by bobtimm on 10/28/2015.
 */
public class CreateCheckInFragment extends PrezioFragment implements NumberPicker.OnValueChangeListener, View.OnClickListener {

    private static final Logger log = LoggerManager.getLogger(CreateCheckInFragment.class);

    private final String[] HEADCOUNT_VALUES =
            {"10", "15", "20", "25", "50", "75", "100", "150", "200", "300", "400", "500"};

    private int mHeadCount = 10;
    private EditText mCheckinName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_check_in, container, false);

        NumberPicker headcount = (NumberPicker) v.findViewById(R.id.headcount_picker);
        headcount.setMinValue(0);
        headcount.setMaxValue(HEADCOUNT_VALUES.length - 1);
        headcount.setDisplayedValues(HEADCOUNT_VALUES);
        headcount.setWrapSelectorWheel(false);
        headcount.setOnValueChangedListener(this);

        Button create = (Button) v.findViewById(R.id.create);
        create.setOnClickListener(this);

        mCheckinName = (EditText)v.findViewById(R.id.checkin_name);

        return v;
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        mHeadCount = Integer.parseInt(HEADCOUNT_VALUES[newVal]);
        log.d("Headcount Value: "+mHeadCount);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.create){

            SecureRandom random = new SecureRandom();

            String key = new BigInteger(130, random).toString(8);


            ParseObject po = new ParseObject("CheckinPlaces");
            po.put("bluetoothId", "PREZIO_"+key);
            po.put("expected", mHeadCount);



        }
    }
}
