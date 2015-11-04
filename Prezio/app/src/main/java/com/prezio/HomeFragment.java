package com.prezio;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bobtimm on 10/28/2015.
 */
public class HomeFragment extends PrezioFragment implements View.OnClickListener{

    private static final Logger log = LoggerManager.getLogger(HomeFragment.class);

    private BluetoothAdapter mBluetoothAdapter;

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

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        return v;
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                log.d("BT", device.getName() + "\n" + device.getAddress());
            }
        }
    };

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.create){
            if(mListener != null){
                mListener.get().onLoadFragment(HomeActivity.FRAGMENT_ID_CREATE_CHECK_IN, true);
            }
        }
    }
}
