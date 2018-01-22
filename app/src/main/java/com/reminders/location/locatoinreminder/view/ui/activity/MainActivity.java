package com.reminders.location.locatoinreminder.view.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.executor.CardsSelected;
import com.reminders.location.locatoinreminder.executor.ContactCard;
import com.reminders.location.locatoinreminder.executor.ContactSync;
import com.reminders.location.locatoinreminder.executor.TaskRunning;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;
import com.reminders.location.locatoinreminder.singleton.ToastMessage;
import com.reminders.location.locatoinreminder.view.adapters.ContactChatAdapter;
import com.reminders.location.locatoinreminder.view.adapters.ViewPagerAdapter;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;
import com.reminders.location.locatoinreminder.view.ui.fragments.ReminderChat;
import com.reminders.location.locatoinreminder.viewmodel.ReminderChatViewModel;
import com.reminders.location.locatoinreminder.viewmodel.ShoutsFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements TaskRunning, View.OnClickListener {
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appDatabase = getMyapp().getDatabase();
        //contactChatAdapter= ReminderChat.getContactChatAdapter();
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



        /*toolbarOptions.inflateMenu(R.menu.mainactivity_options);
        toolbarOptions.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.delete){
                    deleteCards();
                }

                return true;
            }
        });*/

        /*tabLayout.setTabTextColors(
                getResources().getColor(R.color.textColorPrimary1),
                getResources().getColor(R.color.textColorPrimary1)
        );*/


        backLongClick.setOnClickListener(this);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(MainActivity.this, getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

        /*public void deleteCards(){
        if(numbers.size()>0){
            AsyncTask.execute(()->{
                //appDatabase.reminderContactDoa().deleteCard(numbers);
              *//*  int count=appDatabase.cardDoa().deleteContactCards(numbers);
                Log.v("RowsDelted",""+count);
*//*            });

            toolbarOptions.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);

        }
        else
            Toast.makeText(MainActivity.this,"Select Cards for Deletion",Toast.LENGTH_SHORT).show();

        }*/

    @Override
    protected void onStart() {
        super.onStart();
        if (!sharedPreferenceSingleton.getSavedBoolean(this, "Cached_Contacts")) {
            ContactSync contactsSync = new ContactSync(this, getContentResolver(), getMyapp().getDatabase(), this);
            contactsSync.execute();
            Log.d("MainActivity", "Syncing");
            sharedPreferenceSingleton.saveAs(this, "Cached_Contacts", true);
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


       /* @Override
        public void onContactCardSelected(int count, String number, int position, boolean selected) {
            ToastMessage.showMessageShort(this,"Count="+count+",Position="+position);
            if(count>0){
                toolbar.setVisibility(View.GONE);
//                cardCounter.setText(count);
                toolbarOptions.setVisibility(View.VISIBLE);
            }
            else {
                toolbarOptions.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
            }

            if(selected)
                numbers.add(number);
            else {
                int index=number.indexOf(number);
                numbers.remove(index);
            }

        }*/

    @Override
    public void onClick(View v) {
            /*if(v.getId()==backLongClick.getId()){
                toolbarOptions.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);

            }*/
    }
}