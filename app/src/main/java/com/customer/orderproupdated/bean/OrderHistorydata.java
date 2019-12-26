package com.customer.orderproupdated.bean;

import java.io.Serializable;

public class OrderHistorydata implements Serializable {
    private String order_status;
    private String order_date;
    private String dispatch_date;
    private String merchent_name;
    private String merchent_address;
    private int item_image;
    private String order_id;

    public OrderHistorydata() {

    }

    public OrderHistorydata(String order_id,String order_status, String order_date,String dispatch_date,String merchent_name, String merchent_address) {
        this.order_id = order_id;
        this.order_status = order_status;
        this.order_date = order_date;
        this.dispatch_date=dispatch_date;
        this.merchent_name=merchent_name;
        this.merchent_address=merchent_address;

    }


    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getItem_image() {

        return item_image;
    }

    public void setItem_image(int item_image) {
        this.item_image = item_image;
    }

    public String getDispatch_date() {
        return dispatch_date;
    }

    public void setDispatch_date(String dispatch_date) {
        this.dispatch_date = dispatch_date;
    }

    public String getMerchent_name() {
        return merchent_name;
    }

    public void setMerchent_name(String merchent_name) {
        this.merchent_name = merchent_name;
    }

    public String getMerchent_address() {
        return merchent_address;
    }

    public void setMerchent_address(String merchent_address) {
        this.merchent_address = merchent_address;
    }
}
