package com.reminders.location.locatoinreminder.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.reminders.location.locatoinreminder.database.entity.ReminderContact;

import java.util.List;

/**
 * Created by ayush on 12/1/18.
 */
@Dao
public interface ReminderContactDoa {
    @Query("SELECT * FROM reminder_chat")
    LiveData<List<ReminderContact>> getCurrentChats();

    @Query("SELECT count(*) FROM reminder_chat WHERE contact_num=:phone")
    int chatCardPresent(String phone);


    @Query("SELECT * FROM reminder_chat WHERE contact_num=:phone")
    ReminderContact getTopCardFromNumber(String phone);

    @Query("SELECT card_visibilty FROM reminder_chat WHERE contact_num=:phone")
    boolean isCardVisible(String phone);

    @Insert
    void inserChatCard(ReminderContact reminderContact);

    @Delete
    void deleteChatCard(ReminderContact reminderContact);

    @Update
    void updateChatCard(ReminderContact reminderContact);


    @Query("DELETE FROM reminder_chat WHERE contact_num =:number")
    void deleteCard(String number);

    @Query("DELETE FROM reminder_chat")
    void deleteAll();
}