package com.reminders.location.locatoinreminder.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 7/1/18.
 */

public class ChatActivityViewModel extends ViewModel {
    private LiveData<List<ChatCards_Entity>> cardsList;

    public LiveData<List<ChatCards_Entity>> getCardsList(AppDatabase appDatabase,String phone){
        cardsList=appDatabase.cardDoa().getCards(phone);
        return cardsList;
    }

}
