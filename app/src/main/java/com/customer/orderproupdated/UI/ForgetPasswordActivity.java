package com.customer.orderproupdated.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.ForgetPasswordResponse;
import com.customer.orderproupdated.bean.LoginResponse;
import com.customer.orderproupdated.chat.utility.Chatutility;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sony on 2/27/2017.
 */
public class ForgetPasswordActivity extends AppCompatActivity {

    EditText et_email;
    TextView btn_Submit;
    String email;
    LinearLayout back_btn;

    SharedPrefrence share;
    ApiInterface apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_forgetpassword);
        share = SharedPrefrence.getInstance(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        //find id
        et_email = (EditText) findViewById(R.id.input_email);
        btn_Submit = (TextView) findViewById(R.id.submit_btn);
        back_btn = (LinearLayout) findViewById(R.id.back_btn);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void getData() {
        if (Utility.checkInternetConn(this)) {

            email = et_email.getText().toString().trim();

            if (email.length() == 0) {
                DialogUtility.showToast(ForgetPasswordActivity.this, "Please enter email address");
            }  else if (!isValidEmail(email)) {
                DialogUtility.showToast(ForgetPasswordActivity.this, "Please enter correct email");
            } else {
                try {
                    DialogUtility.showProgressDialog(this, true, getString(R.string.please_wait));
                    Call<ForgetPasswordResponse> call = apiService.getForgetPasswordResponse(email);
                    call.enqueue(new Callback<ForgetPasswordResponse>() {
                        @Override
                        public void onResponse(Call<ForgetPasswordResponse> call, Response<ForgetPasswordResponse> response) {

                            if (response.body() != null) {
                                Log.e("GetLogin response", "" + response);

                                if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {

                                    DialogUtility.showToast(ForgetPasswordActivity.this,response.body().getMessage());
                                    DialogUtility.pauseProgressDialog();
                                    et_email.setText("");
                                    finish();
                                } else if(response.body().getStatus().equals(PreferenceConstant.FAIL_STATUS))
                                {
                                    DialogUtility.showToast(ForgetPasswordActivity.this,"Entered email is invalid");
                                    DialogUtility.pauseProgressDialog();

                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {
                            DialogUtility.showToast(ForgetPasswordActivity.this, getResources().getString(R.string.request_fail));
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

    }//getdata

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
