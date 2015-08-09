package com.app.rentalgeek.geekvision;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.app.rentalgeek.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.luttu.fragmentutils.LuttuBaseAbstract;


/**
 * 
 * @author George
 * 
 * @purpose This class which shows the Augment reality[ Current work in progress]
 *
 */
public class GeekVision extends LuttuBaseAbstract{
	private YoYo.YoYoString animation_obj;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v= inflater.inflate(R.layout.geekvision, container,false);
		ButterKnife.inject(this,v);
		return v;
	}

	@Override
	public void parseresult(String response, boolean success, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(String response, int value) {
		// TODO Auto-generated method stub
		
	}
	
	@OnClick(R.id.coming_soon)
	public void ComingSoon(View v)
	{
		animation_obj=YoYo.with(Techniques.Tada).duration(1000).playOn(v);
	}

}
