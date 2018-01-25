package com.reminders.location.locatoinreminder.view.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.view.ui.fragments.ContactsFragment;
import com.reminders.location.locatoinreminder.view.ui.fragments.ReminderChat;
import com.reminders.location.locatoinreminder.view.ui.fragments.ShoutsFragment;

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
        else
            return new ShoutsFragment();

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return ConstantVar.REMINDER;
        else
            return ConstantVar.SHOUT;

    }


}
