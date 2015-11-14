package com.prezio;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.prezio.templates.Preferences;
import com.squareup.picasso.Picasso;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bobtimm on 10/28/2015.
 */
public class HomeFragment extends PrezioFragment implements View.OnClickListener, BluetoothUtil.BluetoothListener{

    private static final Logger log = LoggerManager.getLogger(HomeFragment.class);

    private CardView mCheckin;
    private TextView mSearching;
    private ProgressBar mSpinner;
    private BluetoothUtil mBTUtil;
    private BluetoothDeviceList mBTDeviceList;
    private CheckinModel mDiscoveredCheckin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Button create = (Button) v.findViewById(R.id.create);
        create.setOnClickListener(this);

        Button logout = (Button) v.findViewById(R.id.logout);
        logout.setOnClickListener(this);

        Button checkin = (Button) v.findViewById(R.id.checkin);
        checkin.setOnClickListener(this);

        mSpinner = (ProgressBar) v.findViewById(R.id.loader);
        mSearching = (TextView) v.findViewById(R.id.searching);

        mCheckin = (CardView) v.findViewById(R.id.checkin_card);
        mCheckin.setVisibility(View.GONE);

        return v;
    }

    @Override
    public void onStart(){
        super.onStart();

        mBTDeviceList = new BluetoothDeviceList();
        mBTUtil = new BluetoothUtil(getActivity(), this);

        TextView user = (TextView)getView().findViewById(R.id.user);
        user.setText(mCurrentUser.getName());

        TextView checkins = (TextView)getView().findViewById(R.id.checkin_count);
        checkins.setText("Check Ins: " + (int) ((Math.random() * 100) + 2));

        CircleImageView profileImage = (CircleImageView) getView().findViewById(R.id.profile_image);
        Picasso.with(getActivity()).load(mCurrentUser.getProfilePictureUrl()).into(profileImage);
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
                mListener.get().onLoadFragment(HomeActivity.FRAGMENT_ID_CREATE_CHECK_IN, false);
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

        if(view.getId() == R.id.checkin){
            if(mListener != null){
                mListener.get().setCurrentCheckin(mDiscoveredCheckin);
                mListener.get().onLoadFragment(HomeActivity.FRAGMENT_ID_BROADCAST, true);
            }
        }
    }

    @Override
    public void onBluetoothDeviceDiscovered(String name) {
        log.d("BT Device: " + name);
        if( !mBTDeviceList.exists(name)){
            if(name.contains("PRZ")) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("CheckinPlaces");
                query.whereEqualTo("bluetoothId", name);
                query.include("creator");
                query.setLimit(1);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(objects != null && objects.size() > 0){
                            ParseObject obj = objects.get(0);
                            log.d("Found check in!");
                            log.d(obj.getString("name"));
                            log.d(obj.getString("bluetoothId"));

                            mDiscoveredCheckin = new CheckinModel(obj);

                            DateTime now = DateTime.now(DateTimeZone.forID("UTC"));
                            long diff = mDiscoveredCheckin.getCheckinExpiresUtc() - now.getMillis();

                            if(diff < 0){
                                return;
                            }


                            mSpinner.setVisibility(View.INVISIBLE);
                            mSearching.setVisibility(View.GONE);
                            mCheckin.setVisibility(View.VISIBLE);

                            TextView title = (TextView) getView().findViewById(R.id.checkin_name);
                            title.setText(mDiscoveredCheckin.getName());

                            CircleImageView profileImage = (CircleImageView) getView().findViewById(R.id.checkin_profile);
                            Picasso.with(getActivity()).load(mDiscoveredCheckin.getCreator().getProfilePictureUrl()).into(profileImage);

                            String time = String.format("Expires in %d minutes",
                                    TimeUnit.MILLISECONDS.toMinutes(diff)
                            );
                            TextView expires = (TextView) getView().findViewById(R.id.checkin_expires);
                            expires.setText(time);
                        }
                    }
                });
            }
        }
        mBTDeviceList.add(name);
    }
}
