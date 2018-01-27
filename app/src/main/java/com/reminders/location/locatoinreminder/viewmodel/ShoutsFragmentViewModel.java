package com.reminders.location.locatoinreminder.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.reminders.location.locatoinreminder.database.entity.ChatCardsEntity;

import java.util.List;

/**
 * Created by ayush on 23/1/18.
 */

public class ShoutsFragmentViewModel extends ViewModel {
    MutableLiveData<List<ChatCardsEntity>> shoutsData = new MutableLiveData<>();

    public MutableLiveData<List<ChatCardsEntity>> getShoutsData() {
        return shoutsData;
    }

    public void setShoutsData(MutableLiveData<List<ChatCardsEntity>> shoutsData) {
        this.shoutsData = shoutsData;
    }
}
