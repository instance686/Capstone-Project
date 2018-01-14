package com.reminders.location.locatoinreminder.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.reminders.location.locatoinreminder.entityinterface.ContactSelection;
import com.reminders.location.locatoinreminder.util.Utils;
import com.reminders.location.locatoinreminder.view.ui.activity.ChatActivity;
import com.reminders.location.locatoinreminder.view.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ayush on 29/12/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter {
   private ArrayList<Contact_Entity> contacts;
    private Context c;
    private int count=0;
    private Utils utils =new Utils();
    private int colorSelected = Color.LTGRAY;
    private int colorNormal = Color.WHITE;
    public ContactsAdapter(Context context,ArrayList<Contact_Entity> contacts)
    {
        this.contacts=contacts;
        c=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return new ContactsViewHolder(view);
           }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Contact_Entity contact=contacts.get(position);
        ContactsViewHolder contactsViewHolder= (ContactsViewHolder) holder;
        String name=contact.getName();
        contactsViewHolder.name.setText(name);
        contactsViewHolder.phone.setText(contact.getNumber());
        contactsViewHolder.initials.setText(utils.getInitial(name));
        if(contact.isSelection())
            contactsViewHolder.status.setVisibility(View.VISIBLE);
        else
            contactsViewHolder.status.setVisibility(View.GONE);
        contactsViewHolder.cardView.setCardBackgroundColor(contact.isSelection()?colorSelected:colorNormal);
        ((ContactsViewHolder)holder).contact=contact;
    }



    public void addItems(List<Contact_Entity> contact_entityList){
        this.contacts= (ArrayList<Contact_Entity>) contact_entityList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }
    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        Contact_Entity contact;
        @BindView(R.id.contact_card)
        CardView cardView;
       @BindView(R.id.name)
        TextView name;
        @BindView(R.id.initials)
        TextView initials;
       @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.selected_state)
        ImageButton status;
        @BindView(R.id.pic)
        ImageView pic;


        public ContactsViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
            cardView.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == cardView.getId()) {
                if (count > 0) {
                    if (contact.isSelection()) {
                        contact.setSelection(false);
                        status.setVisibility(View.GONE);
                        count--;
                    } else {
                        contact.setSelection(true);
                        status.setVisibility(View.VISIBLE);
                        status.startAnimation(AnimationUtils.loadAnimation(c, R.anim.selection));
                        count++;
                    }
                    cardView.setCardBackgroundColor(contact.isSelection() ? colorSelected : colorNormal);
                }else{
                    Intent intent=new Intent(c, ChatActivity.class);
                    intent.putExtra(ConstantVar.CHAT_ID,phone.getText().toString());
                    intent.putExtra(ConstantVar.CONTACT_NAME,name.getText().toString());
                    c.startActivity(intent);

                }
            }
        }


        @Override
        public boolean onLongClick(View v) {
            if (contact.isSelection()) {
                contact.setSelection(false);
                status.setVisibility(View.GONE);
                count--;
            } else {
                contact.setSelection(true);
                status.setVisibility(View.VISIBLE);
                status.startAnimation(AnimationUtils.loadAnimation(c, R.anim.selection));
                count++;
            }
            cardView.setCardBackgroundColor(contact.isSelection() ? colorSelected : colorNormal);
            return true;
        }


    }
}
