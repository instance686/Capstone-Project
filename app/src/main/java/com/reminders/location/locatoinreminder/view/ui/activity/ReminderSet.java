package com.reminders.location.locatoinreminder.view.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;
import com.reminders.location.locatoinreminder.database.entity.ReminderContact;
import com.reminders.location.locatoinreminder.executor.ChecklistItemClicked;
import com.reminders.location.locatoinreminder.pojo.ContactFetch;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;
import com.reminders.location.locatoinreminder.singleton.ToastMessage;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;
import com.reminders.location.locatoinreminder.viewmodel.ReminderSetActivityViewModel;

import java.util.List;

import butterknife.BindView;

public class ReminderSet extends BaseActivity implements View.OnClickListener,ChecklistItemClicked
        // ,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
        //,ResultCallback<Status>
{
    @BindView(R.id.appBar)
    AppBarLayout appBarLayout;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.note)
    EditText note;
    @BindView(R.id.addLocation)
    Button addLocation;
    @BindView(R.id.bottom_sheet)
    View bottomSheet;
    @BindView(R.id.background)
    CoordinatorLayout background;
    @BindView(R.id.bottomCard)
    CardView bottomCard;

    BottomSheetBehavior bottomSheetBehavior;
    DatabaseReference databaseReference;
    AppDatabase appDatabase;
    boolean bottomSheetShowing=false;
    boolean insertionFinished=true;

    int currentColor=R.color.colorMainBackground1;

    //GoogleApiClient mGoogleApiClient;
    protected GeoDataClient mGeoDataClient;

    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builder;

    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();
    private ReminderSetActivityViewModel reminderSetActivityViewModel;
    private ChatCards_Entity chatCardsEntity;
    private String location="ABCD";
    private String time="Edited:"+"DUMMY";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase=getMyapp().getDatabase();
        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet);

         if(!getIntent().getBooleanExtra(ConstantVar.UPDATE_CARD,false)) {
             setCurrentColor(currentColor);
         }
         else
         {
             AsyncTask.execute(()->{
                 List<ChatCards_Entity> chatCardsEntities=appDatabase.cardDoa().getCard(getIntent().getIntExtra(ConstantVar.CARD_CLICKED,0));
                 chatCardsEntity=chatCardsEntities.get(0);
                 titleHandling(chatCardsEntity.getCardTitle());
                 notesHandling(true,chatCardsEntity.getNotes());
                 checkListHandling(chatCardsEntity.isCheckListPresent(),chatCardsEntity.getCheckListData());
                 locationHandling(chatCardsEntity.getLocation());
                 colorHandling(chatCardsEntity.getColor());
                 editedTimeHandling(chatCardsEntity.getTime());
             });

         }
        // builder = new PlacePicker.IntentBuilder();



        /*mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
*/
    }


    public void titleHandling(String titleText){
        if(!(titleText==null) || !titleText.trim().equalsIgnoreCase("")){
            title.setText(titleText);
        }
        else {
            title.setHint(getResources().getString(R.string.titlehint));
        }
    }

    public void notesHandling(boolean notesPresent,String notesdata){
        Log.v("NotesPresernt",""+notesPresent);
        if(notesPresent){
            Log.v("NotesData",notesdata);
            if(!(notesdata==null) || !notesdata.trim().equalsIgnoreCase("")){
                note.setText(notesdata);
            }
            else {
                note.setHint(getResources().getString(R.string.note_hint));
            }
        }

    }
    public void checkListHandling(boolean checkListPresent,String checkListData){

    }
    public void locationHandling(String location){

    }
    public void colorHandling(int color){
        setCurrentColor(color);
    }
    public void editedTimeHandling(String editedTime){

    }
    @Override
    protected void onStart() {
        super.onStart();
        // mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveNote(){

        if(!getIntent().getBooleanExtra(ConstantVar.UPDATE_CARD,false))
            insertCard();
        else
            updateCard();


    }

    public void insertCard(){
        int cardId=(int)System.currentTimeMillis();
        String cardTitle=title.getText().toString().trim();
        ContactFetch contactFetch=new ContactFetch(getIntent().getStringExtra(ConstantVar.CONTACT_NAME),
                getIntent().getStringExtra(ConstantVar.CHAT_ID));
        String notes=note.getText().toString().trim();
        if(notes.equalsIgnoreCase(""))
            ToastMessage.showMessageLong(this,"Please enter a note text");
        else {
            ChatCards_Entity chatCards_entity=
                    new ChatCards_Entity(cardId,cardTitle,contactFetch,
                            sharedPreferenceSingleton.getSavedString(this,ConstantVar.CONTACT_SELF_NUMBER)
                            ,notes,
                            location,currentColor,time,false,false
                    );
            AsyncTask.execute(()->{
                //insert chat card
                appDatabase.cardDoa().insertCard(chatCards_entity);
                //get contact card count
                int countReminderCard=appDatabase.cardDoa().getContactCardCount(contactFetch.getContact_number());


                if(countReminderCard >= 1){
                    //get reminder card count
                    int reminderContactCount=appDatabase.reminderContactDoa().chatCardPresent(contactFetch.getContact_number());

                    if(reminderContactCount>0){
                        setSharedValues(true,false,contactFetch.getContact_number());
                        //update contact card
                        appDatabase.reminderContactDoa().updateChatCard(new ReminderContact(contactFetch.getContact_number(),
                                contactFetch.getContact_name(),countReminderCard,false,true,System.currentTimeMillis()));
                    }
                    else {
                        setSharedValues(false,true,contactFetch.getContact_number());
                        //insert contact card
                        appDatabase.reminderContactDoa().inserChatCard(new ReminderContact(contactFetch.getContact_number(),
                                contactFetch.getContact_name(),countReminderCard,false,true ,System.currentTimeMillis()));
                    }
                }
            });

            finish();
        }
    }
    public void updateCard(){
        int cardId=getIntent().getIntExtra(ConstantVar.CARD_CLICKED,0);
        String cardTitle=title.getText().toString().trim();
        String notes=note.getText().toString().trim();
        ContactFetch contactFetch=new ContactFetch(getIntent().getStringExtra(ConstantVar.CONTACT_NAME),
                getIntent().getStringExtra(ConstantVar.CHAT_ID));
        if(notes.equalsIgnoreCase("")){
            ToastMessage.showMessageLong(this,"Please enter a note text");
        }
        else{
            ChatCards_Entity chatCards_entity=
                    new ChatCards_Entity(cardId,cardTitle,contactFetch,
                            sharedPreferenceSingleton.getSavedString(this,ConstantVar.CONTACT_SELF_NUMBER)
                            ,notes,
                            location,currentColor,time,false,false
                    );
            AsyncTask.execute(()->{
                //update chat card
                appDatabase.cardDoa().updateCard(chatCards_entity);
                //get contact card count
                int countReminderCard=appDatabase.cardDoa().getContactCardCount(contactFetch.getContact_number());

                if(countReminderCard >= 1){
                    //get reminder card count
                    int reminderContactCount=appDatabase.reminderContactDoa().chatCardPresent(contactFetch.getContact_number());

                    if(reminderContactCount>0){
                        setSharedValues(true,false,contactFetch.getContact_number());
                        //update contact card
                        appDatabase.reminderContactDoa().updateChatCard(new ReminderContact(contactFetch.getContact_number(),
                                contactFetch.getContact_name(),countReminderCard,false,true,System.currentTimeMillis()));
                    }
                    else {
                        setSharedValues(false,true,contactFetch.getContact_number());
                        //insert contact card
                        appDatabase.reminderContactDoa().inserChatCard(new ReminderContact(contactFetch.getContact_number(),
                                contactFetch.getContact_name(),countReminderCard,false,true ,System.currentTimeMillis()));
                    }
                }
            });
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else {
            finish();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_reminder_set;
    }

    public MyApplication getMyapp() {
        return (MyApplication) getApplicationContext();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addExtra:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.save:
                saveNote();
                break;
            case R.id.back:
                onBackPressed();
            case R.id.bottomCard:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
    }

    public void bottomSheetButtonClicked(View view){
        switch (view.getId()){
            case R.id.addLocation:
                /*try {
                    startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }*/
                break;

            case R.id.color1:
                setCurrentColor(ConstantVar.backgroundColors[0]);
                break;
            case R.id.color2:
                setCurrentColor(ConstantVar.backgroundColors[1]);
                break;
            case R.id.color3:
                setCurrentColor(ConstantVar.backgroundColors[2]);
                break;
            case R.id.color4:
                setCurrentColor(ConstantVar.backgroundColors[3]);
                break;
            case R.id.color5:
                setCurrentColor(ConstantVar.backgroundColors[4]);
                break;
            case R.id.color6:
                setCurrentColor(ConstantVar.backgroundColors[5]);
                break;
        }
    }

    @Override
    public void itemClicked() {
        if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void setCurrentColor(int colorSelected){
        background.setBackgroundColor(ContextCompat.getColor(this,colorSelected));
        appBarLayout.setBackgroundColor(ContextCompat.getColor(this,colorSelected));
        bottomCard.setCardBackgroundColor(ContextCompat.getColor(this,colorSelected));
        currentColor=colorSelected;
    }
    public void setSharedValues(boolean val1,boolean val2,String number){
        sharedPreferenceSingleton.saveAs(this,ConstantVar.UPDATION,val1);
        sharedPreferenceSingleton.saveAs(this,ConstantVar.INSERTION,val2);
        sharedPreferenceSingleton.saveAs(this,ConstantVar.UPDATED_NUMBER,number);

    }


}