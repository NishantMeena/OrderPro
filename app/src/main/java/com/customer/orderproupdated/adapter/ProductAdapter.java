package com.customer.orderproupdated.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.ProdVal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<ProdVal> itemsData;
    List<ProdVal> newlist = new ArrayList<>();
    ArrayList<String> image_list = new ArrayList<>();
    Context contexts;

    public ProductAdapter(Context contexts, List<ProdVal> itemsData) {
        this.itemsData = itemsData;
        this.newlist.addAll(itemsData);
        this.contexts = contexts;
    }

    // Create new views (invoked by the layout manager)

    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_dashboard, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.txtViewTitle.setText(Utility.capitalize(Html.fromHtml(itemsData.get(position).getProductName()).toString()));
        viewHolder.txtViewPrice.setText(itemsData.get(position).getPrice());
        viewHolder.txtViewID.setText(itemsData.get(position).getProductSku());
        viewHolder.txtViewID.setTextSize(13);
        viewHolder.rupee_symbol.setVisibility(View.VISIBLE);
        viewHolder.tv_pid_tag.setVisibility(View.VISIBLE);
        viewHolder.txtViewID.setTextColor(contexts.getResources().getColor(R.color.text_gray));
        viewHolder.imgViewIcon.setImageDrawable(contexts.getResources().getDrawable(R.drawable.order_pro_placeholder));

        image_list = (ArrayList<String>) itemsData.get(position).getImage();

        String url = "";
        if (image_list.size()>0) {
            url = image_list.get(0);
            url = url.replace(" ", "%20");
            Picasso.with(contexts)
                    .load("https://36.255.3.15/order_p/image/"+url)
                    .placeholder(R.drawable.product_default)
                    .error(R.drawable.product_default)
                    .into(viewHolder.imgViewIcon);
        } else {
            url = "";
        }
        // container.addView();
        // viewHolder.imgViewIcon.setBackground(Drawable.createFromPath(itemsData.get(position).getImage()));
        Log.e("@@@", String.valueOf(viewHolder.getAdapterPosition()));
    }

    // inner class to hold a reference to each item of RecyclerView...
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewTitle;
        public ImageView imgViewIcon;
        public TextView txtViewPrice;
        public TextView txtViewID;
        TextView rupee_symbol;
        TextView tv_pid_tag;
        LinearLayout ll_main;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            txtViewPrice = (TextView) itemLayoutView.findViewById(R.id.product_price);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.product_name);
            txtViewID = (TextView) itemLayoutView.findViewById(R.id.product_id);
            rupee_symbol = (TextView) itemLayoutView.findViewById(R.id.rupee_symbol);
            tv_pid_tag = (TextView) itemLayoutView.findViewById(R.id.tv_pid_tag);
            ll_main = (LinearLayout) itemLayoutView.findViewById(R.id.ll_main);
        }
    }

    public int getItemCount() {
        return itemsData.size();
    }

    public void getFilter(String str) {
        itemsData.clear();
        String txt = str.toLowerCase(Locale.getDefault());
        if (str.length() == 0) {
            itemsData.addAll(newlist);
           /* if (quick_order_list.size() > 0) {
                tv_no_contacts.setVisibility(View.INVISIBLE);
            }*/
        } else {
            for (ProdVal prodData : newlist) {
                if (prodData.getProductName().toLowerCase().contains(txt) || prodData.getPrice().toLowerCase().contains(txt)) {
                    itemsData.add(prodData);
                }
            }
        }
        notifyDataSetChanged();
    }
}
