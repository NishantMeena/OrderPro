package com.customer.orderproupdated.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.adapter.StatesAdapter;
import com.customer.orderproupdated.bean.StateDetails;
import com.customer.orderproupdated.bean.StateListResponse;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class

MyProfileActivity extends AppCompatActivity {
    SharedPrefrence share;
    ImageView iv_back;
    TextView name;
    EditText et_first_name,et_last_name,et_number,et_email,et_address,et_pincode;
    Button btn_submit;
    ImageView img_edit;
    Spinner spinner_city,spinner_state;
    SharedPrefrence sharedPrefrence;
    String first_name="",last_name="",mob_number="",address="",state="",city="",pincode="",email="",state_id="";
    ApiInterface apiInterface;
    String countryID="",stateID="";
    StatesAdapter statesAdapter;
    ArrayList<StateDetails> statesList ;
    LinearLayout ll_city_et,ll_state_et,ll_state,ll_city;
    EditText et_city,et_state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_myprofile_new);
        share = SharedPrefrence.getInstance(this);
        apiInterface= ApiClient.getClient().create(ApiInterface.class);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        name = (TextView) findViewById(R.id.tv_name);

        et_first_name = (EditText) findViewById(R.id.et_first_name);
        et_last_name = (EditText) findViewById(R.id.et_last_name);
        et_number = (EditText) findViewById(R.id.et_number);
        et_email = (EditText) findViewById(R.id.et_email);
        et_address = (EditText) findViewById(R.id.et_address);
        et_pincode = (EditText) findViewById(R.id.et_pincode);
        img_edit = (ImageView) findViewById(R.id.img_edit);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);

        ll_city_et=(LinearLayout)findViewById(R.id.ll_city_et);
        ll_state_et=(LinearLayout)findViewById(R.id.ll_state_et);
        ll_state=(LinearLayout)findViewById(R.id.ll_state);
        ll_city=(LinearLayout)findViewById(R.id.ll_city);

        et_city=(EditText) findViewById(R.id.et_city);
        et_state=(EditText) findViewById(R.id.et_state);

        ll_city_et.setVisibility(View.VISIBLE);
        ll_state_et.setVisibility(View.VISIBLE);


        statesList = new ArrayList<StateDetails>();
        displaySpinnerStates();


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_edit.setVisibility(View.INVISIBLE);
                iv_back.setVisibility(View.VISIBLE);

                et_first_name.setFocusable(true);
                et_first_name.setFocusableInTouchMode(true);
                et_first_name.setClickable(true);
                et_first_name.setCursorVisible(true);

                et_last_name.setFocusable(true);
                et_last_name.setFocusableInTouchMode(true);
                et_last_name.setClickable(true);
                et_last_name.setCursorVisible(true);

                et_email.setFocusable(false);
                et_email.setFocusableInTouchMode(true);
                et_email.setClickable(true);
                et_email.setCursorVisible(false);

                et_number.setFocusable(true);
                et_number.setFocusableInTouchMode(true);
                et_number.setClickable(true);
                et_number.setCursorVisible(true);

                et_address.setFocusable(true);
                et_address.setFocusableInTouchMode(true);
                et_address.setClickable(true);
                et_address.setCursorVisible(true);

                et_pincode.setFocusable(true);
                et_pincode.setFocusableInTouchMode(true);
                et_pincode.setClickable(true);
                et_pincode.setCursorVisible(true);


                ll_state.setVisibility(View.VISIBLE);
                ll_state_et.setVisibility(View.GONE);

                ll_city.setVisibility(View.VISIBLE);
                ll_city_et.setVisibility(View.GONE);
                ll_city.setClickable(false);

                et_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MyProfileActivity.this, "Email id is non editable", Toast.LENGTH_SHORT).show();
                        hideKeyboard();
                    }
                });

                btn_submit.setVisibility(View.VISIBLE);

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final ProgressDialog nDialog;
                nDialog = new ProgressDialog(ViewMerchantProfile.this);
                nDialog.setMessage("Loading..");
                nDialog.setTitle("Please Wait..");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(true);
                nDialog.show();
                btn_signup.setVisibility(View.GONE);
                nDialog.dismiss();
                Toast.makeText(ViewMerchantProfile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();*/
            }
        });

        first_name=share.getValue(sharedPrefrence.FIRST_NAME);
        last_name=share.getValue(sharedPrefrence.LAST_NAME);
        mob_number=share.getValue(sharedPrefrence.MOB_NUMBER);
        email=share.getValue(sharedPrefrence.EMAIL);
        address=share.getValue(sharedPrefrence.ADDRESS);
        city=share.getValue(sharedPrefrence.CITY);
        state=share.getValue(sharedPrefrence.STATE);
        pincode=share.getValue(SharedPrefrence.PINCODE);
        et_first_name.setText(Utility.capitalize(first_name));
        et_last_name.setText(Utility.capitalize(last_name));
        et_email.setText(email);
        et_number.setText(mob_number);
        et_address.setText(address);
        et_pincode.setText(pincode);
        et_state.setText(state);
        et_city.setText(city);

    }

    public void displaySpinnerStates(){
        countryID="101";
        if (Utility.checkInternetConn(this)) {

                try {
                    Call<StateListResponse> call = apiInterface.getStateListResponse(countryID);
                    call.enqueue(new Callback<StateListResponse>() {
                        @Override
                        public void onResponse(Call<StateListResponse> call, Response<StateListResponse> response) {

                            if (response.body() != null) {
                                Log.e("GetStateList response", "" + response);

                                if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {

                                    statesList= (ArrayList) response.body().getState();
                                    statesAdapter = new StatesAdapter(MyProfileActivity.this,statesList);
                                    spinner_state.setAdapter(statesAdapter);
                                    spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        public void onItemSelected(AdapterView<?> parent, View view,
                                                                   int pos, long id) {
                                            // An item was selected. You can retrieve the selected item using

                                            StateDetails statesData = statesList.get(pos);
                                            state = statesData.getStateName();
                                            state_id=statesData.getStateId();

                                            if(!stateID.equals(""))

                                            displaySpinnerCities(state_id);

                                            //  Log.d(TAG_SCHOOL_REGISTRATION, "state_selected_item value " + state_spinner_id);

                                        }

                                        public void onNothingSelected(AdapterView<?> parent) {
                                            // Another interface callback
                                        }
                                    });

                                } else if (response.body().getStatus().equals(PreferenceConstant.FAIL_STATUS)) {
                                    DialogUtility.showToast(MyProfileActivity.this,response.body().getMessage());
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<StateListResponse> call, Throwable t) {
                            DialogUtility.showToast(MyProfileActivity.this, getResources().getString(R.string.request_fail));
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

    public void displaySpinnerCities(String stateID){

        stateID="101";
        if (Utility.checkInternetConn(this)) {

            try {
                Call<StateListResponse> call = apiInterface.getStateListResponse(countryID);
                call.enqueue(new Callback<StateListResponse>() {
                    @Override
                    public void onResponse(Call<StateListResponse> call, Response<StateListResponse> response) {

                        if (response.body() != null) {
                            Log.e("GetStateList response", "" + response);

                            if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {

                                statesList= (ArrayList) response.body().getState();
                                statesAdapter = new StatesAdapter(MyProfileActivity.this,statesList);
                                spinner_state.setAdapter(statesAdapter);
                                spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view,
                                                               int pos, long id) {
                                        // An item was selected. You can retrieve the selected item using

                                        StateDetails statesData = statesList.get(pos);
                                        state = statesData.getStateName();
                                        state_id=statesData.getStateId();

                                        ll_city.setClickable(true);
                                        //  Log.d(TAG_SCHOOL_REGISTRATION, "state_selected_item value " + state_spinner_id);

                                    }

                                    public void onNothingSelected(AdapterView<?> parent) {
                                        // Another interface callback
                                    }
                                });

                            } else if (response.body().getStatus().equals(PreferenceConstant.FAIL_STATUS)) {
                                DialogUtility.showToast(MyProfileActivity.this,response.body().getMessage());
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<StateListResponse> call, Throwable t) {
                        DialogUtility.showToast(MyProfileActivity.this, getResources().getString(R.string.request_fail));
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


    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }


}
