package com.reminders.location.locatoinreminder.observer;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.util.Log;

import com.reminders.location.locatoinreminder.view.ui.activity.WalkthroughActivity;

/**
 * Created by ayush on 26/12/17.
 */

public class WalththroughLifeCycleObserver implements LifecycleObserver {
    private static final String LOG_TAG = WalkthroughActivity.class.getSimpleName();

    public WalththroughLifeCycleObserver() {
    }

    WalththroughLifeCycleObserver(Context context, Lifecycle lifecycle) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Log.d(LOG_TAG, "created observing lifecycle.");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void Start() {
        Log.d(LOG_TAG, "start observing lifecycle.");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.d(LOG_TAG, "resumed observing lifecycle.");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.d(LOG_TAG, "paused observing lifecycle.");
    }


}
