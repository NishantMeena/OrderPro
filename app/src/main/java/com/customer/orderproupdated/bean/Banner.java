package com.customer.orderproupdated.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Banner implements Serializable
{

    @SerializedName("banner_name")
    @Expose
    private String bannerName;
    @SerializedName("url")
    @Expose
    private String url;
    private final static long serialVersionUID = 1952029784726144039L;

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
