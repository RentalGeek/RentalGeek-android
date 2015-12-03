package com.rentalgeek.android.ui.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.model.Profile;

/**
 * Created by Alan R on 10/2/15.
 */
public class ProfileFieldSelect extends ProfileFieldAbstractSelect {

    public ProfileFieldSelect(Spinner spinner) {
        super(spinner);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String tag = (String) spinner.getTag();
        String item = (String) spinner.getSelectedItem();

        if (tag != null && !tag.isEmpty()) {
            Profile profile = SessionManager.Instance.getDefaultProfile();

            if (position != 0) {
                profile.set(tag, item);
            } else {
                profile.set(tag, "");
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
