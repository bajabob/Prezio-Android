package com.prezio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * Created by bobtimm on 10/28/2015.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private ProgressBar mLoader;

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

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login){
            mLogin.setVisibility(View.GONE);
            mUsername.setVisibility(View.GONE);
            mPassword.setVisibility(View.GONE);
            mLoader.setVisibility(View.VISIBLE);
        }
    }
}
