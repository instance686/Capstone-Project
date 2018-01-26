package com.reminders.location.locatoinreminder.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.pojo.ListData;

import java.util.ArrayList;

/**
 * Created by ayush on 26/1/18.
 */

public class ListAdapter extends BaseAdapter {

    ArrayList<ListData> listDataArrayAdapter;
    Context context;


    public ListAdapter(ArrayList<ListData> data, Context context) {
        this.listDataArrayAdapter = data;
        this.context=context;

    }

    @Override
    public int getCount() {
        return listDataArrayAdapter.size();
    }

    @Override
    public Object getItem(int position) {
        return listDataArrayAdapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView=inflater.inflate(R.layout.widget_rows,parent,false);
        }
        ListData listData=listDataArrayAdapter.get(position);
        ((TextView)convertView.findViewById(R.id.title)).setText(listData.getTitle());
        ((TextView)convertView.findViewById(R.id.note)).setText(listData.getNote());
        ((TextView)convertView.findViewById(R.id.sender)).setText(listData.getSenderName());
        return convertView;
    }


    private static class ViewHolder {
        TextView title;
        TextView note;
        TextView contact_num;
    }
}
