package com.customer.orderproupdated.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customer.orderproupdated.Fragments.FavouritesFragment;
import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.Attribute_Activity;
import com.customer.orderproupdated.UI.ProductDetailActivity;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.ProdVal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
    ArrayList<ProdVal> favouritselist;
    // Products_bean favouritseListData;
    ArrayList<ProdVal> newlist = new ArrayList<>();
    Context context;
    Fragment fragment;
    ArrayList<String > image_list=new ArrayList<>();

    public FavouritesAdapter(Fragment fragment, ArrayList<ProdVal> favouritselist) {

        this.favouritselist = favouritselist;
        this.newlist.addAll(favouritselist);
        this.context = fragment.getActivity();
        this.fragment = fragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView product_image;
        TextView product_name;
        TextView product_id;
        TextView product_price;
        ImageView list_item_wishlist_button;
        ImageView iv_add_to_cart;
        LinearLayout ll_product_detail;

        public ViewHolder(View v) {
            super(v);
            product_image = (ImageView) v.findViewById(R.id.product_image);
            product_name = (TextView) v.findViewById(R.id.product_name);
            product_id = (TextView) v.findViewById(R.id.product_id);
            product_price = (TextView) v.findViewById(R.id.product_price);
            list_item_wishlist_button = (ImageView) v.findViewById(R.id.list_item_wishlist_button);
            iv_add_to_cart = (ImageView) v.findViewById(R.id.iv_add_to_cart);
            ll_product_detail = (LinearLayout) v.findViewById(R.id.ll_main);
        }
    }

    @Override
    public FavouritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favourite, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(FavouritesAdapter.ViewHolder holder, final int position) {
        holder.product_name.setText(Utility.capitalize(favouritselist.get(position).getProductName()));
       // holder.product_image.setBackgroundResource(favouritselist.get(position).getProduct_image());
        holder.product_id.setText(favouritselist.get(position).getProductId());
        holder.product_price.setText(favouritselist.get(position).getPrice());


        holder.iv_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, Attribute_Activity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("pb", favouritselist.get(position));
                in.putExtras(bundle);
                in.putExtra("position", position);
                //context.startActivity(in);
                fragment.startActivityForResult(in, 200);

            }
        });
        holder.list_item_wishlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemoveFavouriteDialog(context, "Remove  Favourite", "Are you sure you want to remove product from Favourites?", position);
            }
        });

        holder.ll_product_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(context, ProductDetailActivity.class);
                in.putExtra("pid", favouritselist.get(position).getProductId());
                context.startActivity(in);
            }
        });

       // holder.product_image.setImageDrawable(context.getResources().getDrawable(R.drawable.order_pro_placeholder));
        image_list= (ArrayList<String>)favouritselist.get(position).getImage();
        if (image_list.size()>0){
            String url = image_list.get(0);
            url= url.replace(" ","%20");
            Picasso.with(context)
                    .load("https://36.255.3.15/order_p/image/"+url)
                    .placeholder(R.drawable.product_default)
                    .error(R.drawable.product_default)
                    .into(holder.product_image);

        }

    }

    @Override
    public int getItemCount() {
        try {
            return favouritselist.size();
        } catch (NullPointerException e) {
        }
        return 0;
    }

    public void getFilter(String str) {
        favouritselist.clear();
        String txt = str.toLowerCase(Locale.getDefault());
        if (str.length() == 0) {
            favouritselist.addAll(newlist);
           /* if (quick_order_list.size() > 0) {
                tv_no_contacts.setVisibility(View.INVISIBLE);
            }*/
        } else {
            for (ProdVal favouritseListData : newlist) {
                if (favouritseListData.getProductName().toLowerCase().contains(txt) || favouritseListData.getPrice().toLowerCase().contains(txt)) {
                    favouritselist.add(favouritseListData);
                    /*if (quick_order_list.size() > 0) {
                        tv_no_contacts.setVisibility(View.INVISIBLE);
                    }*/
                }
            }
        }
        notifyDataSetChanged();
    }


    public void showRemoveFavouriteDialog(final Context context, String title, String message, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);

        String positiveText = "Yes";
        String negativeText = "No";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        first update is_favorite attribute false of this object and set it into cart_list


                        favouritselist.get(position).setFavourite(false);

                        ((FavouritesFragment)fragment).removeListItem(favouritselist.get(position));



                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


}