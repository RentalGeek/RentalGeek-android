package com.rentalgeek.android;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.crashlytics.android.Crashlytics;
import com.rentalgeek.android.database.ProfileTable;
import com.rentalgeek.android.database.PropertyTable;
import io.fabric.sdk.android.Fabric;


public class RentalGeekApplication extends Application{

	public static Context context;

	private static Thread.UncaughtExceptionHandler mDefaultUEH;
	private static Thread.UncaughtExceptionHandler mCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// Custom logic goes here

			// This will make Crashlytics do its job
			mDefaultUEH.uncaughtException(thread, ex);
		}
	};

	@Override
	public void onCreate() {

		super.onCreate();
        context = this;

		Fabric.with(this, new Crashlytics());

        mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(mCaughtExceptionHandler);

		initializeDB();
	}

	
	@SuppressWarnings("unchecked")
	private void initializeDB() {

		Configuration.Builder configurationBuilder = new Configuration.Builder(this);
		configurationBuilder.addModelClasses(PropertyTable.class);
		configurationBuilder.addModelClasses(ProfileTable.class);
		ActiveAndroid.initialize(configurationBuilder.create());
	}
	
}
