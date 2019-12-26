package com.customer.orderproupdated.Fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.AddMerchantActivity;
import com.customer.orderproupdated.UI.CartActivity;
import com.customer.orderproupdated.UI.HomeActivity;
import com.customer.orderproupdated.UI.NotificationActivity;
import com.customer.orderproupdated.UI.QuickOrderActivity;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.AllMerchantDetails;
import com.customer.orderproupdated.bean.Banner;
import com.customer.orderproupdated.bean.CategoryBean;
import com.customer.orderproupdated.bean.GetAllMerchantList;
import com.customer.orderproupdated.bean.MainCategoryBean;
import com.customer.orderproupdated.bean.MerchantDetail;
import com.customer.orderproupdated.bean.ProdVal;
import com.customer.orderproupdated.chat.activity.ChatWindow;
import com.customer.orderproupdated.database.DatabaseHandler;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sony on 12/22/2016.
 */
public class DashBoardFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private View hiddenPanel;
    private SliderLayout mDemoSlider;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    TextSliderView textSliderView;
    LinearLayout open_quik_orders, open_notification, open_chat;
    RelativeLayout empty_layout;
    RelativeLayout no_prod_layout;
    RelativeLayout no_merchant_layout;
    Button btn_add_merchant;
    RelativeLayout ll_main;
    TextView chatCount;
    HashMap<String, ProdVal> productsMap;

    public DashBoardChildFragment dashBoardChildFragment = new DashBoardChildFragment();
    ArrayList<CategoryBean> itemCategories = new ArrayList<CategoryBean>();
    List<ProdVal> allProductsList = new ArrayList<ProdVal>();
    ArrayList<Banner> urlList = new ArrayList<>();
    ArrayList<AllMerchantDetails> merchantList;
    ArrayList<AllMerchantDetails> merchantList_added_requested;

    DashBoardChildFragment dashBoardChildFragment1Prev;
    int positionPrev;
    SharedPrefrence share;
    ApiInterface apiService;
    View view;
    Menu menu;
    boolean is_merchant_switched;
    private Handler mainHandler = new Handler();
    int pageSelected;
    HomeActivity homeActivity;
    DatabaseHandler db;
    MerchantDetail merchantDetail;
    String ChatCount = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setRetainInstance(true);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        share = SharedPrefrence.getInstance(getActivity());
        apiService = ApiClient.getClient().create(ApiInterface.class);
        homeActivity = new HomeActivity();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("OrderPro");
        itemCategories = share.getMainCategoryList(SharedPrefrence.CATEGORY_LIST);
        hiddenPanel = view.findViewById(R.id.slider);
        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);
        open_quik_orders = (LinearLayout) view.findViewById(R.id.open_quik_orders);
        open_notification = (LinearLayout) view.findViewById(R.id.open_notification);
        open_chat = (LinearLayout) view.findViewById(R.id.open_chat);
        empty_layout = (RelativeLayout) view.findViewById(R.id.empty_layout);
        no_prod_layout = (RelativeLayout) view.findViewById(R.id.no_prod_layout);
        btn_add_merchant = (Button) view.findViewById(R.id.btn_add_merchant);
        no_merchant_layout = (RelativeLayout) view.findViewById(R.id.no_merchant_layout);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        ll_main = (RelativeLayout) view.findViewById(R.id.main);
        chatCount = (TextView) view.findViewById(R.id.chatCount);

        open_quik_orders.setOnClickListener(this);
        open_notification.setOnClickListener(this);
        open_chat.setOnClickListener(this);

        //---------------------------------
       /* //show chat count
        db=new DatabaseHandler(getActivity());
        ChatCount=db.getUnreadCount(share.getValue(SharedPrefrence.JID),Utility.lowercaseAllLetters(merchantDetail.getUsername())+PreferenceConstant.SERVERATTHERSTE);
        if(Integer.parseInt(ChatCount)>0)
        {
            chatCount.setVisibility(View.VISIBLE);
            chatCount.setText(ChatCount);
        }
        else
        {
            chatCount.setVisibility(View.GONE);
        }*/

        //-----------------------------

        is_merchant_switched = share.getBooleanValue(SharedPrefrence.IS_MERCHANT_SWITCHED);

        /*added merchant check*/

        if (share.getMerchantList_added(SharedPrefrence.MERCHANT_LIST_ADDED).size() == 0) {
            //disable viewpager
            viewPager.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View arg0, MotionEvent arg1) {
                    return true;
                }
            });
            empty_layout.setVisibility(View.VISIBLE);
            btn_add_merchant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getMerchantList();
                }
            });
        } else if (is_merchant_switched) {
            merchantDetail = share.getMerchantDetail(SharedPrefrence.MERCHANT_DETAIL);
            getProductList();
            // share.setBooleanValue(SharedPrefrence.IS_MERCHANT_SWITCHED,false);
        } else {
            no_merchant_layout.setVisibility(View.VISIBLE);
        }

        set_banners();
        return view;
    }
    //onCreateView Close

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        pageSelected = position;
        System.out.println("????????????????????????????????????????" + pageSelected);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void set_banners() {
       /* imageLoader = ImageLoader.getInstance();
          options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.mobile_sale)
                .showImageOnLoading(R.drawable.mobile_sale)
                .showImageOnFail(R.drawable.mobile_sale)
                .cacheOnDisc(true).resetViewBeforeLoading(true).build();*/
        BaseSliderView baseSliderView = new DefaultSliderView(getActivity());

        urlList = share.getBannerList(SharedPrefrence.LIST_BANNER_DEFAULT);
        for (Banner banner : urlList) {
            textSliderView = new TextSliderView(getActivity());
            textSliderView
                    .description(banner.getBannerName())
                    .image(banner.getUrl())
                    .error(R.drawable.banner_one)
                    .empty(R.drawable.banner_one)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", banner.getBannerName());

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.startAutoCycle();
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

    }

    public void set_tabs() {
        viewPagerTab.setVisibility(View.VISIBLE);

        final FragmentPagerItems pages = new FragmentPagerItems(getActivity());

        for (int i = 0; i < itemCategories.size(); i++) {
            CategoryBean categories = itemCategories.get(i);
            if (categories.getCategoryName() != null && !categories.getCategoryName().isEmpty()) {
                pages.add(FragmentPagerItem.of(Utility.capitalize(Html.fromHtml(categories.getCategoryName()).toString()), dashBoardChildFragment.getClass()));
            } else {
                itemCategories.remove(i);
                i--;
            }
            dashBoardChildFragment.updateList(itemCategories.get(i).getSubCatg());
        }

        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);

        viewPagerTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                System.out.println("??????????????????????????onPageSelected" + position);
                DashBoardChildFragment dashBoardChildFragment1 = (DashBoardChildFragment) adapter.getPage(position);
                if (itemCategories.get(position).getSubCatg() == null) {
                    itemCategories = share.getMainCategoryList(SharedPrefrence.CATEGORY_LIST);
                    dashBoardChildFragment1.updateList(itemCategories.get(position).getSubCatg());
                    dashBoardChildFragment1.updateProductList(itemCategories.get(position).getProdVal());
                    dashBoardChildFragment1.hideTitleBar();
                    dashBoardChildFragment1Prev = dashBoardChildFragment1;
                    positionPrev = position;
                    if (dashBoardChildFragment1Prev != null) {
                        dashBoardChildFragment1Prev.setUpdateView();
                        dashBoardChildFragment1Prev.hideTitleBar();
                    }


                } else {
                    dashBoardChildFragment1.updateList(itemCategories.get(position).getSubCatg());
                    dashBoardChildFragment1.updateProductList(itemCategories.get(position).getProdVal());
                    dashBoardChildFragment1.hideTitleBar();
                    dashBoardChildFragment1Prev = dashBoardChildFragment1;
                    positionPrev = position;
                    if (dashBoardChildFragment1Prev != null) {
                        dashBoardChildFragment1Prev.setUpdateView();
                        dashBoardChildFragment1Prev.hideTitleBar();
                    }

                }
            }
        });

        if (pageSelected == 0) {
            DashBoardChildFragment dashBoardChildFragment1 = (DashBoardChildFragment) adapter.getPage(pageSelected);
            dashBoardChildFragment1.updateList(itemCategories.get(pageSelected).getSubCatg());
            dashBoardChildFragment1.updateProductList(itemCategories.get(pageSelected).getProdVal());
            //dashBoardChildFragment1.setCategoryId(itemCategories.get(pageSelected).getCategoryId());
            dashBoardChildFragment1.hideTitleBar();
            dashBoardChildFragment1Prev = dashBoardChildFragment1;
            positionPrev = pageSelected;
            if (dashBoardChildFragment1Prev != null) {
                dashBoardChildFragment1Prev.setUpdateView();
                dashBoardChildFragment1Prev.hideTitleBar();
            }
        }
/*
        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("??????????????????????????onPageSelected"+position);
                DashBoardChildFragment dashBoardChildFragment1 = (DashBoardChildFragment) adapter.getPage(position);
                dashBoardChildFragment1.updateList(itemCategories.get(position).getSubCatg());
                dashBoardChildFragment1.updateProductList(itemCategories.get(position).getProdVal());
                dashBoardChildFragment1.hideTitleBar();
                dashBoardChildFragment1Prev = dashBoardChildFragment1;
                positionPrev = position;
                if (dashBoardChildFragment1Prev != null) {
                    dashBoardChildFragment1Prev.setUpdateView();
                    dashBoardChildFragment1Prev.hideTitleBar();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.open_quik_orders:
                startActivity(new Intent(getActivity(), QuickOrderActivity.class));
                break;
            case R.id.open_notification:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;
            case R.id.open_chat:
                if (is_merchant_switched) {

                    String u_name = merchantDetail.getUsername().toString();
                    Intent in = new Intent(getActivity(), ChatWindow.class);
                    in.putExtra("username", u_name);
                    startActivity(in);
                } else {
                    Intent in = new Intent(getActivity(), ChatWindow.class);
                    in.putExtra("username", "");
                    startActivity(in);
                }
                break;
        }

    }

    public void slideUpDown() {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.bottom_up);
            PreferenceConstant.PANEL_VISIBLE = true;
            // menu.getItem(2).setIcon(getResources().getDrawable(R.drawable.down_arrow_small));
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
        } else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.bottom_down);

            PreferenceConstant.PANEL_VISIBLE = false;
            // menu.getItem(2).setIcon(getResources().getDrawable(R.drawable.down_arrow_small));
            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_dashboard, menu);
        //ActivityCompat.invalidateOptionsMenu(getActivity());
        //search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
        if (searchItem != null) {
            prepareSearchView(searchItem);
        }
        //cart badge count
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        itemCart.setVisible(false);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        Utility.setBadgeCount(getActivity(), icon, PreferenceConstant.CART_COUNT + "");
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.arrow:
                slideUpDown();
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
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem searchViewMenuItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchViewMenuItem);
        int searchImgId = android.support.v7.appcompat.R.id.search_button; // I used the explicit layout ID of searchview's ImageView
        ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_action_search);
        super.onPrepareOptionsMenu(menu);
    }

    /*---------------------------search---------------*/

    private void prepareSearchView(@NonNull final MenuItem searchItem) {
        final SearchView searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                try {
                    //  dashBoardAdapter.getFilter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                // dashBoardAdapter.getFilter(query);
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
    }

    /*---------------------------getProduct()---------------*/

    public void getProductList() {
        String merchnatID = share.getValue(SharedPrefrence.MERCHANT_ID);
        // String merchnatID = "85";
        if (Utility.checkInternetConn(getActivity())) {
            DialogUtility.showProgressDialog(getActivity(), true, getString(R.string.please_wait));
            Call<MainCategoryBean> call = apiService.getCategeoryProductList(merchnatID, share.getValue(SharedPrefrence.CUSTOMERID));
            call.enqueue(new Callback<MainCategoryBean>() {
                @Override
                public void onResponse(Call<MainCategoryBean> call, Response<MainCategoryBean> response) {
                    Log.e("CategeoryProd response", "" + response);
                    if (response.body() != null) {
                        if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {
                            // PreferenceConstant.PRODUCT_VERSION=response.body().getVersion();
                            if (response.body().getCategory() != null) {
                                itemCategories = new ArrayList<>();
                                itemCategories.addAll(response.body().getCategory());
                                Log.e("getCategory list size", "" + itemCategories.size());
                            }
                            share.setMainCategoryList(SharedPrefrence.CATEGORY_LIST, itemCategories);
                            Log.e("getCategory list size", "" + itemCategories.size() + " saved in prefs");
                            set_tabs();
                            getAllProductList();
                        } else {
                            no_prod_layout.setVisibility(View.VISIBLE);
                        }
                    }
                    DialogUtility.pauseProgressDialog();
                }


                @Override
                public void onFailure(Call<MainCategoryBean> call, Throwable t) {
                    // Log error here since request failed...
                    Log.e("getCategory Failure", "" + call.request().toString());
                    Log.e("getCategory Failure", "" + call.request().body());
                    Log.e("getCategory Failure", "" + call.request().method());
                    Log.e("getCategory Failure", t.toString());
                    DialogUtility.pauseProgressDialog();
                }
            });
        } else {
            DialogUtility.showMaterialDialog(getActivity(), getString(R.string.alert), getString(R.string.checknet));
        }
    }

    public void getAllProductList() {
        for (int i = 0; i < itemCategories.size(); i++) {
            allProductsList.addAll(itemCategories.get(i).getProdVal());
            for (int j = 0; j < itemCategories.get(i).getSubCatg().size(); j++) {
                allProductsList.addAll(itemCategories.get(i).getSubCatg().get(j).getProdVal());
                for (int k = 0; k < itemCategories.get(i).getSubCatg().get(j).getSubCatg().size(); k++) {
                    allProductsList.addAll(itemCategories.get(i).getSubCatg().get(j).getSubCatg().get(k).getProdVal());
                }
            }
        }

        System.out.println("<<---allProductsList size" + allProductsList.size());
        productsMap = new HashMap<String, ProdVal>();
        for (int j = 0; j < allProductsList.size(); j++) {
            productsMap.put(allProductsList.get(j).getProductId(), allProductsList.get(j));
        }
        share.setProductList(SharedPrefrence.PRODUCT_LIST, productsMap);
        share.setProductList(SharedPrefrence.QUICK_ORDER_LIST, productsMap);
    }

    /*--------------all merchant API---------------*/

    public void getMerchantList() {
        if (Utility.checkInternetConn(getActivity())) {
            String customerID = share.getValue(SharedPrefrence.CUSTOMERID);
            DialogUtility.showProgressDialog(getActivity(), false, "Loading");
            try {
                Call<GetAllMerchantList> call = apiService.getAllMerchantList(customerID);
                call.enqueue(new Callback<GetAllMerchantList>() {
                    @Override
                    public void onResponse(Call<GetAllMerchantList> call, Response<GetAllMerchantList> response) {
                        Log.e("GetCustomers response", "" + response);
                        if (response.body() != null) {
                            if (response.body().getMerchant() != null) {
                                merchantList = new ArrayList<>();
                                merchantList.addAll(response.body().getMerchant());
                                Log.e("GetMerchant list size", "" + merchantList.size());
                                Intent in = new Intent(getActivity(), AddMerchantActivity.class);
                                startActivity(in);
                            }

                            if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {
                                // share.setAllMerchantList(SharedPrefrence.All_MERCHANT_LIST, merchantList);
                                Log.e("GetMerchant list size", "" + merchantList.size());
                                merchantList_added_requested = new ArrayList<>();
                                for (int i = 0; i < merchantList.size(); i++) {
                                    if (merchantList.get(i).getRequestStatus().equals("1") || merchantList.get(i).getRequestStatus().equals("0")) {
                                        merchantList_added_requested.add(merchantList.get(i));
                                    }
                                }
                                share.setAllMerchantList(SharedPrefrence.All_MERCHANT_LIST, merchantList_added_requested);
                            }
                        }
                        DialogUtility.pauseProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<GetAllMerchantList> call, Throwable t) {
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
    }

    /*----------------Refresh Action Bar items---------------*/
    public void refreshview() {
        ActivityCompat.invalidateOptionsMenu(getActivity());
    }
}

