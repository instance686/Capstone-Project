package com.reminders.location.locatoinreminder.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.view.fragments.ContactsFragment;
import com.reminders.location.locatoinreminder.view.fragments.ReminderChat;
import com.reminders.location.locatoinreminder.view.fragments.ShoutsFragment;

/**
 * Created by ayush on 25/12/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    public ViewPagerAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext=context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return new ReminderChat();
        else if(position==1)
            return new ShoutsFragment();
        else
            return new ContactsFragment();

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return ConstantVar.REMINDER;
        else if(position==1)
            return ConstantVar.SHOUT;
        else
            return ConstantVar.CONTACTS;

    }
}
