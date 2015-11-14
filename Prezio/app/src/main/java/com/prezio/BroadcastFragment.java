package com.prezio;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by bob on 11/13/15.
 */
public class BroadcastFragment extends PrezioFragment implements View.OnClickListener {

    private static final Logger log = LoggerManager.getLogger(BroadcastFragment.class);

    private static final int TIMER_INTERVAL = 100;

    private Handler mHandler;
    private boolean mHasExpired = false;
    private TextView mTimer;
    private BluetoothAdapter mBluetoothAdapter;
    private AdvertiseCallback mAdvertiseCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_broadcast, container, false);

        mTimer = (TextView)v.findViewById(R.id.timer);

        ImageView broadcast1 = (ImageView) v.findViewById(R.id.broadcast_ring_1);
        Animation ringAnimation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.broadcast_ring_animation);
        broadcast1.startAnimation(ringAnimation1);

        ImageView broadcast2 = (ImageView) v.findViewById(R.id.broadcast_ring_2);
        Animation ringAnimation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.broadcast_ring_animation);
        ringAnimation2.setStartOffset(200);
        broadcast2.startAnimation(ringAnimation2);

        ImageView broadcast3 = (ImageView) v.findViewById(R.id.broadcast_ring_3);
        Animation ringAnimation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.broadcast_ring_animation);
        ringAnimation3.setStartOffset(400);
        broadcast3.startAnimation(ringAnimation3);

        Button cancel = (Button) v.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart(){
        super.onStart();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final BluetoothLeAdvertiser advertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        //advertise settings
        final AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        settingsBuilder.setConnectable(true);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);

        //advertise data
        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        ParcelUuid uuid = new ParcelUuid(UUID.fromString("0000110B-0000-1000-8000-00805F9B34FB"));
        dataBuilder.addServiceUuid(uuid);

        mBluetoothAdapter.setName(mCurrentCheckin.getBluetoothId()); //8 characters works, 9+ fails
        dataBuilder.setIncludeDeviceName(true);

        mAdvertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                log.d("BLE STARTED ADVERTISING BROADCAST "+settingsInEffect);

            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                log.d("BLE ADVERTISING FAILED TO START: "+errorCode);
            }
        };

        if (advertiser!=null) {
            advertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), mAdvertiseCallback);
        }

        mHandler = new Handler(Looper.getMainLooper());
        mStatusChecker.run();

    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            DateTime now = DateTime.now(DateTimeZone.forID("UTC"));
            long diff = mCurrentCheckin.getCheckinExpiresUtc() - now.getMillis();
            log.d("Time: " + diff);

            if(diff < 0){
                mHasExpired = true;

                // update UI

                return;
            }

            String time = String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(diff),
                    TimeUnit.MILLISECONDS.toSeconds(diff) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
            );
            mTimer.setText(time);
            mHandler.postDelayed(mStatusChecker, TIMER_INTERVAL);
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cancel){
            if(mListener != null){
                mListener.get().onLoadFragment(HomeActivity.FRAGMENT_ID_HOME, true);
            }
        }
    }
}
