package com.customer.orderproupdated.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.ChangePasswordData;
import com.customer.orderproupdated.chat.rooster.RoosterConnectionService;
import com.customer.orderproupdated.chat.rooster.XmppConnect;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;

import org.jivesoftware.smack.SmackException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by AKASH on 08-Sep-16.
 */
public class ChangePasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText etOldPassword;
    EditText etConfirmPassword;
    EditText etNewPassword;
    Button btnSubmit;
    SharedPrefrence share;
    ImageView iv_back;

    String shPass;
    String email;
    String newpassword;
    ApiInterface apiService;
    String comes_from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_changepassword);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        share = SharedPrefrence.getInstance(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        comes_from = getIntent().getStringExtra("comes_from");
        etConfirmPassword = (EditText) findViewById(R.id.input_confirmpassword);
        etOldPassword = (EditText) findViewById(R.id.input_oldpassword);
        etNewPassword = (EditText) findViewById(R.id.input_newpassword);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        if (comes_from.equals("login")) {
            etOldPassword.setFocusable(false);

            etOldPassword.setText(share.getValue(SharedPrefrence.PASSWORD));
            etOldPassword.setVisibility(View.GONE);

        } else {
            etOldPassword.setFocusable(true);
            etOldPassword.setVisibility(View.VISIBLE);

        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (comes_from.equals("login")) {

                    if (etOldPassword.length() > 0 && etNewPassword.length() > 0 && etConfirmPassword.length() > 0) {
                        shPass = share.getValue(SharedPrefrence.PASSWORD);
                        email = share.getValue(SharedPrefrence.EMAIL);
                        newpassword = etNewPassword.getText().toString().trim();

                        if (etNewPassword.length() < 6) {
                            DialogUtility.showToast(ChangePasswordActivity.this, "Password must be atleast 6 digits");
                        } else if (!etConfirmPassword.getText().toString().equals(etNewPassword.getText().toString())) {
                            DialogUtility.showToast(ChangePasswordActivity.this, "Please enter Correct Password");
                        } else {
                            changePassword();
                        }
                    } else {
                        DialogUtility.showToast(ChangePasswordActivity.this, "Enter all fields");
                    }
                } else {

                    if (etOldPassword.length() > 0 && etNewPassword.length() > 0 && etConfirmPassword.length() > 0) {
                        shPass = share.getValue(SharedPrefrence.PASSWORD);
                        email = share.getValue(SharedPrefrence.EMAIL);
                        newpassword = etNewPassword.getText().toString().trim();

                        if (etNewPassword.length() < 6) {
                            DialogUtility.showToast(ChangePasswordActivity.this, "Password must be atleast 6 digits");
                        } else if (!etConfirmPassword.getText().toString().equals(etNewPassword.getText().toString())) {
                            DialogUtility.showToast(ChangePasswordActivity.this, "Please enter Correct Password");
                        } else {

                            changePassword();


                        }
                    } else {
                        DialogUtility.showToast(ChangePasswordActivity.this, "Enter all fields");
                    }
                }



            }
        });
    }

    public void changePassword() {
        if (Utility.checkInternetConn(this)) {
            try {
                DialogUtility.showProgressDialog(this, true, getString(R.string.please_wait));
                Call<ChangePasswordData> call = apiService.getChangePasswordResponse(email, shPass, newpassword);
                call.enqueue(new Callback<ChangePasswordData>() {
                    @Override
                    public void onResponse(Call<ChangePasswordData> call, Response<ChangePasswordData> response) {

                        if (response.body() != null) {
                            Log.e("GetChangePass response", "" + response);
                            if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {
                                if (comes_from.equals("login")){
                                    DialogUtility.showToast(ChangePasswordActivity.this, response.body().getMessage());


                                    Intent i1 = new Intent(ChangePasswordActivity.this, RoosterConnectionService.class);
                                    stopService(i1);
                                    try {
                                        if (XmppConnect.mConnection.isConnected())
                                            XmppConnect.mConnection.disconnect();
                                    } catch (SmackException.NotConnectedException e) {
                                        e.printStackTrace();
                                    }
                                    deleteDatabase("orderpro.db");
                                    share.clearSharedPrefernces(ChangePasswordActivity.this);
                                    PreferenceConstant.CART_COUNT = 0;
                                    Intent intent = new Intent(ChangePasswordActivity.this, SignInActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    finish();

                                }else {
                                    DialogUtility.showToast(ChangePasswordActivity.this, response.body().getMessage());
                                    finish();

                                }


                            } else if (response.body().getStatus().equals(PreferenceConstant.FAIL_STATUS)) {
                                DialogUtility.showToast(ChangePasswordActivity.this, response.body().getMessage());
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ChangePasswordData> call, Throwable t) {
                        DialogUtility.showToast(ChangePasswordActivity.this, getResources().getString(R.string.request_fail));
                        DialogUtility.pauseProgressDialog();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DialogUtility.showMaterialDialog(this, getString(R.string.alert), getString(R.string.checknet));
        }
    }
}

