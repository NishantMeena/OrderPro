package com.customer.orderproupdated.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.adapter.CustomGridViewAdapter;
import com.customer.orderproupdated.bean.MerchantDetail;
import com.customer.orderproupdated.bean.ProdVal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sony on 2/2/2017.
 */
public class AttributeSelectionActivity extends AppCompatActivity {

    GridView androidGridView;
    TextView tv_selected_value;
    Button btn_done;
    String attribute_selected="";
    TextView tv_product_price,tv_product_name;
    ImageView product_image;
    SharedPrefrence share;
    MerchantDetail merchantDetail;
    TextView label_attribute_name;
    ProdVal pb_obj;
    String prod_name,prod_price="";
    String prod_attributes="",attribute_name="";
    ArrayList<String > image_list=new ArrayList<>();

    String[] gridViewString ;
    View lastView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_select_attribute);
        prepareToolbar();
        share=SharedPrefrence.getInstance(this);

        pb_obj = (ProdVal) getIntent().getExtras().getSerializable("pb");
        prod_name = pb_obj.getProductName();
        prod_price=pb_obj.getPrice();
        image_list= (ArrayList<String>) pb_obj.getImage();

        prod_attributes=getIntent().getExtras().getString("attribute_val");
        attribute_name=getIntent().getExtras().getString("attribute_name");
        gridViewString=prod_attributes.split(",");

        CustomGridViewAdapter adapterViewAndroid = new CustomGridViewAdapter(AttributeSelectionActivity.this, gridViewString);
        androidGridView = (GridView) findViewById(R.id.grid_view_image_text);
        tv_selected_value = (TextView) findViewById(R.id.tv_selected_size);
        btn_done = (Button) findViewById(R.id.btn_done);
        tv_product_name = (TextView) findViewById(R.id.product_name);
        tv_product_price = (TextView) findViewById(R.id.p_price);
        label_attribute_name = (TextView) findViewById(R.id.label_attribute_name);
        product_image=(ImageView)findViewById(R.id.product_image);


        tv_product_name.setText(prod_name);
        tv_product_price.setText(prod_price);
        label_attribute_name.setText(attribute_name);



        String url = image_list.get(0);
        url= url.replace(" ","%20");
        Picasso.with(this)
                .load("https://36.255.3.15/order_p/image/"+url)
                .fit().centerInside()
                .placeholder(R.drawable.product_default)
                .error(R.drawable.product_default)
                .into(product_image);


        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int i, long id) {
                tv_selected_value.setText(gridViewString[+i]);
                attribute_selected=tv_selected_value.getText().toString();

                if(lastView==null){
                    v.setBackgroundColor(getResources().getColor(R.color.listselector));

                    lastView=v;
                }
                else{
                    lastView.setBackgroundColor(Color.WHITE);
                    v.setBackgroundColor(getResources().getColor(R.color.listselector));

                    lastView=v;
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent();
                if(attribute_selected.equals(""))
                {
                    DialogUtility.showToast(AttributeSelectionActivity.this,"Please Select variant");
                }else
                {
                    in.putExtra("value",attribute_selected);
                    setResult(Activity.RESULT_OK, in);
                    finish();
                }

            }
        });

    }

    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            //DialogUtility.showLOG( "GetSupportActionBar returned null.");
        }
        actionBar.setTitle("Select Variant");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent in=new Intent();
                if(attribute_selected.equals(""))
                {
                    in.putExtra("value","Select");
                }else
                {
                    in.putExtra("value",attribute_selected);
                }
                setResult(Activity.RESULT_OK, in);*/
                finish();
            }
        });
    }


}
