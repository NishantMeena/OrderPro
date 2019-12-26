package com.customer.orderproupdated.UI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.RecyclerItemClickListener;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.adapter.QuikOrderAdapter;
import com.customer.orderproupdated.bean.CategoryBean;
import com.customer.orderproupdated.bean.ProdVal;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuickOrderActivity extends AppCompatActivity {
    Toolbar toolbar;
    Context ctx;
    private android.support.v7.widget.SearchView searchView = null;
    private android.support.v7.widget.SearchView.OnQueryTextListener queryTextListener;
    ArrayList<ProdVal> cart_list = new ArrayList<ProdVal>();
    public QuikOrderAdapter quikOrderAdapter;
    Menu menu;
    SharedPrefrence prefrence;
    ArrayList<CategoryBean> itemCategories = new ArrayList<CategoryBean>();
    List<ProdVal> allProductsList = new ArrayList<ProdVal>();
    ApiInterface apiService;
    RecyclerView recyclerView;
    RelativeLayout no_prod_layout;

    HashMap<String, ProdVal> productsMap = new HashMap<String, ProdVal>();
    private String[] mKeys;
    HashMap<String, ProdVal> cartMap = new HashMap<String, ProdVal>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_quick_order);
        ctx = this;
        prefrence = SharedPrefrence.getInstance(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        no_prod_layout = (RelativeLayout) findViewById(R.id.no_prod_layout);
        // Prepare toolbar...
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            DialogUtility.showLOG("GetSupportActionBar returned null.");
        }
        actionBar.setTitle("Quick Order");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = ((RecyclerView) findViewById(R.id.recyclerView));

        productsMap.clear();
        productsMap = prefrence.getProduct(SharedPrefrence.QUICK_ORDER_LIST);
        mKeys = productsMap.keySet().toArray(new String[productsMap.size()]);
        checkifEmpty();
        quikOrderAdapter = new QuikOrderAdapter(QuickOrderActivity.this, productsMap, QuickOrderActivity.this);
        recyclerView.setAdapter(quikOrderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(QuickOrderActivity.this));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(QuickOrderActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Log.e("@@@@@", "" + position + productsMap.get(mKeys[position]).getProductName());
                    }
                })
        );
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
        Utility.setBadgeCount(this, icon, PreferenceConstant.CART_COUNT + "");
        return super.onCreateOptionsMenu(menu);
    }

    public void refreshview() {
        ActivityCompat.invalidateOptionsMenu(QuickOrderActivity.this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                return false;
            case R.id.action_cart:
                Intent in = new Intent(this, CartActivity.class);
                startActivityForResult(in, 100);
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
        int searchSrcTextId = android.support.v7.appcompat.R.id.search_src_text;
        EditText searchEditText = (EditText) mSearchView.findViewById(searchSrcTextId);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHintTextColor(Color.LTGRAY);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 200) {
            Log.e("response from popup", "");

            if (data != null) {
                ProdVal pb = (ProdVal) data.getExtras().getSerializable("pb");
                boolean added_to_cart = pb.isAddedToCart();
                int position = data.getIntExtra("position", 0);
                productsMap.get(pb.getProductId()).setAddedToCart(added_to_cart);
                productsMap.get(pb.getProductId()).setCount(pb.getCount());
                prefrence.setProductList(SharedPrefrence.QUICK_ORDER_LIST, productsMap);

                if (resultCode == RESULT_CANCELED) {
                    checkifEmpty();
                    quikOrderAdapter = new QuikOrderAdapter(this, productsMap, QuickOrderActivity.this);
                    refreshview();
                    recyclerView.setAdapter(quikOrderAdapter);
                }

                if (resultCode == 1) {
                    cart_list = prefrence.getCartList(SharedPrefrence.CART_LIST);
                    cart_list.add(pb);
                    prefrence.setCartList(SharedPrefrence.CART_LIST, cart_list);
                    checkifEmpty();
                    //cartMap.put(pb.getProductId(),pb);
                    //prefrence.setProductList(SharedPrefrence.CART_LIST, cartMap);
                    quikOrderAdapter = new QuikOrderAdapter(this, productsMap, QuickOrderActivity.this);
                    refreshview();
                    recyclerView.setAdapter(quikOrderAdapter);
                }

            }
        }

        if (requestCode == 100) {

            if (resultCode == 0) {

            }
            if (resultCode == 1) {
                // getProductList();
                productsMap = prefrence.getProduct(SharedPrefrence.QUICK_ORDER_LIST);

                // mKeys = productsMap.keySet().toArray(new String[productsMap.size()]);
                checkifEmpty();
                quikOrderAdapter = new QuikOrderAdapter(QuickOrderActivity.this, productsMap, QuickOrderActivity.this);

                recyclerView.setAdapter(quikOrderAdapter);
            }
        }
    }

    private void prepareSearchView(@NonNull final MenuItem searchItem) {
        final SearchView searchView = (SearchView) searchItem.getActionView();
        // searchView.setSubmitButtonEnabled(true);
        SearchManager searchManager = (SearchManager) QuickOrderActivity.this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(QuickOrderActivity.this.getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                try {
                    quikOrderAdapter.getFilter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                // Submit search query and hide search action view.
                quikOrderAdapter.getFilter(query);
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshview();
        productsMap.clear();
        productsMap = prefrence.getProduct(SharedPrefrence.QUICK_ORDER_LIST);
        mKeys = productsMap.keySet().toArray(new String[productsMap.size()]);
        checkifEmpty();
        quikOrderAdapter = new QuikOrderAdapter(QuickOrderActivity.this, productsMap, QuickOrderActivity.this);
        recyclerView.setAdapter(quikOrderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(QuickOrderActivity.this));
    }

    public void checkifEmpty() {
        if (productsMap.size() == 0) {
            no_prod_layout.setVisibility(View.VISIBLE);
        }
    }

}
