package com.customer.orderproupdated.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.CartActivity;
import com.customer.orderproupdated.UI.QuickOrderActivity;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.RecyclerItemClickListener;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.adapter.FavouritesAdapter;
import com.customer.orderproupdated.adapter.QuikOrderAdapter;
import com.customer.orderproupdated.bean.ProdVal;
import com.customer.orderproupdated.bean.Products_bean;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;

public class FavouritesFragment extends Fragment {
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    View view;
    Menu menu;
    FavouritesAdapter favouritesAdapter;
    LinearLayout emptylayout;
    RelativeLayout contentlayout;
    SharedPrefrence prefrence;
    Products_bean pb_obj;
    RecyclerView favourite_recyclerView;
    ArrayList<ProdVal> cart_list = new ArrayList<ProdVal>();

    //favourite map & list
    HashMap<String, ProdVal> favouriteMap = new HashMap<String, ProdVal>();
    public ArrayList<ProdVal> favouritelist = new ArrayList<ProdVal>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favourite, container, false);
        emptylayout = (LinearLayout) view.findViewById(R.id.wishlist_empty);
        contentlayout = (RelativeLayout) view.findViewById(R.id.wishlist_content);

        prefrence = SharedPrefrence.getInstance(getActivity());

        //Setting Toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Favourites");

        //getting fav Map from preference
        favouriteMap=prefrence.getFavouriteMap(SharedPrefrence.FAVOURITE_LIST);
        //storing in fav List
        favouritelist= new ArrayList<ProdVal>(favouriteMap.values());


        favouritesAdapter = new FavouritesAdapter(FavouritesFragment.this,favouritelist);

        //passing adpater to RecyclerView
        favourite_recyclerView = ((RecyclerView) view.findViewById(R.id.lv_favourite));
        favourite_recyclerView.setAdapter(favouritesAdapter);
        favourite_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        favourite_recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e("@@@@@", "" + position + favouritelist.get(position).getProductName());
                    }
                })
        );
        checkIfEmpty();
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
                    favouritesAdapter.getFilter(newText);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                /*   quikOrderAdapter.getFilter(newText);*/
                favouritesAdapter.getFilter(query);
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      /*  if (requestCode == 200) {
            if (resultCode == 1) {
                FavouritesFragment.favouritselist.set(data.getIntExtra("position", 0), (ProdVal) data.getExtras().getSerializable("pb"));
                favouritesAdapter.notifyDataSetChanged();
                cartList.add((ProdVal) data.getExtras().getSerializable("pb"));
                prefrence.setCartList(SharedPrefrence.CART_LIST, cartList);
            }
        }*/

        if (requestCode == 200) {
            Log.e("response from popup", "");

            if (data != null) {
                ProdVal pb = (ProdVal) data.getExtras().getSerializable("pb");
                boolean added_to_cart = pb.isAddedToCart();
                int position = data.getIntExtra("position", 0);

                favouriteMap.get(pb.getProductId()).setAddedToCart(added_to_cart);
                favouriteMap.get(pb.getProductId()).setCount(pb.getCount());
                prefrence.setFavouriteMap(SharedPrefrence.FAVOURITE_LIST, favouriteMap);

                if (resultCode == RESULT_CANCELED) {
                    favouritesAdapter = new FavouritesAdapter(this, favouritelist);
                    refreshview();
                    favourite_recyclerView.setAdapter(favouritesAdapter);
                }

                if (resultCode == 1) {
                    cart_list = prefrence.getCartList(SharedPrefrence.CART_LIST);
                    cart_list.add(pb);
                    prefrence.setCartList(SharedPrefrence.CART_LIST, cart_list);

                    //favourite
                    favouritesAdapter = new FavouritesAdapter(this, favouritelist);
                    refreshview();
                    favourite_recyclerView.setAdapter(favouritesAdapter);
                }

            }
        }

    }

    public void removeListItem(ProdVal pb) {
        favouritelist.remove(pb);
        favouriteMap.remove(pb.getProductId());
        prefrence.setFavouriteMap(SharedPrefrence.FAVOURITE_LIST, favouriteMap);
        refreshview();
        checkIfEmpty();
        favouritesAdapter.notifyDataSetChanged();
    }

    public void checkIfEmpty() {
        if (favouritelist.size() == 0) {
            emptylayout.setVisibility(View.VISIBLE);
        }
    }

    public void refreshview() {
        ActivityCompat.invalidateOptionsMenu(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshview();
        favouriteMap.clear();
        favouriteMap = prefrence.getProduct(SharedPrefrence.FAVOURITE_LIST);

        favouritelist.clear();
        favouritelist= new ArrayList<ProdVal>(favouriteMap.values());

        checkIfEmpty();
        favouritesAdapter = new FavouritesAdapter(FavouritesFragment.this, favouritelist);
        favourite_recyclerView.setAdapter(favouritesAdapter);

    }
}