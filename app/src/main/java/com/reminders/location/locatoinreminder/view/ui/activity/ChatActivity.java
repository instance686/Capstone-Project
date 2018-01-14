package com.reminders.location.locatoinreminder.view.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;
import com.reminders.location.locatoinreminder.database.entity.Contact_Entity;
import com.reminders.location.locatoinreminder.executor.CardsSelected;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;
import com.reminders.location.locatoinreminder.util.Utils;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;
import com.reminders.location.locatoinreminder.view.adapters.ChatAdapter;
import com.reminders.location.locatoinreminder.viewmodel.ChatActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends BaseActivity implements View.OnClickListener,CardsSelected{
    @BindView(R.id.background)
    CoordinatorLayout background;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar_chat)
    Toolbar toolbar;
    @BindView(R.id.toolbar_options)
    Toolbar optionsToolbar;
    @BindView(R.id.back)
    ImageButton backbutton;
    @BindView(R.id.profile_pic)
    CircleImageView profilePic;
    @BindView(R.id.initials)
    TextView initials;
    @BindView(R.id.chat_title)
    TextView chatTitle;
    @BindView(R.id.chat_messages)
    RecyclerView chatMessages;
    @BindView(R.id.send_note)
    TextView send_note;
    @BindView(R.id.send_checklist)
    ImageView send_checklist;
    @BindView(R.id.backOnLongClick)
    ImageButton longBackClick;
    @BindView(R.id.cardCounter)
    TextView counter;
    SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();
    ChatActivityViewModel chatActivityViewModel;
    DatabaseReference databaseReference;
    ChatAdapter chatAdapter;
    List<Integer>  cardIds=new ArrayList<>();
    String chatId,name;
    private Utils utils =new Utils();
    AppDatabase appDatabase;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatId=getIntent().getStringExtra(ConstantVar.CHAT_ID);
        name=getIntent().getStringExtra(ConstantVar.CONTACT_NAME);

        appDatabase=getMyapp().getDatabase();

        chatActivityViewModel= ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        chatActivityViewModel.getCardsList(getMyapp().getDatabase(),chatId).observe(this,observer);


        chatTitle.setText(name);
        initials.setText(utils.getInitial(name));


        send_note.setOnClickListener(this);
        send_checklist.setOnClickListener(this);
        backbutton.setOnClickListener((view)->{onBackPressed();});

        chatMessages.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        chatAdapter=new ChatAdapter(new ArrayList<ChatCards_Entity>(),ChatActivity.this);
        chatMessages.setAdapter(chatAdapter);

        optionsToolbar.inflateMenu(R.menu.card_selected_menu);
        optionsToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.delete){
                    deleteCards();
                }
                return true;
            }
        });
    }

    public void deleteCards(){
        if(cardIds.size()>0){
            AsyncTask.execute(()->{
                appDatabase.cardDoa().deleteCard(cardIds);
            });
        }
        else
            Toast.makeText(ChatActivity.this,"Select Cards for Deletion",Toast.LENGTH_SHORT).show();
    }

    Observer<List<ChatCards_Entity>> observer=new Observer<List<ChatCards_Entity>>() {
        @Override
        public void onChanged(@Nullable List<ChatCards_Entity> chatCards_entities) {

            chatAdapter.addItem(chatCards_entities);

            /*if (sharedPreferenceSingleton.getSavedBoolean(ChatActivity.this, ConstantVar.INSERTION_CARD)) {
                sharedPreferenceSingleton.saveAs(ChatActivity.this,ConstantVar.INSERTION_CARD,false);
            }
            if(sharedPreferenceSingleton.getSavedBoolean(ChatActivity.this,ConstantVar.DELETION_CARD)){

                sharedPreferenceSingleton.saveAs(ChatActivity.this,ConstantVar.DELETION_CARD,false);
            }*/
        }


    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_chat;
    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,ReminderSet.class);
        intent.putExtra(ConstantVar.CHAT_ID,chatId);
        intent.putExtra(ConstantVar.CONTACT_NAME,name);
        switch (v.getId()){
            case R.id.send_note:
                intent.putExtra(ConstantVar.NEW_CHOICE,ConstantVar.NOTES_CLICKED);
                startActivity(intent);
                break;
            case R.id.send_checklist:
                intent.putExtra(ConstantVar.NEW_CHOICE,ConstantVar.CHECKLIST_CLICKED);
                startActivity(intent);
                break;

        }
    }

    public MyApplication getMyapp() {
        return (MyApplication) getApplicationContext();
    }




    @Override
    public void onCardSelected(int count,int cardId,int position,boolean seleted) {
        if(count>0){
            toolbar.setVisibility(View.GONE);
            counter.setText(""+count);
            optionsToolbar.setVisibility(View.VISIBLE);
            //counter.setText(count);
        }
        else{
            optionsToolbar.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
        }

        if(seleted){
            cardIds.add(cardId);
            Log.v("AdapterPosAdd",""+position);
        }
        else{
            int index=cardIds.indexOf(cardId);
            cardIds.remove(index);
            Log.v("AdapterPosDel",""+index);
        }



    }
}