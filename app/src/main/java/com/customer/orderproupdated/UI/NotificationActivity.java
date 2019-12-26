package com.customer.orderproupdated.UI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.DummyData;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.adapter.NotificationAdapter;
import com.customer.orderproupdated.bean.NotificationData;

import java.util.ArrayList;


public class NotificationActivity extends AppCompatActivity {
    ListView notification_listview;
    ArrayList<NotificationData> notificationlist;
    DummyData ddobj=new DummyData();
    
    Toolbar toolbar;
    Menu menu;
    private android.support.v7.widget.SearchView searchView = null;
    private android.support.v7.widget.SearchView.OnQueryTextListener queryTextListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_notification_activity);
        notification_listview = (ListView) findViewById(R.id.lv_notification);
        //Prepare toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            DialogUtility.showLOG( "GetSupportActionBar returned null.");
        }

        actionBar.setTitle("Notifications");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        notificationlist = ddobj.initNotificationList();

        NotificationAdapter md = new NotificationAdapter(this, notificationlist);
        notification_listview.setAdapter(md);
        notification_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(CartItemActivity.this, OrderDetailsActivity.class));
                Toast.makeText(NotificationActivity.this, "Notification clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        //search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            prepareSearchView(searchItem);
        }
        //cart badge count
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        Utility.setBadgeCount(this, icon, PreferenceConstant.CART_COUNT+"");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return false;
            case R.id.action_cart:
                //DialogUtility.showToast(this, "Cart");
                Intent in = new Intent(this, CartActivity.class);
                startActivity(in);
                return true;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem searchViewMenuItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchViewMenuItem);
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setIconified(true);
        int searchImgId = android.support.v7.appcompat.R.id.search_button; // I used the explicit layout ID of searchview's ImageView
        ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_action_search);
        //text color
        int searchSrcTextId =android.support.v7.appcompat.R.id.search_src_text;
        EditText searchEditText = (EditText) mSearchView.findViewById(searchSrcTextId);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHintTextColor(Color.LTGRAY);
        return super.onPrepareOptionsMenu(menu);
    }

    private void prepareSearchView(@NonNull final MenuItem searchItem) {
        final SearchView searchView = (SearchView) searchItem.getActionView();
        // searchView.setSubmitButtonEnabled(true);
        SearchManager searchManager = (SearchManager) NotificationActivity.this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(NotificationActivity.this.getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                try {
                    // String txt = f5EditTextSearch.getText().toString().trim().toLowerCase();
                    // quikOrderAdapter.getFilter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                // Submit search query and hide search action view.
                // quikOrderAdapter.getFilter(query);
                /* searchView.setQuery("", false);
                   searchView.setIconified(true);*/
                // searchItem.collapseActionView();
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
    }
}



