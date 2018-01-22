package com.reminders.location.locatoinreminder.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;
import com.reminders.location.locatoinreminder.pojo.ShoutsData;

import java.util.List;

/**
 * Created by ayush on 23/1/18.
 */

public class ShoutsFragmentViewModel extends ViewModel {
MutableLiveData<List<ChatCards_Entity>> shoutsData=new MutableLiveData<>();

    public MutableLiveData<List<ChatCards_Entity>> getShoutsData() {
        return shoutsData;
    }

    public void setShoutsData(MutableLiveData<List<ChatCards_Entity>> shoutsData) {
        this.shoutsData = shoutsData;
    }
}
