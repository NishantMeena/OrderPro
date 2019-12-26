package com.customer.orderproupdated.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nitin on 13/4/17.
 */

public class MainCategoryBean  implements Serializable {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("deleted_product")
    @Expose
    private List<DeletedProductBean> deletedProduct = null;
    @SerializedName("category")
    @Expose
    private List<CategoryBean> category = null;
    private final static long serialVersionUID = 864947194031100095L;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<DeletedProductBean> getDeletedProduct() {
        return deletedProduct;
    }

    public void setDeletedProduct(List<DeletedProductBean> deletedProduct) {
        this.deletedProduct = deletedProduct;
    }

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

}
