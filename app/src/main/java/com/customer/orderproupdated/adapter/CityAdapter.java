package com.customer.orderproupdated.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.bean.CityDetails;
import com.customer.orderproupdated.bean.StateDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Advantal on 8/11/2017.
 */

public class CityAdapter extends BaseAdapter{


    /**
     * Created by user on 6/24/2016.
     */


        private Context context;
        List<CityDetails> city;

        public CityAdapter(Context context, ArrayList<CityDetails> data) {
            this.context = context;
            this.city = data;

        }
        @Override
        public int getCount() {
            return city.size();
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
            com.customer.orderproupdated.adapter.CityAdapter.ViewHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(R.layout.state_item,null,false);

                holder = new com.customer.orderproupdated.adapter.CityAdapter.ViewHolder();

                holder.stateName = (TextView) row.findViewById(R.id.state_name_txt);



            } else {
                holder = (com.customer.orderproupdated.adapter.CityAdapter.ViewHolder) row.getTag();
            }

            final CityDetails citydata = city.get(position);
            holder.stateName.setText(citydata.getCityName());

            return row;
        }

        class ViewHolder {
            TextView stateName;
        }




}
