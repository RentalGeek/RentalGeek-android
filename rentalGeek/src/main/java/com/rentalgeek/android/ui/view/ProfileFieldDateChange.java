package com.rentalgeek.android.ui.view;

import android.widget.DatePicker;

import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.model.Profile;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ProfileFieldDateChange implements DatePicker.OnDateChangedListener {

    private DatePicker datePicker;
    private static DateTimeFormatter date_format = DateTimeFormat.forPattern("yyyy-MM-dd");

    public ProfileFieldDateChange(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (year == 0 || monthOfYear == 0 || dayOfMonth == 0) {
            return;
        }

        String tag = (String) datePicker.getTag();

        DateTime date = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);

        if (tag != null && !tag.isEmpty()) {
            Profile profile = SessionManager.Instance.getDefaultProfile();
            profile.set(tag, date_format.print(date));
        }
    }

}
