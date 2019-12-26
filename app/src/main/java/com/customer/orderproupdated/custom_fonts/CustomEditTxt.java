package com.customer.orderproupdated.custom_fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by SHRIG on 8/30/2016.
 */


public class CustomEditTxt extends EditText {

    public CustomEditTxt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditTxt(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditTxt(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/HumanstBTRoman.ttf");
            setTypeface(tf);
//            setTextColor();
//            setAlpha(0.75F);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setLetterSpacing(0.02F);
            }


        }
    }

}