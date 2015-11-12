package com.prezio;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.prezio.templates.Preferences;
import com.squareup.picasso.Picasso;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bobtimm on 10/28/2015.
 */
public class HomeFragment extends PrezioFragment implements View.OnClickListener, BluetoothUtil.BluetoothListener{

    private static final Logger log = LoggerManager.getLogger(HomeFragment.class);

    private BluetoothUtil mBTUtil;
    private BluetoothDeviceList mBTDEviceList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        TextView user = (TextView)v.findViewById(R.id.user);
        user.setText(mCurrentUser.getName());

        TextView checkins = (TextView)v.findViewById(R.id.checkin_count);
        checkins.setText("Check Ins: " + (int) ((Math.random() * 100) + 2));

        CircleImageView profileImage = (CircleImageView) v.findViewById(R.id.profile_image);
        Picasso.with(getActivity()).load(mCurrentUser.getProfilePictureUrl()).into(profileImage);

        Button create = (Button) v.findViewById(R.id.create);
        create.setOnClickListener(this);

        Button logout = (Button) v.findViewById(R.id.logout);
        logout.setOnClickListener(this);

        mBTDEviceList = new BluetoothDeviceList();
        mBTUtil = new BluetoothUtil(getActivity(), this);

        return v;
    }

    @Override
    public void onDestroy() {
        mBTUtil.onDestroy(getActivity());
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.create){
            if(mListener != null){
                mListener.get().onLoadFragment(HomeActivity.FRAGMENT_ID_CREATE_CHECK_IN, true);
            }
        }

        if(view.getId() == R.id.logout){
            SharedPreferences settings = getActivity().getSharedPreferences(Preferences.PREFERENCE_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("username", null);
            editor.commit();
            if(mListener != null){
                mListener.get().onLoadFragment(HomeActivity.FRAGMENT_ID_LOGIN, false);
            }
        }
    }

    @Override
    public void onBluetoothDeviceDiscovered(String name) {
        log.d("BT Device: " + name);
        if( !mBTDEviceList.exists(name)){
            if(name.contains("PREZIO")) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("CheckinPlaces");
                query.whereEqualTo("bluetoothId", name);
                query.setLimit(1);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(objects != null && objects.size() > 0){
                            ParseObject obj = objects.get(0);
                            log.d(obj.getString("name"));
                        }
                    }
                });
            }
        }
        mBTDEviceList.add(name);
    }
}
