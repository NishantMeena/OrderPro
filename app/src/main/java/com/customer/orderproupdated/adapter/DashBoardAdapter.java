package com.customer.orderproupdated.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.SubCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.ViewHolder> {
    private List<SubCategory> list_subCategory;
    List<SubCategory> newlist = new ArrayList<>();
    Context contexts;

    public DashBoardAdapter(Context contexts, List<SubCategory> itemsData) {
        this.list_subCategory = itemsData;
        this.newlist.addAll(itemsData);
        this.contexts = contexts;
    }

    public DashBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dashboard, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        if (list_subCategory.get(position).getDepth() == 1) {
            viewHolder.txtViewID.setText(Utility.capitalize(Html.fromHtml(list_subCategory.get(position).getSubCategoryName()).toString()));
            viewHolder.txtViewTitle.setText("");
            viewHolder.txtViewPrice.setText("");
            viewHolder.txtViewID.setTextSize(15);
            viewHolder.rupee_symbol.setVisibility(View.GONE);
            viewHolder.txtViewID.setTextColor(contexts.getResources().getColor(R.color.black));

        } else if (list_subCategory.get(position).getDepth() == 2) {
            viewHolder.txtViewID.setText(Utility.capitalize(Html.fromHtml(list_subCategory.get(position).getSubCategoryName()).toString()));
            viewHolder.txtViewTitle.setText("");
            viewHolder.txtViewPrice.setText("");
            viewHolder.txtViewID.setTextSize(15);
            viewHolder.rupee_symbol.setVisibility(View.GONE);
            viewHolder.txtViewID.setTextColor(contexts.getResources().getColor(R.color.black));

        }
        viewHolder.imgViewIcon.setImageDrawable(contexts.getResources().getDrawable(R.drawable.order_pro_placeholder));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public ImageView imgViewIcon;
        public TextView txtViewPrice;
        public TextView txtViewID;
        TextView rupee_symbol;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            txtViewPrice = (TextView) itemLayoutView.findViewById(R.id.product_price);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.product_name);
            txtViewID = (TextView) itemLayoutView.findViewById(R.id.product_id);
            rupee_symbol = (TextView) itemLayoutView.findViewById(R.id.rupee_symbol);
        }
    }


    public int getItemCount() {
        return list_subCategory.size();
    }

    public void getFilter(String str) {
        list_subCategory.clear();
        String txt = str.toLowerCase(Locale.getDefault());
        if (str.length() == 0) {
            list_subCategory.addAll(newlist);
            if (list_subCategory.size() > 0) {

            }
        } else {
            for (SubCategory subCategoryData : newlist) {
                if (subCategoryData.getSubCategoryName().toLowerCase().contains(txt)) {
                    list_subCategory.add(subCategoryData);
                    if (list_subCategory.size() > 0) {

                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
