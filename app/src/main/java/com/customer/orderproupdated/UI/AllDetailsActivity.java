package com.customer.orderproupdated.UI;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.customer.orderproupdated.Fragments.Description_Fragment;
import com.customer.orderproupdated.Fragments.Specification_Fragment;
import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.MerchantDetail;
import com.customer.orderproupdated.bean.ProdVal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sony on 1/25/2017.
 */
public class AllDetailsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView iv_prod_image;
    TextView tv_p_price,tv_prod_name;
    ProdVal pb_obj;
    String prod_name;
    SharedPrefrence share;
    ArrayList<String > image_list=new ArrayList<>();
   // MerchantDetail merchantDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_all_details);
        prepareToolbar();
        initview();
        share=SharedPrefrence.getInstance(this);
        pb_obj = (ProdVal) getIntent().getExtras().getSerializable("pb");
        prod_name = pb_obj.getProductName();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



       /* merchantDetail=share.getMerchantDetail(SharedPrefrence.MERCHANT_DETAIL);
        tv_merchent_name.setText(merchantDetail.getUsername().toString());*/
        tv_prod_name.setText(Utility.capitalize(prod_name));
         tv_p_price.setText(pb_obj.getPrice());

        image_list= (ArrayList<String>) pb_obj.getImage();

        String url = image_list.get(0);
        url= url.replace(" ","%20");
        Picasso.with(this)
                .load("https://36.255.3.15/order_p/image/"+url)
                .fit().centerInside()
                .placeholder(R.drawable.product_default)
                .error(R.drawable.product_default)
                .into(iv_prod_image);


    }
    private void initview()
    {
        iv_prod_image=(ImageView)findViewById(R.id.product_image);
        tv_p_price=(TextView)findViewById(R.id.p_price);
        tv_prod_name=(TextView)findViewById(R.id.product_name);

    }
    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            //DialogUtility.showLOG( "GetSupportActionBar returned null.");
        }
        actionBar.setTitle("All Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Description_Fragment(), getString(R.string.Description));
        adapter.addFragment(new Specification_Fragment(), getString(R.string.Specification));


        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            Bundle args = new Bundle();
            args.putSerializable("prodval", pb_obj);
            fragment.setArguments(args);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
