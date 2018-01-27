package com.reminders.location.locatoinreminder.constants;

import com.reminders.location.locatoinreminder.R;

/**
 * Created by ayush on 25/12/17.
 */

public interface ConstantVar {

    String APP_PACKAGE_NAME="com.reminders.location.locatoinreminder";

    String INTENT_SERVICE_NAME="ServerService";

    String REMINDER = "REMINDER";
    String REMINDER_SPACE=" Reminder";
    String SHOUT = "SHOUT";
    String CONTACTS = "CONTACTS";
    String APPNAME = "LOCSHOUT";
    String VERIFY_PHN = "Verify Phonenumber";
    String LOGGED = "LOGGED";
    String CONTACT_SELF_NUMBER = "Contact_Self_Number";
    String CONSTANT_SELF_NAME = "Contact_Self_Name";
    String SELF_SAVED = "SELF_SAVED";
    String CHAT_ID = "CHAT_ID";
    String CONTACT_NAME = "CONTACT_NAME";
    String INSERTION_CARD = "INSERTION_CARD";
    String DELETION_CARD = "DELETION_CARD";

    String mBroadcastArrayListAction = "com.truiton.broadcast.arraylist";
    String fromActivityToFragment = "com.location.broadcast.arraylist";
    String APPWIDGET_ID = "APPWIDGET_ID";
    String REMINDER_DATA = "REMINDER_DATA";
    String WIDGET_IDS = "WIDGET_IDS";

    String WIDGET_BUTTON_CLICKED = "com.reminders.location.locatoinreminder.REFRESH_BUTTON";


    int[] backgroundColors = {R.color.colorMainBackground1, R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5};

    int RC_SIGN_IN = 9001;
    int CONTACT_LOADER = 1;
    int NOTES_CLICKED = 0;
    int CHECKLIST_CLICKED = 1;
    int INSERTCHAT = 0;
    int UPDATECHAT = 1;
    int DELETECHAT = 2;
    int INSERTCONTACT = 3;
    int UPDATECONACT = 4;
    int DELETECONTACT = 5;

    String FROM_NOTIFICATION = "FROM_NOTIFICATION";

    String NEW_CHOICE = "NEW CHOICE";
    String NOTES_BUTTON = "Notesbutton";
    String CHECKLIST_BUTTON = "Checklistbutton";
    String REFRESH_BUTTON = "Refreshbutton";
    String CLICKFROMCHATCARDS = "CLICKFROMCHATCARDS";
    String FROMCHATCRADS = "FROMCHATCRADS";

    String CLICKFROMCONTACTCARDS = "CLICKFROMCONTACTCARD";
    String FROMCONTACTCARDS = "FROMCONTACTCARDS";
    String UPDATED_NUMBER = "UPDATED_NUMBER";
    String UPDATION = "UPDATION";
    String INSERTION = "INSERTION";
    String DELETION = "DELETION";
    String CARD_CLICKED = "CARD_CLICKED";
    String UPDATE_CARD = "UPDATE_CARD";
    String INTENT_SERVICE_CHOICE = "INTENT_SERVICE_CHOICE";
    String CARD_IDS_DELETION = "CARD_ID_FOR_DELETION";
    String CURRENT_LATITUDE = "CURRENT_LATITUDE";
    String CURRENT_LONGITUDE = "CURRENT_LONGITUDE";
    String LAST_NOTIFICATION_TIME = "LAST_NOTIFICATION_TIME";
    String NOTIFICATION_REMINDERS = "NOTIFICATION_REMINDERS";


    String CACHED_CONTACTS="Cached_Contacts";

    String REMINDERS="reminders";

    String NEW_REMINDERS_NEARBY="You have reminders near by";
    String TOUCH_TO_VIEW="Touch to view them";
    String DELETE_CHAT_WITH="Delete Chat with ";

    String SEND_LOCATION_TASKS="Send Location Tasks";
    String LOCATION_TASK_DESC="You can add,edit and send reminders related to a particular location to your family and friends";

    String SAVE_REMINDER_SELF="Save reminder for yourself";
    String SAVE_REMINDER_SELF_DESC="Add a reminder for yourself as well just by selecting your name in the contact list.";

    String FIND_FRIENDS="Find your friends";
    String FIND_FRIENDS_DESC="Allow LocationReminder to access your contacts so you can find your friends faster.";
    String EDITTIME_FORMAT="HH:mm,dd-MM-yyyy ";
    String EDITED="EDITED: ";

    String LOCATION_SET_TO="Location Set To:-";

    String ENTER_NOTE_TEXT="Please enter a note text";
    String ENTER_LOCATION="Please select a location";

    String PARCALABLE_LIST_NAME="TEST";
    String NO_NEARBY_REMINDERS="No reminders for near by area!";

    String USERS="users";
    String COLLATE_LOCALIZED_ASC="COLLATE LOCALIZED ASC";

   String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";



}