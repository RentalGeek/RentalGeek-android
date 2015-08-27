package com.rentalgeek.android.ui.dialog.manager;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.events.AppDialogRequestEvent;
import com.rentalgeek.android.logging.AppLogger;

public class GeekDialog {

    private static final String TAG = "GeekDialog";

    public enum DialogType {
        Unknown,
        Network,
        LoginConflict,
        Login
    }

    public interface AppDialogListener {
        public void onDialogButtonClick(AppDialogFragment sender, int which, Bundle args);
        public void onDialogDismissed(AppDialogFragment sender);
    }

    public static class AppDialogFragment extends DialogFragment {

        protected int priority = 0;
        protected boolean required = false;

        public AppDialogFragment() { }

        public String getFragmentId() {
            return GeekDialog.getTagName(getClass());
        }

        public AppDialogListener getDialogListener() {
            return GeekDialog.getDialogListener(this);
        }

        @SuppressWarnings("unchecked")
        protected <T extends View> T getView(int id, View root) {
            if (root == null) return (T) getView(id);
            return (T) root.findViewById(id);
        }

        @SuppressWarnings("unchecked")
        protected <T extends View> T getView(int id) {
            return (T) getView().findViewById(id);
        }

    }

    public static AppDialogListener getDialogListener(AppDialogFragment sender) {

        if (sender instanceof AppDialogListener) {
            return (AppDialogListener) sender;
        }

        Fragment target = sender.getTargetFragment();
        if (target != null && target instanceof AppDialogListener) {
            return (AppDialogListener) target;
        }

        FragmentActivity activity = sender.getActivity();
        if (activity instanceof AppDialogListener) {
            return (AppDialogListener) activity;
        }

        return null;
    }

    public static <T extends Fragment> String getTagName(Class<T> clazz) {
        return clazz.getSimpleName();
    }

    public static <T extends AppDialogFragment> T newInstance(Class<T> clazz, Bundle args) throws InstantiationException, IllegalAccessException {
        T instance = clazz.newInstance();
        instance.setArguments(args);
        return instance;
    }

    public static <T extends AppDialogFragment> AppDialogFragment showDialog(FragmentActivity activity, AppDialogRequestEvent<?> event) {
        return showDialog(activity, event.getClazz(), event.getArgs(), event.getCaller());
    }

    public static <T extends AppDialogFragment> AppDialogFragment showDialog(FragmentActivity context, Class<T> clazz, Bundle args, Fragment target) {
        if (context == null) {
            AppLogger.log(TAG, "Tried to show dialog with null context");
            return null;
        }
        try {
            final FragmentManager manager = context.getSupportFragmentManager();
            if (!isShowing(manager, clazz)) {
                AppDialogFragment dialog = newInstance(clazz, args);
                if (target != null) {
                    dialog.setTargetFragment(target, 0);
                }
                dialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.GeekDialog);
                //dialog.show(manager, dialog.getFragmentId());
                FragmentTransaction ft = manager.beginTransaction();
                ft.add(dialog, dialog.getFragmentId());
                ft.commitAllowingStateLoss();
                //dialog.getDialog().setCanceledOnTouchOutside(false);
                return dialog;
            }
        } catch (Exception ex) {
            AppLogger.log(TAG, ex, true);
        }
        return null;
    }

    public static <T extends AppDialogFragment> boolean isShowing(final FragmentActivity activity, Class<T> clazz) {
        if (activity == null) return false;
        return isShowing(activity.getSupportFragmentManager(), clazz);
    }

    public static <T extends AppDialogFragment> boolean isShowing(final FragmentManager manager, Class<T> clazz) {
        final String tagName = getTagName(clazz);
        DialogFragment dialogFragment = (DialogFragment) manager.findFragmentByTag(tagName);
        if (dialogFragment != null) {
            final Dialog dialog = dialogFragment.getDialog();
            return (dialog != null && dialog.isShowing());
        }
        return false;
    }

    public static <T extends AppDialogFragment> AppDialogFragment getDialogFragment(final FragmentManager manager, Class<T> clazz) {
        final String tagName = getTagName(clazz);
        return (AppDialogFragment) manager.findFragmentByTag(tagName);
    }

    public static <T extends AppDialogFragment> void dismiss(FragmentActivity context, Class<T> clazz) {
        if (context == null) return;
        final FragmentManager manager = context.getSupportFragmentManager();
        final String tagName = getTagName(clazz);
        DialogFragment dialogFragment = (DialogFragment) manager.findFragmentByTag(tagName);
        if (dialogFragment != null) {
            try {
                dialogFragment.dismiss();
            } catch (Exception e) {
                AppLogger.log(TAG, e);
            }
        }
    }
}
