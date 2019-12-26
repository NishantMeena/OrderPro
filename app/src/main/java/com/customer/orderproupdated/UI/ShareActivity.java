package com.customer.orderproupdated.UI;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.adapter.ViewPagerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Sony on 2/2/2017.
 */
public class ShareActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    protected View view;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    private Bitmap mbitmap;
    private TextView tv_product_name, tv_product_id, tv_product_price;
    SharedPrefrence share;
    private Button share_button;
    String prod_id = "", prod_name = "", prod_price = "";
    ArrayList<String> image_list = new ArrayList<>();
    int current_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_product_full_size_image);
        prepareToolbar();
        share = SharedPrefrence.getInstance(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        image_list.addAll(ProductDetailActivity.image_list);

        initPager();
        share_button = (Button) findViewById(R.id.share_button);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_product_id = (TextView) findViewById(R.id.tv_product_id);
        tv_product_price = (TextView) findViewById(R.id.tv_product_price);
        Intent in = getIntent();
        prod_id = in.getStringExtra("prod_id");
        prod_name = in.getStringExtra("prod_name");
        prod_price = in.getStringExtra("prod_price");
        image_list = in.getStringArrayListExtra("imglist");
        tv_product_name.setText(prod_name);
        tv_product_id.setText(prod_id);
        tv_product_price.setText(prod_price);
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                String link=""+"https://36.255.3.15/order_p/image/" +image_list.get(0);
                String productdetail = "" + share.getValue(share.FIRST_NAME) + " " + share.getValue(share.LAST_NAME) + " share a "
                        + "OrderPro Product " + prod_name + " and price of the product " + prod_price + "\n" + link;
                String shareBody = "Check out OrderPro for your smartphone.Download it today from https://play.google.com/store/apps/details?";

                sharingIntent.setType("text/plain");
                if (Build.VERSION.SDK_INT >= 24) {
                    Log.e("Inside if", "Refer");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, productdetail);
                } else if (Build.VERSION.SDK_INT < 24) {
                    Log.e("Inside else if", "Refer");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, productdetail);
                }
                startActivity(Intent.createChooser(sharingIntent, "Share via :"));
            }
        });

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
        actionBar.setTitle("Share Product");
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
        mAdapter = new ViewPagerAdapter(ShareActivity.this, image_list);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        current_pos = intro_images.getCurrentItem();
        setUiPageViewController();
    }

    private void setUiPageViewController() {

        // dotsCount = mAdapter.getCount();
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

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        current_pos = position;
        //  dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void screenShot(View view) {
        mbitmap = getBitmapOFRootView(share_button);
        // imageView.setImageBitmap(mbitmap);
        createImage(mbitmap);
    }

    public Bitmap getBitmapOFRootView(View v) {
/*
        View rootview = v.getRootView();
        rootview.setDrawingCacheEnabled(true);
*/
        LinearLayout layout_main = (LinearLayout) findViewById(R.id.layout_main);
        layout_main.setDrawingCacheEnabled(true);
        Bitmap bitmap1 = layout_main.getDrawingCache();
        return bitmap1;
    }

    public void createImage(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        File file = new File(Environment.getExternalStorageDirectory() + "/capturedscreenandroid.jpg");
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        shareImage(file);
    }

    private void shareImage(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Product name -" + tv_product_name);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "price -" + tv_product_price);
        intent.putExtra(Intent.EXTRA_STREAM, image_list.get(current_pos));
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(ShareActivity.this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }


    // https://play.google.com/store/apps/details?id=com.offerak&hl=en
}
