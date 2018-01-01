package com.reminders.location.locatoinreminder.view.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferenceSingleton.getSavedBoolean(SplashActivity.this, ConstantVar.LOGGED)) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, WalkthroughActivity.class));
                    finish();
                }
            }
        },100);


    }

    public MyApplication getMyapp() {
        return (MyApplication) getApplicationContext();
    }
}
