package com.customer.orderproupdated.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.UI.AddMerchantActivity;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.adapter.MerchantAdapter;
import com.customer.orderproupdated.bean.AllMerchantDetails;
import com.customer.orderproupdated.bean.GetAllMerchantList;
import com.customer.orderproupdated.bean.GetMerchantList;
import com.customer.orderproupdated.bean.MerchantDetail;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.customer.orderproupdated.UI.HomeActivity.setVisibility;

/**
 * Created by Sony on 12/27/2016.
 */
public class SwitchMerchentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View view;
    RecyclerView lv_mymerchant;
    RelativeLayout empty_layout;
    ArrayList<MerchantDetail> merchantList;
    MerchantAdapter merchantAdapter;
    ApiInterface apiService;
    SharedPrefrence share;
    String customerID;
    private Handler mainHandler = new Handler();
    Menu menu;
    SwipeRefreshLayout swipeRefreshLayout;
    String merchnat_id, merchant_name, merchant_email, merchnat_number;
    MerchantDetail merchantDetail;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ArrayList<MerchantDetail> merchantList_added = new ArrayList<>();
    ArrayList<AllMerchantDetails> all_merchant_list = new ArrayList<>();
    ArrayList<AllMerchantDetails> merchantList_added_requested = new ArrayList<>();
    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void refreshview() {
        ActivityCompat.invalidateOptionsMenu(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        refreshview();
        view = inflater.inflate(R.layout.fragment_swtichmerchent, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Switch Merchant");
        apiService = ApiClient.getClient().create(ApiInterface.class);
        lv_mymerchant = (RecyclerView) view.findViewById(R.id.lv_mymerchant);
        share = SharedPrefrence.getInstance(getActivity());
        empty_layout = (RelativeLayout) view.findViewById(R.id.empty_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        lv_mymerchant.setLayoutManager(new LinearLayoutManager(getActivity()));
        // getMerchantList();
        // merchantList=merchantList_added;
        merchantList = share.getMerchantList(SharedPrefrence.MERCHANT_LIST_ADDED);
        if (merchantList.size() == 0) {
            empty_layout.setVisibility(View.VISIBLE);
        }
        merchantAdapter = new MerchantAdapter(SwitchMerchentFragment.this, merchantList);
        lv_mymerchant.setAdapter(merchantAdapter);
        return view;
    }

    public void getMerchantList() {
        customerID = share.getValue(SharedPrefrence.CUSTOMERID);
        // DialogUtility.showProgressDialog(getActivity(), false, "Loading");
        Call<GetMerchantList> call = apiService.getMerchantList(customerID);
        call.enqueue(new Callback<GetMerchantList>() {
            @Override
            public void onResponse(Call<GetMerchantList> call, Response<GetMerchantList> response) {
                Log.e("GetCustomers response", "" + response);
                if (response.body() != null) {
                    if (response.body().getMerchant() != null) {
                        merchantList.clear();
                        merchantList = new ArrayList<>();
                        merchantList.addAll(response.body().getMerchant());
                        merchantList_added.addAll(response.body().getMerchant());
                        share.setMerchantList(SharedPrefrence.MERCHANT_LIST_ADDED, merchantList);
                        Log.e("GetMerchant list size", "" + merchantList.size());
                    }

                    if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {
                        //prefference.setCustomersList(SharedPrefference.CUSTOMERS, customersList);
                        Log.e("GetMerchant list size", "" + merchantList.size());
                        if (merchantList.size() == 0) {
                            empty_layout.setVisibility(View.VISIBLE);
                        }
                        merchantAdapter = new MerchantAdapter(SwitchMerchentFragment.this, merchantList);
                        lv_mymerchant.setAdapter(merchantAdapter);
                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);

                    } else {
                        //  DialogUtility.showToast(getActivity(), getResources().getString(R.string.no_merchant_added));
                        //Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                DialogUtility.pauseProgressDialog();

            }

            @Override
            public void onFailure(Call<GetMerchantList> call, Throwable t) {
                //DialogUtility.showToast(getActivity(), getResources().getString(R.string.request_fail));
                DialogUtility.pauseProgressDialog();
            }


        });

    }

    public void showSwitchMerchentDialog(Context context, String title, String message, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);

        String positiveText = "Ok";
        String negetiveText = "Cancel";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        share.setBooleanValue(SharedPrefrence.IS_MERCHANT_SWITCHED, true);
                        share.setValue(SharedPrefrence.MERCHANT_ID, merchnat_id);
                        share.setMerchantDetail(SharedPrefrence.MERCHANT_DETAIL, merchantDetail);
                        merchantList.get(position).setIs_merchant_sleceted(true);
                        share.setMerchantList(SharedPrefrence.MERCHANT_LIST_ADDED, merchantList);
                        share.clearAllList(getActivity());
                        // DialogUtility.showToast(getActivity(), "Merchnat switched ");
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, new DashBoardFragment());
                        fragmentTransaction.commit();
                        setVisibility(R.id.home);

                    }
                });
        builder.setNegativeButton(negetiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        share.setBooleanValue(SharedPrefrence.IS_MERCHANT_SWITCHED, false);
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


    @Override
    public void onRefresh() {
        getMerchantList();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_switchmerchant, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        //search
        MenuItem item_add_merchnat = menu.findItem(R.id.action_plus);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_plus:
                getAllMerchantList();
                return true;


            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getMerchantDetail(MerchantDetail merchantDetail) {
        this.merchantDetail = merchantDetail;
        merchnat_id = merchantDetail.getMerchantId();
        merchant_name = merchantDetail.getUsername();
        merchant_email = merchantDetail.getEmail();
        merchnat_number = merchantDetail.getContactno();

    }

    public void getAllMerchantList() {
        if (Utility.checkInternetConn(getActivity())) {
            String customerID = share.getValue(SharedPrefrence.CUSTOMERID);
            DialogUtility.showProgressDialog(getActivity(), false, "Loading");
            try {
                Call<GetAllMerchantList> call = apiService.getAllMerchantList(customerID);
                call.enqueue(new Callback<GetAllMerchantList>() {
                    @Override
                    public void onResponse(Call<GetAllMerchantList> call, Response<GetAllMerchantList> response) {
                        Log.e("GetCustomers response", "" + response);
                        if (response.body() != null) {
                            if (response.body().getMerchant() != null) {
                                all_merchant_list = new ArrayList<>();
                                all_merchant_list.addAll(response.body().getMerchant());
                                Log.e("GetMerchant list size", "" + all_merchant_list.size());
                            }

                            if (response.body().getStatus().equals(PreferenceConstant.PASS_STATUS)) {
                                // share.setAllMerchantList(SharedPrefrence.All_MERCHANT_LIST, all_merchant_list);
                                Log.e("GetMerchant list size", "" + all_merchant_list.size());

                                merchantList_added_requested = new ArrayList<>();
                                for (int i = 0; i < all_merchant_list.size(); i++) {
                                    if (all_merchant_list.get(i).getRequestStatus().equals("1") || all_merchant_list.get(i).getRequestStatus().equals("0")) {
                                        merchantList_added_requested.add(all_merchant_list.get(i));

                                    }
                                }
                                share.setAllMerchantList(SharedPrefrence.All_MERCHANT_LIST, merchantList_added_requested);

                                Intent in = new Intent(getActivity(), AddMerchantActivity.class);
                                startActivity(in);


                            }
                        }

                        DialogUtility.pauseProgressDialog();

                    }

                    @Override
                    public void onFailure(Call<GetAllMerchantList> call, Throwable t) {
                        DialogUtility.showToast(getActivity(), getResources().getString(R.string.request_fail));
                        DialogUtility.pauseProgressDialog();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DialogUtility.showMaterialDialog(getActivity(), getString(R.string.alert), getString(R.string.checknet));
        }
    }


}







