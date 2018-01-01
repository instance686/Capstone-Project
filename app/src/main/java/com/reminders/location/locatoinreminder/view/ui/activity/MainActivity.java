package com.reminders.location.locatoinreminder.view.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.executor.ContactSync;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;
import com.reminders.location.locatoinreminder.view.adapters.ViewPagerAdapter;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;

import butterknife.BindView;

    public class MainActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
        private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();



        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(MainActivity.this,getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

        @Override
        protected void onStart() {
            super.onStart();
            if(!sharedPreferenceSingleton.getSavedBoolean(this,"Cached_Contacts")) {
                ContactSync contactsSync = new ContactSync(getContentResolver(),getMyapp().getDatabase());
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


}
