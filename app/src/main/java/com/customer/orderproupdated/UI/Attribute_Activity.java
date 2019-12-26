package com.customer.orderproupdated.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.adapter.AttributeAdapter;
import com.customer.orderproupdated.bean.ProdVal;
import com.customer.orderproupdated.bean.Product_attribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sony on 12/27/2016.
 */
public class Attribute_Activity extends Activity implements View.OnClickListener {
    ListView spinners_listview;
    ArrayList<String> spinners_list = new ArrayList<>();
    TextView product_name, product_price, tv_amt_total;
    EditText et_quantity;
    Button btn_done, btn_cancel;
    ProdVal pb_obj;
    LinearLayout iv_plus, iv_minus;

    int position = 0;
    int minteger = 1;
    Double total_amt = 0.0;
    Double price_per_prod = 0.0;

    ArrayList<Product_attribute> product_attribute = new ArrayList<Product_attribute>();
    List<Product_attribute> attribute_list_selcted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //dialog
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        setContentView(R.layout.ui_order_attribute);


        spinners_listview = (ListView) findViewById(R.id.attribute_items_listview);
        product_name = (TextView) findViewById(R.id.product_name);
        product_price = (TextView) findViewById(R.id.product_price);
        btn_done = (Button) findViewById(R.id.btn_done);
        btn_cancel = (Button) findViewById(R.id.btn_Cancel);
        et_quantity = (EditText) findViewById(R.id.tv_quantity);
        tv_amt_total = (TextView) findViewById(R.id.tv_total_amt);
        iv_plus = (LinearLayout) findViewById(R.id.iv_plus);
        iv_minus = (LinearLayout) findViewById(R.id.iv_minus);

        btn_cancel.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        iv_minus.setOnClickListener(this);
        iv_plus.setOnClickListener(this);


        pb_obj = (ProdVal) getIntent().getExtras().getSerializable("pb");
        position = getIntent().getExtras().getInt("position");


        price_per_prod = Double.parseDouble(pb_obj.getPrice());
        matchByProdId();

    }//oncreate

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                attribute_list_selcted = pb_obj.getProductAttribute();
                String prodValue = "";

                for (int i = 0; i < attribute_list_selcted.size(); i++) {
                    prodValue = attribute_list_selcted.get(i).getAttributeValue();
                    // new StringBuilder().append(prodValue).append(prodValue);
                    if (prodValue == null || prodValue.isEmpty() == true) {
                        DialogUtility.showToast(this, "Please select attribute");
                        System.out.println("<<----prodValue--" + prodValue);
                        return;
                    }
                }
                addingToCart();
                break;


            case R.id.btn_Cancel:
                Intent in = new Intent();
                Bundle bn = new Bundle();
                bn.putSerializable("pb", pb_obj);
                in.putExtras(bn);
                in.putExtra("position", position);
                setResult(0, in);
                finish();

                break;
            case R.id.iv_plus:
                increment();
                break;

            case R.id.iv_minus:
                decrement();
                break;

            default:
                break;
        }
    }


    public void matchByProdId() {
        if (pb_obj != null) {

            tv_amt_total.setText(pb_obj.getPrice());
            product_name.setText(pb_obj.getProductName());
            product_price.setText(pb_obj.getPrice());

            product_attribute = (ArrayList<Product_attribute>) pb_obj.getProductAttribute();

            AttributeAdapter obj = new AttributeAdapter(Attribute_Activity.this, pb_obj);
            spinners_listview.setAdapter(obj);
        }

    }

    public void decrement() {
        if (minteger > 1) {
            minteger = minteger - 1;
        }
        total_amt = price_per_prod * minteger;
        et_quantity.setText("" + minteger);
        tv_amt_total.setText("" + total_amt);
    }


    public void increment() {
        minteger = minteger + 1;
        et_quantity.setText("" + minteger);
        total_amt = price_per_prod * minteger;
        tv_amt_total.setText("" + total_amt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            Intent in = new Intent();
            Bundle bn = new Bundle();
            bn.putSerializable("pb", pb_obj);
            in.putExtras(bn);
            in.putExtra("position", position);
            setResult(200, in);
            finish();
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent in = new Intent();
        Bundle bn = new Bundle();
        bn.putSerializable("pb", pb_obj);
        in.putExtras(bn);
        in.putExtra("position", position);
        setResult(200, in);
        finish();
    }

    public void updateProductbean(ProdVal pb) {
        pb_obj = pb;
    }


    public ProdVal ProdVal() {
        return pb_obj;
    }


    public void addingToCart() {

        pb_obj.setAddedToCart(true);
        pb_obj.setCount(pb_obj.getCount() + 1);
        PreferenceConstant.CART_COUNT++;
        int quantity = Integer.parseInt(et_quantity.getText().toString());
        pb_obj.setProduct_quantity_selected(quantity);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("pb", pb_obj);
        intent.putExtras(bundle);
        intent.putExtra("position", position);
        setResult(1, intent);
        finish();

    }
}
