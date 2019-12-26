package com.customer.orderproupdated.UI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;

import java.util.List;


public class ContactusActivity extends AppCompatActivity {

    Button btn_email;
    Toolbar toolbar;
    TextView tv_address,title_address;
    EditText et_name,et_query,et_subject,et_email;
    ImageView iv_back;
    View view_line;

    String email;
    String subject="",content="",name="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.ui_contact_us);

        //find id
        tv_address=(TextView)findViewById(R.id.tv_address);
        btn_email=(Button)findViewById(R.id.button_email);
        et_name=(EditText)findViewById(R.id.et_name);
        et_email=(EditText)findViewById(R.id.et_email);
        et_subject=(EditText)findViewById(R.id.et_subject);
        et_query=(EditText)findViewById(R.id.et_query);
        iv_back=(ImageView)findViewById(R.id.iv_back) ;
        title_address=(TextView)findViewById(R.id.title_address);
        view_line=(View)findViewById(R.id.view_line);

        tv_address.setVisibility(View.GONE);
        title_address.setVisibility(View.GONE);
        view_line.setVisibility(View.GONE);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getvalues
                name=et_name.getText().toString().trim();
                email=et_email.getText().toString().trim();
                subject=et_subject.getText().toString().trim();
                content=et_query.getText().toString().trim();
                if(name.length()==0)
                {
                    DialogUtility.showToast(ContactusActivity.this,"please enter name");
                }else if(email.length()==0)
                {
                    DialogUtility.showToast(ContactusActivity.this,"Please enter email");
                }
                else {
                    shareToGMail(email, subject, content, name);
                }

            }
        });
    }
    public void shareToGMail(String email, String subject, String content,String name) {

        String emailReciever="";
        String mailBody="Name:\t"+name+"\n"+"Email:\t"+email+"\n"+content;


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{emailReciever});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mailBody);
        final PackageManager pm = this.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for(final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        this.startActivity(emailIntent);
        finish();
    }
}
