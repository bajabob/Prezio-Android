package com.prezio;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.prezio.templates.Preferences;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bobtimm on 10/28/2015.
 */
public class LoginFragment extends PrezioFragment implements View.OnClickListener{

    private static final Logger log = LoggerManager.getLogger(LoginFragment.class);

    private TextView mLogo;
    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private ProgressBar mLoader;
    private CircleImageView mProfileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mLogin = (Button) v.findViewById(R.id.login);
        mLogin.setOnClickListener(this);

        mUsername = (EditText)v.findViewById(R.id.username);
        mPassword = (EditText)v.findViewById(R.id.password);

        mLoader = (ProgressBar) v.findViewById(R.id.loader);
        mLoader.setVisibility(View.GONE);

        mProfileImage = (CircleImageView) v.findViewById(R.id.profile_image);
        mProfileImage.setVisibility(View.GONE);

        mLogo = (TextView) v.findViewById(R.id.logo);

        SharedPreferences settings = getActivity().getSharedPreferences(Preferences.PREFERENCE_FILE, 0);
        String username = settings.getString("username", null);

        if(username != null){
            login(username);
        }

        return v;
    }


    private void login(String username){

        mLogin.setVisibility(View.GONE);
        mUsername.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);
        mProfileImage.setVisibility(View.VISIBLE);
        mLoader.setVisibility(View.VISIBLE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username", username.toLowerCase());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects != null && objects.size() > 0){
                    UserModel user = new UserModel(objects.get(0));
                    Picasso.with(getActivity()).load(user.getProfilePictureUrl()).into(mProfileImage);

                    if(mListener != null) {
                        mListener.get().setCurrentUser(user);
                    }

                    Handler handler = new Handler();
                    final Runnable r = new Runnable() {
                        public void run() {
                            if(mListener != null){
                                mListener.get().onLoadFragment(HomeActivity.FRAGMENT_ID_HOME, true);
                            }
                        }
                    };
                    handler.postDelayed(r, 2000);

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login){

            login(mUsername.getText().toString());

        }
    }
}
