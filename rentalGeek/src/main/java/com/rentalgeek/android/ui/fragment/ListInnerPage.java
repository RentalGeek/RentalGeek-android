package com.rentalgeek.android.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint.Align;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseAbstract;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.AddStarBack;
import com.rentalgeek.android.backend.AddStarBack.StarredProperty;
import com.rentalgeek.android.backend.ApplyBackend;
import com.rentalgeek.android.backend.ApplyError;
import com.rentalgeek.android.database.PropertyTable;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.ui.dialog.MoreAmenitiesDialog;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ConnectionDetector;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ir.noghteh.JustifiedTextView;


public class ListInnerPage extends LuttuBaseAbstract {

	private static final String TAG = "ListInnerPage";

	@InjectView(R.id.main_image)
	ImageView main_image;

	@InjectView(R.id.amen_tag)
	TextView amen_tag;

	@InjectView(R.id.amenisties_lay)
	LinearLayout amenisties_lay;

	@InjectView(R.id.apply)
	ImageView apply;

	@InjectView(R.id.like)
	ImageView like;

	@InjectView(R.id.price_range_inner)
	TextView price_range;

	@InjectView(R.id.star_img)
	ImageView star_img;

	@InjectView(R.id.like_tag)
	TextView like_tag;

	@InjectView(R.id.street_name_inner)
	TextView street_name_inner;

	@InjectView(R.id.bed_inner)
	TextView bed_inner;

	@InjectView(R.id.shower_inner)
	TextView shower_inner;

	@InjectView(R.id.am0)
	TextView am0;

	@InjectView(R.id.am1)
	TextView am1;

	@InjectView(R.id.am2)
	TextView am2;

	@InjectView(R.id.am3)
	TextView am3;

	@InjectView(R.id.am4)
	JustifiedTextView am4;

	ArrayList<String> street_name;
    ArrayList<String> amenities = new ArrayList<String>();

    ConnectionDetector con;
    PropertyTable prop;
    AppPrefes appPref;

    private YoYo.YoYoString animation_obj;

    int[] fiilliste;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.listview_inner, container, false);
		ButterKnife.inject(this, v);
		appPref = new AppPrefes(getActivity(), "rentalgeek");
		con = new ConnectionDetector(getActivity());
		// fiilliste = new int[] { R.drawable.tennessee, R.drawable.kentucky,
		// R.drawable.congressional, R.drawable.gateway,
		// R.drawable.janadrive, R.drawable.kentucky,
		// R.drawable.summertree };

		Bundle b = getArguments();
		if (b != null) {
			int id_count = b.getInt("Count");
			// main_image.setImageResource(fiilliste[Image]);
			FetchDataFromDB(id_count);

		}
		return v;
	}

	private void FetchDataFromDB(int id) {
		try {

			prop = new Select().from(PropertyTable.class)
					.where("count = ?", id).executeSingle();

			Picasso.with(getActivity()).load(prop.property_image)
					.placeholder(R.drawable.white).error(R.drawable.banner)
					.into(main_image);

			price_range.setText(String.valueOf(prop.monthly_rent_floor));
			street_name_inner.setText(String.valueOf(prop.headline));
			bed_inner.setText(String.valueOf(prop.bedroom_count));
			shower_inner.setText(String.valueOf(prop.full_bathroom_count));

			System.out.println("the starred value " + prop.starred);

			if (prop.starred) {
				Picasso.with(getActivity()).load(R.drawable.star_select).into(star_img);
				Picasso.with(getActivity()).load(R.drawable.like).into(like);
				like_tag.setText("Like");
			} else {
				Picasso.with(getActivity()).load(R.drawable.star).into(star_img);
				like_tag.setText("Like");
				Picasso.with(getActivity()).load(R.drawable.unlikeb).into(like);
			}

			parseAmenities();

			am4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
			am4.setAlignment(Align.LEFT);
			am4.setLineSpacing(5);
			am4.setText(prop.salesy_description
					+ ". You can reach the property manager at "
					+ prop.customer_contact_email_address);
		} catch (Exception e) {
			AppLogger.log(TAG, e);
		}

	}

	@Override
	public void parseresult(String response, boolean success, int value) {

		switch (value) {
		case 1:
			addStar(response);
			break;
		case 2:
			removeStar(response);
			break;
		case 3:
			ApplyParse(response);
			break;
		case 4:
			removeStar(response);
			break;
		}

	}

	private void ApplyParse(String response) {

		System.out.println("the apply response id " + response);

		ApplyBackend detail = (new Gson()).fromJson(response.toString(),
				ApplyBackend.class);

		if (detail.apply == null) {

			if (detail.messsage != null)
				toastsuccess("already applied to the property");

		} else {
			toastsuccess("successfully applied to the property");
		}

	}

	private void removeStar(String response) {


		System.out.println("star response " + response);
		like_tag.setText("Like");
		Picasso.with(getActivity()).load(R.drawable.star).into(star_img);
		Picasso.with(getActivity()).load(R.drawable.unlikeb).into(like);
		prop.starred_property_id = null;
		prop.starred = false;
		prop.save();

		Intent refreshIntent = new Intent("likeref");
		Bundle args = new Bundle();
		args.putBoolean("remove_params", false);
		refreshIntent.putExtras(args);
		getActivity().sendBroadcast(refreshIntent);

	}

	private void addStar(String response) {

		System.out.println("star response " + response);
		AddStarBack detail = (new Gson()).fromJson(response.toString(), AddStarBack.class);

		StarredProperty session;
		session = detail.starred_property;

		toast("Property added to favorites");
		like_tag.setText("Like");
		Picasso.with(getActivity()).load(R.drawable.star_select).into(star_img);
		Picasso.with(getActivity()).load(R.drawable.like).into(like);
		prop.starred = true;
		prop.starred_property_id = detail.starred_property.id;
		prop.save();

		Intent refreshIntent = new Intent("likeref");
		Bundle args = new Bundle();
		args.putBoolean("remove_params", true);
		refreshIntent.putExtras(args);
		getActivity().sendBroadcast(refreshIntent);

	}

	@Override
	public void error(String response, int value) {

		System.out.println("error apply " + value + " res " + response);
		switch (value) {
		case 1:
			ErroraddStar(response);
			break;
		case 3:
			ErrorApply(response);
		default:
			break;
		}
	}

	private void ErrorApply(String response) {


		ApplyError detail = (new Gson()).fromJson(response.toString(), ApplyError.class);

		if (!detail.success) {

			if (detail.message.contains("FragmentProfile")) {
				messageProfile("Complete your profile in order to apply");

			} else if (detail.message.contains("FragmentPayment")) {
				messagePayment(getActivity().getResources().getString(R.string.geek_go));

			} else {
				toast(detail.message);
			}
		}

	}

	private void ErroraddStar(String response) {


		AddStarBack detail = (new Gson()).fromJson(response.toString(),
				AddStarBack.class);

		if (detail.getErrors() != null) {
			toast("Property already added");
		}
	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@OnClick({ R.id.like_lay, R.id.star_img })
	public void Like() {

		try {
			if (con.isConnectingToInternet()) {

				animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(like);

				if (prop.starred) {

					System.out.println("inside starred click");
					if (prop.starred_property_id != null) {
						asynkhttpDelete(2, ApiManager.getStarredPrpoertiesUrl(prop.starred_property_id), AppPreferences.getAuthToken(), true);
					}
				} else {

					System.out.println("inside un starred click");
					RequestParams params = new RequestParams();

					System.out.println("prop id is " + prop.uid + " user id is " + appPref.getData("Uid"));
					String Uid = appPref.getData("Uid");
					String suid = "" + prop.uid;
					System.out.println("Uid" + Uid);
					System.out.println("Uid" + suid);
					params.put("starred_property[user_id]", Uid);
					params.put("starred_property[rental_offering_id]", suid);

					asynkhttp(params, 1, ApiManager.getStarredPrpoertiesUrl(""), AppPreferences.getAuthToken(), true);

				}

			} else {
				toast("No Connection");
			}
		} catch (Exception e) {
			AppLogger.log(TAG, e);
		}

	}

	@OnClick(R.id.apply)
	public void Apply() {

		if (con.isConnectingToInternet()) {
			// show animation
			animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(apply);

			RequestParams params = new RequestParams();
			params.put("apply[applicable_id]", appPref.getData("Uid"));
			params.put("apply[rental_offering_id]", String.valueOf(prop.uid));
			params.put("apply[applicable_type]", "Applicant");

			System.out.println("prop id is " + prop.uid + " user id is " + appPref.getData("Uid"));

			asynkhttp(params, 3, ApiManager.getApplyUrl(), AppPreferences.getAuthToken(), true);
		} else {
			toast("No Connection");
		}

	}

	@OnClick(R.id.show_mre)
	public void show_mre() {

		if (amenities.size() > 4) {
			new MoreAmenitiesDialog(amenities, getActivity());
		} else {
			toast("No more amenities available");
		}

	}

	public void parseAmenities() {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {


				if (prop.buzzer_intercom) {
					amenities.add("Buzzer Intercom");
				}

				if (prop.central_air) {
					amenities.add("Central Air");
				}

				if (prop.deck_patio) {
					amenities.add("Deck Patio");
				}

				if (prop.dishwasher) {
					amenities.add("Dishwasher");
				}

				if (prop.doorman) {
					amenities.add("Doorman");
				}

				if (prop.elevator) {
					amenities.add("Elevator");
				}

				if (prop.fireplace) {
					amenities.add("Fireplace");
				}

				if (prop.gym) {
					amenities.add("Gym");
				}

				if (prop.hardwood_floor) {
					amenities.add("Hardwood Floor");
				}

				if (prop.new_appliances) {
					amenities.add("New Appliances");
				}

				if (prop.parking_garage) {
					amenities.add("Parking Garage");
				}

				if (prop.parking_outdoor) {
					amenities.add("Parking Outdoor");
				}

				if (prop.pool) {
					amenities.add("Pool");
				}
				if (prop.storage_space) {
					amenities.add("Storage Space");
				}
				if (prop.walkin_closet) {
					amenities.add("Walkin Closet");
				}
				if (prop.washer_dryer) {
					amenities.add("Washer Dryer");
				}
				if (prop.yard_private) {
					amenities.add("Yard Private");
				}
				if (prop.yard_shared) {
					amenities.add("Yard Shared");
				}
				if (prop.property_manager_accepts_cash) {
					amenities.add("Manager Accepts Cash");
				}
				if (prop.property_manager_accepts_checks) {
					amenities.add("Accepts Checks");
				}
				if (prop.property_manager_accepts_online_payments) {
					amenities.add("Online FragmentPayment");
				}
				if (prop.property_manager_accepts_money_orders) {
					amenities.add("Money Order");
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				super.onPostExecute(result);
				if (amenities.size() == 1) {
					am0.setText(amenities.get(0));
				} else if (amenities.size() == 2) {
					am0.setText(amenities.get(0));
					am0.setText(amenities.get(1));
				} else if (amenities.size() == 3) {
					am0.setText(amenities.get(0));
					am0.setText(amenities.get(1));
					am0.setText(amenities.get(2));
				} else if (amenities.size() > 4) {
					am0.setText(amenities.get(0));
					am1.setText(amenities.get(1));
					am2.setText(amenities.get(2));
					am3.setText(amenities.get(3));
				} else {

				}

				if (amenities.size() > 4) {
					setAminities();
				} else {
					amen_tag.setVisibility(View.GONE);
				}
				// setAminities();

			}

		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

	}

	private void setAminities() {

		try {
			final TextView[] myTextViews = new TextView[amenities.size()]; // create
																			// an
																			// empty
																			// array;

			for (int i = 0; i < amenities.size(); i++) {
				// create a new textview
				final TextView rowTextView = new TextView(getActivity());

				// set some properties of rowTextView or something
				LayoutParams params = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(4, 4, 4, 4);
				rowTextView.setText(getActivity().getResources().getString(
						R.string.bullet)
						+ "  " + amenities.get(i));

				// add the textview to the linearlayout
				amenisties_lay.addView(rowTextView);

				// save a reference to the textview for later
				myTextViews[i] = rowTextView;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// go to profile dialog
	private void messageProfile(String message) {


		try {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					getActivity());
			builder1.setMessage(message);
			builder1.setTitle("Alert");
			builder1.setCancelable(true);
			builder1.setPositiveButton("Go to profile",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							dialog.cancel();
							nextfragment(new FragmentProfile(), false, R.id.container);
						}
					});

			builder1.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			AlertDialog alert11 = builder1.create();
			alert11.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// go to payment dialog
	private void messagePayment(String message) {


		try {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					getActivity());
			builder1.setMessage(message);
			builder1.setTitle("Alert");
			builder1.setCancelable(true);
			builder1.setPositiveButton("Go to payment",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							dialog.cancel();
							nextfragment(new FragmentGeekScoreMain(), false,
									R.id.container);
						}
					});

			builder1.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			AlertDialog alert11 = builder1.create();
			alert11.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
