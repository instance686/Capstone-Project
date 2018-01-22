package com.reminders.location.locatoinreminder.view.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.reminders.location.locatoinreminder.pojo.ContactFetch;
import com.reminders.location.locatoinreminder.pojo.ShoutsData;
import com.reminders.location.locatoinreminder.singleton.ToastMessage;
import com.reminders.location.locatoinreminder.view.adapters.ShoutAdapter;
import com.reminders.location.locatoinreminder.view.ui.activity.MainActivity;
import com.reminders.location.locatoinreminder.viewmodel.ShoutsFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ayush on 25/12/17.
 */

public class ShoutsFragment extends Fragment {

    @BindView(R.id.shouts)
    RecyclerView recyclerView;
    @BindView(R.id.empty_state)
    ConstraintLayout empty_state;

    private ShoutsFragmentViewModel shoutsFragmentViewModel;

    private AppDatabase appDatabase;
    private ShoutAdapter shoutAdapter;

    private IntentFilter mIntentFilter;
    private BroadcastReceiver localBroadCast=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
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

    @Override
    public void onStop() {
        super.onStop();
    }
}