package com.rentalgeek.android.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.rentalgeek.android.R;

import java.util.ArrayList;

/**
 * 
 * @author George
 * 
 * @purpose Dialog class which shows amenities available in the Rental Offerings
 *
 */

public class MoreAmenitiesDialog {

	Context context;
	//Dialog dialog;
	ArrayList<String> amen_list;

	public MoreAmenitiesDialog(ArrayList<String> amenities, Context c) {

		
		this.context = c;
		amen_list=amenities;
		CallDialog(amenities);
		
	}

	private void CallDialog(ArrayList<String> amenities) {


		Dialog	dialog = new Dialog(context);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.amenity_list);

		GridView grid = (GridView) dialog.findViewById(R.id.amen_list);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
				 R.layout.amenities_single, amen_list);

		grid.setAdapter(arrayAdapter);
		
		  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		    lp.copyFrom(dialog.getWindow().getAttributes());
		    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		    dialog.getWindow().setAttributes(lp);

		dialog.show();

	}

}