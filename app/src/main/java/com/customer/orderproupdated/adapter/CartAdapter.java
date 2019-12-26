package com.customer.orderproupdated.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.CartActivity;
import com.customer.orderproupdated.UI.ProductDetailActivity;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.ProdVal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Sony on 1/9/2017.
 */
public class CartAdapter extends BaseAdapter {
    ArrayList<ProdVal> cart_item_list;
    ArrayList<ProdVal> newlist = new ArrayList<>();
    ProdVal cartData;
    Context context;
    int minteger = 1;
    Double total_amt = 1.0;
    Double price_per_prod = 0.0;
    SharedPrefrence prefrence;
    HashMap<String, ProdVal> favouriteMap = new HashMap<String, ProdVal>();
    ArrayList<String > image_list=new ArrayList<>();

    public CartAdapter(Context context,   ArrayList<ProdVal> cart_item_list) {

        this.cart_item_list = cart_item_list;
        this.newlist.addAll(cart_item_list);
        this.context = context;
        prefrence = SharedPrefrence.getInstance(context);
        favouriteMap=prefrence.getFavouriteMap(SharedPrefrence.FAVOURITE_LIST);

    }

    public static class ViewHolder {
        // each data item is just a string in this case
        ImageView iv_cart_palceholder, iv_add_to_favourite, iv_remove_cart;
        TextView tv_itemName, tv_itemPrice, tv_itemId, tv_item_attributes_selected, tv_itemTotalPrice;
        LinearLayout iv_plus, iv_minus, ll_product_detail;
        TextView tv_quantity;

        public ViewHolder(View v) {
            iv_cart_palceholder = (ImageView) v.findViewById(R.id.iv_placeholder_item);
            iv_add_to_favourite = (ImageView) v.findViewById(R.id.iv_favourite);
            iv_remove_cart = (ImageView) v.findViewById(R.id.iv_delete);

            iv_minus = (LinearLayout) v.findViewById(R.id.iv_minus);
            iv_plus = (LinearLayout) v.findViewById(R.id.iv_plus);
            ll_product_detail = (LinearLayout) v.findViewById(R.id.ll_product_detail);

            tv_itemName = (TextView) v.findViewById(R.id.item_name);
            tv_itemId = (TextView) v.findViewById(R.id.item_id);
            tv_itemPrice = (TextView) v.findViewById(R.id.tv_itemPrice);
            tv_item_attributes_selected = (TextView) v.findViewById(R.id.tv_item_attributes_selected);
            tv_quantity = (TextView) v.findViewById(R.id.tv_quantity);
            tv_itemTotalPrice = (TextView) v.findViewById(R.id.tv_total_amt);
        }
    }

    @Override
    public int getCount() {
        return cart_item_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_cart, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (favouriteMap.containsKey(cart_item_list.get(position).getProductId())) {
            holder.iv_add_to_favourite.setBackgroundResource(R.drawable.heart_blue_icon);
        } else {
            holder.iv_add_to_favourite.setBackgroundResource(R.drawable.heart_line_blue_24dp);
        }
        setDataValues(holder, position);


        holder.iv_add_to_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cart_item_list.get(position).isFavourite()) {
                    cart_item_list.get(position).setFavourite(false);
                    ((CartActivity) context).removeFromFavouritselist(cart_item_list.get(position));
                    holder.iv_add_to_favourite.setBackgroundResource(R.drawable.heart_line_blue_24dp);

                } else {
                    cart_item_list.get(position).setFavourite(true);
                    ((CartActivity) context).addToFavouritselist(cart_item_list.get(position));
                    holder.iv_add_to_favourite.setBackgroundResource(R.drawable.heart_blue_icon);
                }


            }
        });
        holder.iv_remove_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemoveCartDialog(context, "Remove From Cart", "Are you sure you want to remove product from Cart?", position);

            }
        });
        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                minteger = cart_item_list.get(position).getProduct_quantity_selected();

                if (minteger > 1) {
                    minteger = minteger - 1;
                    PreferenceConstant.TOTAL_PRICE = PreferenceConstant.TOTAL_PRICE - Double.parseDouble(cart_item_list.get(position).getPrice());
                    ((CartActivity) context).txt_grand_total.setText(PreferenceConstant.TOTAL_PRICE + "");
                }
                total_amt = Double.parseDouble(cart_item_list.get(position).getPrice()) * minteger;
                holder.tv_quantity.setText("" + minteger);
                holder.tv_itemTotalPrice.setText("" + total_amt);

                cart_item_list.get(position).setProduct_quantity_selected(minteger);
                prefrence.setCartList(SharedPrefrence.CART_LIST, cart_item_list);


            }
        });
        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                minteger = cart_item_list.get(position).getProduct_quantity_selected();

                minteger = minteger + 1;
                holder.tv_quantity.setText("" + minteger);
                total_amt = Double.parseDouble(cart_item_list.get(position).getPrice()) * minteger;
                holder.tv_itemTotalPrice.setText("" + total_amt);

                PreferenceConstant.TOTAL_PRICE = PreferenceConstant.TOTAL_PRICE + Double.parseDouble(cart_item_list.get(position).getPrice());
                ((CartActivity) context).txt_grand_total.setText(PreferenceConstant.TOTAL_PRICE + "");


                cart_item_list.get(position).setProduct_quantity_selected(minteger);

                prefrence.setCartList(SharedPrefrence.CART_LIST, cart_item_list);


            }
        });

        holder.ll_product_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(context, ProductDetailActivity.class);
                in.putExtra("pid", cart_item_list.get(position).getProductId());
                context.startActivity(in);
            }
        });

        holder.tv_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                builder.setTitle("Select Quantity");
                final EditText edittext = new EditText(context);
                edittext.setHeight(80);
                edittext.setWidth(300);
                edittext.setBackgroundColor(R.color.dots_background);
                edittext.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_CLASS_NUMBER);

                builder.setView(edittext);

                String positiveText = "Yes";
                String negetiveText = "Cancel";
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                builder.setNegativeButton(negetiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();

*/


            }
        });


        return v;
    }

    public void setDataValues(ViewHolder holder, int position) {
        cartData =cart_item_list.get(position);
        price_per_prod = Double.parseDouble(cartData.getPrice());
        minteger = cartData.getProduct_quantity_selected();
        String attribute_selected=setAttributeInCartItem();

        holder.tv_itemName.setText(Utility.capitalize(cartData.getProductName()));
        holder.tv_itemPrice.setText(cartData.getPrice());
        holder.tv_item_attributes_selected.setText(attribute_selected);
        holder.tv_quantity.setText(cartData.getProduct_quantity_selected() + "");
        holder.tv_itemTotalPrice.setText(price_per_prod * cartData.getProduct_quantity_selected() + "");
        //  holder.iv_cart_palceholder.setBackgroundResource(cartData.getProduct_image());
        image_list= (ArrayList<String>)cart_item_list.get(position).getImage();

       if (image_list.size()>0){
           String url = image_list.get(0);
           url= url.replace(" ","%20");
           Picasso.with(context)
                   .load("https://36.255.3.15/order_p/image/"+url)
                   .placeholder(R.drawable.product_default)
                   .error(R.drawable.product_default)
                   .into(holder.iv_cart_palceholder);
       }

    }

    public void getFilter(String str) {
        cart_item_list.clear();
        String txt = str.toLowerCase(Locale.getDefault());
        if (str.length() == 0) {
            cart_item_list.addAll(newlist);

        } else {
            for (ProdVal quikOrderData : newlist) {
                if (quikOrderData.getProductName().toLowerCase().contains(txt) || quikOrderData.getPrice().toLowerCase().contains(txt)) {
                    cart_item_list.add(quikOrderData);

                }
            }
        }
        notifyDataSetChanged();
    }

    public void showRemoveCartDialog(final Context context, String title, String message, final int position) {
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

                        ((CartActivity) context).removeListItem(position);


                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public  String setAttributeInCartItem()
    {
        String attributes="";
        for(int i=0;i<cartData.getProductAttribute().size();i++)
        {
            if (i>0) {
                attributes=attributes.concat(","+cartData.getProductAttribute().get(i).getAttributeValue());
            }
            else
            {
                attributes=attributes.concat(cartData.getProductAttribute().get(i).getAttributeValue());
            }

        }
        return attributes;
    }

}