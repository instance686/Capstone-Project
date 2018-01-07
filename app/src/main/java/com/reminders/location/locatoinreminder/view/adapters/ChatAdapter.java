package com.reminders.location.locatoinreminder.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;
import com.reminders.location.locatoinreminder.executor.CardsSelected;
import com.reminders.location.locatoinreminder.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ayush on 7/1/18.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    private ArrayList<ChatCards_Entity> chatCards_entities;
    private Context c;
    private int count=0;
    private Utils utils =new Utils();
    private int colorSelected = Color.LTGRAY;
    private int colorNormal = Color.WHITE;
    private CardsSelected cardsSelected;
    List<Boolean> selected=new ArrayList<>();

    public ChatAdapter(ArrayList<ChatCards_Entity> chatCards_entities, Context c) {
        this.chatCards_entities = chatCards_entities;
        this.c = c;
        cardsSelected=(CardsSelected)c;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_card,parent,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatCards_Entity chatCards_entity=chatCards_entities.get(position);
        CardViewHolder cardViewHolder= (CardViewHolder) holder;
        cardViewHolder.title.setText(chatCards_entity.getCardTitle());
        cardViewHolder.note.setText(chatCards_entity.getNotes());
        cardViewHolder.coordinates.setText(chatCards_entity.getLocation());

        ((CardViewHolder)cardViewHolder).chatCards_entity=chatCards_entity;

    }
    public void addItem(List<ChatCards_Entity> chatCards_entity){
        chatCards_entities= (ArrayList<ChatCards_Entity>) chatCards_entity;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chatCards_entities.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        ChatCards_Entity chatCards_entity;
        @BindView(R.id.reminder_card)
        CardView reminderCard;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.Note)
        TextView note;
        @BindView(R.id.list)
        LinearLayout list;
        @BindView(R.id.checkBox)
        CheckBox checkBox;
        @BindView(R.id.item)
        TextView item;
        @BindView(R.id.coordinates)
        TextView coordinates;

        public CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            reminderCard.setOnClickListener(this);
            reminderCard.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {

                    if(chatCards_entity.isSelected()){
                        chatCards_entity.setSelected(false);
                        count--;
                        cardsSelected.onCardSelected(count,chatCards_entity.getCardId(),getAdapterPosition(),false);
                    }
                    else
                    {
                        chatCards_entity.setSelected(true);
                        count++;
                        cardsSelected.onCardSelected(count,chatCards_entity.getCardId(),getAdapterPosition(),true);
                    }
                    reminderCard.setBackgroundColor(chatCards_entity.isSelected()?colorSelected:colorNormal);



            return true;
        }
    }
}
