package com.customer.orderproupdated.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.bean.ProdVal;


/**
 * Created by Sony on 1/25/2017.
 */
public class Description_Fragment extends Fragment {
    Bundle b;
    ProdVal pb_obj;
    WebView webView;
    TextView tv_No_Description;
    View view;
    public Description_Fragment(){

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             view=inflater.inflate(R.layout.fragment_description, container, false);
             tv_No_Description=(TextView)view.findViewById(R.id.tv_No_Description);
            webView = (WebView)view.findViewById(R.id.webView_description);
        b = getArguments();
        pb_obj = (ProdVal) b.getSerializable("prodval");

        if(!pb_obj.getDescription().equals("")) {

            tv_No_Description.setVisibility(View.GONE);

            String htmlText = "<html><body style=\"text-align:justify;color:#000;line-height: 1.5\">%s</body></html>";
            Spanned description;

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setScrollContainer(false);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);



            if (Build.VERSION.SDK_INT >= 24) {
                description= Html.fromHtml(String.format(htmlText, pb_obj.getDescription()),1);
                webView.loadDataWithBaseURL(null, description.toString(), "text/html", "utf-8", null);
            } else {
                description= Html.fromHtml(String.format(htmlText, pb_obj.getDescription()));
                webView.loadDataWithBaseURL(null,description.toString(), "text/html", "utf-8", null);
            }
        }else
        {
            webView.setVisibility(View.GONE);
            tv_No_Description.setVisibility(View.VISIBLE);
        }

        // Inflate the layout for this fragment
        return view ;
    }
}

