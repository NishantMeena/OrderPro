package com.customer.orderproupdated.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nitin on 13/4/17.
 */

public class CategoryBean implements Serializable {
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("depth")
    @Expose
    private Integer depth;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("sub_catg")
    @Expose
    private List<SubCategory> subCatg = null;
    @SerializedName("prod_val")
    @Expose
    private List<ProdVal> prodVal = null;
    private final static long serialVersionUID = -4462918078355270386L;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<SubCategory> getSubCatg() {
        return subCatg;
    }

    public void setSubCatg(List<SubCategory> subCatg) {
        this.subCatg = subCatg;
    }

    public List<ProdVal> getProdVal() {
        return prodVal;
    }

    public void setProdVal(List<ProdVal> prodVal) {
        this.prodVal = prodVal;
    }

}

