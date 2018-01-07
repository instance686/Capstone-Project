package com.reminders.location.locatoinreminder.view.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;
import com.reminders.location.locatoinreminder.pojo.ContactFetch;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;
import com.reminders.location.locatoinreminder.viewmodel.ReminderSetActivityViewModel;

import butterknife.BindView;

public class ReminderSet extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.note)
     EditText note;
    @BindView(R.id.list)
     LinearLayout checkList;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.item)
    EditText item;
    @BindView(R.id.delete)
    ImageView deleteButton;
    @BindView(R.id.addExtra)
    ImageView addExtra;
    @BindView(R.id.time)
    TextView editedTime;
    @BindView(R.id.addLocation)
     TextView addLocation;
     BottomSheetBehavior bottomSheetBehavior;
     DatabaseReference databaseReference;
     AppDatabase appDatabase;
    boolean bottomSheetShowing=false;


    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();
    private ReminderSetActivityViewModel reminderSetActivityViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reminderSetActivityViewModel= ViewModelProviders.of(this).get(ReminderSetActivityViewModel.class);
        bottomSheetBehavior=BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        appDatabase=getMyapp().getDatabase();
        addExtra.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        int cardId= (int) System.currentTimeMillis();
        Log.v("CARDID",""+cardId);
        String cardTitle=title.getText().toString().trim();
        ContactFetch contactFetch=new ContactFetch("Self","+918081775811");
        boolean notesPresent=true;
        String notes=note.getText().toString().trim();
        boolean checkListPresent=false;
        String checkList="";
        String location="ABCD";
        int color=R.color.white;
        String time="Edited:"+"DUMMY";
        ChatCards_Entity chatCards_entity=
                new ChatCards_Entity(cardId,cardTitle,contactFetch,notesPresent,notes
                ,checkListPresent,checkList,location,color,time,false);
        /*AsyncTask.execute(()->{
            appDatabase.cardDoa().insertCard(chatCards_entity);
        });
        Log.v("Datainserted","DataInserted");*/
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
        if(v.getId()==addExtra.getId()){
            if(bottomSheetShowing){
                bottomSheetShowing=false;
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
            else {
                bottomSheetShowing=true;
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }
}
