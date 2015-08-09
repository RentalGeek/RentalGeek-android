package com.app.rentalgeek;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.app.rentalgeek.database.ProfileTable;
import com.app.rentalgeek.database.PropertyTable;


/**
 * 
 * @author George
 * 
 * @purpose Application class which handles the active android operations
 *
 */
public class RentalApplication extends Application{
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		initializeDB();
	}

	
	@SuppressWarnings("unchecked")
	private void initializeDB() {
		// TODO Auto-generated method stub
		Configuration.Builder configurationBuilder = new Configuration.Builder(
				this);
		configurationBuilder.addModelClasses(PropertyTable.class);
		configurationBuilder.addModelClasses(ProfileTable.class);
		ActiveAndroid.initialize(configurationBuilder.create());
	}
	
}
