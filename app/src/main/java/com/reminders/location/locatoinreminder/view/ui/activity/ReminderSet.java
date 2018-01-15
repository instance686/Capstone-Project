package com.reminders.location.locatoinreminder.view.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;
import com.reminders.location.locatoinreminder.view.adapters.CheckBoxAdapter;
import com.reminders.location.locatoinreminder.viewmodel.ReminderSetActivityViewModel;

import java.util.ArrayList;

import butterknife.BindView;

public class ReminderSet extends BaseActivity implements View.OnClickListener,ChecklistItemClicked
       // ,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
        //,ResultCallback<Status>
        {

    @BindView(R.id.toolbar_set)
    Toolbar toolbar;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.note)
    EditText note;
    @BindView(R.id.checkbox_container)
    RecyclerView recyclerView;
    @BindView(R.id.add_checkbox)
    Button add_checkbox;
    CheckBoxAdapter checkBoxAdapter;
    ArrayList<String> checkboxNotes=new ArrayList<>();
    @BindView(R.id.addExtra)
    ImageView addExtra;
    @BindView(R.id.addLocation)
    Button addLocation;
    @BindView(R.id.bottom_sheet)
    View bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    DatabaseReference databaseReference;
    AppDatabase appDatabase;
    boolean notesShowing,checklistShowing;
    boolean bottomSheetShowing=false;
    boolean insertionFinished=true;

            //GoogleApiClient mGoogleApiClient;
    protected GeoDataClient mGeoDataClient;

    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builder;

    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();
    private ReminderSetActivityViewModel reminderSetActivityViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        int choice=getIntent().getIntExtra(ConstantVar.NEW_CHOICE,0);
        manageLayoutChoice(choice);


        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        toolbar.inflateMenu(R.menu.reminder_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.save)
                {
                    saveButtonClicked();
                }
                return true;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkboxNotes.add(null);
        checkBoxAdapter=new CheckBoxAdapter(this,checkboxNotes);
        recyclerView.setAdapter(checkBoxAdapter);
        add_checkbox.setOnClickListener(this);
        addExtra.setOnClickListener(this);
        appDatabase=getMyapp().getDatabase();


        reminderSetActivityViewModel= ViewModelProviders.of(this).get(ReminderSetActivityViewModel.class);
        reminderSetActivityViewModel.getChatCardsEntityMutableLiveData(getMyapp().getDatabase(),
                getIntent().getIntExtra(ConstantVar.CARD_CLICKED,0)).observe(this,observer);

        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet);
        if(getIntent().getIntExtra(ConstantVar.NEW_CHOICE,0)==ConstantVar.NOTES_CLICKED){
            notesShowing=true;checklistShowing=false;
        }
        else if(getIntent().getIntExtra(ConstantVar.NEW_CHOICE,0)==ConstantVar.CHECKLIST_CLICKED){
            notesShowing=false;checklistShowing=true;

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

    Observer<ChatCards_Entity> observer=(chatCards_entity)->{
        setActivityData(chatCards_entity);
    };


    public void setActivityData(ChatCards_Entity chatCardsEntity){
        titleHandling(chatCardsEntity.getCardTitle());
        notesHandling(chatCardsEntity.isNotesPresent(),chatCardsEntity.getNotes());
        checkListHandling(chatCardsEntity.isCheckListPresent(),chatCardsEntity.getCheckListData());
        locationHandling(chatCardsEntity.getLocation());
        colorHandling(chatCardsEntity.getColor());
        editedTimeHandling(chatCardsEntity.getTime());


    }

    public void titleHandling(String titleText){
        if(!(titleText==null) || !titleText.equalsIgnoreCase("")){
            title.setText(titleText);
        }
        else {
            title.setHint(getResources().getString(R.string.titlehint));
        }
    }

    public void notesHandling(boolean notesPresent,String notesdata){
        if(notesPresent){

        }

    }
    public void checkListHandling(boolean checkListPresent,String checkListData){

    }
    public void locationHandling(String location){

    }
    public void colorHandling(int color){

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

    private void saveButtonClicked(){
        int cardId=(int)System.currentTimeMillis();
        Log.v("CARDID",""+cardId);
        String cardTitle=title.getText().toString().trim();
        Log.v("CARD_TITLE",""+cardTitle);
        ContactFetch contactFetch=new ContactFetch(getIntent().getStringExtra(ConstantVar.CONTACT_NAME),
                getIntent().getStringExtra(ConstantVar.CHAT_ID));


        if(notesShowing && !checklistShowing){
            String notes=note.getText().toString().trim();
            if(notes.equalsIgnoreCase(""))
                Toast.makeText(this,"Please enter a note text",Toast.LENGTH_LONG).show();
            else {
                String checkList="";
                String location="ABCD";
                int color=R.color.white;
                String time="Edited:"+"DUMMY";
                ChatCards_Entity chatCards_entity=
                        new ChatCards_Entity(cardId,cardTitle,contactFetch,
                                sharedPreferenceSingleton.getSavedString(this,ConstantVar.CONTACT_SELF_NUMBER)
                                ,notesShowing,notes
                                ,checklistShowing,checkList,
                                location,color,time,false,false
                                );
                 AsyncTask.execute(()->{
                 appDatabase.cardDoa().insertCard(chatCards_entity);
                     int countReminderCard=appDatabase.cardDoa().getContactCardCount(contactFetch.getContact_number());
                     Log.v("Log1=",""+countReminderCard);


                        if(countReminderCard >= 1){
                         int reminderContactCount=appDatabase.reminderContactDoa().chatCardPresent(contactFetch.getContact_number());
                         Log.v("Log2=",""+reminderContactCount);

                         if(reminderContactCount>0){
                             sharedPreferenceSingleton.saveAs(this,ConstantVar.UPDATION,true);
                             sharedPreferenceSingleton.saveAs(this,ConstantVar.INSERTION,false);
                             sharedPreferenceSingleton.saveAs(this,ConstantVar.UPDATED_NUMBER,contactFetch.getContact_number());

                             ReminderContact reminderContact=new ReminderContact(contactFetch.getContact_number(),
                                     contactFetch.getContact_name(),countReminderCard,false,true,System.currentTimeMillis() );
                             appDatabase.reminderContactDoa().updateChatCard(reminderContact);
                                                      }
                         else {
                             sharedPreferenceSingleton.saveAs(this,ConstantVar.UPDATION,false);
                             sharedPreferenceSingleton.saveAs(this,ConstantVar.INSERTION,true);
                             sharedPreferenceSingleton.saveAs(this,ConstantVar.UPDATED_NUMBER,contactFetch.getContact_number());

                             appDatabase.reminderContactDoa().inserChatCard(new ReminderContact(contactFetch.getContact_number(),
                                     contactFetch.getContact_name(),countReminderCard,false,true ,System.currentTimeMillis()));
                         }
                     }


                 });

                finish();
            }

        }
        else if(!notesShowing && checklistShowing){

        }
    }
    private void manageLayoutChoice(int choice) {
        switch (choice){
            case ConstantVar.NOTES_CLICKED:
                notesShowing=true;
                checklistShowing=false;
                recyclerView.setVisibility(View.GONE);
                add_checkbox.setVisibility(View.GONE);
                note.setVisibility(View.VISIBLE);
                break;
            case ConstantVar.CHECKLIST_CLICKED:
                notesShowing=false;
                checklistShowing=true;
                recyclerView.setVisibility(View.VISIBLE);
                add_checkbox.setVisibility(View.VISIBLE);
                note.setVisibility(View.GONE);
                break;
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
            case R.id.add_checkbox:
                checkboxNotes.add(null);
                checkBoxAdapter.notifyItemInserted(checkboxNotes.size()-1);
                recyclerView.smoothScrollToPosition(checkboxNotes.size()-1);
                break;
            case R.id.addExtra:
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
            case R.id.checkBoxes:
                manageLayoutChoice(ConstantVar.CHECKLIST_CLICKED);
                break;
            case R.id.note:
                manageLayoutChoice(ConstantVar.NOTES_CLICKED);
                break;
            case R.id.color1:
                Toast.makeText(this, "Color1", Toast.LENGTH_SHORT).show();
                //change to color1
                break;
            case R.id.color2:
                Toast.makeText(this, "Color2", Toast.LENGTH_SHORT).show();
                //change to color2
                break;
            case R.id.color3:
                Toast.makeText(this, "Color3", Toast.LENGTH_SHORT).show();
                //change to color3
                break;
            case R.id.color4:
                Toast.makeText(this, "Color4", Toast.LENGTH_SHORT).show();
                //change to color4
                break;
            case R.id.color5:
                Toast.makeText(this, "Color5", Toast.LENGTH_SHORT).show();
                //change to color5
                break;
            case R.id.color6:
                Toast.makeText(this, "Color6", Toast.LENGTH_SHORT).show();
                //change to color6
                break;
        }
    }



    @Override
    public void itemClicked() {
        if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


}