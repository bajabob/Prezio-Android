package com.prezio;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

public class HomeActivity extends AppCompatActivity implements HomeActivityListener {

    private static final Logger log = LoggerManager.getLogger(HomeActivity.class);

    public static final int FRAGMENT_ID_LOGIN = 1;


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

            Fragment frag = null;
            switch (fragmentId){

                default:
                case FRAGMENT_ID_LOGIN:
                    frag = new LoginFragment();
                    break;

            }
            if(frag != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_panel, frag).commit();
            }
        }
    }
}
