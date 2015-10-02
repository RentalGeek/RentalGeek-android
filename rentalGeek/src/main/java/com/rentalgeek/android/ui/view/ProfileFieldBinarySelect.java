package com.rentalgeek.android.ui.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.model.Profile;

/**
 * Created by Alan R on 10/2/15.
 */
public class ProfileFieldBinarySelect extends ProfileFieldAbstractSelect {

    public ProfileFieldBinarySelect(Spinner spinner) {
        super(spinner);
    }

    public ProfileFieldBinarySelect(Spinner spinner, View layoutView) {
        super(spinner,layoutView);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String tag = (String) spinner.getTag();

        if (tag != null && !tag.isEmpty()) {

            Profile profile = SessionManager.Instance.getDefaultProfile();

            if (position == 0 || position == 2) {

                profile.set(tag, false);

                if (layoutToShowHide != null) {
                    layoutToShowHide.setVisibility(View.GONE);
                }
            }

            else if ( position == 1) {
                profile.set(tag, true);

                if (layoutToShowHide != null) {
                    layoutToShowHide.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
