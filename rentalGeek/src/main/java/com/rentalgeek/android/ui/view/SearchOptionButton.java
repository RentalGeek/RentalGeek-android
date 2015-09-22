package com.rentalgeek.android.ui.view;

import android.content.Context;
import android.content.res.TypedArray;

import android.widget.TextView;

import android.util.AttributeSet;

import com.rentalgeek.android.R;

public class SearchOptionButton extends TextView {
   
    private String value;
    private boolean selected = false;
    private int selected_bg;
    private int unselected_bg;
    private int selected_txt_color;
    private int unselected_txt_color;

    public SearchOptionButton(Context context) {
        super(context);
    }

    public SearchOptionButton(Context context, AttributeSet attrs) {
        super(context,attrs);

        TypedArray tarray = context.obtainStyledAttributes(attrs,R.styleable.SearchOptionButton);

        final int count = tarray.getIndexCount();

        for(int i = 0; i < count; i++) {

            int attr = tarray.getIndex(i);

            switch(attr) {
                case R.styleable.SearchOptionButton_value:
                    value = tarray.getString(attr);
                    break;
                case R.styleable.SearchOptionButton_unselected_bg:
                    unselected_bg = tarray.getResourceId(attr,-1);
                    break;
                case R.styleable.SearchOptionButton_selected_bg:
                    selected_bg = tarray.getResourceId(attr,-1);
                    break;
                case R.styleable.SearchOptionButton_selected_txt_color:
                    selected_txt_color = tarray.getColor(attr,-1);
                    break;
                case R.styleable.SearchOptionButton_unselected_txt_color:
                    unselected_txt_color = tarray.getColor(attr,-1);
                    break;            
            }
        }

        tarray.recycle();

        setTextColor(unselected_txt_color);
        setBackgroundResource(unselected_bg);
        setMinimumHeight(0);
        setMinimumWidth(0);
    }

    public void pressed() {
        selected = ! selected;

        if( selected )
            selected();
        else
            unselected();
    }

    private void unselected() {
        setTextColor(unselected_txt_color);
        setBackgroundResource(unselected_bg);
    }

    private void selected() {
        setTextColor(selected_txt_color);
        setBackgroundResource(selected_bg);
    }

    public String getValue() {
        return value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void reset() {
        unselected();
    }
}
