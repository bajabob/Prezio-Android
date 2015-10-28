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

    private AppBarLayout mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = (AppBarLayout) findViewById(R.id.toolbar);

        onLoadFragment(new LoginFragment(), false);
    }


    @Override
    public void onLoadFragment(Fragment fragment, boolean hasToolbar) {

        if (findViewById(R.id.fragment_panel) != null) {

            mToolbar.setVisibility(hasToolbar ? View.VISIBLE : View.INVISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_panel, fragment).commit();
        }
    }
}
