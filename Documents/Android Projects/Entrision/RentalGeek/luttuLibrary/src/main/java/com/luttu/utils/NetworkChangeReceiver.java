package com.luttu.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {
	public static Internet internet;
	public static String changestatus = "";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		String status = NetworkUtil.getConnectivityStatusString(context);
		System.out
				.println("---------------------------****************-------------------------"
						+ status);
		if (status.equals("Not connected to Internet")) {
			if (context != null && internet != null) {
				internet.net();
			}
		}
		changestatus = status;
	}

	public interface Internet {
		public void net();
	}
}
