package com.customer.orderproupdated.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.CartActivity;
import com.customer.orderproupdated.UI.OrderDetailsActivity;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.adapter.OrderHistoryAdapter;
import com.customer.orderproupdated.bean.MyOrderResponse;
import com.customer.orderproupdated.bean.Order;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends Fragment {
    ListView order_listview;
    ArrayList<Order> orderlist;
    View view;
    OrderHistoryAdapter orderHistoryAdapter;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    Menu menu;
    TextView tv_no_orders;
    // DummyData ddobj=new DummyData();
    ApiInterface apiService;
    String customerID = "", merchantID = "";
    SharedPrefrence share;
    HashMap<String, Order> orderMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_myorder, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Order List");
        order_listview = (ListView) view.findViewById(R.id.lv_orderhistory);
        tv_no_orders=(TextView)view.findViewById(R.id.tv_no_orders);
        share = SharedPrefrence.getInstance(getActivity());
        apiService = ApiClient.getClient().create(ApiInterface.class);
        //orderlist = ddobj.initOrderList();

        merchantID=share.getValue(SharedPrefrence.MERCHANT_ID);
        if(!merchantID.equals(""))
        {
            getData();
        }
        else
        {
            tv_no_orders.setVisibility(View.VISIBLE);
            order_listview.setVisibility(View.GONE);
        }


        order_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getActivity(), OrderDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ob", orderlist.get(position));
                in.putExtras(bundle);
                startActivity(in);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_main, menu);


        //search
        MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            prepareSearchView(searchItem);
        }
        //cart badge count
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        Utility.setBadgeCount(getActivity(), icon, PreferenceConstant.CART_COUNT + "");
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                return false;
            case R.id.action_cart:
                Intent in = new Intent(getActivity(), CartActivity.class);
                startActivity(in);
                return true;

            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem searchViewMenuItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchViewMenuItem);
        int searchImgId = android.support.v7.appcompat.R.id.search_button; // I used the explicit layout ID of searchview's ImageView
        ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_action_search);
        super.onPrepareOptionsMenu(menu);
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
                    orderHistoryAdapter.getFilter(newText);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                /*   quikOrderAdapter.getFilter(newText);*/
                if(orderlist.size()>0) {
                    orderHistoryAdapter.getFilter(query);
                }else
                {
                    hideKeyBoard();
                    DialogUtility.showToast(getActivity(), "No result found");
                }


                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
    }

    public void getData() {
        if (Utility.checkInternetConn(getActivity())) {
            customerID = share.getValue(SharedPrefrence.CUSTOMERID);
            merchantID = share.getValue(SharedPrefrence.MERCHANT_ID);
            try {
                DialogUtility.showProgressDialog(getActivity(), true, getString(R.string.please_wait));
                Call<MyOrderResponse> call = apiService.getMyOrderList(merchantID, customerID);
                call.enqueue(new Callback<MyOrderResponse>() {
                    @Override
                    public void onResponse(Call<MyOrderResponse> call, Response<MyOrderResponse> response) {

                        if (response.body() != null) {

                            if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {

                                orderlist=new ArrayList<Order>();
                                orderlist=(ArrayList<Order>) response.body().getOrder();

                                Log.e("orderlist_size",orderlist.size()+"");


                                //---setAdapter--

                                if(orderlist.size()>0)
                                {
                                     orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryFragment.this, orderlist);
                                    order_listview.setAdapter(orderHistoryAdapter);
                                }
                                else
                                {
                                    order_listview.setVisibility(View.GONE);
                                    tv_no_orders.setVisibility(View.VISIBLE);

                                }

                                //---store in sharedPreference--
                                orderMap=new HashMap<String, Order>();
                                for(int j=0;j<orderlist.size();j++)
                                {
                                    orderMap.put(orderlist.get(j).getOrderId(),orderlist.get(j));
                                }
                                share.setOrderList(SharedPrefrence.ORDERLIST, orderMap);
                                DialogUtility.pauseProgressDialog();

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<MyOrderResponse> call, Throwable t) {
                        DialogUtility.showToast(getActivity(), getResources().getString(R.string.request_fail));
                        DialogUtility.pauseProgressDialog();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DialogUtility.showMaterialDialog(getActivity(), getString(R.string.alert), getString(R.string.checknet));
        }




    } //getdata
    public void hideKeyBoard() {
        // Then just use the following:
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
