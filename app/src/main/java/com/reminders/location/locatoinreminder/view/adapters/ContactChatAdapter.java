package com.reminders.location.locatoinreminder.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.entity.Contact_Entity;
import com.reminders.location.locatoinreminder.database.entity.ReminderContact;
import com.reminders.location.locatoinreminder.executor.ContactCard;
import com.reminders.location.locatoinreminder.util.Utils;
import com.reminders.location.locatoinreminder.view.ui.activity.ChatActivity;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ayush on 13/1/18.
 */

public class ContactChatAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ReminderContact> reminderContacts;
    private int count=0;
    private Utils utils=new Utils();
    private int colorSelected = Color.LTGRAY;
    private int colorNormal = Color.WHITE;
    private ContactCard contactCard;

    public ContactChatAdapter(Context context, List<ReminderContact> reminderContacts) {
        this.context = context;
        this.reminderContacts = reminderContacts;
        contactCard= (ContactCard) context;
    }
    public ContactChatAdapter(Context context){
        this.context=context;
        contactCard= (ContactCard) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return new ReminderContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ReminderContact reminderContact=reminderContacts.get(position);
        ReminderContactViewHolder reminderContactViewHolder= (ReminderContactViewHolder) holder;
       reminderContactViewHolder.name.setText(reminderContact.getName());
//       reminderContactViewHolder.cardCount.setText(reminderContact.getReminderCount()+" Reminders");
       reminderContactViewHolder.initials.setText(utils.getInitial(reminderContact.getName()));
        if(reminderContact.isSelection())
            reminderContactViewHolder.status.setVisibility(View.VISIBLE);
        else
            reminderContactViewHolder.status.setVisibility(View.GONE);
        reminderContactViewHolder.cardView.setCardBackgroundColor(reminderContact.isSelection()?colorSelected:colorNormal);
        ((ReminderContactViewHolder)holder).contact=reminderContact;

    }

    public void addItems(List<ReminderContact> reminderContacts){
        this.reminderContacts= (ArrayList<ReminderContact>) reminderContacts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return reminderContacts.size();
    }
    public class ReminderContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        ReminderContact contact;
        @BindView(R.id.contact_card)
        CardView cardView;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.initials)
        TextView initials;
        @BindView(R.id.phone)
        TextView cardCount;
        @BindView(R.id.selected_state)
        ImageButton status;
        @BindView(R.id.pic)
        ImageView pic;


        public ReminderContactViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            cardView.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == cardView.getId()) {
                    Intent intent=new Intent(context, ChatActivity.class);
                    intent.putExtra(ConstantVar.CHAT_ID,contact.getNumber());
                    intent.putExtra(ConstantVar.CONTACT_NAME,name.getText().toString());
                    context.startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(contact.isSelection()){
                contact.setSelection(false);
                count--;
                contactCard.onContactCardSelected(count,contact
                        .getNumber(),getAdapterPosition(),false);
            }
            else
            {
                contact.setSelection(true);
                count++;
                contactCard.onContactCardSelected(count,contact
                        .getNumber(),getAdapterPosition(),true);
            }
            cardView.setBackgroundColor(contact.isSelection()?colorSelected:colorNormal);



            return true;
        }
    }
}

