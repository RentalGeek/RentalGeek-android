package com.rentalgeek.android.bus.events;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.rentalgeek.android.ui.dialog.manager.GeekDialog;


public class AppDialogRequestEvent<T extends GeekDialog.AppDialogFragment> {

	private Class<T> clazz;
	//private FragmentActivity activity;
	private Bundle args = null;
	private Fragment caller = null;
	private boolean isCancellable = false;
	
	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

//	public FragmentActivity getActivity() {
//		return activity;
//	}
//
//	public void setActivity(FragmentActivity activity) {
//		this.activity = activity;
//	}

	public Bundle getArgs() {
		return args;
	}

	public void setArgs(Bundle args) {
		this.args = args;
	}

	public Fragment getCaller() {
		return caller;
	}

	public void setCaller(Fragment caller) {
		this.caller = caller;
	}

	public boolean isCancellable() {
		return isCancellable;
	}

	public void setCancellable(boolean isCancellable) {
		this.isCancellable = isCancellable;
	}
	
	public AppDialogRequestEvent(Class<T> clazz) {
		super();
		this.clazz = clazz;
		//this.activity = activity;
	}

	public AppDialogRequestEvent(Class<T> clazz, boolean isCancellable) {
		super();
		this.clazz = clazz;
		//this.activity = activity;
		this.isCancellable = isCancellable;
	}
	
	public AppDialogRequestEvent(Class<T> clazz, Bundle args, Fragment caller, boolean isCancellable) {
		super();
		this.clazz = clazz;
		//this.activity = activity;
		this.args = args;
		this.caller = caller;
		this.isCancellable = isCancellable;
	}

}
