package com.customer.orderproupdated.Fragments;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.adapter.RecentAdapter;
import com.customer.orderproupdated.chat.activity.ChatWindow;
import com.customer.orderproupdated.chat.bean.RecentChat;
import com.customer.orderproupdated.database.DatabaseHandler;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    Toolbar toolbar;
    Menu menu;
    View view;
    public ListView recentList;
    RecentAdapter adapter;
    ArrayList<com.customer.orderproupdated.chat.bean.RecentChat> lstSMS = new ArrayList<RecentChat>();
    DatabaseHandler db;
    SharedPrefrence share;
    TextView tv_no_chat;
    private BroadcastReceiver receiveSMS;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // refreshview();
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        // toolbar.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Chats");
        share = SharedPrefrence.getInstance(getActivity());
        db = new DatabaseHandler(getActivity());
        tv_no_chat = (TextView) view.findViewById(R.id.tv_no_chat);
        recentList = (ListView) view.findViewById(R.id.recentList);
        recentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoFriendDetail(position);
            }
        });

        loadAllLstContentFromDB();
        return view;
    }

    public ChatFragment() {
        // Required empty public constructor...
    }

    public void gotoFriendDetail(int position) {
        Intent intent = null;
        String mobile_no = "";
        String user_name = "";
        user_name = (lstSMS.get(position).getName());
        // user_name = Utility.contactExists(getActivity(), mobile_no);
        intent = new Intent(getActivity(), ChatWindow.class);
        intent.putExtra("username", user_name);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyBoard();
        loadAllLstContentFromDB();

        receiveSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateSMSlist();

            }
        };

    }

    public void loadAllLstContentFromDB() {


        lstSMS = db.getChatByGroup();
        adapter = new RecentAdapter(this, lstSMS, "SMS");
        recentList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (lstSMS.size() == 0) {
            tv_no_chat.setVisibility(View.VISIBLE);
        } else {
            tv_no_chat.setVisibility(View.INVISIBLE);

        }

    }

    public void updateSMSlist() {
        // lstSMS = db.getOpnConForSMS();
        adapter = new RecentAdapter(this, lstSMS, "SMS");
        recentList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (lstSMS.size() == 0) {
            tv_no_chat.setVisibility(View.VISIBLE);
        } else {
            tv_no_chat.setVisibility(View.INVISIBLE);

        }


    }


    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(receiveSMS);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideKeyBoard() {
        // Then just use the following:
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(recentList.getWindowToken(), 0);
    }

    private void prepareSearchView(@NonNull final MenuItem searchItem) {
        final SearchView searchView = (SearchView) searchItem.getActionView();

        // searchView.setSubmitButtonEnabled(true);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                try {

                    /*   quikOrderAdapter.getFilter(newText);*/
                    adapter.getFilter(newText);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter(query);
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
    }

}