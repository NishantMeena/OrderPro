package com.customer.orderproupdated.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.adapter.ViewPagerAdapter;
import com.customer.orderproupdated.bean.ProdVal;
import com.customer.orderproupdated.bean.Product_attribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    protected View view;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    private FloatingActionButton FABshare, FABfavourite, FABdownload;
    ProdVal pb_obj;
    Menu menu;
    TextView tv_selected_color, tv_selected_material, tv_selected_size, tv_product_name, tv_product_id, tv_product_price, tv_more_specifications, tv_more_details;
    RelativeLayout ll_alldetails;
    Button btn_add_to_cart;
    LinearLayout ll_plus, ll_minus, ll_one, ll_two, ll_three;
    EditText et_quantity;
    int minteger = 1;
    int total_amt = 1;
    String attribute_one_selected = "", attribute_two_selected = "", attribute_three_selected = "";
    String prod_name = "", attribute_id;
    ArrayList<ProdVal> cart_list = new ArrayList<ProdVal>();
    ArrayList<ProdVal> favouritselist = new ArrayList<ProdVal>();
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;


    List<Product_attribute> attributelist = new ArrayList<Product_attribute>();

    SharedPrefrence prefrence;
    LinearLayout layout_details, layout_specifications;

    TextView tv_No_Description, tv_No_Specifications;
    TextView tv_detail_one;

    WebView webView;

    String pid = "";

    HashMap<String, ProdVal> allproductsMap = new HashMap<String, ProdVal>();
    HashMap<String, ProdVal> favouriteMap = new HashMap<String, ProdVal>();


   /* private int[] mImageResources = {
            R.drawable.product_default,
            R.drawable.product_default,
            R.drawable.product_default,
            R.drawable.product_default,
            R.drawable.product_default
    };*/

    static ArrayList<String> image_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_product);
        prefrence = SharedPrefrence.getInstance(this);
        allproductsMap = prefrence.getProduct(SharedPrefrence.PRODUCT_LIST);

        prepareToolbar();


        if (getIntent().hasExtra("pid")) {
            pid = getIntent().getStringExtra("pid");

            if (allproductsMap.containsKey(pid)) {
                pb_obj = allproductsMap.get(pid);
            } else {
                DialogUtility.showToast(this, "Product not exist");
                finish();
            }

            prod_name = pb_obj.getProductName();
        }

        favouriteMap = prefrence.getFavouriteMap(SharedPrefrence.FAVOURITE_LIST);
        cart_list = prefrence.getCartList(SharedPrefrence.CART_LIST);

        image_list = (ArrayList<String>) pb_obj.getImage();

        hideTitleWhenExpanded();
        initPager();
        initview();
        FetchAttribute();

        DialogUtility.showLOG("<<-----Description" + pb_obj.getDescription());
        if (!pb_obj.getDescription().equals("")) {
            layout_details.setVisibility(View.VISIBLE);
            tv_No_Description.setVisibility(View.GONE);
            tv_more_details.setVisibility(View.VISIBLE);
            String htmlText = "<html><body style=\"text-align:justify;color:#000;line-height: 1.5\">%s</body></html>";
            Spanned description;
            WebView webView = (WebView) findViewById(R.id.webView);

            //disbale scrolling
            webView.setScrollContainer(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return (event.getAction() == MotionEvent.ACTION_MOVE);
                }
            });
            //display text webView
            if (Build.VERSION.SDK_INT >= 24) {
                description = Html.fromHtml(String.format(htmlText, pb_obj.getDescription()), 1);
                webView.loadDataWithBaseURL(null, description.toString(), "text/html", "utf-8", null);
            } else {
                description = Html.fromHtml(String.format(htmlText, pb_obj.getDescription()));
                webView.loadDataWithBaseURL(null, description.toString(), "text/html", "utf-8", null);
            }
        }

        if (!pb_obj.getSpecification().equals("")) {
            layout_specifications.setVisibility(View.VISIBLE);
            tv_No_Specifications.setVisibility(View.GONE);
            tv_more_specifications.setVisibility(View.VISIBLE);
            String htmlText = "<html><body style=\"text-align:justify;color:#000;line-height: 1.5\">%s</body></html>";
            Spanned specification;
            WebView webView = (WebView) findViewById(R.id.webView_specification);
            //disbale scrolling
            webView.setScrollContainer(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return (event.getAction() == MotionEvent.ACTION_MOVE);
                }
            });

            if (Build.VERSION.SDK_INT >= 24) {
                specification = Html.fromHtml(String.format(htmlText, pb_obj.getDescription()), 1);
                webView.loadDataWithBaseURL(null, specification.toString(), "text/html", "utf-8", null);
            } else {
                specification = Html.fromHtml(String.format(htmlText, pb_obj.getDescription()));
                webView.loadDataWithBaseURL(null, specification.toString(), "text/html", "utf-8", null);
            }
        }

        if (!pb_obj.getSpecification().equals("") || !pb_obj.getDescription().equals("")) {
            ll_alldetails.setVisibility(View.VISIBLE);
        }
        //set selected color
        tv_selected_color.setText("Select");
        tv_product_name.setText(pb_obj.getProductName());
        tv_product_id.setText(pb_obj.getProductSku());
        tv_product_price.setText(pb_obj.getPrice());

        // clicklisteners
        // tv_more.setOnClickListener(this);
        // tv_view_details.setOnClickListener(this);
        btn_add_to_cart.setOnClickListener(this);
        ll_plus.setOnClickListener(this);
        ll_minus.setOnClickListener(this);
        ll_one.setOnClickListener(this);
        ll_two.setOnClickListener(this);
        ll_three.setOnClickListener(this);
        ll_alldetails.setOnClickListener(this);
        FABshare.setOnClickListener(this);
        FABdownload.setOnClickListener(this);
        FABfavourite.setOnClickListener(this);
        tv_more_specifications.setOnClickListener(this);
        tv_more_details.setOnClickListener(this);

        if (favouriteMap.containsKey(pb_obj.getProductId())) {
            pb_obj.setFavourite(true);
            FABfavourite.setImageResource(R.drawable.heart_blue_icon);

        } else {
            pb_obj.setFavourite(false);
            FABfavourite.setImageResource(R.drawable.favorite_icon);

        }
    }

    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            //DialogUtility.showLOG( "GetSupportActionBar returned null.");
        }
        actionBar.setTitle(prod_name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initPager() {
        intro_images = (ViewPager) findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        mAdapter = new ViewPagerAdapter(ProductDetailActivity.this, image_list);
        intro_images.setAdapter(mAdapter);
        //intro_images.setCurrentItem(0);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == image_list.size() - 1) {
                    currentPage = 0;
                }
                intro_images.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();


    }

    private void initview() {
        tv_product_price = (TextView) findViewById(R.id.tv_product_price);
        tv_product_id = (TextView) findViewById(R.id.tv_product_id);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        // tv_more = (TextView) findViewById(R.id.tv_more);
        //tv_view_details = (TextView) findViewById(R.id.tv_view_details);
        ll_alldetails = (RelativeLayout) findViewById(R.id.ll_alldetails);
        btn_add_to_cart = (Button) findViewById(R.id.btn_add_to_cart);
        ll_plus = (LinearLayout) findViewById(R.id.ll_plus);
        ll_minus = (LinearLayout) findViewById(R.id.ll_minus);
        et_quantity = (EditText) findViewById(R.id.et_quantity);
        ll_one = (LinearLayout) findViewById(R.id.ll_color);
        ll_two = (LinearLayout) findViewById(R.id.ll_sizes);
        ll_three = (LinearLayout) findViewById(R.id.ll_material);
        tv_selected_color = (TextView) findViewById(R.id.tv_selected_color);
        tv_selected_material = (TextView) findViewById(R.id.tv_selected_material);
        tv_selected_size = (TextView) findViewById(R.id.tv_selected_size);
        FABshare = (FloatingActionButton) findViewById(R.id.FABshare);
        FABfavourite = (FloatingActionButton) findViewById(R.id.FABfavourite);
        FABdownload = (FloatingActionButton) findViewById(R.id.FABdownload);
        layout_details = (LinearLayout) findViewById(R.id.layout_details);
        layout_specifications = (LinearLayout) findViewById(R.id.layout_specifications);

        tv_No_Description = (TextView) findViewById(R.id.tv_No_Description);
        tv_No_Specifications = (TextView) findViewById(R.id.tv_No_Specifications);

        tv_more_details = (TextView) findViewById(R.id.tv_more_details);
        tv_more_specifications = (TextView) findViewById(R.id.tv_more_specifications);

    }


    private void setUiPageViewController() {

        //dotsCount = mAdapter.getCount();
        dots = new ImageView[image_list.size()];

        for (int i = 0; i < image_list.size(); i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);


        }

        if (dots.length > 0) {
            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void hideTitleWhenExpanded() {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(prod_name);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_alldetails:
                Intent intent_allDetails = new Intent(this, com.customer.orderproupdated.UI.AllDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("pb", pb_obj);
                intent_allDetails.putExtras(bundle);
                startActivity(intent_allDetails);
                break;
            case R.id.tv_more_details:
                Intent intent_more = new Intent(this, com.customer.orderproupdated.UI.AllDetailsActivity.class);
                Bundle bundle_more = new Bundle();
                bundle_more.putSerializable("pb", pb_obj);
                intent_more.putExtras(bundle_more);
                startActivity(intent_more);
                break;
            case R.id.tv_more_specifications:
                Intent intent_viewDetails = new Intent(this, com.customer.orderproupdated.UI.AllDetailsActivity.class);
                Bundle bundle_viewDetails = new Bundle();
                bundle_viewDetails.putSerializable("pb", pb_obj);
                intent_viewDetails.putExtras(bundle_viewDetails);
                startActivity(intent_viewDetails);
                break;
            case R.id.btn_add_to_cart:
                onAddingToCart();
                break;
            case R.id.ll_color:
                Intent intent_one = new Intent(this, com.customer.orderproupdated.UI.AttributeSelectionActivity.class);
                Bundle bundle_one = new Bundle();
                bundle_one.putSerializable("pb", pb_obj);
                intent_one.putExtras(bundle_one);
                intent_one.putExtra("attribute_val", pb_obj.getProductAttribute().get(0).getAttributeValue());
                intent_one.putExtra("attribute_name", pb_obj.getProductAttribute().get(0).getAttributeName());
                startActivityForResult(intent_one, 1);
                break;

            case R.id.ll_sizes:
                Intent intent_two = new Intent(this, com.customer.orderproupdated.UI.AttributeSelectionActivity.class);
                Bundle bundle_two = new Bundle();
                bundle_two.putSerializable("pb", pb_obj);
                intent_two.putExtras(bundle_two);
                intent_two.putExtra("attribute_val", pb_obj.getProductAttribute().get(1).getAttributeValue());
                intent_two.putExtra("attribute_name", pb_obj.getProductAttribute().get(1).getAttributeName());
                startActivityForResult(intent_two, 2);
                break;

            case R.id.ll_material:
                Intent intent_three = new Intent(this, com.customer.orderproupdated.UI.AttributeSelectionActivity.class);
                Bundle bundle_three = new Bundle();
                bundle_three.putSerializable("pb", pb_obj);
                intent_three.putExtras(bundle_three);
                intent_three.putExtra("attribute_val", pb_obj.getProductAttribute().get(3).getAttributeValue());
                intent_three.putExtra("attribute_name", pb_obj.getProductAttribute().get(3).getAttributeName());
                startActivityForResult(intent_three, 3);
                break;

            case R.id.ll_minus:
                decrement();
                break;

            case R.id.ll_plus:
                increment();
                break;

            case R.id.FABshare:
                Intent in = new Intent(this, com.customer.orderproupdated.UI.ShareActivity.class);
                in.putExtra("prod_id", pb_obj.getProductId());
                in.putExtra("prod_name", pb_obj.getProductName());
                in.putExtra("prod_price", pb_obj.getPrice());
                in.putExtra("imglist", (Serializable) pb_obj.getImage());
                startActivity(in);
                break;

            case R.id.FABfavourite:
                if (pb_obj.isFavourite()) {
                    pb_obj.setFavourite(false);
                    favouriteMap.remove(pb_obj.getProductId());
                    FABfavourite.setImageResource(R.drawable.favorite_icon);
                } else {
                    pb_obj.setFavourite(true);
                    favouriteMap.put(pb_obj.getProductId(), pb_obj);
                    FABfavourite.setImageResource(R.drawable.heart_blue_icon);
                    DialogUtility.showToast(this, "Added to favourite");
                }
                prefrence.setFavouriteMap(SharedPrefrence.FAVOURITE_LIST, favouriteMap);
                break;

            case R.id.FABdownload:

                break;

            default:
                break;
        }

    }

    public void decrement() {
        if (minteger > 1) {
            minteger = minteger - 1;
        }
        et_quantity.setText("" + minteger);
    }

    public void increment() {
        minteger = minteger + 1;
        et_quantity.setText("" + minteger);
    }

    public void FetchAttribute() {
        TextView color_label = (TextView) findViewById(R.id.color_label);
        TextView size_label = (TextView) findViewById(R.id.size_label);
        TextView material_label = (TextView) findViewById(R.id.material_label);
        attributelist = pb_obj.getProductAttribute();
        for (int i = 0; i < attributelist.size(); i++) {
            if (i == 0) {
                color_label.setEnabled(true);
                ll_one.setVisibility(View.VISIBLE);
                color_label.setText(attributelist.get(0).getAttributeName());

            } else if (i == 1) {
                color_label.setEnabled(true);
                size_label.setEnabled(true);
                ll_one.setVisibility(View.VISIBLE);
                ll_two.setVisibility(View.VISIBLE);
                color_label.setText(attributelist.get(0).getAttributeName());
                size_label.setText(attributelist.get(1).getAttributeName());
            } else if (i == 2) {
                color_label.setEnabled(true);
                size_label.setEnabled(true);
                material_label.setEnabled(true);
                ll_one.setVisibility(View.VISIBLE);
                ll_two.setVisibility(View.VISIBLE);
                ll_three.setVisibility(View.VISIBLE);
                color_label.setText(attributelist.get(0).getAttributeName());
                size_label.setText(attributelist.get(1).getAttributeName());
                material_label.setText(attributelist.get(2).getAttributeName());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_only_cart, menu);
        // scart badge count...
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        Utility.setBadgeCount(this, icon, PreferenceConstant.CART_COUNT + "");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent in = new Intent(ProductDetailActivity.this, com.customer.orderproupdated.UI.CartActivity.class);
                startActivity(in);
                finish();
                return false;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshview() {
        ActivityCompat.invalidateOptionsMenu(ProductDetailActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                attribute_one_selected = data.getStringExtra("value");
                tv_selected_color.setText(attribute_one_selected);
                pb_obj.getProductAttribute().get(0).setAttributeValue(attribute_one_selected);
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                attribute_two_selected = data.getStringExtra("value");
                tv_selected_size.setText(attribute_two_selected);
                pb_obj.getProductAttribute().get(1).setAttributeValue(attribute_two_selected);

            }
        }
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                attribute_three_selected = data.getStringExtra("value");
                tv_selected_material.setText(attribute_three_selected);
                pb_obj.getProductAttribute().get(2).setAttributeValue(attribute_three_selected);
            }
        }
    }

    public void onAddingToCart() {
        PreferenceConstant.CART_COUNT++;
        pb_obj.setCount(pb_obj.getCount() + 1);
        int quantity = Integer.parseInt(et_quantity.getText().toString());
        pb_obj.setProduct_quantity_selected(quantity);
        cart_list.add(pb_obj);
        prefrence.setCartList(SharedPrefrence.CART_LIST, cart_list);
        DialogUtility.showToast(this, "Item added to cart");
        refreshview();
    }
}
