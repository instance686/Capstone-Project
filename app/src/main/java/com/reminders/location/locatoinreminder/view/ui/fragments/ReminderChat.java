package com.reminders.location.locatoinreminder.view.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantLog;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseFragment;
import com.reminders.location.locatoinreminder.view.ui.activity.ContactsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ayush on 25/12/17.
 */

public class ReminderChat extends Fragment implements View.OnClickListener {

    @BindView(R.id.fab)
     FloatingActionButton floatingActionButton;
    Unbinder unbinder;

    public ReminderChat() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(ConstantLog.ViewConstants.REMINDERCHAT_TAG, ConstantLog.MethodConstants.ONCREATEVIEW_TAG);
        View view= inflater.inflate(R.layout.fragment_reminderchat, container, false);
        unbinder=ButterKnife.bind(this,view);
        floatingActionButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
       if(v.getId()==floatingActionButton.getId()){
           startActivity(new Intent(getActivity(), ContactsActivity.class));
       }
    }
}
