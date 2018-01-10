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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.database.entity.Contact_Entity;
import com.reminders.location.locatoinreminder.executor.ChecklistItemClicked;
import com.reminders.location.locatoinreminder.util.Utils;
import com.reminders.location.locatoinreminder.view.ui.activity.ChatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ayush on 29/12/17.
 */

public class CheckBoxAdapter extends RecyclerView.Adapter {
   private ArrayList<String> notes;
    private Context c;
    private ChecklistItemClicked checklistItemClicked;
    public CheckBoxAdapter(Context context, ArrayList<String> notes)
    {
        this.notes=notes;
        c=context;
        checklistItemClicked= (ChecklistItemClicked) c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_item, parent, false);
        return new CheckboxItemViewHolder(view);
           }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CheckboxItemViewHolder checkboxItemViewHolder= (CheckboxItemViewHolder) holder;
        String note=notes.get(position);
        if(note!=null)
        checkboxItemViewHolder.edit_point.setText(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void deleteItem(int position){
        notes.remove(position);
        notifyDataSetChanged();
    }

    public class CheckboxItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
       @BindView(R.id.reorder)
        ImageButton reorder;
        @BindView(R.id.edit_point)
        EditText edit_point;
       @BindView(R.id.delete)
        ImageButton delete;


        public CheckboxItemViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
            edit_point.setOnClickListener(this);
            delete.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == delete.getId()) {
                    deleteItem(getAdapterPosition());
            }
            else if(v.getId()==edit_point.getId()){
                checklistItemClicked.itemClicked();
            }
        }


        @Override
        public boolean onLongClick(View v) {

            return true;
        }


    }
}
