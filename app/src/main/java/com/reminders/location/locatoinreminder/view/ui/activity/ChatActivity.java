package com.reminders.location.locatoinreminder.view.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;
import com.reminders.location.locatoinreminder.database.entity.Contact_Entity;
import com.reminders.location.locatoinreminder.database.entity.ReminderContact;
import com.reminders.location.locatoinreminder.executor.CardsSelected;
import com.reminders.location.locatoinreminder.executor.ChatCardSync;
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
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.backOnLongClick)
    ImageButton longBackClick;
    @BindView(R.id.cardCounter)
    TextView counter;

    @BindView(R.id.empty_state)
    ConstraintLayout empty_state;

    SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();
    ChatActivityViewModel chatActivityViewModel;
    DatabaseReference databaseReference,r2;
    ChatAdapter chatAdapter;
    List<Integer>  cardIds=new ArrayList<>();
    String chatId,name,selfNum;
    private Utils utils =new Utils();
    AppDatabase appDatabase;
    List<ChatCards_Entity> presentCards=new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatId=getIntent().getStringExtra(ConstantVar.CHAT_ID);
        name=getIntent().getStringExtra(ConstantVar.CONTACT_NAME);
        selfNum=sharedPreferenceSingleton.getSavedString(this,ConstantVar.CONTACT_SELF_NUMBER);

        appDatabase=getMyapp().getDatabase();

        chatActivityViewModel= ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        chatActivityViewModel.getCardsList(getMyapp().getDatabase(),chatId).observe(this,observer);


        chatTitle.setText(name);
        initials.setText(utils.getInitial(name));

        databaseReference= FirebaseDatabase.getInstance().getReference("reminders");
        toolbar.inflateMenu(R.menu.chat_sync);
        toolbar.setOnMenuItemClickListener(item->{
            new ChatCardSync(this,appDatabase,databaseReference,chatId,name,selfNum
                    , (ArrayList<ChatCards_Entity>) presentCards).execute();
            return true;
        });


        fab.setOnClickListener(this);
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
                deleteLocally();
                //deleteFromServer();

            });
            optionsToolbar.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
        }
        else
            Toast.makeText(ChatActivity.this,"Select Cards for Deletion",Toast.LENGTH_SHORT).show();
    }

    Observer<List<ChatCards_Entity>> observer=new Observer<List<ChatCards_Entity>>() {
        @Override
        public void onChanged(@Nullable List<ChatCards_Entity> chatCards_entities) {
            presentCards=chatCards_entities;
            chatAdapter.addItem(chatCards_entities);
            if(chatCards_entities.size()>0) {
                empty_state.setVisibility(View.GONE);
            }else {
                empty_state.setVisibility(View.VISIBLE);
            }
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
            case R.id.fab:
                intent.putExtra(ConstantVar.UPDATE_CARD,false);
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
    public void deleteLocally(){
        appDatabase.cardDoa().deleteCard(cardIds);
        int countReminderCard=appDatabase.cardDoa().getContactCardCount(chatId);
        ReminderContact reminderContact=new ReminderContact(chatId,
                name,countReminderCard,false,true,System.currentTimeMillis() );

        appDatabase.reminderContactDoa().updateChatCard(reminderContact);
        sharedPreferenceSingleton.saveAs(this,ConstantVar.UPDATION,true);
        sharedPreferenceSingleton.saveAs(this,ConstantVar.INSERTION,false);
        sharedPreferenceSingleton.saveAs(this,ConstantVar.UPDATED_NUMBER,chatId);
    }
    public void deleteFromServer(){
        r2=FirebaseDatabase.getInstance().getReference("reminders").child(new Utils().getFullNumber(chatId)+selfNum);

        r2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post:dataSnapshot.getChildren()){
                    if(cardIds.contains(Integer.parseInt(post.getKey()))){
                        post.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}