package com.rentalgeek.android.ui.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
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

    public void nextfragment(Fragment fragment, boolean stack, int id) {

        if (getActivity() == null) {
            return;
        }
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.one_, R.anim.two_, R.anim.three_, R.anim.four_);
        ft.replace(id, fragment);
        if (stack)
            ft.addToBackStack(fragment.getClass().getName());
        ft.commitAllowingStateLoss();
    }

    public void addfragment(Fragment fragment, boolean stack, int id) {

        if (getActivity() == null) {
            return;
        }
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        ft.setCustomAnimations(R.anim.one_, R.anim.two_, R.anim.three_, R.anim.four_);
        ft.add(id, fragment);
        if (stack)
            ft.addToBackStack(fragment.getClass().getName());
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity) activity;
    }

    protected void changeColor(ImageView view, int colorId) {
        Resources res = RentalGeekApplication.context.getResources();
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
