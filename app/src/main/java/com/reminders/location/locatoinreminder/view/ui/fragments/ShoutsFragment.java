package com.reminders.location.locatoinreminder.view.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantLog;

/**
 * Created by ayush on 25/12/17.
 */

public class ShoutsFragment extends Fragment {

    public ShoutsFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(ConstantLog.ViewConstants.SHOUT_TAG, ConstantLog.MethodConstants.ONCREATEVIEW_TAG);
        return inflater.inflate(R.layout.fragment_shouts, container, false);
    }


}
