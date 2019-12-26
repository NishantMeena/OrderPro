package com.customer.orderproupdated.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.SharedPrefrence;


public class NotificationSetting_Activity extends AppCompatActivity implements View.OnClickListener{

    //Toolbar toolbar;
    ImageView iv_back;
    com.kyleduo.switchbutton.SwitchButton btn_prod_notification,btn_offer_notification,btn_chat_notification;
    SharedPrefrence share;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_notification_setting);
        share= SharedPrefrence.getInstance(this);

        iv_back=(ImageView)findViewById(R.id.iv_back);
        btn_prod_notification=(com.kyleduo.switchbutton.SwitchButton)findViewById(R.id.prod_notification);
        btn_offer_notification=(com.kyleduo.switchbutton.SwitchButton)findViewById(R.id.offer_notification);
        btn_chat_notification=(com.kyleduo.switchbutton.SwitchButton)findViewById(R.id.chat_notification);

        iv_back.setOnClickListener(this);
        checkButtoons();


        btn_prod_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        btn_offer_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        btn_chat_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    share.setBooleanValue(SharedPrefrence.SHOW_CHAT_NOTIFICATION,true);
                }
                else
                {
                    share.setBooleanValue(SharedPrefrence.SHOW_CHAT_NOTIFICATION,false);
                }


            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }
    public void checkButtoons()
    {
        if(share.getBooleanValue(SharedPrefrence.SHOW_CHAT_NOTIFICATION))
        {
            btn_chat_notification.setChecked(true);
        }
        if(share.getBooleanValue(SharedPrefrence.SHOW_PRODUCT_NOTIFICATION))
        {
            btn_prod_notification.setChecked(true);
        }
        if(share.getBooleanValue(SharedPrefrence.SHOW_OFFER_NOTIFICATION))
        {
            btn_offer_notification.setChecked(true);
        }
    }
}