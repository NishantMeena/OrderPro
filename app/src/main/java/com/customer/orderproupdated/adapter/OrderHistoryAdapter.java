package com.customer.orderproupdated.adapter;
/**
 * Created by AKASH on 29-Aug-16.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.customer.orderproupdated.Fragments.OrderHistoryFragment;
import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.bean.Order;
import com.customer.orderproupdated.bean.OrderHistorydata;
import com.customer.orderproupdated.bean.Products_bean;

import java.util.ArrayList;
import java.util.Locale;

public class OrderHistoryAdapter extends BaseAdapter {
    ArrayList<Order> orderlist;
    Context context;
    ArrayList<Order> newlist = new ArrayList<>();
    Fragment fragment;

    public OrderHistoryAdapter(OrderHistoryFragment orderHistoryFragment, ArrayList<Order> orderlist) {

        this.orderlist = orderlist;
        this.newlist.addAll(orderlist);
        this.fragment = orderHistoryFragment;
        this.context = orderHistoryFragment.getActivity();
    }

    @Override
    public int getCount() {
        return orderlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lif.inflate(R.layout.list_item_myorder, null);

        TextView order_date, order_status, order_id;

        // ImageView item_image=(ImageView) v.findViewById(R.id.order_placeholder);
        order_date = (TextView) v.findViewById(R.id.order_date);
        order_status = (TextView) v.findViewById(R.id.order_status);
        order_id = (TextView) v.findViewById(R.id.order_id);

        Order orderHistorydata = orderlist.get(position);

        order_id.setText(orderHistorydata.getOrderId());
        order_status.setText(orderHistorydata.getOrderStatus());
        order_date.setText(orderHistorydata.getOrderDate());

        return v;

    }

    public void getFilter(String str) {
        orderlist.clear();
        String txt = str.toLowerCase(Locale.getDefault());
        if (str.length() == 0) {
            orderlist.addAll(newlist);
           /* if (quick_order_list.size() > 0) {
                tv_no_contacts.setVisibility(View.INVISIBLE);
            }*/
        } else {
            for (Order orderdata : newlist) {
                if (orderdata.getOrderId().toLowerCase().contains(txt)) {
                    orderlist.add(orderdata);

                }
            }
        }
        notifyDataSetChanged();
    }
}

