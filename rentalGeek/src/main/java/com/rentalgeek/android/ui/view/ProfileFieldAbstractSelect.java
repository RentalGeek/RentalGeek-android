package com.rentalgeek.android.ui.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;


/**
 * Created by Alan R on 10/1/15.
 */
public abstract class ProfileFieldAbstractSelect implements AdapterView.OnItemSelectedListener {

    protected Spinner spinner;
    protected View layoutToShowHide;

    public ProfileFieldAbstractSelect() {
    }

    public ProfileFieldAbstractSelect(Spinner spinner) {
        this.spinner = spinner;
    }

    public ProfileFieldAbstractSelect(Spinner spinner, View layoutToShowHide) {
        this.spinner = spinner;
        this.layoutToShowHide = layoutToShowHide;
    }

}
