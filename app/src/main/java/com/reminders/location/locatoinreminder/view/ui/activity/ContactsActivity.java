package com.reminders.location.locatoinreminder.view.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.database.entity.ContactEntity;
import com.reminders.location.locatoinreminder.executor.ContactSync;
import com.reminders.location.locatoinreminder.executor.TaskRunning;
import com.reminders.location.locatoinreminder.util.Utils;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;
import com.reminders.location.locatoinreminder.view.adapters.ContactsAdapter;
import com.reminders.location.locatoinreminder.viewmodel.ContactsActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

public class ContactsActivity extends BaseActivity implements TaskRunning {
    @BindView(R.id.contacts)
    RecyclerView recyclerView;
    @BindView(R.id.contact_toolbar)
    Toolbar contactToolBar;
    @BindView(R.id.button_done)
    FloatingActionButton done;
    Unbinder unbinder;
    @BindView(R.id.empty_state)
    LinearLayout empty_state;
    @BindView(R.id.invite)
    Button invite;
    @BindView(R.id.loading_screen)
    RelativeLayout loading_screen;
    @BindView(R.id.contact_number)
    TextView contact_number;
    private ContactsAdapter contactsAdapter;
    Observer<List<ContactEntity>> observer = new Observer<List<ContactEntity>>() {
        @Override
        public void onChanged(@Nullable List<ContactEntity> contact_entities) {
            contact_number.setText(contact_entities.size() + " "+getResources().getString(R.string.contacts));
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
    private ContactsActivityViewModel contactsActivityViewModel;
    private String UID;
    private MyApplication myApplication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        empty_state.setVisibility(View.GONE);
        contactsActivityViewModel = ViewModelProviders.of(this).get(ContactsActivityViewModel.class);
        UID = getMyapp().getUID();
        contactToolBar.inflateMenu(R.menu.contact_sync);
        contactToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.sync_contact) {
                    synchContacts();
                }
                return true;
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsAdapter = new ContactsAdapter(this, new ArrayList<ContactEntity>());
        recyclerView.setAdapter(contactsAdapter);
        contactsActivityViewModel.getContactList(getMyapp().getDatabase()).observe(this, observer);

    }

    public void synchContacts() {
        if (Utils.isConnectedToNetwork(ContactsActivity.this)) {
            empty_state.setVisibility(View.GONE);
            loading_screen.setVisibility(View.VISIBLE);
            ContactSync contactsSync = new ContactSync(this, getContentResolver(), getMyapp().getDatabase(), ContactsActivity.this);
            contactsSync.execute();
        }
    }

    public MyApplication getMyapp() {
        return (MyApplication) getApplicationContext();
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_contacts;
    }

    public String getUID() {
        return UID;
    }


    @Override
    public void taskRunning(boolean runVal) {
        if (runVal) {
            empty_state.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            loading_screen.setVisibility(View.VISIBLE);
        } else {
            empty_state.setVisibility(View.GONE);
            loading_screen.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        }
    }
}
