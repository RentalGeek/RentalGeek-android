package com.rentalgeek.android.system;

import android.content.Context;
import android.content.pm.ApplicationInfo;


public enum AppSystem {

    Instance;

    public static final boolean isV1Build = false;

    public boolean isDebugBuild(Context context) {
        return ( 0 != ( context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
    }
}
