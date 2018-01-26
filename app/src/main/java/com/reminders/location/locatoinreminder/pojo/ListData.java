package com.reminders.location.locatoinreminder.pojo;

/**
 * Created by ayush on 26/1/18.
 */

public class ListData {
int cardId;
    String title;
    String note;
    String senderName;
    String location;

    public ListData(int cardId, String title, String note, String senderName, String location) {
        this.cardId = cardId;
        this.title = title;
        this.note = note;
        this.senderName = senderName;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
