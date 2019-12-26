package com.customer.orderproupdated.bean;

import java.io.Serializable;

/**
 * Created by Sony on 1/11/2017.
 */
public class Products_bean implements Serializable {
    private String product_name="";
    private String product_id="";
    private String product_price="";
    private String product_attribute="";
    private boolean isAddedToCart=false;
    private boolean isFavourite=false;
    private int count=0;
    private int product_image;

    private int level=1;

    private int product_quantity=0;
    private String categeory_id="";
    private String product_color="";

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private String product_size="";
    private String product_material="";


    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public String getProduct_material() {
        return product_material;
    }

    public void setProduct_material(String product_material) {
        this.product_material = product_material;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Products_bean(String product_name, int product_image,int level) {
        this.product_name = product_name;
        this.product_image = product_image;
        this.level=level;
    }
    public Products_bean(String product_name, String product_id, String product_price, int product_image) {
        this.setProduct_name(product_name);
        this.setProduct_id(product_id);
        this.setProduct_image(product_image);
    }
    public Products_bean(String product_name, String product_id, String product_price, int product_image,String categeory_id,int level) {
        this.setProduct_name(product_name);
        this.setProduct_id(product_id);
        this.setProduct_price(product_price);
        this.setProduct_image(product_image);
        this.setCategeory_id(categeory_id);
        this.setLevel(level);

    }
    public Products_bean(String product_name, String product_id, String product_price, int product_image,String categeory_id) {
        this.setProduct_name(product_name);
        this.setProduct_id(product_id);
        this.setProduct_price(product_price);
        this.setProduct_image(product_image);
        this.setCategeory_id(categeory_id);


    }


    public Products_bean(String product_name, String product_id, String product_color, String product_size, String product_material, int product_quantity, String product_price) {
        this.product_name = product_name;
        this.product_id = product_id;
        this.product_color = product_color;
        this.product_size = product_size;
        this.product_material = product_material;
        this.product_quantity = product_quantity;
        this.product_price = product_price;

    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_attribute() {
        return product_attribute;
    }

    public void setProduct_attribute(String product_attribute) {
        this.product_attribute = product_attribute;
    }

    public int getProduct_image() {
        return product_image;
    }

    public void setProduct_image(int product_image) {
        this.product_image = product_image;
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

    public String getCategeory_id() {
        return categeory_id;
    }

    public void setCategeory_id(String categeory_id) {
        this.categeory_id = categeory_id;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }


}
