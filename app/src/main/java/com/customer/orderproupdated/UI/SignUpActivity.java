package com.customer.orderproupdated.UI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.RegistrationResponse;
import com.customer.orderproupdated.chat.utility.Chatutility;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout back_btn;
    Button btn_signup;
    SharedPrefrence share;
    ApiInterface apiService;
    TextView tv_signIn;
    EditText et_firstname, et_lastname;
    EditText et_email;
    EditText et_password;
    EditText et_confirm_password;
    EditText et_number;
    String username, email, password, confirm_password, number, xmpp_username, xmpp_password;
    String device_id = "", device_token = "", device_type = "";
    String firstname = "", last_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_sign_up);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        back_btn = (RelativeLayout) findViewById(R.id.back_btn);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        tv_signIn = (TextView) findViewById(R.id.tv_signIn);
        et_firstname = (EditText) findViewById(R.id.input_firstname);
        et_lastname = (EditText) findViewById(R.id.input_lastname);
        et_email = (EditText) findViewById(R.id.input_email);
        et_password = (EditText) findViewById(R.id.input_password);
        et_confirm_password = (EditText) findViewById(R.id.input_confirm_password);
        et_number = (EditText) findViewById(R.id.input_number);
        share = SharedPrefrence.getInstance(this);
        back_btn.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        tv_signIn.setOnClickListener(this);
        methodForchanegeGet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                Intent in = new Intent(this, SignInActivity.class);
                startActivity(in);
                finish();
                break;
            case R.id.btn_signup:
                signup();
                // startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                break;
            case R.id.tv_signIn:
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(this, SignInActivity.class);
        startActivity(in);
        finish();
    }

    public void signup() {
        getData();
    }

    public void getData() {
        if (Utility.checkInternetConn(this)) {
            firstname = et_firstname.getText().toString().trim();
            last_name = et_lastname.getText().toString().trim();
            username = firstname + last_name;
            email = et_email.getText().toString().trim();
            password = et_password.getText().toString().trim();
            number = et_number.getText().toString().trim();
            confirm_password = et_confirm_password.getText().toString().trim();
            device_id = Chatutility.getDeviceToken(this);
            device_type = "android";
            device_token = Utility.getSharedPreferences(this, "fcm_token");
            if (firstname.length() == 0) {
                DialogUtility.showToast(SignUpActivity.this, "Please enter FirstName");
            } else if (last_name.length() == 0) {
                DialogUtility.showToast(SignUpActivity.this, "Please enter LastName");
            } else if (email.length() == 0) {
                DialogUtility.showToast(SignUpActivity.this, "Please enter Email");
            } else if (password.length() == 0) {
                DialogUtility.showToast(SignUpActivity.this, "Please enter Password");
            } else if (confirm_password.length() == 0) {
                DialogUtility.showToast(SignUpActivity.this, "Please enter Confirm Password");
            } else if (number.length() == 0) {
                DialogUtility.showToast(SignUpActivity.this, "Please enter Mobile Number");
            } else if (!isValidEmail(email)) {
                DialogUtility.showToast(SignUpActivity.this, "Please enter correct Email");
            } else if (password.length() < 6) {
                DialogUtility.showToast(SignUpActivity.this, "Password must be of atleast 6 characters");
            } else if (!password.equals(confirm_password)) {
                DialogUtility.showToast(SignUpActivity.this, "Password not matched please Re-enter");
            } else if (!isValidEmail(email)) {
                DialogUtility.showToast(SignUpActivity.this, "Please enter correct Email");
            } else if (!validCellPhone(number)) {
                DialogUtility.showToast(SignUpActivity.this, "Please enter correct Mobile Number");
            } else {
                try {
                    DialogUtility.showProgressDialog(this, true, getString(R.string.please_wait));
                    Call<RegistrationResponse> call = apiService.getRegistrationResponse(firstname, last_name, username, email, password, number, device_token, device_id, device_type);
                    call.enqueue(new Callback<RegistrationResponse>() {
                        @Override
                        public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                            try {
                                int code = response.code();
                                /* Response{protocol=http/1.0, code=500, message=Internal Server Error, url=https://36.255.3.15/order_p/API/register_customer.php}*/
                                if (code == 500) {
                                    Toast.makeText(SignUpActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                                    DialogUtility.pauseProgressDialog();
                                } else {
                                    if (response != null) {
                                        String status = String.valueOf(response.body().getStatus());
                                        int statusCode = Integer.parseInt(status);
                                        if (statusCode == PreferenceConstant.PASS_STATUS) {
                                            onResponseSuccess();
                                            DialogUtility.pauseProgressDialog();
                                        } else if (response.body().getStatus().equals(PreferenceConstant.FAIL_STATUS)) {
                                            onResponseFail(response.body().getMessage());
                                            DialogUtility.pauseProgressDialog();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                            DialogUtility.showToast(SignUpActivity.this, getResources().getString(R.string.request_fail));
                            DialogUtility.pauseProgressDialog();
                        }
                    });

                    // DialogUtility.showToast(SignUpActivity.this, getResources().getString(R.string.request_fail));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            DialogUtility.showMaterialDialog(this, getString(R.string.alert), getString(R.string.checknet));
        }
    }

    public void onResponseFail(String message) {
        DialogUtility.pauseProgressDialog();
        DialogUtility.showToast(SignUpActivity.this, message);
    }

    public void onResponseSuccess() {
        // share.setValue(SharedPrefrence.XMPP_USER_NAME, xmpp_username);
        // share.setValue(SharedPrefrence.XMPPPASSWORD, xmpp_password);
        DialogUtility.showToast(SignUpActivity.this, "Registered Successfully!");
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean validCellPhone(String number) {
        String expression = "^([+][9][1]|[9][1]|[0]){0,1}([7-9]{1})([0-9]{9})$";
        CharSequence inputString = number;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private void methodForchanegeGet() {
        if (Utility.getSharedPreferences(this, "fcm_token").equals("")) {
            String token = FirebaseInstanceId.getInstance().getToken();
            System.out.println("token is" + token);
            Utility.setSharedPreference(this, "fcm_token", token);
        }
    }
}