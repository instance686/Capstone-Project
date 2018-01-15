package com.reminders.location.locatoinreminder.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;

/**
 * Created by ayush on 7/1/18.
 */

public class ReminderSetActivityViewModel extends ViewModel {

    MutableLiveData<ChatCards_Entity> chatCardsEntityMutableLiveData=new MutableLiveData<>();

    public MutableLiveData<ChatCards_Entity> getChatCardsEntityMutableLiveData(AppDatabase appDatabase,int cardID) {
        chatCardsEntityMutableLiveData=appDatabase.cardDoa().getCard(cardID);
        return chatCardsEntityMutableLiveData;
    }

    public void setChatCardsEntityMutableLiveData(MutableLiveData<ChatCards_Entity> chatCardsEntityMutableLiveData) {
        this.chatCardsEntityMutableLiveData = chatCardsEntityMutableLiveData;
    }
}
