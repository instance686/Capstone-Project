package com.reminders.location.locatoinreminder.pojo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by ayush on 6/1/18.
 */
@IgnoreExtraProperties
public class User {

    private String Name;
    private String Phone;

    public User() {
    }

    public User(String Name, String Phone) {
        super();
        this.Name = Name;
        this.Phone = Phone;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

