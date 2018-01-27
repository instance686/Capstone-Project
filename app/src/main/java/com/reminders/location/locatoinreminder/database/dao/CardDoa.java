package com.reminders.location.locatoinreminder.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.reminders.location.locatoinreminder.database.entity.ChatCardsEntity;

import java.util.List;

/**
 * Created by ayush on 7/1/18.
 */
@Dao
public interface CardDoa {

    @Query("SELECT * FROM chat_card_entity WHERE contact_number_reciever=:phone")
    LiveData<List<ChatCardsEntity>> getCards(String phone);

    @Query("SELECT * FROM chat_card_entity")
    List<ChatCardsEntity> getCardsForLocation();


    @Query("SELECT count(*) FROM chat_card_entity WHERE card_id=:cardId")
    int checkCard(int cardId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCard(ChatCardsEntity chatCards_entity);

    @Query("SELECT count(*) FROM chat_card_entity WHERE contact_number_reciever=:phone")
    int getContactCardCount(String phone);

    @Query("DELETE FROM chat_card_entity WHERE card_id IN (:cardsList)")
    void deleteCard(List<Integer> cardsList);

    @Query("DELETE FROM chat_card_entity WHERE contact_number_reciever =:number")
    void deleteContactCards(String number);

    @Query("SELECT * FROM chat_card_entity WHERE card_id=:cardID")
    List<ChatCardsEntity> getCard(int cardID);

    @Update
    void updateCard(ChatCardsEntity chatCards_entity);

    @Query("SELECT * FROM chat_card_entity")
    Cursor getAllCards();

    @Delete
    void deleteCard(ChatCardsEntity chatCardsEntity);

}