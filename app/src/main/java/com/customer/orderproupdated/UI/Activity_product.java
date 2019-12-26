package com.customer.orderproupdated.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

/**
 * Created by Sony on 1/19/2017.
 */
public class Activity_product extends AppCompatActivity {

    private SliderLayout mDemoSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_layout);
        //Prepare toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            DialogUtility.showLOG("GetSupportActionBar returned null.");
        }
        actionBar.setTitle("Product Detail");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //initialize components
        initview();
        //set images in Pager
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Smartphones offers", R.drawable.product_default);
        file_maps.put("clothing offers", R.drawable.product_default);
        file_maps.put("Electronics Discount", R.drawable.product_default);
        file_maps.put("Kitchen Utensils", R.drawable.product_default);

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
//                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
//                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        //mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Left_Bottom);
//      mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(10000);
//      mDemoSlider.addOnPageChangeListener(this);

    }
    public void initview()
    {
        mDemoSlider = (SliderLayout) findViewById(R.id.image_slider);
   /* fab1=(FloatingActionButton)findViewById(R.id.floatingButton1);
    fab2=(FloatingActionButton)findViewById(R.id.floatingButton2);
    fab3=(FloatingActionButton)findViewById(R.id.floatingButton3);*/
    }
}
