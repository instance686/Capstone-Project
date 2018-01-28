package com.reminders.location.locatoinreminder.view.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCardsEntity;
import com.reminders.location.locatoinreminder.executor.CURDTasks;
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

public class ChatActivity extends BaseActivity implements View.OnClickListener, CardsSelected {
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
    DatabaseReference databaseReference, r2;
    ChatAdapter chatAdapter;
    List<Integer> cardIds = new ArrayList<>();
    String chatId, name, selfNum;
    AppDatabase appDatabase;
    List<ChatCardsEntity> presentCards = new ArrayList<>();
    Observer<List<ChatCardsEntity>> observer = new Observer<List<ChatCardsEntity>>() {
        @Override
        public void onChanged(@Nullable List<ChatCardsEntity> chatCards_entities) {
            presentCards = chatCards_entities;
            chatAdapter.addItem(chatCards_entities);
            if (chatCards_entities.size() > 0) {
                empty_state.setVisibility(View.GONE);
            } else {
                empty_state.setVisibility(View.VISIBLE);
            }
        }
    };
    private Utils utils = new Utils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatId = getIntent().getStringExtra(ConstantVar.CHAT_ID);
        name = getIntent().getStringExtra(ConstantVar.CONTACT_NAME);
        selfNum = sharedPreferenceSingleton.getSavedString(this, ConstantVar.CONTACT_SELF_NUMBER);

        appDatabase = getMyapp().getDatabase();

        chatActivityViewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        chatActivityViewModel.getCardsList(getMyapp().getDatabase(), chatId).observe(this, observer);


        chatTitle.setText(name);
        initials.setText(utils.getInitial(name));

        databaseReference = FirebaseDatabase.getInstance().getReference(ConstantVar.REMINDERS);
        toolbar.inflateMenu(R.menu.chat_sync);
        toolbar.setOnMenuItemClickListener(item -> {
            new ChatCardSync(this, appDatabase, databaseReference, chatId, name, selfNum
                    , (ArrayList<ChatCardsEntity>) presentCards).execute();
            return true;
        });


        fab.setOnClickListener(this);
        backbutton.setOnClickListener((view) -> {
            onBackPressed();
        });

        chatMessages.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        chatAdapter = new ChatAdapter(new ArrayList<ChatCardsEntity>(), ChatActivity.this);
        chatMessages.setAdapter(chatAdapter);

        optionsToolbar.inflateMenu(R.menu.card_selected_menu);
        optionsToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.delete) {
                    deleteCards();
                }
                return true;
            }
        });
    }

    public void deleteCards() {
        if (cardIds.size() > 0) {
                deleteLocally();
            optionsToolbar.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
        } else
            Toast.makeText(ChatActivity.this, getResources().getString(R.string.card_for_del), Toast.LENGTH_SHORT).show();
    }

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
        Intent intent = new Intent(this, ReminderSet.class);
        intent.putExtra(ConstantVar.CHAT_ID, chatId);
        intent.putExtra(ConstantVar.CONTACT_NAME, name);
        switch (v.getId()) {
            case R.id.fab:
                intent.putExtra(ConstantVar.UPDATE_CARD, false);
                startActivity(intent);
                break;
        }
    }

    public MyApplication getMyapp() {
        return (MyApplication) getApplicationContext();
    }


    @Override
    public void onCardSelected(int count, int cardId, int position, boolean seleted) {
        if (count > 0) {
            toolbar.setVisibility(View.GONE);
            counter.setText(count+"");
            optionsToolbar.setVisibility(View.VISIBLE);
            //counter.setText(count);
        } else {
            optionsToolbar.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
        }

        if (seleted) {
            cardIds.add(cardId);
        } else {
            int index = cardIds.indexOf(cardId);
            cardIds.remove(index);
        }


    }

    public void deleteLocally() {
        new CURDTasks(appDatabase, ConstantVar.DELETECHAT, this, cardIds, chatId, name).execute();
    }


}