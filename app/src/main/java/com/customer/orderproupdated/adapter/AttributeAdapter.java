package com.customer.orderproupdated.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.Attribute_Activity;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.ProdVal;
import com.customer.orderproupdated.bean.Product_attribute;

import java.util.List;


public class AttributeAdapter extends BaseAdapter {
    List<Product_attribute> attribute_list;
    ProdVal pbobj;
    Context context;
    View view;
    ViewHolder holder = null;
    List<Product_attribute> attribute_list_selcted;

    public AttributeAdapter(Context context, ProdVal prodVal) {
        this.context = context;
        this.pbobj = prodVal;
        attribute_list= pbobj.getProductAttribute();
    }

    @Override
    public int getCount() {
        return attribute_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = lif.inflate(R.layout.list_item_attribute, null);
            holder = new ViewHolder();
            initview(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }



        String attribute_val = attribute_list.get(position).getAttributeValue();
        String[] attribute_val_array=attribute_val.split(",");

        String[] items = new String[attribute_val_array.length+1];

        items[0] = Utility.capitalize(attribute_list.get(position).getAttributeName());
        System.out.println("items = "  +  items[0]);

        for (int i = 0; i <attribute_val_array.length; i++) {
            items[i+1] = attribute_val_array[i];
            System.out.println("items = "  +  items[i]);
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, R.layout.spinner_main_text, items);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_text);
        holder.spn_label.setAdapter(spinnerAdapter);

        holder.spn_label.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                if(pos>0) {
                    attribute_list_selcted = pbobj.getProductAttribute();
                    attribute_list_selcted.get(position).setAttributeValue(parent.getItemAtPosition(pos).toString());
                    pbobj.setProductAttribute(attribute_list_selcted);
                    ((Attribute_Activity)context).updateProductbean(pbobj);

                }
                else
                {
                attribute_list_selcted = pbobj.getProductAttribute();
                attribute_list_selcted.get(position).setAttributeValue("");
                pbobj.setProductAttribute(attribute_list_selcted);

                ((Attribute_Activity)context).updateProductbean(pbobj);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
               /*
                for(int j=0;j<attribute_list_selcted.size();j++) {
                    attribute_list_selcted = pbobj.getProductAttribute();
                    attribute_list_selcted.get(position).setAttributeValue("");
                    pbobj.setProductAttribute(attribute_list_selcted);
                }
                ((Attribute_Activity)context).updateProductBean(pbobj);*/
            }

        });

        //spinnerAdapter.add("value");
        // spinnerAdapter.notifyDataSetChanged();
        return convertView;
    }

    private void initview(View convertView) {
        holder.spn_label = (Spinner) convertView.findViewById(R.id.spinner_label);
    }

    public static class ViewHolder {
        Spinner spn_label;
    }
}



