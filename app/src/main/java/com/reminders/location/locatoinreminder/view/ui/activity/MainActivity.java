package com.reminders.location.locatoinreminder.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.executor.ContactSync;
import com.reminders.location.locatoinreminder.executor.TaskRunning;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;
import com.reminders.location.locatoinreminder.view.adapters.ViewPagerAdapter;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;

import butterknife.BindView;

    public class MainActivity extends BaseActivity implements TaskRunning {
    @BindView(R.id.maintoolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();
        private FirebaseAuth mAuth;



        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.inflateMenu(R.menu.mainactivity_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sharedPreferenceSingleton.saveAs(MainActivity.this,ConstantVar.LOGGED,false);
                startActivity(new Intent(MainActivity.this,SplashActivity.class));
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            }
        });
        tabLayout.setTabTextColors(
                getResources().getColor(R.color.textColorPrimary1),
                getResources().getColor(R.color.textColorPrimary1)
        );
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(MainActivity.this,getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

        @Override
        protected void onStart() {
            super.onStart();
            if(!sharedPreferenceSingleton.getSavedBoolean(this,"Cached_Contacts")) {
                ContactSync contactsSync = new ContactSync(this,getContentResolver(),getMyapp().getDatabase(),this);
                contactsSync.execute();
                Log.d("MainActivity","Syncing");
                sharedPreferenceSingleton.saveAs(this,"Cached_Contacts",true);
            }
        }


        public MyApplication getMyapp() {
            return (MyApplication) getApplicationContext();
        }


        @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }


        @Override
        public void taskRunning(boolean runVal) {

        }
    }
