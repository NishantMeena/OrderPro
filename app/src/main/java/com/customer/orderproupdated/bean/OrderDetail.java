package com.customer.orderproupdated.bean;


import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail implements Serializable
{

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("selected_attribute")
    @Expose
    private String selectedAttribute;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
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
    private List<Product_attribute> productAttribute = null;
    private final static long serialVersionUID = 8071083233335229374L;

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

    public String getSelectedAttribute() {
        return selectedAttribute;
    }

    public void setSelectedAttribute(String selectedAttribute) {
        this.selectedAttribute = selectedAttribute;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
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

    public List<Product_attribute> getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(List<Product_attribute> productAttribute) {
        this.productAttribute = productAttribute;
    }

}