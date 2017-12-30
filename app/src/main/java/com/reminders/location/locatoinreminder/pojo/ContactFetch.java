package com.reminders.location.locatoinreminder.pojo;

/**
 * Created by ayush on 31/12/17.
 */
public class ContactFetch {
    private String contact_name;
    private String contact_number;

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
