package com.customer.orderproupdated.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.bean.StateDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 6/24/2016.
 */
public class StatesAdapter extends BaseAdapter {

    private Context context;
    List<StateDetails> state;

    public StatesAdapter(Context context, ArrayList<StateDetails> data) {
        this.context = context;
        this.state = data;

    }
    @Override
    public int getCount() {
        return state.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row = null;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.state_item,null,false);

            holder = new ViewHolder();

            holder.stateName = (TextView) row.findViewById(R.id.state_name_txt);



        } else {
            holder = (ViewHolder) row.getTag();
        }

        final StateDetails statesData = state.get(position);
        holder.stateName.setText(statesData.getStateName());

        return row;
    }

    class ViewHolder {
        TextView stateName;
    }

}

