package com.customer.orderproupdated.UI;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.adapter.Order_detail_adapter;
import com.customer.orderproupdated.bean.MerchantDetail;
import com.customer.orderproupdated.bean.Order;
import com.customer.orderproupdated.bean.OrderDetail;
import com.customer.orderproupdated.bean.OrderHistorydata;
import com.customer.orderproupdated.bean.ProdVal;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ArrayList<OrderDetail> productList;
    public Order_detail_adapter order_detail_adapter;
    ListView order_list_view;
    private android.support.v7.widget.SearchView searchView = null;
    private android.support.v7.widget.SearchView.OnQueryTextListener queryTextListener;
    Menu menu;
    Order ob_obj;
    ImageView icon_call, icon_chat;
    Button cancel;
    TextView tv_merchent_name,tv_merchent_address,tv_order_date,tv_total_items,tv_grand_total;
    MerchantDetail merchantDetail;
    SharedPrefrence share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_order_detail);
        share=SharedPrefrence.getInstance(this);
        //getintent
        ob_obj = (Order) getIntent().getExtras().getSerializable("ob");


        //Prepare toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            DialogUtility.showLOG( "GetSupportActionBar returned null.");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        actionBar.setTitle("Order ID - "+ob_obj.getOrderId());
        actionBar.setSubtitle(ob_obj.getOrderStatus());

        //findviewbyid
        icon_call = (ImageView) findViewById(R.id.icon_call);
        icon_chat = (ImageView) findViewById(R.id.icon_chat);
        cancel=(Button)findViewById(R.id.btn_cancel) ;
        tv_merchent_name=(TextView)findViewById(R.id.merchent_name);
        tv_merchent_address=(TextView)findViewById(R.id.merchant_address);
        tv_order_date=(TextView)findViewById(R.id.order_date);
        tv_total_items=(TextView)findViewById(R.id.tv_total_items);
        tv_grand_total=(TextView)findViewById(R.id.tv_grand_total);
        order_list_view = (ListView) findViewById(R.id.order_list);

        //setProductList
        productList=(ArrayList<OrderDetail>)ob_obj.getOrderDetail();

        //getMerchnatDetail
        merchantDetail=share.getMerchantDetail(SharedPrefrence.MERCHANT_DETAIL);

        //setvalues
        tv_merchent_name.setText(merchantDetail.getUsername());
        tv_merchent_address.setText(merchantDetail.getContactno());
        tv_order_date.setText(ob_obj.getOrderDate());
        tv_grand_total.setText(ob_obj.getGrandTotal());
        tv_total_items.setText(productList.size()+"");

        //setadapter
        order_detail_adapter = new Order_detail_adapter(this, productList);
        order_list_view.setAdapter(order_detail_adapter);

        //setonclicklistener
        icon_call.setOnClickListener(this);
        icon_chat.setOnClickListener(this);
        cancel.setOnClickListener(this);

        //list item click
        order_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // startActivity(new Intent(getActivity(), OrderDetailsActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_only_cart, menu);

        //cart badge count
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        Utility.setBadgeCount(this, icon, PreferenceConstant.CART_COUNT + "");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent in = new Intent(OrderDetailsActivity.this, CartActivity.class);
                startActivity(in);
                return false;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_call:
                dial();
                break;
            case R.id.icon_chat:
                startActivity(new Intent(this,ChatActivity.class));
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;

        }
    }

    public void dial()
    {
        Intent dial = new Intent();
        dial.setAction("android.intent.action.DIAL");
        dial.setData(Uri.parse("tel:"+merchantDetail.getContactno()));
        startActivity(dial);
    }
}





