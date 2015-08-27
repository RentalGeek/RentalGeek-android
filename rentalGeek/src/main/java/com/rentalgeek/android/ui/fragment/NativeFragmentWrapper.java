package com.rentalgeek.android.ui.fragment;

import android.app.Fragment;
import android.content.Intent;

public class NativeFragmentWrapper extends Fragment {

    private final android.support.v4.app.Fragment nativeFragment;

    public NativeFragmentWrapper(android.support.v4.app.Fragment nativeFragment) {
        this.nativeFragment = nativeFragment;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        nativeFragment.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        nativeFragment.onActivityResult(requestCode, resultCode, data);
    }
}