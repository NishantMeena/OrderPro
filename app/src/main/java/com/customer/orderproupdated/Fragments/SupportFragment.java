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
import com.customer.orderproupdated.UI.ContactusActivity;
import com.customer.orderproupdated.UI.FeedbackActivity;
import com.customer.orderproupdated.UI.TermsofuseActivity;
import com.customer.orderproupdated.Utility.DialogUtility;

/**
 * Created by Sony on 12/22/2016.
 */
public class SupportFragment extends Fragment implements View.OnClickListener {
    View view;
    LinearLayout contact_us, terms_of_use, faqs,feedback;

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
        view = inflater.inflate(R.layout.fragment_support, container, false);
        contact_us = (LinearLayout) view.findViewById(R.id.contactus);
        terms_of_use = (LinearLayout) view.findViewById(R.id.termsofuse);
        faqs = (LinearLayout) view.findViewById(R.id.faqs);
        feedback=(LinearLayout)view.findViewById(R.id.feedback);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Support");

        contact_us.setOnClickListener(this);
        terms_of_use.setOnClickListener(this);
        faqs.setOnClickListener(this);
        feedback.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.contactus:
                startActivity(new Intent(getActivity(),ContactusActivity.class));
                break;
            case R.id.termsofuse:
                //DialogUtility.showToast(getActivity(),"Under development");
                startActivity(new Intent(getActivity(),TermsofuseActivity.class));
                break;
            case R.id.faqs:
                DialogUtility.showToast(getActivity(),"Under development");
              // Toast.makeText(getActivity(),"Faq clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.feedback:
                //DialogUtility.showToast(getActivity(),"Under development");
                startActivity(new Intent(getActivity(),FeedbackActivity.class));
                break;
        }

    }


}
