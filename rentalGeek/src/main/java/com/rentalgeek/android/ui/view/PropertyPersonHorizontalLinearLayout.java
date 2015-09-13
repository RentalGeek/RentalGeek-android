package com.rentalgeek.android.ui.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by rajohns on 9/13/15.
 *
 */
public class PropertyPersonHorizontalLinearLayout extends LinearLayout {

    public PropertyPersonHorizontalLinearLayout(Context context) {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 10);
        this.setLayoutParams(layoutParams);
    }

}
