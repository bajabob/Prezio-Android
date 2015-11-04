package com.prezio;

import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Created by bobtimm on 11/4/2015.
 */
public class PrezioFragment extends Fragment {

    protected UserModel mCurrentUser;
    protected WeakReference<HomeActivityListener> mListener;

    public void setHomeActivityListener(HomeActivityListener listener){
        mListener = new WeakReference<HomeActivityListener>(listener);
    }

    public void setCurrentUser(UserModel user){
        mCurrentUser = user;
    }

}
