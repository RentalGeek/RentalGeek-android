package com.rentalgeek.android.constants;

import android.content.Context;

import com.rentalgeek.android.R;

public class ManhattanKansasImpl implements ManhattanKansas {

    private String latitude;
    private String longitude;
    private String radius;

    public ManhattanKansasImpl(Context context) {
        this.latitude = context.getString(R.string.manhattan_kansas_latitude);
        this.longitude = context.getString(R.string.manhattan_kansas_longitude);
        this.radius = context.getString(R.string.manhattan_kansas_radius);
    }

    @Override
    public String latitude() {
        return latitude;
    }

    @Override
    public String longitude() {
        return longitude;
    }

    @Override
    public String radius() {
        return radius;
    }

}
