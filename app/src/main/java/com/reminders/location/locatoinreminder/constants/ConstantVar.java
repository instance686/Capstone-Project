package com.reminders.location.locatoinreminder.constants;

import com.reminders.location.locatoinreminder.R;

/**
 * Created by ayush on 25/12/17.
 */

public interface ConstantVar {

    String REMINDER="REMINDER";
    String SHOUT="SHOUT";
    String CONTACTS="CONTACTS";
    String APPNAME="LOCSHOUT";
    String VERIFY_PHN="Verify Phonenumber";
    String LOGGED="LOGGED";
    String CONTACT_SELF_NUMBER="Contact_Self_Number";
    String CONSTANT_SELF_NAME="Contact_Self_Name";
    String SELF_SAVED="SELF_SAVED";
    String CHAT_ID="CHAT_ID";
    String CONTACT_NAME="CONTACT_NAME";
    String INSERTION_CARD="INSERTION_CARD";
    String DELETION_CARD="DELETION_CARD";

    String mBroadcastArrayListAction = "com.truiton.broadcast.arraylist";
    String fromActivityToFragment="com.location.broadcast.arraylist";





    int[] backgroundColors={R.color.colorMainBackground1,R.color.color1,R.color.color2,R.color.color3,R.color.color4,R.color.color5};

    int RC_SIGN_IN = 9001;
    int CONTACT_LOADER=1;
    int NOTES_CLICKED=0;
    int CHECKLIST_CLICKED=1;
    int INSERTCHAT=0;
    int UPDATECHAT=1;
    int DELETECHAT=2;
    int INSERTCONTACT=3;
    int UPDATECONACT=4;
    int DELETECONTACT=5;

    String FROM_NOTIFICATION="FROM_NOTIFICATION";

    String NEW_CHOICE="NEW CHOICE";
    String NOTES_BUTTON="Notesbutton";
    String CHECKLIST_BUTTON="Checklistbutton";
    String REFRESH_BUTTON="Refreshbutton";
    String CLICKFROMCHATCARDS="CLICKFROMCHATCARDS";
    String FROMCHATCRADS="FROMCHATCRADS";

    String CLICKFROMCONTACTCARDS="CLICKFROMCONTACTCARD";
    String FROMCONTACTCARDS="FROMCONTACTCARDS";
    String UPDATED_NUMBER ="UPDATED_NUMBER" ;
    String UPDATION ="UPDATION" ;
    String INSERTION ="INSERTION" ;
    String DELETION="DELETION";
    String CARD_CLICKED ="CARD_CLICKED" ;
    String UPDATE_CARD ="UPDATE_CARD" ;
    String INTENT_SERVICE_CHOICE="INTENT_SERVICE_CHOICE";
    String CARD_IDS_DELETION="CARD_ID_FOR_DELETION";
    String CURRENT_LATITUDE="CURRENT_LATITUDE";
    String CURRENT_LONGITUDE="CURRENT_LONGITUDE";
    String LAST_NOTIFICATION_TIME="LAST_NOTIFICATION_TIME";
    String NOTIFICATION_REMINDERS="NOTIFICATION_REMINDERS";
}