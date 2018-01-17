package com.reminders.location.locatoinreminder.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;

import java.util.List;

/**
 * Created by ayush on 7/1/18.
 */

public class ReminderSetActivityViewModel extends ViewModel {

    LiveData<List<ChatCards_Entity>> chatCardsEntityLiveData;

    public LiveData<List<ChatCards_Entity>> getChatCardsEntityLiveData(AppDatabase appDatabase, int cardID) {
        //chatCardsEntityLiveData=appDatabase.cardDoa().getCard(cardID);
        return chatCardsEntityLiveData;
    }

    public void setChatCardsEntityLiveData(LiveData<List<ChatCards_Entity>> chatCardsEntityMutableLiveData) {
        this.chatCardsEntityLiveData = chatCardsEntityMutableLiveData;
    }
}
