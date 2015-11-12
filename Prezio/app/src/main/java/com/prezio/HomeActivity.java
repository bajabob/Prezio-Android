package com.prezio;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.prezio.templates.Preferences;

public class HomeActivity extends AppCompatActivity implements HomeActivityListener {

    private static final Logger log = LoggerManager.getLogger(HomeActivity.class);

    public static final int FRAGMENT_ID_LOGIN = 1;
    public static final int FRAGMENT_ID_HOME = 2;
    public static final int FRAGMENT_ID_CREATE_CHECK_IN = 3;

    private UserModel mCurrentUser;
    private AppBarLayout mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = (AppBarLayout) findViewById(R.id.toolbar);

        onLoadFragment(FRAGMENT_ID_LOGIN, false);
    }


    @Override
    public void onLoadFragment(int fragmentId, boolean hasToolbar) {

        if (findViewById(R.id.fragment_panel) != null) {

            mToolbar.setVisibility(hasToolbar ? View.VISIBLE : View.INVISIBLE);

            PrezioFragment frag = null;
            switch (fragmentId){

                default:
                case FRAGMENT_ID_LOGIN:
                    frag = new LoginFragment();
                    break;

                case FRAGMENT_ID_HOME:
                    frag = new HomeFragment();
                    break;

                case FRAGMENT_ID_CREATE_CHECK_IN:
                    frag = new CreateCheckInFragment();
                    break;

            }
            if(frag != null) {
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_panel, frag).commit();
                frag.setHomeActivityListener(this);
                frag.setCurrentUser(mCurrentUser);
            }
        }
    }

    @Override
    public void setCurrentUser(UserModel user){
        log.d("User set: "+user.getUsername());
        mCurrentUser = user;

        // save user to local system so it loads next time the app is opened
        SharedPreferences settings = getSharedPreferences(Preferences.PREFERENCE_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", user.getUsername());
        editor.commit();
    }
}
