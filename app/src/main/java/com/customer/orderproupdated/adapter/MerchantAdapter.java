package com.customer.orderproupdated.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customer.orderproupdated.Fragments.SwitchMerchentFragment;
import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.MerchantDetailsActivity;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.MerchantDetail;

import java.util.ArrayList;


public class MerchantAdapter extends RecyclerView.Adapter<MerchantAdapter.ViewHolder> {
    ArrayList<MerchantDetail> merchantList;
    Context context;
    MerchantDetail merchantDetail;
    boolean switchBtnClicked = false;
    Fragment fragment;
    String merchnatid = "";
    SharedPrefrence share;

    public MerchantAdapter(Fragment fragment, ArrayList<MerchantDetail> merchantList) {
        this.merchantList = merchantList;
        this.context = fragment.getActivity();
        this.fragment = fragment;
        share = SharedPrefrence.getInstance(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView merchant_name;
        public TextView merchant_no;
        public TextView merchant_address;
        public ImageView btn_switch_merchnat;
        public LinearLayout merchant_iv_ll, merchant_image_ll;
        public RelativeLayout ll_main;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            merchant_name = (TextView) itemLayoutView.findViewById(R.id.merchant_name);
            merchant_no = (TextView) itemLayoutView.findViewById(R.id.merchant_no);
            merchant_address = (TextView) itemLayoutView.findViewById(R.id.merchant_emailaddress);
            btn_switch_merchnat = (ImageView) itemLayoutView.findViewById(R.id.iv_switch_merchant);
            merchant_iv_ll = (LinearLayout) itemLayoutView.findViewById(R.id.merchant_iv_ll);
            merchant_image_ll = (LinearLayout) itemLayoutView.findViewById(R.id.merchant_image_ll);
            ll_main = (RelativeLayout) itemLayoutView.findViewById(R.id.ll_main);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view...
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_merchants, null);

        // create ViewHolder...

        MerchantAdapter.ViewHolder viewHolder = new MerchantAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // merchantDetail = merchantList.get(position);
        holder.merchant_name.setText(Utility.capitalize(merchantList.get(position).getUsername()));
        holder.merchant_no.setText(merchantList.get(position).getContactno());
        holder.merchant_address.setText(merchantList.get(position).getEmail());
        holder.btn_switch_merchnat.setImageResource(R.drawable.grey_switch_merchant);
        String merchantid = share.getValue(SharedPrefrence.MERCHANT_ID);

        if (merchantid != null && merchantid.equalsIgnoreCase(merchantList.get(position).getMerchantId())) {
            holder.btn_switch_merchnat.setImageResource(R.drawable.selected_switch_merchant);

        } else {
            holder.btn_switch_merchnat.setImageResource(R.drawable.grey_switch_merchant);
        }

        holder.merchant_image_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, MerchantDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Merchant Profile", merchantList.get(position));
                in.putExtras(bundle);
                context.startActivity(in);
            }
        });

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, MerchantDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Merchant Profile", merchantList.get(position));
                in.putExtras(bundle);
                context.startActivity(in);
            }
        });
        holder.merchant_iv_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = position;
                Log.e("pos", "" + pos);
                switchBtnClicked = true;
                holder.btn_switch_merchnat.setImageResource(R.drawable.selected_switch_merchant);
                ((SwitchMerchentFragment) fragment).showSwitchMerchentDialog(context, "Switch Merchant!", "Are you sure you want to switch the merchant?", position);
                ((SwitchMerchentFragment) fragment).getMerchantDetail(merchantList.get(position));
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return merchantList.size();
    }


}
