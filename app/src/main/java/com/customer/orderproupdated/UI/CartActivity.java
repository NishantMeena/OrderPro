package com.customer.orderproupdated.UI;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.adapter.CartAdapter;
import com.customer.orderproupdated.bean.PlaceOrderResponse;
import com.customer.orderproupdated.bean.ProdVal;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.customer.orderproupdated.parser.JSONParser.getJsonString;

/**
 * Created by Sony on 1/9/2017.
 */
public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ListView list_cart;
    private android.support.v7.widget.SearchView searchView = null;
    private android.support.v7.widget.SearchView.OnQueryTextListener queryTextListener;
    ArrayList<ProdVal> cart_list = new ArrayList<>();
    ArrayList<ProdVal> temp_cart_list = new ArrayList<>();
    SearchView.OnQueryTextListener listener;
    CartAdapter cartAdapter;
    public TextView txt_grand_total;
    LinearLayout EmptyLayout;
    Button btn_placeorder;
    Menu menu;
    Button btn_start_shopping;
    SharedPrefrence prefrence;
    RelativeLayout layout_total;

    Double total = 0.0;
    ActionBar actionBar;
    String jsonToPlaceOrder;
    ApiInterface apiService;
    //Favourite MAP
    HashMap<String, ProdVal> favouriteMap = new HashMap<String, ProdVal>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_cart);
        refreshview();
        prefrence = SharedPrefrence.getInstance(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        cart_list=prefrence.getCartList(SharedPrefrence.CART_LIST);

        favouriteMap = prefrence.getFavouriteMap(SharedPrefrence.FAVOURITE_LIST);

        txt_grand_total = (TextView) findViewById(R.id.grand_total);

        //Prepare toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            DialogUtility.showLOG("GetSupportActionBar returned null.");
        }
        actionBar.setTitle("Cart" + " (" + cart_list.size() + ")");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cart_list.size()<1)
                {
                    setResult(1);
                    finish();
                }
                else
                {
                    setResult(0);
                    finish();
                }

            }
        });
        //find id
        list_cart = (ListView) findViewById(R.id.lv_cart);

        btn_placeorder = (Button) findViewById(R.id.btn_place_order);


        //btn_clicks

        btn_placeorder.setOnClickListener(this);
        //setting data in  adapter
        cartAdapter = new CartAdapter(CartActivity.this, cart_list);
        list_cart.setAdapter(cartAdapter);
        PreferenceConstant.CART_COUNT = cartAdapter.getCount();
        list_cart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
         countGrandTotal();

        layout_total = (RelativeLayout) findViewById(R.id.total_layout);
        EmptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
        checkIfEmpty();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_place_order:
                createJson();
                showPlaceOrderDialog(this, "Confirmation!", "Press ok to confirm your order");


                break;

        }

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
        itemCart.setVisible(false);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        Utility.setBadgeCount(this, icon, PreferenceConstant.CART_COUNT + "");

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
                finish();
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
            //cartMap.set(data.getIntExtra("position", 0), (ProdVal) data.getExtras().getSerializable("pb"));

            cartAdapter.notifyDataSetChanged();
        }

    }

    private void prepareSearchView(@NonNull final MenuItem searchItem) {
        final SearchView searchView = (SearchView) searchItem.getActionView();

        // searchView.setSubmitButtonEnabled(true);
        SearchManager searchManager = (SearchManager) CartActivity.this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(CartActivity.this.getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                try {

                    //String txt = f5EditTextSearch.getText().toString().trim().toLowerCase();
                    //cartAdapter.getFilter(newText);
                    cartAdapter.getFilter(newText);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                cartAdapter.getFilter(query);

                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
    }

    public void removeListItem(int position) {

        cart_list.remove(position);
        prefrence.setCartList(SharedPrefrence.CART_LIST, cart_list);
        PreferenceConstant.CART_COUNT--;
        actionBar.setTitle("Cart" + " (" + cart_list.size() + ")");
        cartAdapter.notifyDataSetChanged();
        checkIfEmpty();
        Double total_amt = 0.0;

        for (int i = 0; i < cart_list.size(); i++) {
            total_amt = total_amt + Double.parseDouble(cart_list.get(i).getPrice()) * cart_list.get(i).getProduct_quantity_selected();
        }
        txt_grand_total.setText(total_amt + "");
        if(cart_list.size()==0)
        {
            layout_total.setVisibility(View.GONE);
        }
    }

    public void refreshview() {
        ActivityCompat.invalidateOptionsMenu(CartActivity.this);

    }

    public void addToFavouritselist(ProdVal pb) {
        favouriteMap.put(pb.getProductId(),pb);
        prefrence.setFavouriteMap(SharedPrefrence.FAVOURITE_LIST, favouriteMap);
    }

    public void removeFromFavouritselist(ProdVal pb) {
        favouriteMap.remove(pb.getProductId());
        prefrence.setFavouriteMap(SharedPrefrence.FAVOURITE_LIST, favouriteMap);
    }

    public void checkIfEmpty() {
        if (cart_list.size() == 0) {
            layout_total.setVisibility(View.GONE);
            btn_placeorder.setVisibility(View.GONE);
            EmptyLayout.setVisibility(View.VISIBLE);
            btn_start_shopping = (Button) findViewById(R.id.btn_start);
            btn_start_shopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                    startActivity(intent);

                }
            });


        }
    }

    public void countGrandTotal() {

        if (cart_list != null)
            cart_list.clear();
        cart_list = prefrence.getCartList(SharedPrefrence.CART_LIST);
       //mKeys = cartMap.keySet().toArray(new String[cartMap.size()]);
        try {
            for (int i = 0; i < cart_list.size(); i++) {
                total = total + Double.parseDouble(cart_list.get(i).getPrice()) * cart_list.get(i).getProduct_quantity_selected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        txt_grand_total.setText(total + "");
        PreferenceConstant.TOTAL_PRICE = total;
        cartAdapter = new CartAdapter(CartActivity.this, cart_list);
        list_cart.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

    }

    public void showPlaceOrderDialog(final Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);

        String positiveText = "ok";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        placeOrderToMerchnat();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void createJson() {
        JSONObject json_orderToPlace = new JSONObject();
        JSONArray products_array = new JSONArray();
        JSONArray jsonArray;
        JSONObject jsonObject_order;
        String attribute_id;
        String attribute_value;
        String product_id, quantity;
        Gson gson = new Gson();
        //cart_list.clear();
        temp_cart_list.addAll(cart_list);
        String json = gson.toJson(temp_cart_list);
        System.out.println("<<---json cartlist-" + json);
        try {
            jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject_order = jsonArray.getJSONObject(i);
                JSONArray jsonArray_productAttribute = jsonObject_order.getJSONArray("product_attribute");
                JSONObject item = new JSONObject();
                try {
                    json_orderToPlace.put("customer_id", prefrence.getValue(SharedPrefrence.CUSTOMERID));
                    json_orderToPlace.put("merchant_id", prefrence.getValue(SharedPrefrence.MERCHANT_ID));
                    json_orderToPlace.put("total_amount", prefrence.getValue(PreferenceConstant.TOTAL_PRICE + ""));
                    json_orderToPlace.put("order_date", Utility.getCurentDateIFormate());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                product_id = getJsonString(jsonObject_order, "product_id");
                item.put("product_id", product_id);
                quantity = getJsonString(jsonObject_order, "product_quantity_selected");
                item.put("quantity", quantity);
                for (int j = 0; j < jsonArray_productAttribute.length(); j++) {
                    JSONObject jsonObject_attribute = jsonArray_productAttribute.getJSONObject(j);
                    attribute_id = getJsonString(jsonObject_attribute, "attribute_id");
                    attribute_value = getJsonString(jsonObject_attribute, "attribute_value");
                    if (j == 0) {
                        item.put("prod_attribute_id1", attribute_id);
                        item.put("prod_attribute_value1", attribute_value);
                    } else if (j == 1) {
                        item.put("prod_attribute_id2", attribute_id);
                        item.put("prod_attribute_value2", attribute_value);
                    } else if (j == 2) {
                        item.put("prod_attribute_id3", attribute_id);
                        item.put("prod_attribute_value3", attribute_value);
                    }
                }
                products_array.put(item);
            }
            json_orderToPlace.put("product", products_array);
            jsonToPlaceOrder = json_orderToPlace.toString();
            System.out.println("<<----jsonToPlaceOrder--" + jsonToPlaceOrder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void placeOrderToMerchnat() {
        if (Utility.checkInternetConn(CartActivity.this)) {
            DialogUtility.showProgressDialog(CartActivity.this, false, "Loading");
            try {
                Call<PlaceOrderResponse> call = apiService.getPlaceOrderResponse(jsonToPlaceOrder);
                call.enqueue(new Callback<PlaceOrderResponse>() {
                    @Override
                    public void onResponse(Call<PlaceOrderResponse> call, Response<PlaceOrderResponse> response) {
                        Log.e("placeorder response", "" + response);
                        if (response.body() != null) {
                            if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {
                                DialogUtility.showToast(CartActivity.this, "Order Placed Successfully");
                                if (cart_list != null)
                                    cart_list.clear();
                                cartAdapter = new CartAdapter(CartActivity.this, cart_list);
                                list_cart.setAdapter(cartAdapter);
                                actionBar.setTitle("Cart" + " (" + cart_list.size() + ")");
                                checkIfEmpty();
                                prefrence.setCartList(SharedPrefrence.CART_LIST, cart_list);
                                Log.e("placeorder response", "" + cart_list);

                                PreferenceConstant.CART_COUNT=0;

                            } else if (response.body().getStatus().equals(PreferenceConstant.FAIL_STATUS)) {
                                DialogUtility.showToast(CartActivity.this, "Order Placement Failed");
                            }

                        }
                        DialogUtility.pauseProgressDialog();

                    }

                    @Override
                    public void onFailure(Call<PlaceOrderResponse> call, Throwable t) {
                        DialogUtility.showToast(CartActivity.this, getResources().getString(R.string.request_fail));
                        DialogUtility.pauseProgressDialog();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DialogUtility.showMaterialDialog(CartActivity.this, getString(R.string.alert), getString(R.string.checknet));

        }


    }

}