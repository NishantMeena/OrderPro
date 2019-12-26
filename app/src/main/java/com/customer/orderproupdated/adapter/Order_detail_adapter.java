package com.customer.orderproupdated.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.ProductDetailActivity;
import com.customer.orderproupdated.bean.OrderDetail;
import com.customer.orderproupdated.bean.ProdVal;
import com.customer.orderproupdated.bean.Products_bean;

import java.util.ArrayList;

/**
 * Created by Sony on 1/4/2017.
 */
public class Order_detail_adapter extends BaseAdapter {
    ArrayList<OrderDetail> orderDetailList;
    Context context;
    OrderDetail orderDetailData;
    public Order_detail_adapter(Context context, ArrayList<OrderDetail> orderDetailList) {

        this.orderDetailList = orderDetailList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return orderDetailList.size();
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lif.inflate(R.layout.list_item_order_detail, null);
        TextView item_serial_no, product_name, product_id, product_attribute, quantity, rate, amount,tv_item_attributes_selected;
        LinearLayout ll_product_detail;

        item_serial_no = (TextView) v.findViewById(R.id.item_serial_no);
        product_name = (TextView) v.findViewById(R.id.product_name);
        product_id = (TextView) v.findViewById(R.id.product_id);
        quantity = (TextView) v.findViewById(R.id.quantity);
        rate = (TextView) v.findViewById(R.id.rate);
        amount = (TextView) v.findViewById(R.id.amount);
        ll_product_detail=(LinearLayout)v.findViewById(R.id.ll_product_detail);
        tv_item_attributes_selected=(TextView)v.findViewById(R.id.tv_item_attributes_selected);

        int serial_no=position+1;
        item_serial_no.setText(serial_no+"");

        orderDetailData = orderDetailList.get(position);

        String amount_item=orderDetailData.getTotalAmount();
        String prod_quan=orderDetailData.getQuantity();

        product_name.setText(orderDetailData.getProductName());
        product_id.setText(orderDetailData.getProductId());
        tv_item_attributes_selected.setText(orderDetailData.getSelectedAttribute());

        quantity.setText(orderDetailData.getQuantity());
        rate.setText(orderDetailData.getPrice());
        amount.setText(orderDetailData.getTotalAmount());
        product_name.setPaintFlags(product_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ll_product_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(context, ProductDetailActivity.class);
                in.putExtra("pid", orderDetailList.get(position).getProductId());
                context.startActivity(in);

            }
        });


        return v;
    }


}





