package com.customer.orderproupdated.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import com.customer.orderproupdated.R;

import java.util.List;

/**
 * Created by Advantal on 8/16/2017.
 */

public class FeedbackActivity extends Activity implements View.OnClickListener {
    Button btn_rate_now, btn_give_suggestions;
    ImageView iv_cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_feedback);

        //dialog
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        //find ids
        btn_give_suggestions = (Button) findViewById(R.id.btn_give_suggestions);
        btn_rate_now = (Button) findViewById(R.id.btn_rate_now);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);

        btn_give_suggestions.setOnClickListener(this);
        btn_rate_now.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_give_suggestions:

                givesuggestion();

                break;

            case R.id.btn_rate_now:

                giveRating();

                break;

            case R.id.iv_cancel:

                finish();

                break;
        }
    }

    public void givesuggestion() {

        String emailReciever="";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{emailReciever});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback:OrderPro Android App");
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
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

    public void giveRating() {

        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

    }
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();

            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

}
