package com.reminders.location.locatoinreminder.view.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantLog;
import com.reminders.location.locatoinreminder.database.entity.ContactEntity;
import com.reminders.location.locatoinreminder.util.Utils;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseFragment;
import com.reminders.location.locatoinreminder.view.adapters.ContactsAdapter;
import com.reminders.location.locatoinreminder.viewmodel.ContactsActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ayush on 25/12/17.
 */

public class ContactsFragment extends BaseFragment implements DialogInterface.OnClickListener {

    @BindView(R.id.contacts)
    RecyclerView recyclerView;
    @BindView(R.id.button_done)
    FloatingActionButton done;
    Unbinder unbinder;
    @BindView(R.id.empty_state)
    LinearLayout empty_state;
    @BindView(R.id.invite)
    Button invite;
    @BindView(R.id.loading_screen)
    RelativeLayout loading_screen;
    private ArrayList<ContactEntity> c = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    Observer<List<ContactEntity>> observer = new Observer<List<ContactEntity>>() {
        @Override
        public void onChanged(@Nullable List<ContactEntity> contact_entities) {
            contactsAdapter.addItems(contact_entities);
            loading_screen.setVisibility(View.GONE);
            if (contact_entities.size() > 0) {
                empty_state.setVisibility(View.GONE);
            } else {
                empty_state.setVisibility(View.VISIBLE);
            }
            done.hide();
        }
    };
    private Utils utils = new Utils();
    private ContactsActivityViewModel contactsFragmentViewModel;
    private String UID;
    private MyApplication myApplication;

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(ConstantLog.ViewConstants.CONTACT_TAG, ConstantLog.MethodConstants.ONCREATEVIEW_TAG);
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, view);
        contactsFragmentViewModel = ViewModelProviders.of(this).get(ContactsActivityViewModel.class);
        UID = getMyapp().getUID();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactsAdapter = new ContactsAdapter(getActivity(), new ArrayList<ContactEntity>());
        recyclerView.setAdapter(contactsAdapter);
        contactsFragmentViewModel.getContactList(getMyapp().getDatabase()).observe(getActivity(), observer);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public MyApplication getMyapp() {
        return (MyApplication) getActivity().getApplicationContext();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    public String getUID() {
        return UID;
    }


}
