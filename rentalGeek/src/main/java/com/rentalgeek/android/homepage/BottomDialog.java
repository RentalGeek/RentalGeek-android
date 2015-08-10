package com.rentalgeek.android.homepage;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.rentalgeek.android.R;
import com.rentalgeek.android.backend.AddStarBack;
import com.rentalgeek.android.database.PropertyTable;
import com.rentalgeek.android.ui.fragment.FragmentMap.FragmentCallback;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.utils.StaticClass;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.nettoast.Crouton;
import com.luttu.nettoast.Style;
import com.luttu.utils.GetSSl;
import com.luttu.utils.GlobalFunctions;
import com.luttu.utils.GlobalFunctions.HttpResponseHandler;
import com.squareup.picasso.Picasso;

public class BottomDialog {

	
	/**
	 * @author george
	 * 
	 * @purpose This is a dialog class, which pops Up when the user clicks on the marker in the FragmentMap
	 */
	
	
	Context context;
	FragmentCallback callBack;
	Dialog dialog;
	PropertyTable prop;
	AppPrefes appPref;
    private YoYo.YoYoString animation_obj;
	ImageView starredProp;
	View topView;
	ImageView image_prop;
	TextView price_range, street_name_inner, bed_inner, shower_inner;
	int which_marker_clicked;
	AsyncHttpClient client;
	PersistentCookieStore mCookieStore;

	public BottomDialog(int whichmarrker, Context context,
			FragmentCallback fragmentCallback) {
		// TODO Auto-generated constructor stub
		this.context = context;
		client = new AsyncHttpClient();
		appPref = new AppPrefes(context, "rentalgeek");
		which_marker_clicked = whichmarrker;
		callBack = fragmentCallback;
		callDialog();

	}

	private void callDialog() {
		// TODO Auto-generated method stub

		dialog = new Dialog(context, R.style.MyDialognew);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.map_dialog);

		final FrameLayout dialog_click = (FrameLayout) dialog
				.findViewById(R.id.dialog_click);
		price_range = (TextView) dialog.findViewById(R.id.price_dialog);
		street_name_inner = (TextView) dialog.findViewById(R.id.address_dialog);
		bed_inner = (TextView) dialog.findViewById(R.id.bed_dialog);
		image_prop=(ImageView)dialog.findViewById(R.id.image_prop);
		topView=(View)dialog.findViewById(R.id.top_view);
		starredProp = (ImageView) dialog.findViewById(R.id.starred_prop);
		shower_inner = (TextView) dialog.findViewById(R.id.shower_dialog);
		fetchFromDb(which_marker_clicked);
		if(prop!=null)
		{
			if(prop.starred)
			{
				System.out.println("starred");
				Picasso.with(context).load(R.drawable.star_select)
				.into(starredProp);
			}
			else
			{
				System.out.println("not starred");
				Picasso.with(context).load(R.drawable.star)
				.into(starredProp);
			}
		}
		
		topView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		

		dialog_click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				animation_obj=YoYo.with(Techniques.Hinge).duration(1000).playOn(dialog_click);
			 
				
			 	new CountDownTimer(1000, 1000) {

					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						dialog.dismiss();
						callBack.onTaskDone(which_marker_clicked);
					}
				}.start();
			 	
				

			}
		});

		starredProp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				starProperty();

			}
		});

		dialog.show();

	}

	private void fetchFromDb(int id) {
		// TODO Auto-generated method stub

		try {
			prop = new Select().from(PropertyTable.class).where("count = ?", id)
					.executeSingle();
			if(prop!=null)
			{
				price_range.setText("$" + String.valueOf(prop.monthly_rent_floor));
				street_name_inner.setText(String.valueOf(prop.headline));
				bed_inner.setText(String.valueOf(prop.bedroom_count));
				shower_inner.setText(String.valueOf(prop.full_bathroom_count));
				
				Picasso.with(context).load(prop.property_image).placeholder(R.drawable.white).error(R.drawable.banner).into(image_prop);
			}
			else
			{
				dialog.dismiss();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			
		}
	//	Picasso.with(context).load(fiilliste[2]).placeholder(R.drawable.banner).error(R.drawable.banner).into(image_prop);

	}

	public void starProperty() {
		new GetSSl().getssl(client);
		mCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(mCookieStore); 
		RequestParams params = new RequestParams();

		String Uid = appPref.getData("Uid");
		String suid = "" + prop.uid;
		System.out.println("Uid" + Uid);
		System.out.println("Uid" + suid);
		params.put("starred_property[applicant_id]", Uid);
		params.put("starred_property[rental_offering_id]", suid);

		String url = StaticClass.headlink + "/v2/starred_properties";

		GlobalFunctions.postApiCall(context, url, params, client,
				new HttpResponseHandler() {

					@Override
					public void handle(String response, boolean failre) {
						try {
							// TODO Auto-generated method stub
							System.out.println("dialog response "+response);
							AddStarBack detail = (new Gson()).fromJson(response.toString(),
									AddStarBack.class);
							if (failre) {
								Crouton crouton = Crouton.makeText(
										(ActivityHome) context,
										"Added to favorites", Style.ALERT);
								crouton.show();
								Picasso.with(context).load(R.drawable.star_select)
										.into(starredProp);
								prop.starred = true;
								prop.starred_property_id=detail.starred_property.id;
								prop.save();
							} else {
								
								
								if(detail.error!=null)
								{
									if(detail.error.equals("You need to sign in or sign up before continuing."))
									{
										((ActivityHome) context).finish();
									}
								}
								else
								{
									Crouton crouton = Crouton.makeText(
											(ActivityHome) context,
											"To unlike a property go to favorites", Style.ALERT);
									crouton.show();
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}
}
