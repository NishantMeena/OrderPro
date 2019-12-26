package com.customer.orderproupdated.bean;

import java.io.Serializable;

/**
 * Created by Sony on 1/4/2017.
 */
public class OrderDetailData implements Serializable {
    private int serialno;
    private String prod_name;
    private String prod_id;
    private String prod_attributes;
    private String prod_quantity;
    private String prod_rate;
    private String prod_amount;

    public OrderDetailData(String prod_name, String prod_id, String prod_attributes, String prod_quantity, String prod_rate, String prod_amount) {
        this.setProd_name(prod_name);
        this.setProd_id(prod_id);
        this.setProd_attributes(prod_attributes);
        this.setProd_quantity(prod_quantity);
        this.setProd_rate(prod_rate);
        this.setProd_amount(prod_amount);
    }

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_attributes() {
        return prod_attributes;
    }

    public void setProd_attributes(String prod_attributes) {
        this.prod_attributes = prod_attributes;
    }

    public String getProd_quantity() {
        return prod_quantity;
    }

    public void setProd_quantity(String prod_quantity) {
        this.prod_quantity = prod_quantity;
    }

    public String getProd_rate() {
        return prod_rate;
    }

    public void setProd_rate(String prod_rate) {
        this.prod_rate = prod_rate;
    }

    public String getProd_amount() {
        return prod_amount;
    }

    public void setProd_amount(String prod_amount) {
        this.prod_amount = prod_amount;
    }
}
