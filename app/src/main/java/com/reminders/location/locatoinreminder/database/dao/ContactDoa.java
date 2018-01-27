package com.reminders.location.locatoinreminder.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.reminders.location.locatoinreminder.database.entity.ContactEntity;

import java.util.List;

/**
 * Created by ayush on 15/11/17.
 */

@Dao
public interface ContactDoa {

    @Query("SELECT * FROM ContactEntity")
    LiveData<List<ContactEntity>> getAll();

    @Insert
    void insertFeed(List<ContactEntity> contact_entities);

    @Query("DELETE FROM ContactEntity")
    public void clearContacts();

    /*@Query("SELECT COUNT(*) FROM contact_entity WHERE contact_number=:phone")
    int contactCardCount(String phone);*/

    /*@Query("SELECT contact_name,contact_number FROM contact_entity WHERE contact_number=:phone")
    ContactFetch getContact(String phone);
*/

}