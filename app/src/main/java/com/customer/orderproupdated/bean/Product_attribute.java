package com.customer.orderproupdated.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nitin on 13/4/17.
 */

public class Product_attribute implements Serializable {

    @SerializedName("attribute_id")
    @Expose
    private String attributeId;
    @SerializedName("attribute_name")
    @Expose
    private String attributeName;
    @SerializedName("attribute_value")
    @Expose
    private String attributeValue;
    private final static long serialVersionUID = -1469027103568707506L;

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

}
