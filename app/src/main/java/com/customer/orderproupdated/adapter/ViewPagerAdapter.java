package com.customer.orderproupdated.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.customer.orderproupdated.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    //private int[] mResources;
    ArrayList<String>imgList=new ArrayList<>();

    public ViewPagerAdapter(Context mContext, ArrayList<String>imgList) {
        this.mContext = mContext;
        //this.mResources = mResources;
        this.imgList=imgList;
    }
    @Override
    public int getCount() {
        return imgList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);

        String url = imgList.get(position);
        url= url.replace(" ","%20");

        Picasso.with(mContext)
                .load("https://36.255.3.15/order_p/image/"+url)
                .placeholder(R.drawable.product_default)
                .error(R.drawable.product_default)
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }


}