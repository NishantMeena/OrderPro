package com.customer.orderproupdated.bean;

import java.io.Serializable;

public class NotificationData implements Serializable {
    String item_title;
    String item_content;
    private String item_time;

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getItem_content() {
        return item_content;
    }

    public void setItem_content(String item_content) {
        this.item_content = item_content;
    }


    public NotificationData() {

    }

    public String getItem_time() {
        return item_time;
    }

    public void setItem_time(String item_time) {
        this.item_time = item_time;
    }

    public NotificationData(String item_title, String item_content, String item_time) {

        this.item_title = item_title;
        this.item_content = item_content;
        this.item_time = item_time;

    }
}
