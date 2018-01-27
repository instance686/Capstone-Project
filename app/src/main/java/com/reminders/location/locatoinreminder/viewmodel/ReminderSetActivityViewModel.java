package com.reminders.location.locatoinreminder.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCardsEntity;

import java.util.List;

/**
 * Created by ayush on 7/1/18.
 */

public class ReminderSetActivityViewModel extends ViewModel {

    LiveData<List<ChatCardsEntity>> chatCardsEntityLiveData;

    public LiveData<List<ChatCardsEntity>> getChatCardsEntityLiveData(AppDatabase appDatabase, int cardID) {
        //chatCardsEntityLiveData=appDatabase.cardDoa().getCard(cardID);
        return chatCardsEntityLiveData;
    }

    public void setChatCardsEntityLiveData(LiveData<List<ChatCardsEntity>> chatCardsEntityMutableLiveData) {
        this.chatCardsEntityLiveData = chatCardsEntityMutableLiveData;
    }
}
