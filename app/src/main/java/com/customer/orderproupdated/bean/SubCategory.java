package com.customer.orderproupdated.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nitin on 13/4/17.
 */

public class SubCategory implements Serializable {
    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;
    @SerializedName("depth")
    @Expose
    private Integer depth;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("sub_catg")
    @Expose
    private List<SubCategory> subCatg = null;
    @SerializedName("prod_val")
    @Expose
    private List<ProdVal> prodVal = null;
    private final static long serialVersionUID = -7446156254130475857L;

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
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
