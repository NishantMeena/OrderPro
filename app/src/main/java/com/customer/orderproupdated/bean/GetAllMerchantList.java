package com.customer.orderproupdated.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Advantal on 6/14/2017.
 */

public class GetAllMerchantList {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("merchant")
    @Expose
    private List<AllMerchantDetails> merchant = null;
    private final static long serialVersionUID = 4730179379642355950L;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AllMerchantDetails> getMerchant() {
        return merchant;
    }

    public void setMerchant(List<AllMerchantDetails> merchant) {
        this.merchant = merchant;
    }


}
