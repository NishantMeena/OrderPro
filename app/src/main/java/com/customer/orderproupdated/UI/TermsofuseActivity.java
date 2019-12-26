package com.customer.orderproupdated.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.Utility.DialogUtility;


/**
 * Created by AKASH on 08-Sep-16.
 */
public class TermsofuseActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    WebView webview;
    ImageView back;
    TextView textView_tc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_terms_and_use);

        back = (ImageView) findViewById(R.id.iv_back);
        webview = (WebView) findViewById(R.id.webview);
        textView_tc = (TextView) findViewById(R.id.textView_tc);
        back.setOnClickListener(this);

        textView_tc.setVisibility(View.GONE);
        textView_tc.setEnabled(false);

        webview.setVisibility(View.VISIBLE);
        webview.setEnabled(true);

        webview.setWebViewClient(new MyBrowser());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setUseWideViewPort(true);
        webview.loadUrl("http://36.255.3.15/termAndConditions.html");
    }


    @Override
    public void onClick(View view) {
        finish();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            DialogUtility.pauseProgressDialog();
        }
    }
}







