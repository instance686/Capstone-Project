package com.reminders.location.locatoinreminder.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by ayush on 26/12/17.
 */

public class WalkthroughActivityViewModel extends ViewModel{


    private MutableLiveData<Integer> imageSource=new MutableLiveData<>();
    private MutableLiveData<String> heading=new MutableLiveData<>();
    private MutableLiveData<String> subHeading=new MutableLiveData<>();


    public MutableLiveData<Integer> getImageSource() {
        return imageSource;
    }

    public MutableLiveData<String> getSubHeading() {
        return subHeading;
    }

    public MutableLiveData<String> getHeading(){
        return heading;
    }

}
