package com.customer.orderproupdated.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.MerchantDetail;
import com.customer.orderproupdated.bean.ProdVal;


public class MerchantDetailsActivity extends AppCompatActivity {
 Toolbar toolbar;
    com.customer.orderproupdated.custom_fonts.CustomEditTxt name,emailid,number,name_below;
    //TextView merchent_name,merchant_address,number,email;
    MerchantDetail merchantDetail;
    ImageView iv_back,call,chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_merchent_profile);


        name=( com.customer.orderproupdated.custom_fonts.CustomEditTxt)findViewById(R.id.name);
        emailid=( com.customer.orderproupdated.custom_fonts.CustomEditTxt)findViewById(R.id.emailid);
        number=( com.customer.orderproupdated.custom_fonts.CustomEditTxt)findViewById(R.id.number);
        name_below=( com.customer.orderproupdated.custom_fonts.CustomEditTxt)findViewById(R.id.name_below);
        iv_back=(ImageView) findViewById(R.id.iv_back);
        call=(ImageView) findViewById(R.id.call);
        chat=(ImageView) findViewById(R.id.chat);


        merchantDetail = (MerchantDetail) getIntent().getExtras().getSerializable("Merchant Profile");

        if(merchantDetail.getUsername().equals(""))
        {
            name.setText(Utility.capitalize(merchantDetail.getFirstname()));
            emailid.setText(merchantDetail.getEmail());
            number.setText(merchantDetail.getContactno());
            name_below.setText(merchantDetail.getFirstname());

        }
        else
        {
            name.setText(Utility.capitalize(merchantDetail.getUsername()));
            emailid.setText(merchantDetail.getEmail());
            number.setText(merchantDetail.getContactno());
            name_below.setText(merchantDetail.getUsername());

        }
iv_back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
    }
});
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DialogUtility.showToast(MerchantDetailsActivity.this,"Chatting disabled utill you Switch Merchant");
            }
        });



    }

    public void dial()
    {
        Intent dial = new Intent();
        dial.setAction("android.intent.action.DIAL");
        dial.setData(Uri.parse("tel:"));
        startActivity(dial);
    }
}
