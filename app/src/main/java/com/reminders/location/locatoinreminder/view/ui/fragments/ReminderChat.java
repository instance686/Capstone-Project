package com.reminders.location.locatoinreminder.view.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantLog;
import com.reminders.location.locatoinreminder.database.entity.Contact_Entity;
import com.reminders.location.locatoinreminder.database.entity.ReminderContact;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseFragment;
import com.reminders.location.locatoinreminder.view.adapters.ContactChatAdapter;
import com.reminders.location.locatoinreminder.view.ui.activity.ContactsActivity;
import com.reminders.location.locatoinreminder.viewmodel.ReminderChatViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ayush on 25/12/17.
 */

public class ReminderChat extends Fragment implements View.OnClickListener {

    @BindView(R.id.fab)
     FloatingActionButton floatingActionButton;
    @BindView(R.id.contactsRem)
    RecyclerView recyclerView;

    ReminderChatViewModel reminderChatViewModel;
    ContactChatAdapter contactChatAdapter;
    Unbinder unbinder;

    public ReminderChat() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(ConstantLog.ViewConstants.REMINDERCHAT_TAG, ConstantLog.MethodConstants.ONCREATEVIEW_TAG);
        View view= inflater.inflate(R.layout.fragment_reminderchat, container, false);
        unbinder=ButterKnife.bind(this,view);
        reminderChatViewModel= ViewModelProviders.of(this).get(ReminderChatViewModel.class);
        floatingActionButton.setOnClickListener(this);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactChatAdapter=new ContactChatAdapter(getActivity(),new ArrayList<ReminderContact>());
        recyclerView.setAdapter(contactChatAdapter);
        reminderChatViewModel.getContactList(getMyapp().getDatabase()).observe(getActivity(),observer);

        return view;
    }

    Observer<List<ReminderContact>> observer=new Observer<List<ReminderContact>>() {
        @Override
        public void onChanged(@Nullable List<ReminderContact> contact_entities) {
            contactChatAdapter.addItems(contact_entities);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
       if(v.getId()==floatingActionButton.getId()){
           startActivity(new Intent(getActivity(), ContactsActivity.class));
       }
    }
    public MyApplication getMyapp() {
        return (MyApplication) getActivity().getApplicationContext();
    }

}

