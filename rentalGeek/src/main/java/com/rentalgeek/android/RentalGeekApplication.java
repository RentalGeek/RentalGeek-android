package com.rentalgeek.android;


import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.crashlytics.android.Crashlytics;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.NonEvent;
import com.rentalgeek.android.bus.events.UserNotificationEvent;
import com.rentalgeek.android.database.ProfileTable;
import com.rentalgeek.android.database.PropertyTable;
import com.rentalgeek.android.system.AppSystem;

import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;

public class RentalGeekApplication extends Application {

    public static final String GCM_CLIENT_ID = "433959508661";

    public static Context context;
	public static final EventBus eventBus = EventBus.builder().logSubscriberExceptions(false).throwSubscriberException(false).build();

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		//MultiDex.install(this);
	}

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


		AppEventBus.register(this);
		/// AppEventBus.register(AppSystem.Instance);


		initializeDB();

        AppSystem.Instance.checkGCMRegistration(this);
	}


	@SuppressWarnings("unchecked")
	private void initializeDB() {

		Configuration.Builder configurationBuilder = new Configuration.Builder(this);
		configurationBuilder.addModelClasses(PropertyTable.class);
		configurationBuilder.addModelClasses(ProfileTable.class);
		ActiveAndroid.initialize(configurationBuilder.create());
	}

	public static void postUserNotification(int resId) {
		String message = context.getString(resId);
		eventBus.post(new UserNotificationEvent(message));
	}

	public static void postUserNotification(String message) {
		eventBus.post(new UserNotificationEvent(message));
	}

    public static float getDimension(int dimenId) {
        return context.getResources().getDimension(dimenId);
    }
    
	public static String getResourceString(int resId) {
		return context.getString(resId);
	}

	public static String getResourceString(int resId, Object... formatArgs) {
		return context.getString(resId, formatArgs);
	}

	public static String getStringDefault(String string, int defaultResId) {
		if (TextUtils.isEmpty(string)) return getResourceString(defaultResId);
		return string;
	}

    public static int getColor(int colorId) {
        return context.getResources().getColor(colorId);
    }

    public static String[] getStringArray(int resId) {
        return context.getResources().getStringArray(resId);
    }

    public static int getScreenHeight() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static int getScreenWidth() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

	public void onEvent(NonEvent event) {

	}
}
