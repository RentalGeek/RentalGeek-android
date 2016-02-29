package com.rentalgeek.android.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.NonEvent;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.dialog.GeekProgressDialog;

public class GeekBaseFragment extends Fragment {

    protected AppCompatActivity activity;

    protected void toast(String message) {
        if (!TextUtils.isEmpty(message)) {
            DialogManager.showCrouton(activity, message);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity) activity;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(int id, View root) {
        if (root == null) return (T) getView(id);
        return (T) root.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(int id) {
        final View view = getView();
        if (view != null) return (T) view.findViewById(id);
        return null;
    }

    @Override
    public void onStop() {
        unregisterBus();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerBus();
    }

    protected void unregisterBus() { AppEventBus.unregister(this);
    }

    protected void registerBus() {
        AppEventBus.register(this);
    }

    public void onEventMainThread(NonEvent event) {
    }

    protected void hideProgressDialog() {
        GeekProgressDialog.dismiss();
    }

    protected void showProgressDialog(int messageResourceId) {
        GeekProgressDialog.show(activity, messageResourceId);
    }

}
