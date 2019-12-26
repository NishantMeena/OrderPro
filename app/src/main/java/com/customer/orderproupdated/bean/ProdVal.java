package com.customer.orderproupdated.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nitin on 13/4/17.
 */

public class ProdVal implements Serializable {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_sku")
    @Expose
    private String productSku;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("specification")
    @Expose
    private String specification;
    @SerializedName("image")
    @Expose
    private List<String> image = null;
    @SerializedName("product_attribute")
    @Expose
    private List<com.customer.orderproupdated.bean.Product_attribute> productAttribute = null;
    private final static long serialVersionUID = -5322005465436419930L;

    private boolean isAddedToCart=false;
    private boolean isFavourite=false;
    private int count=0;
    private int product_quantity_selected=0;


    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<com.customer.orderproupdated.bean.Product_attribute> getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(List<com.customer.orderproupdated.bean.Product_attribute> productAttribute) {
        this.productAttribute = productAttribute;
    }
    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public boolean isAddedToCart() {
        return isAddedToCart;
    }

    public void setAddedToCart(boolean addedToCart) {
        isAddedToCart = addedToCart;
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getProduct_quantity_selected() {
        return product_quantity_selected;
    }

    public void setProduct_quantity_selected(int product_quantity_selected) {
        this.product_quantity_selected = product_quantity_selected;
    }

}
