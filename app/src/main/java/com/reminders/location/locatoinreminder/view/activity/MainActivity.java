package com.reminders.location.locatoinreminder.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.adapters.ViewPagerAdapter;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(MainActivity.this,getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }


}
