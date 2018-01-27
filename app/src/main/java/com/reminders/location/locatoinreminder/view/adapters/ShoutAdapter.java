package com.reminders.location.locatoinreminder.view.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCardsEntity;
import com.reminders.location.locatoinreminder.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ayush on 22/1/18.
 */

public class ShoutAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ChatCardsEntity> shouts;
    private AppDatabase appDatabase;


    public ShoutAdapter(Context context, List<ChatCardsEntity> shouts) {
        this.context = context;
        this.shouts = shouts;
        appDatabase = ((MyApplication) context.getApplicationContext()).getDatabase();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shouts_row, parent, false);
        return new ShoutsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatCardsEntity chatCards_entity = shouts.get(position);
        ShoutsViewHolder cardViewHolder = (ShoutsViewHolder) holder;
        cardViewHolder.title.setText(chatCards_entity.getCardTitle());
        cardViewHolder.note.setText(chatCards_entity.getNotes());
        cardViewHolder.coordinates.setText(new Utils().getCoordinates(chatCards_entity.getLocation()));
        cardViewHolder.sender.setText(chatCards_entity.getContactFetch().getContact_name());
        cardViewHolder.shoutCard.setBackgroundColor(ContextCompat.getColor(context,
                chatCards_entity.getColor()));
        cardViewHolder.chatCards_entity = chatCards_entity;
    }

    public void addItems(List<ChatCardsEntity> shouts) {
        this.shouts = shouts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return shouts.size();
    }

    class ShoutsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ChatCardsEntity chatCards_entity;
        @BindView(R.id.shout_card)
        CardView shoutCard;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.note)
        TextView note;
        @BindView(R.id.location)
        Button coordinates;
        @BindView(R.id.sender)
        Button sender;


        public ShoutsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        @Override
        public void onClick(View v) {
            //manage reminder completion
        }
    }
}