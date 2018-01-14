package com.reminders.location.locatoinreminder.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;

import java.util.List;

/**
 * Created by ayush on 7/1/18.
 */
@Dao
public interface cardDoa {

    @Query("SELECT * FROM chat_card_entity WHERE contact_number_reciever=:phone")
    LiveData<List<ChatCards_Entity>> getCards(String phone);


    @Query("SELECT count(*) FROM chat_card_entity WHERE card_id=:cardId")
    int checkCard(int cardId);

    @Insert
    void insertCard(ChatCards_Entity chatCards_entity);

    @Query("SELECT count(*) FROM chat_card_entity WHERE contact_number_reciever=:phone")
    int getContactCardCount(String phone);

    @Query("DELETE FROM chat_card_entity WHERE card_id IN (:cardsList)")
    void deleteCard(List<Integer> cardsList);

    @Update
    void updateCard(ChatCards_Entity chatCards_entity);

}
