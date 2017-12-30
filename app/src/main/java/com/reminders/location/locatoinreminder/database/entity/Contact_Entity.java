package com.reminders.location.locatoinreminder.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by ayush on 31/12/17.
 */
@Entity
public class Contact_Entity {
    @NonNull@PrimaryKey@ColumnInfo(name="contact_number")
    private String number;
    @ColumnInfo(name="contact_name")
    private String name;
    @ColumnInfo(name="contact_selection")
    private boolean selection;

    public Contact_Entity(String number, @NonNull String name, boolean selection) {
        this.number = number;
        this.name = name;
        this.selection = selection;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }
}
