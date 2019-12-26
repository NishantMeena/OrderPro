package com.customer.orderproupdated.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.ChangePasswordActivity;
import com.customer.orderproupdated.UI.MyProfileActivity;
import com.customer.orderproupdated.UI.NotificationSetting_Activity;
import com.customer.orderproupdated.Utility.SharedPrefrence;




public class SettingsFragment extends Fragment implements View.OnClickListener {
    View view;
    LinearLayout my_profile, change_password, notification_settings;
    SharedPrefrence share;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

    }
    public  void refreshview()
    {
        ActivityCompat.invalidateOptionsMenu(getActivity());

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        refreshview();
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        my_profile = (LinearLayout) view.findViewById(R.id.my_profile);
        //my_merchants = (LinearLayout) view.findViewById(R.id.my_merchants);
        change_password = (LinearLayout) view.findViewById(R.id.change_password);
        notification_settings = (LinearLayout) view.findViewById(R.id.notification_settings);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");

        share=SharedPrefrence.getInstance(getActivity());

        my_profile.setOnClickListener(this);
        //my_merchants.setOnClickListener(this);
        change_password.setOnClickListener(this);
        notification_settings.setOnClickListener(this);




        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.my_profile:
                startActivity(new Intent(getActivity(), MyProfileActivity.class));
                //DialogUtility.showToast(getActivity(),"Under Development");

                break;
            case R.id.change_password:
               startActivity(new Intent(getActivity(), ChangePasswordActivity.class)
                       .putExtra("comes_from","setting"));

                break;
            case R.id.notification_settings:
               startActivity(new Intent(getActivity(), NotificationSetting_Activity.class));

                break;


        }

    }





}
