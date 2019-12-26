package com.customer.orderproupdated.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.Banner;
import com.customer.orderproupdated.bean.LoginResponse;
import com.customer.orderproupdated.bean.MerchantList_LoginResponse;
import com.customer.orderproupdated.chat.rooster.RoosterConnectionService;
import com.customer.orderproupdated.chat.utility.Chatutility;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_signUp;
    EditText et_username;
    EditText et_password;
    TextView btn_signin, tv_forget_password;
    SharedPrefrence share;
    String email = "", password = "", device_type = "", device_token = "", device_id = "", xmpp_username = "", xmpp_password = "";
    ApiInterface apiService;
    String customerID = "", first_name = "", last_name = "", mob_number = "", address = "", state = "", city = "", pincode = "";
    private List<MerchantList_LoginResponse> merchant_list_added = new ArrayList<>();
    private ArrayList<Banner> list_banner_default = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_signin);
        share = SharedPrefrence.getInstance(this);
        methodForchanegeGet();
        apiService = ApiClient.getClient().create(ApiInterface.class);
        tv_signUp = (TextView) findViewById(R.id.tv_signup);
        et_username = (EditText) findViewById(R.id.input_name);
        et_password = (EditText) findViewById(R.id.input_email);
        btn_signin = (TextView) findViewById(R.id.signin_btn);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        tv_signUp.setOnClickListener(this);
        btn_signin.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        /* et_username.setText("nupurjain15893@gmail.com");
           et_password.setText("123456"); */
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signup:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
                break;

            case R.id.signin_btn:
                signIn();
                break;

            case R.id.tv_forget_password:
                forgetPassword();
                break;
        }
    }

    public void signIn() {
        getData();
    }

    public void getData() {
        if (Utility.checkInternetConn(this)) {
            email = et_username.getText().toString().trim();
            password = et_password.getText().toString().trim();
            device_id = Chatutility.getDeviceToken(this);
            device_token = Utility.getSharedPreferences(this, "fcm_token");
            device_type = "android";
            if (email.length() == 0) {
                DialogUtility.showToast(SignInActivity.this, "Please enter email address");
            } else if (password.length() == 0) {
                DialogUtility.showToast(SignInActivity.this, "Please enter password");
            } else if (!isValidEmail(email)) {
                DialogUtility.showToast(SignInActivity.this, "Please enter correct email");
            } else {
                try {
                    DialogUtility.showProgressDialog(this, true, getString(R.string.please_wait));
                    Call<LoginResponse> call = apiService.getLoginResponse(email, password, device_token, device_id, device_type);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.body() != null) {
                                Log.e("GetLogin response", "" + response);
                                xmpp_username = response.body().getUsername();
                                xmpp_password = response.body().getPassword();
                                if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {
                                    if (response.body().getIs_approved().equals("1")) {
                                        customerID = response.body().getCustomerId();
                                        first_name = response.body().getFirstname();
                                        last_name = response.body().getLastname();
                                        mob_number = response.body().getContactno();
                                        address = response.body().getAddress();
                                        pincode = response.body().getPincode();
                                        city = response.body().getCity();
                                        state = response.body().getState();

                                        if (response.body().getBanner() != null) {
                                            list_banner_default = new ArrayList<>();
                                            list_banner_default.addAll(response.body().getBanner());
                                            share.setBannerList(SharedPrefrence.LIST_BANNER_DEFAULT, list_banner_default);
                                            share.setBooleanValue(SharedPrefrence.IS_MERCHANT_SWITCHED, false);
                                            Log.e("Banner list size", "" + list_banner_default.size());
                                        }

                                        if (response.body().getMerchantList() != null) {
                                            merchant_list_added = new ArrayList<>();
                                            merchant_list_added.addAll(response.body().getMerchantList());
                                            share.setMerchantList_added(SharedPrefrence.MERCHANT_LIST_ADDED, merchant_list_added);
                                            Log.e("merchant list size", "" + merchant_list_added.size());
                                        }
                                        onResponseSuccess();
                                    } else {
                                        share.setValue(SharedPrefrence.EMAIL, email);
                                        share.setValue(SharedPrefrence.PASSWORD, et_password.getText().toString().trim());
                                        xmmpplogin();
                                        share.setValue(SharedPrefrence.CUSTOMERID, response.body().getCustomerId());
                                        startActivity(new Intent(SignInActivity.this, ChangePasswordActivity.class)
                                                .putExtra("comes_from", "login"));
                                    }

                                } else if (response.body().getStatus().equals(PreferenceConstant.FAIL_STATUS)) {
                                    onResponseFail(response.body().getMessage());
                                } else if (response.body().getStatus().equals("2")) {
                                    Toast.makeText(SignInActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            DialogUtility.showToast(SignInActivity.this, getResources().getString(R.string.request_fail));
                            DialogUtility.pauseProgressDialog();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            DialogUtility.showMaterialDialog(this, getString(R.string.alert), getString(R.string.checknet));
        }
    }

    public void onResponseFail(String message) {
        if (message.equals("null")) {
            DialogUtility.showToast(SignInActivity.this, "Enter valid Email id and password");
        }
        DialogUtility.showToast(SignInActivity.this, message);
        DialogUtility.pauseProgressDialog();

    }

    public void onResponseSuccess() {
        share.setValue(SharedPrefrence.EMAIL, email);
        share.setValue(SharedPrefrence.PASSWORD, et_password.getText().toString().trim());
        share.setBooleanValue(SharedPrefrence.IS_USER_LOGIN, true);

        share.setValue(SharedPrefrence.CUSTOMERID, customerID);
        share.setValue(SharedPrefrence.FIRST_NAME, first_name);
        share.setValue(SharedPrefrence.LAST_NAME, last_name);
        share.setValue(SharedPrefrence.ADDRESS, address);
        share.setValue(SharedPrefrence.STATE, state);
        share.setValue(SharedPrefrence.CITY, city);
        share.setValue(SharedPrefrence.PINCODE, pincode);
        share.setValue(SharedPrefrence.MOB_NUMBER, mob_number);
        xmmpplogin();

        DialogUtility.showToast(SignInActivity.this, "Login Successfully!");

        Intent i = new Intent(getApplicationContext(), com.customer.orderproupdated.UI.HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();

        DialogUtility.pauseProgressDialog();


    }

    private void xmmpplogin() {
        share.setValue(SharedPrefrence.JID, xmpp_username + PreferenceConstant.SERVERATTHERSTE);
        share.setValue(SharedPrefrence.XMPPPASSWORD, xmpp_password);
        share.setValue(SharedPrefrence.XMPP_USER_NAME, xmpp_username);
        share.setValue(SharedPrefrence.IS_XMPP_LOGIN, "true");

        Intent i1 = new Intent(SignInActivity.this, RoosterConnectionService.class);
        startService(i1);

    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void forgetPassword() {
        Intent in = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
        startActivity(in);
    }

    private void methodForchanegeGet() {
        if (Utility.getSharedPreferences(this, "fcm_token").equals("")) {
            String token = FirebaseInstanceId.getInstance().getToken();
            System.out.println("token is" + token);
            Utility.setSharedPreference(this, "fcm_token", token);
        }
    }

}