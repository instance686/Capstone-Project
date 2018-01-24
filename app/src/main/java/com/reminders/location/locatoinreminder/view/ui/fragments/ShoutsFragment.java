package com.reminders.location.locatoinreminder.view.ui.fragments;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantLog;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;
import com.reminders.location.locatoinreminder.database.entity.Contact_Entity;
import com.reminders.location.locatoinreminder.executor.DistanceComparator;
import com.reminders.location.locatoinreminder.executor.GetShoutsList;
import com.reminders.location.locatoinreminder.executor.LocationUpdateList;
import com.reminders.location.locatoinreminder.pojo.ContactFetch;
import com.reminders.location.locatoinreminder.pojo.ShoutsData;
import com.reminders.location.locatoinreminder.singleton.ToastMessage;
import com.reminders.location.locatoinreminder.view.adapters.ShoutAdapter;
import com.reminders.location.locatoinreminder.view.ui.activity.MainActivity;
import com.reminders.location.locatoinreminder.viewmodel.ShoutsFragmentViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by ayush on 25/12/17.
 */

public class ShoutsFragment extends Fragment implements LocationListener{

    @BindView(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.shouts)
    RecyclerView recyclerView;
    @BindView(R.id.empty_state)
    ConstraintLayout empty_state;
    LocationManager locationManager;
    Location currentLocation=new Location("");



    private ShoutsFragmentViewModel shoutsFragmentViewModel;

    private AppDatabase appDatabase;
    private ShoutAdapter shoutAdapter;

    private IntentFilter mIntentFilter;
    private BroadcastReceiver localBroadCast=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!intent.getParcelableArrayListExtra("TEST").isEmpty())
            shoutsFragmentViewModel.getShoutsData().setValue(
                    intent.getParcelableArrayListExtra("TEST")
            );
        }
    };


    public ShoutsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(ConstantLog.ViewConstants.SHOUT_TAG, ConstantLog.MethodConstants.ONCREATEVIEW_TAG);
        View view= inflater.inflate(R.layout.fragment_shouts, container, false);
        appDatabase=getMyapp().getDatabase();
        ButterKnife.bind(this, view);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ConstantVar.mBroadcastArrayListAction);
        shoutsFragmentViewModel=ViewModelProviders.of(this).get(ShoutsFragmentViewModel.class);
        currentLocation=new Location("");
        swipeRefreshLayout.setOnRefreshListener(()->{refresh();});
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        shoutAdapter=new ShoutAdapter(getActivity(), new ArrayList<>());
        recyclerView.setAdapter(shoutAdapter);

        shoutsFragmentViewModel.getShoutsData().observe(getActivity(),observer);

        return view;
    }

    Observer<List<ChatCards_Entity>> observer=new Observer<List<ChatCards_Entity>>() {
        @Override
        public void onChanged(@Nullable List<ChatCards_Entity> chatCards_entities) {
            //Collections.sort(chatCards_entities,new DistanceComparator());
            shoutAdapter.addItems(chatCards_entities);
        }
    };


    public MyApplication getMyapp() {
        return ((MainActivity) getActivity()).getMyapp();
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(localBroadCast,mIntentFilter);

        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(localBroadCast);
        super.onPause();
    }
    public void refresh(){
        Location location;
        locationManager = (LocationManager)getContext().getSystemService(LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(isGPSEnabled && isNetworkEnabled){
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Log.v("FromShouts","onSwipeRefresh");
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0, this);
            if(locationManager!=null){
                location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                getShoutsList(location);
            }
        }


    }

    public void getShoutsList(Location location){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(localBroadCast);
        Log.v("FromShouts","GetLocationList");
        new GetShoutsList(getActivity(),appDatabase,location, locationUpdateList).execute();


    }
    LocationUpdateList locationUpdateList=new LocationUpdateList() {
        @Override
        public void locationList(List<ChatCards_Entity> chatCardsEntityList) {
            Log.v("FromShouts","OnTaskComplete");
            if(!chatCardsEntityList.isEmpty())
                shoutsFragmentViewModel.getShoutsData().setValue(chatCardsEntityList);
            else
                ToastMessage.showMessageLong(getActivity(),"No reminders for near by area!");

            swipeRefreshLayout.setRefreshing(false);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(localBroadCast,mIntentFilter);

        }
    };

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
       // getShoutsList(location);
        Log.v("FromShouts","onLocationChanged");

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}