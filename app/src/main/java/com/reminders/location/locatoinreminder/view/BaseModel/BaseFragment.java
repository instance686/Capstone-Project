package com.reminders.location.locatoinreminder.view.BaseModel;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reminders.location.locatoinreminder.constants.ConstantLog;

/**
 * Created by ayush on 25/12/17.
 */

public abstract class BaseFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONATTACH_TAG);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONCREATE_TAG);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONCREATEVIEW_TAG);

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONACTIVITYCREATED_TAG);

    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONSTART_TAG);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONRESUME_TAG);

    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONPAUSE_TAG);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.DESTROYVIEW_TAG);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONDESTROY_TAG);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONDETACH_TAG);

    }

}
