package com.customer.orderproupdated.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.RecyclerItemClickListener;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.adapter.AllMerchantAdapter;
import com.customer.orderproupdated.bean.AddMerchantResponse;
import com.customer.orderproupdated.bean.AllMerchantDetails;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddMerchantActivity extends Activity {
    SharedPrefrence share;

    EditText edit_add_merchnat;
    String merchantEmail = "", customerID = "",requestStatus="";
    ApiInterface apiService;
    public RecyclerView mRecyclerView;
    AllMerchantAdapter mAdapter;
    ArrayList<AllMerchantDetails> merchantList;
    Button btn_add;
    View lastView;
    Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        share = SharedPrefrence.getInstance(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        //dialog
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        setContentView(R.layout.ui_add_merchant);


        //------------------------setmaxheight-------------------------------------------------

       /* WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);


        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);


        //here you can get height of the device.
        Log.d("check", metrics.heightPixels+"");
        if(metrics.heightPixels< 200)
        {
            this.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);

        }
        else {
            this.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT,300);

        }*/
//-------------------------------------------------------------------------
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_merchant);

        edit_add_merchnat = (EditText) findViewById(R.id.edit_add_merchnat);
        btn_add = (Button) findViewById(R.id.btn_add);

        //getMerchantList();
        merchantList = share.getAllMerchantList(SharedPrefrence.All_MERCHANT_LIST);

       mAdapter=new AllMerchantAdapter(AddMerchantActivity.this, merchantList);
        //mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AddMerchantActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(AddMerchantActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {

                        if (merchantList.size() > 0) {
                            merchantEmail = merchantList.get(position).getEmail();
                            requestStatus=merchantList.get(position).getRequestStatus();
                        }

                        //list item color selector
                        if(lastView==null){
                            v.setBackgroundColor(getResources().getColor(R.color.listselector));

                            lastView=v;
                            edit_add_merchnat.setText(merchantEmail);

                        }
                        else{
                            lastView.setBackgroundColor(Color.WHITE);
                            v.setBackgroundColor(getResources().getColor(R.color.listselector));
                            lastView=v;
                            edit_add_merchnat.setText(merchantEmail);
                        }
                    }
                })
        );


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                merchantEmail=edit_add_merchnat.getText().toString().trim();
                sendRequestToMerchant(merchantEmail);
            }
        });
/*
        edit_add_merchnat.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                try {
                    if (edit_add_merchnat.getText().toString() != null) {
                        String txt = edit_add_merchnat.getText().toString().trim().toLowerCase();
                        mAdapter.getFilter(txt);
                        mRecyclerView.setAdapter(mAdapter);
                        final int maxTextLength = 40;
                        if (arg3 == maxTextLength) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edit_add_merchnat.getWindowToken(), 0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {


                if (edit_add_merchnat.getText().length() == 0) {


                }
            }

        });
*/

    }


    public void sendRequestToMerchant(String merchantEmail) {
        if (Utility.checkInternetConn(this)) {
            {
                customerID = share.getValue(SharedPrefrence.CUSTOMERID);
                if (merchantEmail.length() == 0) {
                    DialogUtility.showToast(AddMerchantActivity.this,"Please enter email");
                }
                else if(!isValidEmail(merchantEmail))
                {
                    DialogUtility.showToast(AddMerchantActivity.this,"Enter valid email");
                }
                else {
                    try {
                        DialogUtility.showProgressDialog(this, true, getString(R.string.please_wait));
                        Call<AddMerchantResponse> call = apiService.getAddMerchantResponse(customerID, merchantEmail);
                        call.enqueue(new Callback<AddMerchantResponse>() {
                            @Override
                            public void onResponse(Call<AddMerchantResponse> call, Response<AddMerchantResponse> response) {
                                if (response.body() != null) {
                                    Log.e("GetAddMerchant response", "" + response);

                                    if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {
                                        onResponseSuccessOrfail(response.body().getMessage());
                                    } else if (response.body().getStatus().equals(PreferenceConstant.FAIL_STATUS)) {
                                        onResponseSuccessOrfail(response.body().getMessage());

                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<AddMerchantResponse> call, Throwable t) {
                                DialogUtility.showToast(AddMerchantActivity.this, getResources().getString(R.string.request_fail));
                                DialogUtility.pauseProgressDialog();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } else {
            DialogUtility.showMaterialDialog(this, getString(R.string.alert), getString(R.string.checknet));

        }
    }

    public void onResponseSuccessOrfail(String message) {
        DialogUtility.showToast(AddMerchantActivity.this,message);
        DialogUtility.pauseProgressDialog();
        finish();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();

            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }
    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }



}

