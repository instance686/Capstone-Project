package com.reminders.location.locatoinreminder.adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reminders.location.locatoinreminder.R;

/**
 * Created by ayush on 25/12/17.
 */

public class WalkThroughViewPagerAdapter extends PagerAdapter {
    LayoutInflater layoutInflater;
    public WalkThroughViewPagerAdapter(LayoutInflater layoutInflater){
        this.layoutInflater=layoutInflater;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.pageritem, container, false);
        ImageView displayImage = (ImageView) view.findViewById(R.id.image);
        TextView heading = (TextView) view.findViewById(R.id.heading);
        TextView subheading = (TextView) view.findViewById(R.id.subheading);
        switch (position) {
            case 0:
                displayImage.setImageResource(R.drawable.ic_walkthrough1);
                heading.setText("");
                subheading.setText("");
                break;
            case 1:
                displayImage.setImageResource(R.drawable.ic_walkthrough2);
                heading.setText("");
                subheading.setText("");
                break;
            case 2:
                displayImage.setImageResource(R.drawable.ic_walkthrough3);
                heading.setText("");
                subheading.setText("");
                break;
        }
        container.addView(view);
        return view;
    }
}
