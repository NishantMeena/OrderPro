package com.customer.orderproupdated.adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.AddMerchantActivity;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.AllMerchantDetails;
import com.customer.orderproupdated.bean.MerchantDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sony on 3/27/2017.
 */
public class AllMerchantAdapter extends RecyclerView.Adapter<AllMerchantAdapter.ViewHolder> {

    private List<AllMerchantDetails> allmerchantList=new ArrayList<>();

    List<AllMerchantDetails> newlist = new ArrayList<>();

    Context contexts;
    int mSelected;



    public AllMerchantAdapter(Context contexts, ArrayList<AllMerchantDetails> list_all_merchant) {
        this.allmerchantList = list_all_merchant;
        this.newlist.addAll(list_all_merchant);
        this.contexts = contexts;
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewMerchantName;
        public TextView txtViewMerchantEmail;
        public TextView txtViewMerchantStatus;
        public LinearLayout ll_main;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewMerchantName = (TextView) itemLayoutView.findViewById(R.id.merchant_name);
            txtViewMerchantEmail = (TextView) itemLayoutView.findViewById(R.id.merchant_email);
            txtViewMerchantStatus=(TextView)itemLayoutView.findViewById(R.id.merchant_status);
            ll_main=(LinearLayout)itemLayoutView.findViewById(R.id.ll_main);


        }
    }
    @Override
    public AllMerchantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_merchant_all, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AllMerchantAdapter.ViewHolder holder, final int position) {


            holder.txtViewMerchantName.setText(Utility.capitalize(allmerchantList.get(position).getUsername()));
            holder.txtViewMerchantEmail.setText(allmerchantList.get(position).getEmail());
           // holder.ll_main.setBackgroundColor(R.color.white);

        if(allmerchantList.get(position).getRequestStatus().equals("1"))
        {
            holder.txtViewMerchantStatus.setVisibility(View.VISIBLE);
            holder.txtViewMerchantStatus.setText("Added");
        }
        else if(allmerchantList.get(position).getRequestStatus().equals("0"))
        {
            holder.txtViewMerchantStatus.setVisibility(View.VISIBLE);
            holder.txtViewMerchantStatus.setText("Requested");
        }
        else
        {
            holder.txtViewMerchantStatus.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return allmerchantList.size();
    }

    public void getFilter(String str) {
        allmerchantList.clear();
        String txt = str.toLowerCase(Locale.getDefault());
        if (str.length() == 0) {
            allmerchantList.addAll(newlist);
        } else {
            for (AllMerchantDetails
                    merchantDetail : newlist) {
                if (merchantDetail.getUsername().toLowerCase().contains(txt) || merchantDetail.getEmail().toLowerCase().contains(txt)) {
                    allmerchantList.add(merchantDetail);
                }
            }
        }
        notifyDataSetChanged();
    }


}
