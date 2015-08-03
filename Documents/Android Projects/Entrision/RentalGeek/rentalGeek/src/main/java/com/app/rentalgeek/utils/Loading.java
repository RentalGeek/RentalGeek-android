package com.app.rentalgeek.utils;



import com.app.rentalgeek.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 
 * @author George
 * 
 * @purpose Utils class which shoes Loading
 *
 */
public class Loading  {
	Dialog dialog;
	Context context;
	Animation rotation;
	ImageView img;
	
	
	
	public Loading(Context context) {
		// TODO Auto-generated constructor stub
		
		this.context = context;
		dialog = new Dialog(context, android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.loadingicon);
		img = (ImageView) dialog.findViewById(R.id.progressBar1);
		img.setBackgroundResource(R.drawable.loadinga);
		rotation = AnimationUtils.loadAnimation(context,
				R.anim.clockwise_rotation);
		rotation.setRepeatCount(Animation.INFINITE);
		dialog.setCanceledOnTouchOutside(false);
		
	}
	
	public void Diashow() {
		// TODO Auto-generated method stub
		dialog.show();
		img.startAnimation(rotation);
	}

	public void Diacancel() {
		// TODO Auto-generated method stub
		
		dialog.dismiss();
	
	}
	
	public void remove()
	{
		 if(dialog != null)
			 dialog.dismiss();
		 dialog = null;
	}
}
