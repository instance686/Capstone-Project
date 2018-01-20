package com.reminders.location.locatoinreminder.pojo;

import android.arch.persistence.room.ColumnInfo;

/**
 * Created by ayush on 31/12/17.
 */
public class ContactFetch {
    @ColumnInfo(name = "contact_name_reciever")
    private String contact_name;
    @ColumnInfo(name = "contact_number_reciever")
    private String contact_number;

    public ContactFetch(){}

    public ContactFetch(String contact_name, String contact_number) {
        this.contact_name = contact_name;
        this.contact_number = contact_number;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }
}
