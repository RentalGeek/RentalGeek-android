package com.rentalgeek.android.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by rajohns on 9/13/15.
 */
public class PropertyRightTextView extends TextView {

    public PropertyRightTextView(Context context) {
        super(context);
        this.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));
        this.setTextColor(Color.BLACK);
        this.setGravity(Gravity.RIGHT);
        this.setTextSize(11);
    }

}
