package com.reminders.location.locatoinreminder.view.BaseModel;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.reminders.location.locatoinreminder.constants.ConstantLog;

import butterknife.ButterKnife;

/**
 * Created by ayush on 25/12/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements LifecycleOwner{

    private LifecycleRegistry mLifecycleRegistry;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONCREATE_TAG);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);


    }


    @Override
    protected void onStart() {
        super.onStart();
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONSTART_TAG);
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);

    }

    @Override
    protected void onResume() {
        super.onResume();
       // Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONRESUME_TAG);
        mLifecycleRegistry.markState(Lifecycle.State.RESUMED);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONPAUSE_TAG);

    }
    @Override
    protected void onStop() {
        super.onStop();
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONSTOP_TAG);

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        //Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONRESTART_TAG);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // Log.v(this.getClass().getSimpleName(), ConstantLog.MethodConstants.ONDESTROY_TAG);
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);

    }
    @NonNull
    @Override
    public Lifecycle getLifecycle(){
        return mLifecycleRegistry;
    }

    protected abstract int getLayoutResourceId();
}
