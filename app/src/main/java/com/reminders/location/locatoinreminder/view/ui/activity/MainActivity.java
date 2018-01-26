package com.reminders.location.locatoinreminder.view.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.MyContentProvider;
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

public class MainActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
       {
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
    private GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
           private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
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
        buildGoogleApiClient();
        checkPermissions();
        backLongClick.setOnClickListener(this);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(MainActivity.this, getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }
    public void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

           private void checkPermissions() {
               if (Build.VERSION.SDK_INT >= 23) {
                   if (ContextCompat.checkSelfPermission(MainActivity.this,
                           android.Manifest.permission.ACCESS_FINE_LOCATION)
                           != PackageManager.PERMISSION_GRANTED)
                       requestLocationPermission();
                   else
                       showSettingDialog();
               } else
                   showSettingDialog();

           }
           private void requestLocationPermission() {
               if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                   ActivityCompat.requestPermissions(MainActivity.this,
                           new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                           ACCESS_FINE_LOCATION_INTENT_ID);

               } else {
                   ActivityCompat.requestPermissions(MainActivity.this,
                           new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                           ACCESS_FINE_LOCATION_INTENT_ID);
               }
           }

           /* Show Location Access Dialog */
           private void showSettingDialog() {
               LocationRequest locationRequest = LocationRequest.create();
               locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
               locationRequest.setInterval(30 * 1000);
               locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
               LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                       .addLocationRequest(locationRequest);
               builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

               PendingResult<LocationSettingsResult> result =
                       LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
               result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                   @Override
                   public void onResult(LocationSettingsResult result) {
                       final Status status = result.getStatus();
                       final LocationSettingsStates state = result.getLocationSettingsStates();
                       switch (status.getStatusCode()) {
                           case LocationSettingsStatusCodes.SUCCESS:
                               // All location settings are satisfied. The client can initialize location
                               // requests here.
                               break;
                           case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                               // Location settings are not satisfied. But could be fixed by showing the user
                               // a dialog.
                               try {
                                   // Show the dialog by calling startResolutionForResult(),
                                   // and check the result in onActivityResult().
                                   status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                               } catch (IntentSender.SendIntentException e) {
                                   e.printStackTrace();
                                   // Ignore the error.
                               }
                               break;
                           case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                               // Location settings are not satisfied. However, we have no way to fix the
                               // settings so we won't show the dialog.
                               break;
                       }
                   }
               });
           }

           @Override
           protected void onActivityResult(int requestCode, int resultCode, Intent data) {
               switch (requestCode) {
                   // Check for the integer request code originally supplied to startResolutionForResult().
                   case REQUEST_CHECK_SETTINGS:
                       switch (resultCode) {
                           case RESULT_OK:
                               Log.e("Settings", "Result OK");
                               //startLocationUpdates();
                               break;
                           case RESULT_CANCELED:
                               Log.e("Settings", "Result Cancel");
                               checkPermissions();
                               break;
                       }
                       break;
               }
           }



           @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

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
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS

        if(getIntent().getBooleanExtra(ConstantVar.FROM_NOTIFICATION,false)) {
            viewPager.setCurrentItem(1);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
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

           @Override
           public void onConnected(@Nullable Bundle bundle) {

           }

           @Override
           public void onConnectionSuspended(int i) {

           }

           @Override
           public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

           }
           private Runnable sendUpdatesToUI = new Runnable() {
               public void run() {
                   showSettingDialog();
               }
           };


           /* Broadcast receiver to check status of GPS */
           private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

               @Override
               public void onReceive(Context context, Intent intent) {

                   //If Action is Location
                   if (intent.getAction().matches(BROADCAST_ACTION)) {
                       LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                       //Check if GPS is turned ON or OFF
                       if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                           Log.e("About GPS", "GPS is Enabled in your device");
                       } else {
                           //If GPS turned OFF show Location Dialog
                           new Handler().postDelayed(sendUpdatesToUI, 10);
                           // showSettingDialog();
                           Log.e("About GPS", "GPS is Disabled in your device");
                       }

                   }
               }
           };

           @Override
           protected void onDestroy() {
               super.onDestroy();
               if (gpsLocationReceiver != null)
                   unregisterReceiver(gpsLocationReceiver);
           }
           }
