package com.customer.orderproupdated.Utility;

import com.customer.orderproupdated.R;

import com.customer.orderproupdated.bean.NotificationData;
import com.customer.orderproupdated.bean.OrderHistorydata;
import com.customer.orderproupdated.bean.Products_bean;


import java.util.ArrayList;
import java.util.HashMap;

public class DummyData {


    public ArrayList<Products_bean> itemData = new ArrayList<Products_bean>();
    public ArrayList<Products_bean> order_detail_list = new ArrayList<Products_bean>();
    public ArrayList<OrderHistorydata> orderlist = new ArrayList<OrderHistorydata>();
    public ArrayList<NotificationData> notificationlist = new ArrayList<NotificationData>();
    HashMap<String, Integer> file_maps = new HashMap<>();

    /*public ArrayList<Categories> initItemCategories() {

        ArrayList<Categories> itemCategories = new ArrayList<Categories>();
        itemCategories.add(new Categories("C1", "Clothing & Accessories", initSubCategeoryFirst_clothing()));
        itemCategories.add(new Categories("C2", "Electronics & Appliances", initSubCategeoryFirst_Electronics()));
//        itemCategories.add(new Categories("C3", "Grocery", subCategeoryFirst_Grocery));
//        itemCategories.add(new Categories("C4", "Cosmetics", subCategeoryFirst_Cosmetics));
//        itemCategories.add(new Categories("C5", "Kids", subCategeoryFirst_Kids));

        return itemCategories;
    }*/

   /* //data clothing
    public ArrayList<SubCategeoryFirst> initSubCategeoryFirst_clothing() {
        ArrayList<SubCategeoryFirst> subCategeoryFirst_clothing = new ArrayList<SubCategeoryFirst>();
        subCategeoryFirst_clothing.add(new SubCategeoryFirst("C1_S1", "Dresses", 1, initSubCategeorySecond_clothing()));
        subCategeoryFirst_clothing.add(new SubCategeoryFirst("C1_S2", "footwear", 1, initSubCategeorySecond_clothing()));
        subCategeoryFirst_clothing.add(new SubCategeoryFirst("C1_S3", "jackets and coats", 1, initSubCategeorySecond_clothing()));
        subCategeoryFirst_clothing.add(new SubCategeoryFirst("C1_S4", "winter wear", 1, initSubCategeorySecond_clothing()));
        subCategeoryFirst_clothing.add(new SubCategeoryFirst("C1_S5", "Sportswear", 1, initSubCategeorySecond_clothing()));

        return subCategeoryFirst_clothing;
    }
*/
    /*public ArrayList<SubCategeoryFirst> initSubCategeorySecond_clothing() {
        ArrayList<SubCategeoryFirst> subCategeorySecond_Dresses = new ArrayList<>();
        subCategeorySecond_Dresses.add(new SubCategeoryFirst("C1_S1", "Mens casual", 2,null));
        subCategeorySecond_Dresses.add(new SubCategeoryFirst("C1_S2", "Mens partywear", 2, null));
        subCategeorySecond_Dresses.add(new SubCategeoryFirst("C1_S3", "Womens casual", 2, null));
        subCategeorySecond_Dresses.add(new SubCategeoryFirst("C1_S4", "Womens partywear", 2,null));


        *//*subCategeorySecond_Dresses.add(new SubCategeorySecond("C1_S11", "Mens casual", 2));
        subCategeorySecond_Dresses.add(new SubCategeorySecond("C1_S12", "Mens partywear", 2));
        subCategeorySecond_Dresses.add(new SubCategeorySecond("C1_S13", "Womens casual", 2));
        subCategeorySecond_Dresses.add(new SubCategeorySecond("C1_S14", "Womens partywear", 2));*//*

        return subCategeorySecond_Dresses;
    }*/

 /*   //data Elecronics
    public ArrayList<SubCategeoryFirst> initSubCategeoryFirst_Electronics() {
        ArrayList<SubCategeoryFirst> subCategeoryFirst_Electronics = new ArrayList<SubCategeoryFirst>();
        subCategeoryFirst_Electronics.add(new SubCategeoryFirst("C2_S1","Mobile & Accessories" , 1, initSubCategeorySecond_Electronics()));
        subCategeoryFirst_Electronics.add(new SubCategeoryFirst("C2_S2", "Home Entertainment", 1, initSubCategeorySecond_Electronics()));
        subCategeoryFirst_Electronics.add(new SubCategeoryFirst("C2_S3", "Small Home Aplliances", 1, initSubCategeorySecond_Electronics()));
        subCategeoryFirst_Electronics.add(new SubCategeoryFirst("C2_S4", "Refrigerators", 1, initSubCategeorySecond_Electronics()));
        subCategeoryFirst_Electronics.add(new SubCategeoryFirst("C2_S5", "Kitchen", 1, initSubCategeorySecond_Electronics()));

        return subCategeoryFirst_Electronics;
    }*/
/*

    public ArrayList<SubCategeoryFirst> initSubCategeorySecond_Electronics() {
        ArrayList<SubCategeoryFirst> subCategeorySecond_a = new ArrayList<>();
        subCategeorySecond_a.add(new SubCategeoryFirst("C2_S11", "Mens casual", 2, null));
        subCategeorySecond_a.add(new SubCategeoryFirst("C2_S12", "Mens partywear", 2, null));
        subCategeorySecond_a.add(new SubCategeoryFirst("C2_S13", "Womens casual", 2, null));
        subCategeorySecond_a.add(new SubCategeoryFirst("C2_S14", "Womens partywear", 2, null));

        return subCategeorySecond_a;
    }
*/

    public void init_subcategeory_one_clothing() {
        itemData.clear();

        itemData.add(new Products_bean("Dresses", R.drawable.order_pro_placeholder, 1));
        itemData.add(new Products_bean("footwear", R.drawable.order_pro_placeholder, 1));
        itemData.add(new Products_bean("jackets and coats", R.drawable.order_pro_placeholder, 1));
        itemData.add(new Products_bean("winter wear", R.drawable.order_pro_placeholder, 1));
        itemData.add(new Products_bean("Sportswear", R.drawable.order_pro_placeholder, 1));
    }

    public void init_subcategeory_dresses() {
        itemData.clear();
        itemData.add(new Products_bean("Mens casual", R.drawable.order_pro_placeholder, 2));
        itemData.add(new Products_bean("Mens partywear", R.drawable.order_pro_placeholder, 2));
        itemData.add(new Products_bean("Womens casual", R.drawable.order_pro_placeholder, 2));
        itemData.add(new Products_bean("Womens partywear", R.drawable.order_pro_placeholder, 2));

    }

    public ArrayList<Products_bean> initChildData() {
        itemData.clear();
        itemData.add(new Products_bean("Floral shirt", "PD101", "1700", R.drawable.order_pro_placeholder, "Black,xl", 3));
        itemData.add(new Products_bean("V neck Tshirt", "PD101", "1500", R.drawable.order_pro_placeholder, "Red,xxl", 3));
        itemData.add(new Products_bean("Denim jeans", "PD102", "4000", R.drawable.order_pro_placeholder, "blue,40", 3));
        itemData.add(new Products_bean("Printed blue shirt", "PD101", "2500", R.drawable.order_pro_placeholder, "pink,L,semi cotton", 3));
        itemData.add(new Products_bean("Rounded neck Tshirt", "PD101", "2000", R.drawable.order_pro_placeholder, "gray,xxl", 3));
        //itemData.add(new Products_bean(" silk saree", "PD101", "2000", R.drawable.order_pro_placeholder,"yellow,pure silk"));
        //itemData.add(new Products_bean("samsung s7 smart phone", "PD104", "2000", R.drawable.order_pro_placeholder,"silver"));
        //itemData.add(new Products_bean("iphone7", "PD104", "65000", R.drawable.order_pro_placeholder,"black"));
        return itemData;
    }

    public void initAppliancesData() {
        itemData.clear();
        itemData.add(new Products_bean("Mobile & Accessories", R.drawable.order_pro_placeholder, 1));
        itemData.add(new Products_bean("Home Entertainment", R.drawable.order_pro_placeholder, 1));
        itemData.add(new Products_bean("Small Home Aplliances", R.drawable.order_pro_placeholder, 1));
        itemData.add(new Products_bean("Refrigerators", R.drawable.order_pro_placeholder, 1));
        itemData.add(new Products_bean("Kitchen", R.drawable.order_pro_placeholder, 1));

    }

    public ArrayList<OrderHistorydata> initOrderList() {
        orderlist.add(new OrderHistorydata("ORD101234", "Pending", "10/01/2017", "12/01/2017", "JK Enterprises", "Geeta Bhavan,Indore"));
        orderlist.add(new OrderHistorydata("ORD101289", "Pending", "21/12/2016", "22/12/2016", "JK Enterprises", "Geeta Bhavan,Indore"));
        orderlist.add(new OrderHistorydata("ORD101778", "Delivered", "11/12/2016", "12/01/2017", "Surya Traders", "Geeta Bhavan,Indore"));
        orderlist.add(new OrderHistorydata("ORD101341", "Pending", "12/11/2016", "11/12/2016", "Surya Traders", "Geeta Bhavan,Indore"));
        orderlist.add(new OrderHistorydata("ORD101453", "Pending", "08/09/2016", "12/10/2016", "JK Enterprises", "Geeta Bhavan,Indore"));
        orderlist.add(new OrderHistorydata("ORD101874", "Dispatched", "19/08/2016", "22/09/2016", "K & K Enterprises", "Geeta Bhavan,Indore"));
        orderlist.add(new OrderHistorydata("ORD101234", "Pending", "18/08/2016", "24/09/2016", "K & K Enterprises", "Geeta Bhavan,Indore"));
        orderlist.add(new OrderHistorydata("ORD101289", "Pending", "10/07/2016", "12/07/2016", "JK Enterprises", "Geeta Bhavan,Indore"));
        orderlist.add(new OrderHistorydata("ORD101234", "Pending", "23/06/2016", "4/07/2016", "Surya Traders", "Geeta Bhavan,Indore"));
        orderlist.add(new OrderHistorydata("ORD101289", "Pending", "02/06/2016", "12/06/2016", "K & K Enterprises", "Geeta Bhavan,Indore"));

        return orderlist;
    }
    /*public ArrayList<Products_bean> initFavouritesData() {

        favouritseList.add(new Products_bean("Floral shirt", "PD101", "1700", R.drawable.order_pro_placeholder,"Black,xl"));
        favouritseList.add(new Products_bean("V neck Tshirt", "PD101", "1500", R.drawable.order_pro_placeholder,"Red,xxl"));
        favouritseList.add(new Products_bean("Denim jeans", "PD102", "4000", R.drawable.order_pro_placeholder,"blue,40"));
        favouritseList.add(new Products_bean("Cotton saree", "PD103", "2500", R.drawable.order_pro_placeholder,"pink,semi cotton"));
        favouritseList.add(new Products_bean("iphone7", "PD104", "65000", R.drawable.order_pro_placeholder,"black"));

        return favouritseList;
    }
    public ArrayList<Products_bean> initCartList() {

        cartlist.add(new Products_bean("V neck Tshirt", "PD101","", R.drawable.order_pro_placeholder,"Red,xxl"));
        cartlist.add(new Products_bean("Denim jeans", "PD102","",  R.drawable.order_pro_placeholder,"blue,40"));
        cartlist.add(new Products_bean("Floral shirt", "PD101","", R.drawable.order_pro_placeholder,"Black,xl"));
        cartlist.add(new Products_bean("iphone7", "PD104", "", R.drawable.order_pro_placeholder,"black"));
        return cartlist;
    }*/

    public ArrayList<NotificationData> initNotificationList() {

        notificationlist.add(new NotificationData("Order Dispatch", "Your Order(Order No. :OD1234) For fans has been dispatched.", "11/12/2016"));
        notificationlist.add(new NotificationData("Buying Request", "You requested for elle pen Packet,Available now", "1:30PM"));
        notificationlist.add(new NotificationData("Promotions and events", "Explore What new in Collection.", "2:00PM"));
        notificationlist.add(new NotificationData("Inquiries", "You were watching for Fossil m14 watch,available now", "4:36PM"));
        return notificationlist;
    }

    public HashMap<String, Integer> initFileMaps() {

        file_maps.put("Smartphones offers", R.drawable.mobile_sale);
        file_maps.put("clothing offers", R.drawable.clothing_offers);
        file_maps.put("Electronics Discount", R.drawable.electronics_offers);
        file_maps.put("Kitchen Utensils", R.drawable.kitchen_utensils);
        return file_maps;
    }


    public ArrayList<Products_bean> initorder_detail_data() {
        order_detail_list.add(new Products_bean("Floral Shirt", "PD101", "Blue", "XL", "Denim", 1, "1200"));
        order_detail_list.add(new Products_bean("V neck Tshirt", "PD101", "Red", "XL", "", 1, "720"));
        order_detail_list.add(new Products_bean("Denim jeans", "PD102", "Black", "40", "", 10, "1120"));
        order_detail_list.add(new Products_bean("Cotton saree", "PD103", "Black", "", "Semi_Cotton", 1, "1100"));
        order_detail_list.add(new Products_bean("iphone7 ", "PD104", "Silver", "", "", 1, "60000"));
        order_detail_list.add(new Products_bean("Printed blue shirt", "PD101", "pink", "L", "semi cotton", 1, "230"));
        order_detail_list.add(new Products_bean("Rounded neck Tshirt", "PD101", "gray", "xxl", "", 1, "120"));
        return order_detail_list;
    }

}
