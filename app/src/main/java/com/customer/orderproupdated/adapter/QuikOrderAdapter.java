package com.customer.orderproupdated.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.Attribute_Activity;
import com.customer.orderproupdated.UI.ProductDetailActivity;
import com.customer.orderproupdated.UI.QuickOrderActivity;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.ProdVal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class QuikOrderAdapter extends RecyclerView.Adapter<QuikOrderAdapter.ViewHolder> {

    ArrayList<ProdVal> quick_order_list = new ArrayList<>();
    ArrayList<ProdVal> newlist = new ArrayList<>();
    ArrayList<String > image_list=new ArrayList<>();

    Context context;
    QuickOrderActivity quickOrderActivity;
    ProdVal obj;
    HashMap<String,ProdVal> productMap;
    HashMap<String,ProdVal> tempProductMap=new HashMap<String,ProdVal>();
    private String[] mKeys;


    public QuikOrderAdapter(Context context, HashMap<String,ProdVal> productMap, QuickOrderActivity quickOrderActivity) {
        this.productMap=productMap;
        mKeys = productMap.keySet().toArray(new String[productMap.size()]);
        this.tempProductMap.putAll(productMap);
        this.context = context;
        this.quickOrderActivity = quickOrderActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView product_id;
        public TextView product_name;
        public TextView product_price;
        public TextView tvCount;
        public ImageView product_image;
        public ImageView btn_add_to_cart;
        public LinearLayout llCount,ll_main;

        public ViewHolder(View v) {
            super(v);
            product_name = (TextView) v.findViewById(R.id.product_name);
            product_image = (ImageView) v.findViewById(R.id.product_image);
            product_id = (TextView) v.findViewById(R.id.product_id);
            tvCount = (TextView) v.findViewById(R.id.tvCount);
            product_price = (TextView) v.findViewById(R.id.product_price);
            btn_add_to_cart = (ImageView) v.findViewById(R.id.btn_add_to_cart);
            llCount = (LinearLayout) v.findViewById(R.id.llCount);
            ll_main = (LinearLayout) v.findViewById(R.id.layout_main);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_quickorder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.product_name.setText(Utility.capitalize(productMap.get(mKeys[position]).getProductName()));
        //holder.product_image.setBackground(quick_order_list.get(position).getImage());
        holder.product_id.setText(productMap.get(mKeys[position]).getProductId());
        holder.product_price.setText(productMap.get(mKeys[position]).getPrice());

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(context, ProductDetailActivity.class);
                in.putExtra("pid", productMap.get(mKeys[position]).getProductId());
                context.startActivity(in);
            }
        });

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obj= productMap.get(mKeys[position]);

                Intent in = new Intent(context, Attribute_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("pb", obj);
                in.putExtras(bundle);
                in.putExtra("position", position);
                quickOrderActivity.startActivityForResult(in,200);

            }
        });
        holder.llCount.setVisibility(View.GONE);
        if (productMap.get(mKeys[position]).getCount() == 0) {
            holder.llCount.setVisibility(View.GONE);
        } else {
            holder.llCount.setVisibility(View.VISIBLE);
            holder.tvCount.setText(""+productMap.get(mKeys[position]).getCount());
        }

        if (productMap.get(mKeys[position]).isAddedToCart()) {
            holder.btn_add_to_cart.setImageResource(R.drawable.cart_right_green_background);
        }


        holder.product_image.setImageDrawable(context.getResources().getDrawable(R.drawable.order_pro_placeholder));
        image_list= (ArrayList<String>)productMap.get(mKeys[position]).getImage();

        if(image_list.size()>0) {
            String url = image_list.get(0);
            url = url.replace(" ", "%20");
            Picasso.with(context)
                    .load("https://36.255.3.15/order_p/image/"+url)
                    .placeholder(R.drawable.product_default)
                    .error(R.drawable.product_default)
                    .into(holder.product_image);
        }

        holder.product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, ProductDetailActivity.class);
                in.putExtra("pid", productMap.get(mKeys[position]).getProductId());
                context.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        try {
            return productMap.size();
        } catch (NullPointerException e) {
        }
        return 0;
    }




        public void getFilter(String str) {
        productMap.clear();
        String txt = str.toLowerCase(Locale.getDefault());
        if (str.length() == 0) {
            productMap.putAll(tempProductMap);

        } else {
            for (ProdVal quikOrderData : tempProductMap.values()) {
                if (quikOrderData.getProductName().toLowerCase().contains(txt) || quikOrderData.getPrice().toLowerCase().contains(txt)) {
                    productMap.put(quikOrderData.getProductId(),quikOrderData);

                }
            }
        }
            mKeys = productMap.keySet().toArray(new String[productMap.size()]);
        notifyDataSetChanged();
    }
}
