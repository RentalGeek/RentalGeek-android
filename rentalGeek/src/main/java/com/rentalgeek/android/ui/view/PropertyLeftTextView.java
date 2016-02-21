package com.rentalgeek.android.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

public class PropertyLeftTextView extends TextView {

    public PropertyLeftTextView(Context context) {
        super(context);
        this.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.75f));
        this.setTextColor(Color.BLACK);
        this.setTextSize(11);
    }

}
