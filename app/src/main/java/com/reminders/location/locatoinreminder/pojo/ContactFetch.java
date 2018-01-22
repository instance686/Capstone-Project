package com.reminders.location.locatoinreminder.pojo;

import android.arch.persistence.room.ColumnInfo;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ayush on 31/12/17.
 */
public class ContactFetch implements Parcelable {
    @ColumnInfo(name = "contact_name_reciever")
    private String contact_name;
    @ColumnInfo(name = "contact_number_reciever")
    private String contact_number;

    public ContactFetch(Parcel source){
    contact_name=source.readString();
    contact_number=source.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contact_name);
        dest.writeString(contact_number);

    }
    public static final Creator CREATOR = new Creator() {
        public ContactFetch createFromParcel(Parcel in) {
            return new ContactFetch(in);
        }

        public ContactFetch[] newArray(int size) {
            return new ContactFetch[size];
        }
    };
}
