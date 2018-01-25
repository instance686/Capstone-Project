package com.reminders.location.locatoinreminder.view.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.AppDatabase;

import com.reminders.location.locatoinreminder.executor.ContactSync;
import com.reminders.location.locatoinreminder.executor.TaskRunning;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;
import com.reminders.location.locatoinreminder.view.adapters.ContactChatAdapter;
import com.reminders.location.locatoinreminder.view.adapters.ViewPagerAdapter;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;
import com.reminders.location.locatoinreminder.viewmodel.ShoutsFragmentViewModel;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.maintoolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarDelOptions)
    Toolbar toolbarOptions;
    @BindView(R.id.backOnLongClick)
    ImageButton backLongClick;
    @BindView(R.id.cardCounter)
    TextView cardCounter;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    List<String> numbers = new ArrayList<>();
    AppDatabase appDatabase;
    ContactChatAdapter contactChatAdapter;

    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();
    private FirebaseAuth mAuth;
    private ShoutsFragmentViewModel shoutsFragmentViewModel;

    boolean notificationClicked=false;

    public boolean isNotificationClicked() {
        return notificationClicked;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appDatabase = getMyapp().getDatabase();
        toolbar.inflateMenu(R.menu.mainactivity_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sharedPreferenceSingleton.saveAs(MainActivity.this, ConstantVar.LOGGED, false);
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            }
        });
        shoutsFragmentViewModel= ViewModelProviders.of(this).get(ShoutsFragmentViewModel.class);

        backLongClick.setOnClickListener(this);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(MainActivity.this, getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!sharedPreferenceSingleton.getSavedBoolean(this, "Cached_Contacts")) {
            ContactSync contactsSync = new ContactSync(this, getContentResolver(), getMyapp().getDatabase(), taskRunning);
            contactsSync.execute();
            Log.d("MainActivity", "Syncing");
            sharedPreferenceSingleton.saveAs(this, "Cached_Contacts", true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getBooleanExtra(ConstantVar.FROM_NOTIFICATION,false)) {
            viewPager.setCurrentItem(1);
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
    public void onClick(View v) {
            /*if(v.getId()==backLongClick.getId()){
                toolbarOptions.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);

            }*/
    }

    TaskRunning taskRunning=new TaskRunning() {
        @Override
        public void taskRunning(boolean runVal) {

        }
    };


}