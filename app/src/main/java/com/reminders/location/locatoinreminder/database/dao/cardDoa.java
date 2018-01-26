package com.reminders.location.locatoinreminder.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;

import java.util.List;

/**
 * Created by ayush on 7/1/18.
 */
@Dao
public interface cardDoa {

    @Query("SELECT * FROM chat_card_entity WHERE contact_number_reciever=:phone")
    LiveData<List<ChatCards_Entity>> getCards(String phone);

    @Query("SELECT * FROM chat_card_entity")
    List<ChatCards_Entity> getCardsForLocation();


    @Query("SELECT count(*) FROM chat_card_entity WHERE card_id=:cardId")
    int checkCard(int cardId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCard(ChatCards_Entity chatCards_entity);

    @Query("SELECT count(*) FROM chat_card_entity WHERE contact_number_reciever=:phone")
    int getContactCardCount(String phone);

    @Query("DELETE FROM chat_card_entity WHERE card_id IN (:cardsList)")
    void deleteCard(List<Integer> cardsList);

    @Query("DELETE FROM chat_card_entity WHERE contact_number_reciever =:number")
    void deleteContactCards(String number);

    @Query("SELECT * FROM chat_card_entity WHERE card_id=:cardID")
    List<ChatCards_Entity> getCard(int cardID);

    @Update
    void updateCard(ChatCards_Entity chatCards_entity);

    @Query("SELECT * FROM chat_card_entity")
     Cursor getAllCards();

    @Delete
    void deleteCard(ChatCards_Entity chatCardsEntity);

}