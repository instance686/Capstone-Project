package com.reminders.location.locatoinreminder.view.adapters;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.viewmodel.WalkthroughActivityViewModel;

/**
 * Created by ayush on 25/12/17.
 */

public class WalkThroughViewPagerAdapter extends PagerAdapter {

    LifecycleOwner lifecycleOwner;
    LayoutInflater layoutInflater;
    ImageView displayImage;
    final Observer<Integer> imageSrcObserver = (@Nullable Integer integer) -> {
        displayImageSrc(integer);

    };
    TextView heading;
    final Observer<String> headingObserver = (@Nullable String s) -> {
        displayHeading(s);
    };
    TextView subheading;
    final Observer<String> subHeadingObserver = (@Nullable String s) -> {
        displaySubHeading(s);
    };
    private WalkthroughActivityViewModel walkthroughActivityViewModel;

    public WalkThroughViewPagerAdapter(LifecycleOwner lifecycleOwner,
                                       LayoutInflater layoutInflater, WalkthroughActivityViewModel walkthroughActivityViewModel) {
        this.layoutInflater = layoutInflater;
        this.walkthroughActivityViewModel = walkthroughActivityViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.pageritem, container, false);

        displayImage = (ImageView) view.findViewById(R.id.image);
        heading = (TextView) view.findViewById(R.id.heading);
        subheading = (TextView) view.findViewById(R.id.subheading);
        walkthroughActivityViewModel.getImageSource().observe(lifecycleOwner, imageSrcObserver);
        walkthroughActivityViewModel.getHeading().observe(lifecycleOwner, headingObserver);
        walkthroughActivityViewModel.getSubHeading().observe(lifecycleOwner, subHeadingObserver);

        switch (position) {
            case 0:
                walkthroughActivityViewModel.getImageSource().setValue(R.drawable.ic_walkthrough1);
                walkthroughActivityViewModel.getHeading().setValue(ConstantVar.SEND_LOCATION_TASKS);
                walkthroughActivityViewModel.getSubHeading().setValue(ConstantVar.LOCATION_TASK_DESC);
                break;
            case 1:
                walkthroughActivityViewModel.getImageSource().setValue(R.drawable.ic_walkthrough2);
                walkthroughActivityViewModel.getHeading().setValue(ConstantVar.SAVE_REMINDER_SELF);
                walkthroughActivityViewModel.getSubHeading().setValue(ConstantVar.SAVE_REMINDER_SELF_DESC);
                break;
            case 2:
                walkthroughActivityViewModel.getImageSource().setValue(R.drawable.ic_walkthrough3);
                walkthroughActivityViewModel.getHeading().setValue(ConstantVar.FIND_FRIENDS);
                walkthroughActivityViewModel.getSubHeading().setValue(ConstantVar.FIND_FRIENDS_DESC);
                break;
        }
        container.addView(view);
        return view;
    }

    public void displayImageSrc(Integer src) {
        displayImage.setImageResource(src);
    }

    public void displayHeading(String s) {
        heading.setText(s);
    }

    public void displaySubHeading(String s) {
        subheading.setText(s);
    }
}