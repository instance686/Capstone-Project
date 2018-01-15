package com.reminders.location.locatoinreminder.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by ayush on 10/1/18.
 */
@Entity(tableName = "reminder_chat")
public class ReminderContact {
    @NonNull@PrimaryKey@ColumnInfo(name="contact_num")
    String number;
    @ColumnInfo(name="contact_name")
    String name;
    @ColumnInfo(name="reminder_count")
    int reminderCount;
    @ColumnInfo(name="contact_selection")
    boolean selection=false;
    @ColumnInfo(name="card_visibilty")
    boolean cardVisibility=false;
    @ColumnInfo(name="current_milliseconds")
    long curentTimeinMilliseconds;


    public ReminderContact(@NonNull String number, String name,
                           int reminderCount, boolean selection,boolean cardVisibility,
                           long curentTimeinMilliseconds) {
        this.number = number;
        this.name = name;
        this.reminderCount = reminderCount;
        this.selection = selection;
        this.cardVisibility=cardVisibility;
        this.curentTimeinMilliseconds=curentTimeinMilliseconds;
    }

    public long getCurentTimeinMilliseconds() {
        return curentTimeinMilliseconds;
    }

    public void setCurentTimeinMilliseconds(long curentTimeinMilliseconds) {
        this.curentTimeinMilliseconds = curentTimeinMilliseconds;
    }

    public boolean isCardVisibility() {
        return cardVisibility;
    }

    public void setCardVisibility(boolean cardVisibility) {
        this.cardVisibility = cardVisibility;
    }

    @NonNull
    public String getNumber() {
        return number;
    }

    public void setNumber(@NonNull String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReminderCount() {
        return reminderCount;
    }

    public void setReminderCount(int reminderCount) {
        this.reminderCount = reminderCount;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }
}
