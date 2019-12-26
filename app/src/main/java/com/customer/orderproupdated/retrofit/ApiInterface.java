package com.customer.orderproupdated.retrofit;


import com.customer.orderproupdated.bean.AddMerchantResponse;
import com.customer.orderproupdated.bean.ChangePasswordData;
import com.customer.orderproupdated.bean.ForgetPasswordResponse;
import com.customer.orderproupdated.bean.GetAllMerchantList;
import com.customer.orderproupdated.bean.GetMerchantList;
import com.customer.orderproupdated.bean.LoginResponse;
import com.customer.orderproupdated.bean.Logout;
import com.customer.orderproupdated.bean.MainCategoryBean;
import com.customer.orderproupdated.bean.MyOrderResponse;
import com.customer.orderproupdated.bean.PlaceOrderResponse;
import com.customer.orderproupdated.bean.RegistrationResponse;
import com.customer.orderproupdated.bean.StateListResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("register_customer.php")
    Call<RegistrationResponse> getRegistrationResponse(@Field("first_name") String firstName, @Field("last_name") String lastName, @Field("customer_name") String customer_name, @Field("email") String email, @Field("password") String password, @Field("mobile") String mobile, @Field("device_token")
            String device_token, @Field("device_id") String device_id, @Field("device_type") String device_type);

    @FormUrlEncoded
    @POST("customer_login.php")
    //https://36.255.3.15/order_p/API/customer_login.php
    Call<LoginResponse> getLoginResponse(@Field("email") String email, @Field("password") String password, @Field("device_token") String device_token, @Field("device_id") String device_id, @Field("device_type") String device_type);

    @FormUrlEncoded
    @POST("list_merchant_customerwise.php")
    Call<GetMerchantList> getMerchantList(@Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST("check_customer_to_merchant_request.php")
    Call<AddMerchantResponse> getAddMerchantResponse(@Field("customer_id") String customerId, @Field("merchant_email") String merchant_email);

    @FormUrlEncoded
    @POST("list_merchant.php")
    Call<GetAllMerchantList> getAllMerchantList(@Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST("change_password_customer.php")
    Call<ChangePasswordData> getChangePasswordResponse(@Field("email") String email, @Field("current_password") String current_password, @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST("place_order.php")
    Call<PlaceOrderResponse> getPlaceOrderResponse(@Field("order_json") String PlaceOrderJson);

    /* @FormUrlEncoded
        @POST("get_merchantwise_product_list_v2.php")
        Call<MainCategoryBean> getCategeoryProductList(@Field("merchant_id") String merchnatID,@Field("product_version") String product_version);
    */
    @FormUrlEncoded
    @POST("get_merchantwise_product_list.php")
    Call<MainCategoryBean> getCategeoryProductList(@Field("merchant_id") String merchnatID,@Field("customer_id") String id);

    @FormUrlEncoded
    @POST("place_order_list_customerwise.php")
    Call<MyOrderResponse> getMyOrderList(@Field("merchant_id") String merchnatID, @Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST("forgot_password_customer.php")
    Call<ForgetPasswordResponse> getForgetPasswordResponse(@Field("email") String email);


    @FormUrlEncoded
    @POST("list_state.php")
    Call<StateListResponse> getStateListResponse(@Field("country_id") String country_id);

    // Logout...
    @FormUrlEncoded
    @POST("customer_logout.php")
    Call<Logout> customerLogout(@Field("email") String email, @Field("device_token")
            String device_token, @Field("device_id") String device_id, @Field("device_type") String device_type);


}