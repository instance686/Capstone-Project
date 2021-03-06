package com.reminders.location.locatoinreminder.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.reminders.location.locatoinreminder.pojo.ContactFetch;

/**
 * Created by ayush on 7/1/18.
 */


@Entity(tableName = "chat_card_entity")
public class ChatCardsEntity implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public ChatCardsEntity createFromParcel(Parcel in) {
            return new ChatCardsEntity(in);
        }

        public ChatCardsEntity[] newArray(int size) {
            return new ChatCardsEntity[size];
        }
    };
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "card_id")
    private int cardId;
    @ColumnInfo(name = "card_title")
    private String cardTitle;
    @Embedded
    private ContactFetch contactFetch;
    @ColumnInfo(name = "sender_contact_number")
    private String sendContact;
    @ColumnInfo(name = "notes_present")
    private boolean notesPresent;
    @ColumnInfo(name = "notes_data")
    private String notes;
    @ColumnInfo(name = "check_notes")
    private boolean checkListPresent;
    @ColumnInfo(name = "check_list_data")
    private String checkListData;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "color_code")
    private int color;
    @ColumnInfo(name = "edited_time")
    private String time;
    @ColumnInfo(name = "selected_card")
    private boolean selected;
    @ColumnInfo(name = "sent_success")
    private boolean sentSuccess;
    @ColumnInfo(name = "update_time")
    private long editMilliseconds;

    public ChatCardsEntity(final Parcel source) {
        cardId = source.readInt();
        cardTitle = source.readString();
        contactFetch.setContact_number(source.readString());
        contactFetch.setContact_name(source.readString());
        sendContact = source.readString();
        notes = source.readString();
        location = source.readString();
        color = source.readInt();
        time = source.readString();
        editMilliseconds = source.readLong();
    }


    public ChatCardsEntity(@NonNull int cardId, String cardTitle, ContactFetch contactFetch,
                           String sendContact, String notes,
                           String location,
                           int color, String time, boolean selected, boolean sentSuccess,
                           long editMilliseconds) {
        this.cardId = cardId;
        this.cardTitle = cardTitle;
        this.contactFetch = contactFetch;
        this.sendContact = sendContact;
        this.notesPresent = notesPresent;
        this.notes = notes;
        this.checkListPresent = checkListPresent;
        this.checkListData = checkListData;
        this.location = location;
        this.color = color;
        this.time = time;
        this.selected = selected;
        this.sentSuccess = sentSuccess;
        this.editMilliseconds = editMilliseconds;

    }

    public ChatCardsEntity() {
    }

    public long getEditMilliseconds() {
        return editMilliseconds;
    }

    public void setEditMilliseconds(long editMilliseconds) {
        this.editMilliseconds = editMilliseconds;
    }

    public String getSendContact() {
        return sendContact;
    }

    public void setSendContact(String sendContact) {
        this.sendContact = sendContact;
    }

    public boolean isSentSuccess() {
        return sentSuccess;
    }

    public void setSentSuccess(boolean sentSuccess) {
        this.sentSuccess = sentSuccess;
    }

    @NonNull
    public int getCardId() {
        return cardId;
    }

    public void setCardId(@NonNull int cardId) {
        this.cardId = cardId;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public ContactFetch getContactFetch() {
        return contactFetch;
    }

    public void setContactFetch(ContactFetch contactFetch) {
        this.contactFetch = contactFetch;
    }

    public boolean isNotesPresent() {
        return notesPresent;
    }

    public void setNotesPresent(boolean notesPresent) {
        this.notesPresent = notesPresent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isCheckListPresent() {
        return checkListPresent;
    }

    public void setCheckListPresent(boolean checkListPresent) {
        this.checkListPresent = checkListPresent;
    }

    public String getCheckListData() {
        return checkListData;
    }

    public void setCheckListData(String checkListData) {
        this.checkListData = checkListData;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(cardId);
        dest.writeString(cardTitle);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dest.writeTypedObject(contactFetch, flags);
        }*/
        dest.writeString(contactFetch.getContact_number());
        dest.writeString(contactFetch.getContact_name());
        dest.writeString(sendContact);
        dest.writeString(notes);
        dest.writeString(location);
        dest.writeInt(color);
        dest.writeString(time);
        dest.writeLong(editMilliseconds);

    }
}