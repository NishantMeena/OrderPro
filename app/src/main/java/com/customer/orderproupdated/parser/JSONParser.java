package com.customer.orderproupdated.parser;

import android.content.Context;


import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONParser {

    public Context context;
    public String jsonObjResponse;
    public JSONObject jObj;
    public SharedPrefrence share;
    public String STATUS = "";
    public String MESSAGE = "";
    public String RESULT = "";
    public String URL = "";

    public String TAG = "";
    public String MOBILE = "";

    public boolean VERIFY;





    String attribute_id;
    String attribute_name;
    String attribute_value;



    public JSONParser(Context context, String response) {
        this.context = context;
        this.jsonObjResponse = response;
        share = SharedPrefrence.getInstance(context);
            try {
            jObj = new JSONObject(response);
            MESSAGE = getJsonString(jObj, "message");
            STATUS = getJsonString(jObj, "status");
            TAG = getJsonString(jObj, "tag");
            RESULT = getJsonString(jObj, "result");
                URL = getJsonString(jObj, "url");

                if (STATUS.equals(PreferenceConstant.PASS_STATUS)) {
                VERIFY = true;
            } else {
                VERIFY = false;
            }
        } catch (JSONException e) {
            jObj = null;
            // e.printStackTrace();
        }
    }


    public JSONParser(String response) {
        this.context = context;
        this.jsonObjResponse = response;
        try {
            jObj = new JSONObject(response);
            MESSAGE = getJsonString(jObj, "message");
            STATUS = getJsonString(jObj, "status");
            TAG = getJsonString(jObj, "tag");
            MOBILE = getJsonString(jObj, "mobile");
        } catch (JSONException e) {
            jObj = null;
            e.printStackTrace();
        }
    }


    public static JSONObject getJsonObject(JSONObject obj, String parameter) {
        try {
            return obj.getJSONObject(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }

    }

    public static String getJsonString(JSONObject obj, String parameter) {
        try {
            return obj.optString(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static Integer getJsonInteger(JSONObject obj, String parameter) {
        try {
            return obj.optInt(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
    public static JSONArray getJsonArray(JSONObject obj, String parameter) {
        try {
            return obj.getJSONArray(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }

    }




    public String getUrlFromResponse() {

        return getJsonString(jObj, "url");
    }

    public void CreateJsonToPlaceOrder()
    {

    }

    /*public void getCategeoryLst(Context ctx) {
        try {

            JSONArray categeories = getJsonArray(jObj, "category");

            ArrayList<CategoryBean> list_Category = new ArrayList<CategoryBean>();

            ArrayList<SubCategory> list_subCategory = new ArrayList<SubCategory>();

            ArrayList<ProdVal> list_products = new ArrayList<ProdVal>();

            ArrayList<Product_attribute> list_prodAttribute = new ArrayList<Product_attribute>();



            for (int i = 0; i < categeories.length(); i++) {

                JSONObject arr = categeories.getJSONObject(i);





                Integer category_id  = getJsonInteger(arr,"category_id");
                String category_name  = getJsonString(arr,"category_name");
                Integer depth_zero = getJsonInteger(arr,"depth");
                JSONArray subCategeories =getJsonArray(arr,"sub_catg");
                JSONArray prodVal=getJsonArray(arr,"prod_val");


-----------------------------Categeories------------------------------------


                for(int j = 0; j < subCategeories.length(); j++)
                {
                    JSONObject subCategeory_arr = subCategeories.getJSONObject(j);

                     Integer  sub_category_id_top=getJsonInteger(subCategeory_arr,"sub_category_id");
                     String sub_category_name_top=getJsonString(subCategeory_arr,"sub_category_name");
                     Integer depth_one= getJsonInteger(subCategeory_arr,"depth");
                     subCategeories =getJsonArray(subCategeory_arr,"sub_catg");

                        for(int k= 0; k < subCategeories.length(); k++)
                        {
                            JSONObject product = subCategeories.getJSONObject(k);


                            Integer sub_category_id=getJsonInteger(product,"sub_category_id");
                            String  sub_category_name=getJsonString(product,"sub_category_name");
                            Integer depth_two = getJsonInteger(product,"depth");

                            SubCategory mSubCategory_level_two=new SubCategory();
                            mSubCategory_level_two.setSub_category_id(sub_category_id);
                            mSubCategory_level_two.setSub_category_name(sub_category_name);
                            mSubCategory_level_two.setDepth(depth_two);
                            list_subCategory.add(mSubCategory_level_two);

                        }

                    SubCategory mSubCategory=new SubCategory();
                    mSubCategory.setSub_category_id(sub_category_id_top);
                    mSubCategory.setSub_category_name(sub_category_name_top);
                    mSubCategory.setDepth(depth_one);
                    mSubCategory.setSubCatg(list_subCategory);

                    list_subCategory.add(mSubCategory);

                }
-----------------------------Products------------------------------------

                        for(int m = 0; m < prodVal.length(); m++){

                            JSONObject product = prodVal.getJSONObject(m);







                            String product_id=getJsonString(product,"product_id");
                            String product_name=getJsonString(product,"product_name");
                            String price = getJsonString(product,"price");
                            String image=getJsonString(product,"image");
                            JSONArray product_attribute  =getJsonArray(product,"product_attribute");


                            for(int n= 0; n < product_attribute.length(); n++)
                            {
                                JSONObject productAttribute = product_attribute.getJSONObject(n);

                                attribute_id=getJsonString(productAttribute,"attribute_id");
                                attribute_name=getJsonString(productAttribute,"attribute_name");
                                attribute_value = getJsonString(productAttribute,"attribute_value");

                                Product_attribute mproduct_attribute=new Product_attribute();
                                mproduct_attribute.setAttribute_id(attribute_id);
                                mproduct_attribute.setAttribute_name(attribute_name);
                                mproduct_attribute.setAttribute_value(attribute_value);

                                list_prodAttribute.add(mproduct_attribute);

                            }
                            ProdVal prodVal_obj=new ProdVal();
                            prodVal_obj.setProductId(product_id);
                            prodVal_obj.setProductName(product_name);
                            prodVal_obj.setPrice(price);
                            prodVal_obj.setImage(image);
                            prodVal_obj.setProductAttribute(list_prodAttribute);


                            list_products.add(prodVal_obj);


                        }

                CategoryBean mCategoryBean=new CategoryBean();
                mCategoryBean.setCategoryId(category_id);
                mCategoryBean.setCategoryName(category_name);
                mCategoryBean.setDepth(depth_zero);
               // mCategoryBean.setSubCatg(list_subCategory);
               // mCategoryBean.setProdVal(list_products);


                list_Category.add(mCategoryBean);


            }
                    try{
                        share.setMainCategoryList(SharedPrefrence.CATEGORY_LIST,list_Category);

                      //  share.setSubCategoryList(SharedPrefrence.SUBCATEGORY_LIST,list_subCategory);

                      //  share.setProductValList(SharedPrefrence.PRODUCTVAL_LIST,list_products);

                       // share.setProductAttributeList(SharedPrefrence.PRODUCT_ATTRIBUTE_LIST,list_prodAttribute);


                    }catch(Exception e){
                        e.printStackTrace();
                    }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

*/
}
