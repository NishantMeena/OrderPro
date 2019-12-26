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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.CartActivity;
import com.customer.orderproupdated.UI.ProductDetailActivity;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.RecyclerItemClickListener;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.adapter.DashBoardAdapter;
import com.customer.orderproupdated.adapter.ProductAdapter;
import com.customer.orderproupdated.bean.ProdVal;
import com.customer.orderproupdated.bean.SubCategory;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

public class DashBoardChildFragment extends Fragment implements View.OnClickListener {
    public RecyclerView mRecyclerView;
    public LinearLayout insertlayout;
    public HorizontalScrollView horizontalscroll;
    public TextView tv_a, tv_b;
    public ImageView iv_a, iv_b;
    public DashBoardAdapter mAdapter;
    View v;
    Menu menu;
    private SubCategory subcatObj = new SubCategory();
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    ApiInterface apiService;
    public List<SubCategory> firstList = new ArrayList<>();
    public List<SubCategory> firstListTemp;
    public List<SubCategory> subcategory_first = new ArrayList<>();
    public List<ProdVal> prodList = new ArrayList<>();
    public List<SubCategory> TempList;
    public List<ProdVal> prodListIncat = new ArrayList<>();
    public List<SubCategory> blanklist = new ArrayList<>();
    ProductAdapter adapter;

    SharedPrefrence share;
    private int posi;
    private int flag = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_dashboard_child, container, false);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        share = SharedPrefrence.getInstance(getActivity());

        mRecyclerView = (RecyclerView) v.findViewById(R.id.Recyclerview);
        insertlayout = (LinearLayout) v.findViewById(R.id.insertlayout);
        horizontalscroll = (HorizontalScrollView) v.findViewById(R.id.horizontalscroll);
        tv_a = (TextView) v.findViewById(R.id.tv_a);
        tv_b = (TextView) v.findViewById(R.id.tv_b);
        tv_a.setOnClickListener(this);
        tv_b.setOnClickListener(this);
        iv_a = (ImageView) v.findViewById(R.id.iv_a);
        iv_b = (ImageView) v.findViewById(R.id.iv_b);

        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setListOnUI();

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (flag == 1) {
                            //initially if product list...
                            Intent in = new Intent(getActivity(), ProductDetailActivity.class);
                            in.putExtra("pid", prodList.get(position).getProductId());
                            getActivity().startActivity(in);

                        } else if (flag == 2) {
                            Intent in = new Intent(getActivity(), ProductDetailActivity.class);
                            in.putExtra("pid", prodListIncat.get(position).getProductId());
                            getActivity().startActivity(in);

                        } else if (flag == 0) {
                            //initially if category list
                            posi = position;

                            horizontalscroll.setVisibility(View.VISIBLE);
                            if (firstList.get(posi).getDepth() == 1) {
                                tv_a.setVisibility(View.VISIBLE);
                                iv_a.setVisibility(View.VISIBLE);
                                tv_a.setText(Html.fromHtml(firstList.get(posi).getSubCategoryName()).toString());
                                subcategory_first = new ArrayList<>();
                                subcategory_first.addAll(firstList);
                                firstList.clear();
                                if (subcategory_first.get(posi).getSubCatg().size() == 0) {
                                    if (subcategory_first.get(posi).getProdVal().size() != 0) {
                                        adapter = new ProductAdapter(getActivity(), subcategory_first.get(posi).getProdVal());
                                        //no subcategory but products in maincategory
                                        mRecyclerView.setAdapter(adapter);
                                        prodListIncat = new ArrayList<ProdVal>();
                                        prodListIncat.addAll(subcategory_first.get(posi).getProdVal());
                                        flag = 2;
                                    } else {
                                        DialogUtility.showToast(getActivity(), "No products found ");
                                    }
                                } else {
                                    firstList.clear();
                                    firstList = new ArrayList<>();
                                    firstList.addAll(subcategory_first.get(posi).getSubCatg());
                                    if (subcategory_first.get(posi).getProdVal().size() != 0) {
                                        setSubCateoryOther(subcategory_first.get(posi).getDepth() + 1, subcategory_first.get(posi).getProdVal());
                                        firstList.add(subcatObj);
                                        mAdapter = new DashBoardAdapter(getActivity(), firstList);
                                        mRecyclerView.setAdapter(mAdapter);
                                        flag = 0;
                                    } else {
                                        mAdapter = new DashBoardAdapter(getActivity(), firstList);
                                        mRecyclerView.setAdapter(mAdapter);
                                        flag = 0;
                                    }
                                }
                            } else if (firstList.get(posi).getDepth() == 2) {
                                tv_b.setVisibility(View.VISIBLE);
                                if (firstList.size() > 0) {
                                    tv_b.setText(Html.fromHtml(firstList.get(posi).getSubCategoryName()).toString());
                                    adapter = new ProductAdapter(getActivity(), firstList.get(position).getProdVal());
                                    mRecyclerView.setAdapter(adapter);
                                    prodListIncat = new ArrayList<ProdVal>();
                                    prodListIncat.addAll(firstList.get(position).getProdVal());
                                    flag = 2;
                                    // firstList.clear();
                                }
                            } else {
                                adapter = new ProductAdapter(getActivity(), firstList.get(position).getProdVal());
                                mRecyclerView.setAdapter(adapter);
                                prodListIncat = new ArrayList<ProdVal>();
                                prodListIncat.addAll(firstList.get(position).getProdVal());
                                flag = 2;
                            }
                        }
                    }
                })
        );


        tv_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_b.setVisibility(View.GONE);
                iv_b.setVisibility(View.GONE);
                TempList = new ArrayList<>();
                TempList.clear();
                if (firstList.size() > 0) {

                    try {
                        TempList.addAll(firstList);
                        mAdapter = new DashBoardAdapter(getActivity(), TempList);
                        mRecyclerView.setAdapter(mAdapter);
                        flag = 0;
                    } catch (Exception e) {

                    }
                }
            }
        });

        return v;
    }

    public void setUpdateView() {
        setListOnUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_a:
                tv_b.setVisibility(View.INVISIBLE);
                iv_b.setVisibility(View.INVISIBLE);
                break;

            case R.id.tv_b:
                break;
        }
    }

    public void hideTitleBar() {
        horizontalscroll.setVisibility(View.GONE);
        tv_a.setVisibility(View.INVISIBLE);
        iv_a.setVisibility(View.INVISIBLE);
        tv_b.setVisibility(View.INVISIBLE);
        iv_b.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_dashboard, menu);

        MenuItem itemCart = menu.findItem(R.id.action_cart);
        MenuItem itemarrow = menu.findItem(R.id.arrow);
        itemarrow.setVisible(true);

        //search
        MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            prepareSearchView(searchItem);
        }

        // cart badge count...
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        Utility.setBadgeCount(getActivity(), icon, PreferenceConstant.CART_COUNT + "");
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void refreshview() {
        ActivityCompat.invalidateOptionsMenu(getActivity());
    }

    // In your onOptionItemSelected() method write the following...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.arrow:
                DashBoardFragment frag = ((DashBoardFragment) this.getParentFragment());
                frag.slideUpDown();
                if (PreferenceConstant.PANEL_VISIBLE) {
                    menu.getItem(2).setIcon(getResources().getDrawable(R.drawable.up_arrow_small_two));
                } else {
                    menu.getItem(2).setIcon(getResources().getDrawable(R.drawable.down_arrow));
                }
                // menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.up_arrow));
                return true;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                try {
                    // mAdapter.getFilter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                // mAdapter.getFilter(query);
                hideKeyboard();
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    public void updateList(List<SubCategory> list) {
        firstList.clear();
        firstList.addAll(list);
        firstListTemp = list;
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }


    public void updateProductList(List<ProdVal> list) {
        prodList.clear();
        prodList.addAll(list);
    }

    public void onResume() {
        super.onResume();
        refreshview();
    }

    public void setSubCateoryOther(int depth, List<ProdVal> productList) {
        subcatObj.setDepth(depth);
        subcatObj.setSubCategoryId("0");
        subcatObj.setSubCategoryName("Others");
        subcatObj.setProdVal(productList);
        subcatObj.setSubCatg(blanklist);

    }

    public void setListOnUI() {
        if (firstList.size() == 0) {
            if (prodList.size() != 0) {
                adapter = new ProductAdapter(getActivity(), prodList);//no subcategory but products in maincategory
                mRecyclerView.setAdapter(adapter);
                flag = 1;
            } else {
                //DialogUtility.showToast(getActivity(),"No Products in this category");//Neither subcategory nor products in mainCategory
            }

        } else {
            if (prodList.size() != 0) {
                setSubCateoryOther(firstList.get(0).getDepth(), prodList);//subcategory and products in mainCategory  also
                firstList.add(subcatObj);
                mAdapter = new DashBoardAdapter(getActivity(), firstList);
                mRecyclerView.setAdapter(mAdapter);
                flag = 0;
            } else {
                mAdapter = new DashBoardAdapter(getActivity(), firstList);
                mRecyclerView.setAdapter(mAdapter);//subcategory but no products in mainCategory
                flag = 0;
            }

        }
    }

}