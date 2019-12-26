package com.customer.orderproupdated.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
public class Specification_Fragment extends Fragment {
    ProdVal pb_obj;
    WebView webView;
    TextView tv_No_Specifications;
    View view;
    public Specification_Fragment(){}



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pb_obj = (ProdVal) getArguments().getSerializable("prodval");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_specification, container, false);
        tv_No_Specifications=(TextView)view.findViewById(R.id.tv_No_Specifications);
        webView = (WebView)view.findViewById(R.id.webView_specification);


        if(!pb_obj.getSpecification().equals("")) {

            tv_No_Specifications.setVisibility(View.GONE);

            String htmlText = "<html><body style=\"text-align:justify;color:#000;line-height: 1.5\">%s</body></html>";
            Spanned specification;


            webView.getSettings().setJavaScriptEnabled(true);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setScrollContainer(false);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


            if (Build.VERSION.SDK_INT >= 24) {
                specification= Html.fromHtml(String.format(htmlText, pb_obj.getDescription()),1);
                webView.loadDataWithBaseURL(null, specification.toString(), "text/html", "utf-8", null);
            } else {
                specification= Html.fromHtml(String.format(htmlText, pb_obj.getDescription()));
                webView.loadDataWithBaseURL(null,specification.toString(), "text/html", "utf-8", null);
            }
        }
        else
        {
            webView.setVisibility(View.GONE);
            tv_No_Specifications.setVisibility(View.VISIBLE);
        }
        // Inflate the layout for this fragment
        return view;
    }
}
