package com.customer.orderproupdated.bean;

        import java.io.Serializable;
        import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class GetMerchantList implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("merchant")
    @Expose
    private List<MerchantDetail> merchant = null;
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

    public List<MerchantDetail> getMerchant() {
        return merchant;
    }

    public void setMerchant(List<MerchantDetail> merchant) {
        this.merchant = merchant;
    }

}


