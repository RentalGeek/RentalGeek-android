package com.rentalgeek.android.ui.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AppDialogRequestEvent;
import com.rentalgeek.android.bus.events.NonEvent;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.dialog.AppProgressDialog;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.dialog.manager.GeekDialog;

public class GeekBaseFragment extends Fragment {
    
    
    public boolean registerWithEventBus = true;

    protected FragmentActivity activity;
    
    protected void toast(String message) {
        if (!TextUtils.isEmpty(message)) {
            DialogManager.showCrouton(activity, message);
        }
    }

    public void nextfragment(Fragment fragment, boolean stack, int id) {

        if (getActivity() == null) {
            return;
        }
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.one_, R.anim.two_, R.anim.three_, R.anim.four_);
        ft.replace(id, fragment);
        if (stack)
            ft.addToBackStack(fragment.getClass().getName().toString());
        ft.commitAllowingStateLoss();
    }

    public void addfragment(Fragment fragment, boolean stack, int id) {

        if (getActivity() == null) {
            return;
        }
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        ft.setCustomAnimations(R.anim.one_, R.anim.two_, R.anim.three_,
                R.anim.four_);
        ft.add(id, fragment);
        if (stack)
            ft.addToBackStack(fragment.getClass().getName().toString());
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
    }

    protected void changeColor(ImageView view, int colorId) {
        Resources res = RentalGeekApplication.context.getResources();
        //View view = getView(imageViewId);
        if (view != null) {
            final ImageView image = (ImageView) view;
            final int newColor = res.getColor(colorId);
            image.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        }
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
    public void onPause() {
        super.onPause();
        if (registerWithEventBus) unregisterBus();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (registerWithEventBus) registerBus();
    }

    protected void unregisterBus() {
        AppEventBus.unregister(this);
    }

    protected void registerBus() {
        AppEventBus.register(this);
    }

    public void onEventMainThread(NonEvent event) { }


    protected void hideProgressDialog() {
        GeekDialog.dismiss(getActivity(), AppProgressDialog.class);
    }

    protected void showProgressDialog(int messageResId) {
        Bundle args = new Bundle();
        args.putInt(Common.DIALOG_MSG_ID, messageResId);
        AppEventBus.post(new AppDialogRequestEvent<AppProgressDialog>(AppProgressDialog.class, args, this, false));
    }

    
}
