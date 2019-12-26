package com.customer.orderproupdated.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.customer.orderproupdated.R;

/**
 * Created by Sony on 12/22/2016.
 */
public class InviteFriendsFragment extends Fragment implements View.OnClickListener {

    View view;
    Button btnInvite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

    }

    public void refreshview() {
        ActivityCompat.invalidateOptionsMenu(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        refreshview();
        view = inflater.inflate(R.layout.fragment_invite_friends, container, false);
        btnInvite = (Button) view.findViewById(R.id.btnInvite);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Invite Friends");

        btnInvite.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnInvite:
                inviteFriends();
                break;
        }

    }

    void inviteFriends() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        String shareBody = "Check out OrderPro for your smartphone.Download it today from https://play.google.com/store/apps/details?";
        sharingIntent.setType("text/plain");
        if (Build.VERSION.SDK_INT >= 24) {
            Log.e("Inside if", "Refer");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        } else if (Build.VERSION.SDK_INT < 24) {
            Log.e("Inside else if", "Refer");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        }
        startActivity(Intent.createChooser(sharingIntent,"Share via :"));

    }

}
