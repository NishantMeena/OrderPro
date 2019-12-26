package com.customer.orderproupdated.custom_fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by SHRIG on 8/30/2016.
 */

public class Custom_Btntxt extends Button {

    public Custom_Btntxt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Custom_Btntxt(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Custom_Btntxt(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/HumanstBTLight.ttf");
            setTypeface(tf);
//            setTextColor();
//            setAlpha(0.75F);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setLetterSpacing(0.02F);
            }


        }
    }

}