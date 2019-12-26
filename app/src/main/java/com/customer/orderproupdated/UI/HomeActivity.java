package com.customer.orderproupdated.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.customer.orderproupdated.Fragments.ChatFragment;
import com.customer.orderproupdated.Fragments.DashBoardFragment;
import com.customer.orderproupdated.Fragments.FavouritesFragment;
import com.customer.orderproupdated.Fragments.InviteFriendsFragment;
import com.customer.orderproupdated.Fragments.OrderHistoryFragment;
import com.customer.orderproupdated.Fragments.SettingsFragment;
import com.customer.orderproupdated.Fragments.SupportFragment;
import com.customer.orderproupdated.Fragments.SwitchMerchentFragment;
import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;
import com.customer.orderproupdated.Utility.PreferenceConstant;
import com.customer.orderproupdated.Utility.SharedPrefrence;
import com.customer.orderproupdated.Utility.Utility;
import com.customer.orderproupdated.bean.Logout;
import com.customer.orderproupdated.chat.rooster.RoosterConnectionService;
import com.customer.orderproupdated.chat.rooster.XmppConnect;
import com.customer.orderproupdated.chat.utility.Chatutility;
import com.customer.orderproupdated.retrofit.ApiClient;
import com.customer.orderproupdated.retrofit.ApiInterface;

import org.jivesoftware.smack.SmackException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    LinearLayout switch_merchant, myorders, chat, settings, invite_friends, support, offers, home, myfav, logout;
    static View focusimage_switch_merchant;
    static View focusimage_myorders;
    static View focusimage_chat;
    static View focusimage_settings;
    static View focusimage_invite_friends;
    static View focusimage_logout;
    static View focusimage_support;
    static View focusimage_offers;
    static View focusimage_home;
    static View focusimage_myfav;
    View getFocusimage_logout;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawer;
    SharedPrefrence share;
    ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_home);
        share = SharedPrefrence.getInstance(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        // getMerchantList();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new DashBoardFragment());
        fragmentTransaction.commit();
        init();
        SetDrawer();
    }


    @Override
    public void onBackPressed() {
       /* new AlertDialog(HomeActivity.this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HomeActivity.this.finish();
                        ActivityCompat.finishAffinity(HomeActivity.this);
                    }
                })
                .setNegativeButton("No", null)
                .setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACKs);
                    }
                })
                .show();*/

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(true);
        builder1.setTitle("App Alert");
        builder1.setMessage("Are you sure you want to exit?");
        builder1.setInverseBackgroundForced(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        HomeActivity.this.finish();
                        ActivityCompat.finishAffinity(HomeActivity.this);
                    }
                });

        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonbackground.setTextColor(Color.BLACK);

        Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setTextColor(Color.BLACK);

    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        focusimage_home = (View) findViewById(R.id.focusimage_home);
        focusimage_switch_merchant = (View) findViewById(R.id.focusimage_switch_merchant);
        focusimage_myorders = (View) findViewById(R.id.focusimage_myorders);
        focusimage_chat = (View) findViewById(R.id.focusimage_chat);
        focusimage_myfav = (View) findViewById(R.id.focusimage_myfav);
        focusimage_settings = (View) findViewById(R.id.focusimage_settings);
        focusimage_invite_friends = (View) findViewById(R.id.focusimage_invite_friends);
        focusimage_support = (View) findViewById(R.id.focusimage_support);
        focusimage_offers = (View) findViewById(R.id.focusimage_offers);
        focusimage_logout = (View) findViewById(R.id.focusimage_logout);
        home = (LinearLayout) findViewById(R.id.home);
        switch_merchant = (LinearLayout) findViewById(R.id.switch_merchant);
        myorders = (LinearLayout) findViewById(R.id.myorders);
        chat = (LinearLayout) findViewById(R.id.chat);
        myfav = (LinearLayout) findViewById(R.id.myfav);
        settings = (LinearLayout) findViewById(R.id.settings);
        invite_friends = (LinearLayout) findViewById(R.id.invite_friends);
        support = (LinearLayout) findViewById(R.id.support);
        offers = (LinearLayout) findViewById(R.id.offers);
        logout = (LinearLayout) findViewById(R.id.Logout);
        switch_merchant.setOnClickListener(this);
        myorders.setOnClickListener(this);
        chat.setOnClickListener(this);
        myfav.setOnClickListener(this);
        settings.setOnClickListener(this);
        invite_friends.setOnClickListener(this);
        support.setOnClickListener(this);
        offers.setOnClickListener(this);
        home.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    private void SetDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      /*  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
          this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank...
                super.onDrawerClosed(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank...
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkxmppconnetion();
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.home:
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, new DashBoardFragment());
                fragmentTransaction.commit();
                setVisibility(R.id.home);
                drawer.closeDrawers();
                break;
            case R.id.switch_merchant:
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, new SwitchMerchentFragment());
                fragmentTransaction.commit();
                setVisibility(R.id.switch_merchant);
                //Toast.makeText(this, "Switch merchent clicked", Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                break;
            case R.id.myorders:
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, new OrderHistoryFragment());
                fragmentTransaction.commit();
                //Toast.makeText(this, "myorders clicked", Toast.LENGTH_SHORT).show();
                setVisibility(R.id.myorders);
                drawer.closeDrawers();
                break;
            case R.id.chat:
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, new ChatFragment());
                fragmentTransaction.commit();
                //Toast.makeText(this, "chat clicked", Toast.LENGTH_SHORT).show();
                setVisibility(R.id.chat);
                drawer.closeDrawers();
                break;
            case R.id.settings:
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, new SettingsFragment());
                fragmentTransaction.commit();
                setVisibility(R.id.settings);
                drawer.closeDrawers();
                break;
            case R.id.invite_friends:
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, new InviteFriendsFragment());
                fragmentTransaction.commit();
                //Toast.makeText(this,"invite_friends clicked",Toast.LENGTH_SHORT).show();
                setVisibility(R.id.invite_friends);
                drawer.closeDrawers();
                break;
            case R.id.support:
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, new SupportFragment());
                fragmentTransaction.commit();
                setVisibility(R.id.support);
                drawer.closeDrawers();
                break;
           /* case R.id.offers:
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, new OfferFragment());
                fragmentTransaction.commit();
                // Toast.makeText(this, "offers clicked", Toast.LENGTH_SHORT).show();
                setVisibility(R.id.offers);
                drawer.closeDrawers();
                break;*/
            case R.id.myfav:
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, new FavouritesFragment());
                fragmentTransaction.commit();
                // Toast.makeText(this, "myfav clicked", Toast.LENGTH_SHORT).show();
                setVisibility(R.id.myfav);
                drawer.closeDrawers();
                break;
            case R.id.Logout:
               /*
                setVisibility(R.id.Logout);
               */
                showLogoutDialog(this, "Logout", "Do you really want to Logout?");


                break;
        }
    }

    public static void setVisibility(int id) {
        switch (id) {
            case R.id.home:
                focusimage_home.setVisibility(View.VISIBLE);
                focusimage_switch_merchant.setVisibility(View.GONE);
                focusimage_myorders.setVisibility(View.GONE);
                focusimage_chat.setVisibility(View.GONE);
                focusimage_settings.setVisibility(View.GONE);
                focusimage_offers.setVisibility(View.GONE);
                focusimage_support.setVisibility(View.GONE);
                focusimage_invite_friends.setVisibility(View.GONE);
                focusimage_myfav.setVisibility(View.GONE);
                focusimage_logout.setVisibility(View.GONE);
                break;

            case R.id.switch_merchant:
                focusimage_switch_merchant.setVisibility(View.VISIBLE);
                focusimage_home.setVisibility(View.GONE);
                focusimage_myorders.setVisibility(View.GONE);
                focusimage_chat.setVisibility(View.GONE);
                focusimage_settings.setVisibility(View.GONE);
                focusimage_offers.setVisibility(View.GONE);
                focusimage_support.setVisibility(View.GONE);
                focusimage_invite_friends.setVisibility(View.GONE);
                focusimage_myfav.setVisibility(View.GONE);
                focusimage_logout.setVisibility(View.GONE);
                break;

            case R.id.myorders:
                focusimage_myorders.setVisibility(View.VISIBLE);
                focusimage_home.setVisibility(View.GONE);
                focusimage_switch_merchant.setVisibility(View.GONE);
                focusimage_chat.setVisibility(View.GONE);
                focusimage_settings.setVisibility(View.GONE);
                focusimage_offers.setVisibility(View.GONE);
                focusimage_support.setVisibility(View.GONE);
                focusimage_invite_friends.setVisibility(View.GONE);
                focusimage_myfav.setVisibility(View.GONE);
                focusimage_logout.setVisibility(View.GONE);
                break;

            case R.id.chat:
                focusimage_chat.setVisibility(View.VISIBLE);
                focusimage_home.setVisibility(View.GONE);
                focusimage_myorders.setVisibility(View.GONE);
                focusimage_switch_merchant.setVisibility(View.GONE);
                focusimage_settings.setVisibility(View.GONE);
                focusimage_offers.setVisibility(View.GONE);
                focusimage_support.setVisibility(View.GONE);
                focusimage_invite_friends.setVisibility(View.GONE);
                focusimage_myfav.setVisibility(View.GONE);
                focusimage_logout.setVisibility(View.GONE);
                break;
            case R.id.myfav:
                focusimage_myfav.setVisibility(View.VISIBLE);
                focusimage_chat.setVisibility(View.GONE);
                focusimage_home.setVisibility(View.GONE);
                focusimage_myorders.setVisibility(View.GONE);
                focusimage_switch_merchant.setVisibility(View.GONE);
                focusimage_settings.setVisibility(View.GONE);
                focusimage_offers.setVisibility(View.GONE);
                focusimage_support.setVisibility(View.GONE);
                focusimage_invite_friends.setVisibility(View.GONE);
                focusimage_logout.setVisibility(View.GONE);
                break;

            case R.id.settings:
                focusimage_home.setVisibility(View.GONE);
                focusimage_settings.setVisibility(View.VISIBLE);
                focusimage_chat.setVisibility(View.GONE);
                focusimage_myorders.setVisibility(View.GONE);
                focusimage_switch_merchant.setVisibility(View.GONE);
                focusimage_offers.setVisibility(View.GONE);
                focusimage_support.setVisibility(View.GONE);
                focusimage_invite_friends.setVisibility(View.GONE);
                focusimage_myfav.setVisibility(View.GONE);
                focusimage_logout.setVisibility(View.GONE);
                break;

            case R.id.offers:
                focusimage_offers.setVisibility(View.VISIBLE);
                focusimage_home.setVisibility(View.GONE);
                focusimage_settings.setVisibility(View.GONE);
                focusimage_chat.setVisibility(View.GONE);
                focusimage_myorders.setVisibility(View.GONE);
                focusimage_switch_merchant.setVisibility(View.GONE);
                focusimage_support.setVisibility(View.GONE);
                focusimage_invite_friends.setVisibility(View.GONE);
                focusimage_myfav.setVisibility(View.GONE);
                focusimage_logout.setVisibility(View.GONE);
                break;

            case R.id.support:
                focusimage_support.setVisibility(View.VISIBLE);
                focusimage_home.setVisibility(View.GONE);
                focusimage_offers.setVisibility(View.GONE);
                focusimage_settings.setVisibility(View.GONE);
                focusimage_chat.setVisibility(View.GONE);
                focusimage_myorders.setVisibility(View.GONE);
                focusimage_switch_merchant.setVisibility(View.GONE);
                focusimage_invite_friends.setVisibility(View.GONE);
                focusimage_myfav.setVisibility(View.GONE);
                focusimage_logout.setVisibility(View.GONE);
                break;

            case R.id.invite_friends:
                focusimage_invite_friends.setVisibility(View.VISIBLE);
                focusimage_home.setVisibility(View.GONE);
                focusimage_support.setVisibility(View.GONE);
                focusimage_offers.setVisibility(View.GONE);
                focusimage_settings.setVisibility(View.GONE);
                focusimage_chat.setVisibility(View.GONE);
                focusimage_myorders.setVisibility(View.GONE);
                focusimage_switch_merchant.setVisibility(View.GONE);
                focusimage_myfav.setVisibility(View.GONE);
                focusimage_logout.setVisibility(View.GONE);
                break;
            case R.id.Logout:
                focusimage_logout.setVisibility(View.VISIBLE);
                focusimage_invite_friends.setVisibility(View.GONE);
                focusimage_home.setVisibility(View.GONE);
                focusimage_support.setVisibility(View.GONE);
                focusimage_offers.setVisibility(View.GONE);
                focusimage_settings.setVisibility(View.GONE);
                focusimage_chat.setVisibility(View.GONE);
                focusimage_myorders.setVisibility(View.GONE);
                focusimage_switch_merchant.setVisibility(View.GONE);
                focusimage_myfav.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    public void showLogoutDialog(final Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);
        String positiveText = "Yes";
        String negetiveText = "Cancel";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       logout(context);
                    }
                });
        builder.setNegativeButton(negetiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog...
        dialog.show();
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void checkxmppconnetion() {
        try {
            if (Utility.checkInternetConn(HomeActivity.this)) {
                if (XmppConnect.mConnection == null || XmppConnect.mConnection.isConnected() == false) {
                    if (Utility.isMyServiceRunning(RoosterConnectionService.class, getApplicationContext())) {
                        Intent i1 = new Intent(this, RoosterConnectionService.class);
                        stopService(i1);
                    }
                    Intent i1 = new Intent(this, RoosterConnectionService.class);
                    startService(i1);
                } else if (XmppConnect.mConnection != null && XmppConnect.mConnection.isConnected() == false) {
                    if (Utility.isMyServiceRunning(RoosterConnectionService.class, getApplicationContext())) {
                        Intent i1 = new Intent(this, RoosterConnectionService.class);
                        stopService(i1);
                    }
                    Intent i1 = new Intent(this, RoosterConnectionService.class);
                    startService(i1);
                }
            }
        } catch (Exception e) {

        }
    }

    public void logout(final Context mContext) {
        if (Utility.checkInternetConn(this)) {
            String device_id = Chatutility.getDeviceToken(this);
            String device_token = Utility.getSharedPreferences(this, "fcm_token");
            String device_type = "android";

            String email = share.getValue(SharedPrefrence.EMAIL);

            try {
                DialogUtility.showProgressDialog(this, true, getString(R.string.please_wait));
                Call<Logout> call = apiService.customerLogout(email, device_token, device_id, device_type);
                call.enqueue(new Callback<Logout>() {
                    @Override
                    public void onResponse(Call<Logout> call, Response<Logout> response) {
                        try {
                            int code = response.code();
                            /* Response{protocol=http/1.0, code=500, message=Internal Server Error, url=https://36.255.3.15/order_p/API/register_customer.php}*/
                            if (code == 500) {
                                Toast.makeText(HomeActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                                DialogUtility.pauseProgressDialog();
                            } else {
                                if (response != null) {
                                    String status = String.valueOf(response.body().getStatus());
                                    int statusCode = Integer.parseInt(status);
                                    if (statusCode == PreferenceConstant.PASS_STATUS) {
                                        DialogUtility.pauseProgressDialog();
                                        Intent i1 = new Intent(HomeActivity.this, RoosterConnectionService.class);
                                        stopService(i1);
                                        try {
                                            if (XmppConnect.mConnection.isConnected())
                                                XmppConnect.mConnection.disconnect();
                                        } catch (SmackException.NotConnectedException e) {
                                            e.printStackTrace();
                                        }

                                        share.clearSharedPrefernces(mContext);
                                        share.setValue(SharedPrefrence.EMAIL, "");
                                        share.setValue(SharedPrefrence.PASSWORD, "");
                                        share.setBooleanValue(SharedPrefrence.IS_USER_LOGIN, false);

                                        share.setValue(SharedPrefrence.CUSTOMERID, "");
                                        share.setValue(SharedPrefrence.FIRST_NAME, "");
                                        share.setValue(SharedPrefrence.LAST_NAME, "");
                                        share.setValue(SharedPrefrence.ADDRESS, "");
                                        share.setValue(SharedPrefrence.STATE, "");
                                        share.setValue(SharedPrefrence.CITY, "");
                                        share.setValue(SharedPrefrence.PINCODE, "");
                                        share.setValue(SharedPrefrence.MOB_NUMBER, "");

                                        mContext.deleteDatabase("orderpro.db");

                                        PreferenceConstant.CART_COUNT = 0;
                                        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);

                                        setVisibility(R.id.Logout);
                                    } else if (response.body().getStatus().equals(PreferenceConstant.FAIL_STATUS)) {
                                        Toast.makeText(HomeActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        DialogUtility.pauseProgressDialog();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Logout> call, Throwable t) {
                        DialogUtility.showToast(HomeActivity.this, getResources().getString(R.string.request_fail));
                        DialogUtility.pauseProgressDialog();
                    }
                });

                // DialogUtility.showToast(SignUpActivity.this, getResources().getString(R.string.request_fail));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DialogUtility.showMaterialDialog(this, getString(R.string.alert), getString(R.string.checknet));
        }
    }
}