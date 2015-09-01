package com.rentalgeek.android.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.MapBackend;
import com.rentalgeek.android.database.PropertyTable;
import com.rentalgeek.android.homepage.BottomDialog;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ListUtils;
import com.rentalgeek.android.utils.StaticClass;

import java.util.HashMap;
import java.util.List;

public class FragmentMap extends GeekBaseFragment {

	private static final String TAG = "FragmentMap";

	SupportMapFragment supportMapFragment;
	GoogleMap myMap;
	boolean broadcast_flag = false;
	String image_url;
	AppPrefes appPref;
	HashMap<String, Integer> mMarkers = new HashMap<String, Integer>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.map, container, false);
		//con = new ConnectionDetector(getActivity());
		getActivity().registerReceiver(receiver, new IntentFilter("search"));
		supportmap();
		appPref = new AppPrefes(getActivity(), "rentalgeek");

		return v;
	}

/*	@Override
	public void parseresult(String response, boolean success, int value) {


		switch (value) {
		case 1:
			NormalMapParse(response, success);
			break;
		case 2:
			SearchFilterParse(response, success);
			break;
		case 3:
			BackgroundProcessing(response);
			break;
		default:
			break;
		}
	}*/

	private void BackgroundProcessing(final String response) {

		AppLogger.log(TAG, "back ground response " + response);

		new AsyncTask<Void, Void, Void>() {

			MapBackend detail;

			@Override
			protected void onPreExecute() {

				try {
					detail = (new Gson()).fromJson(response.toString(), MapBackend.class);
				} catch (JsonSyntaxException e) {
					AppLogger.log(TAG, e);
				}
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(Void result) {

				try {
					hideProgressDialog();
					Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
					if (f instanceof FragmentMap) {
						myMap.clear();
						setmarkersFromDB();
					}
				} catch (Exception e) {
                    AppLogger.log(TAG, e);
				}

				super.onPostExecute(result);
			}

			@Override
			protected Void doInBackground(Void... arg) {

				if (detail.rental_offerings.size() > 0) {

					new Delete().from(PropertyTable.class).execute();
					for (int i = 0; i < detail.rental_offerings.size(); i++) {

						image_url = "";

						if (!ListUtils.isNullOrEmpty(detail.rental_offerings.get(i).primary_property_photo_url)) {

							for (int j = 0; j < detail.rental_offerings.get(i).primary_property_photo_url.size(); j++) {
								image_url = detail.rental_offerings.get(i).primary_property_photo_url
										.get(0).photo_full_url;

							}
						} else {
							image_url = "missing.png";
						}

						try {

							ActiveAndroid.beginTransaction();
							PropertyTable dbobj = new PropertyTable(
									i,
									detail.rental_offerings.get(i).id,
									detail.rental_offerings.get(i).rental_complex_latitude,
									detail.rental_offerings.get(i).rental_complex_longitude,
									detail.rental_offerings.get(i).bedroom_count,
									detail.rental_offerings.get(i).monthly_rent_floor,
									detail.rental_offerings.get(i).monthly_rent_ceiling,
									detail.rental_offerings.get(i).headline,
									detail.rental_offerings.get(i).full_bathroom_count,
									detail.rental_offerings.get(i).rental_offering_type,
									detail.rental_offerings.get(i).customer_contact_email_address,
									detail.rental_offerings.get(i).rental_complex_name,
									detail.rental_offerings.get(i).rental_complex_full_address,
									detail.rental_offerings.get(i).rental_complex_street_name,
									detail.rental_offerings.get(i).rental_complex_cross_street_name,
									detail.rental_offerings.get(i).starred,
									detail.rental_offerings.get(i).buzzer_intercom,
									detail.rental_offerings.get(i).central_air,
									detail.rental_offerings.get(i).deck_patio,
									detail.rental_offerings.get(i).dishwasher,
									detail.rental_offerings.get(i).doorman,
									detail.rental_offerings.get(i).elevator,
									detail.rental_offerings.get(i).fireplace,
									detail.rental_offerings.get(i).gym,
									detail.rental_offerings.get(i).hardwood_floor,
									detail.rental_offerings.get(i).new_appliances,
									detail.rental_offerings.get(i).parking_garage,
									detail.rental_offerings.get(i).parking_outdoor,
									detail.rental_offerings.get(i).pool,
									detail.rental_offerings.get(i).storage_space,
									detail.rental_offerings.get(i).vaulted_ceiling,
									detail.rental_offerings.get(i).walkin_closet,
									detail.rental_offerings.get(i).washer_dryer,
									detail.rental_offerings.get(i).yard_private,
									detail.rental_offerings.get(i).yard_shared,
									detail.rental_offerings.get(i).property_manager_accepts_cash,
									detail.rental_offerings.get(i).property_manager_accepts_checks,
									detail.rental_offerings.get(i).property_manager_accepts_credit_cards_offline,
									detail.rental_offerings.get(i).property_manager_accepts_online_payments,
									detail.rental_offerings.get(i).property_manager_accepts_money_orders,
									image_url,
									detail.rental_offerings.get(i).salesy_description,
									detail.rental_offerings.get(i).starred_property_id);
							dbobj.save();

							ActiveAndroid.setTransactionSuccessful();
						} catch (Exception e) {

                            AppLogger.log(TAG, e);
						} finally {
							ActiveAndroid.endTransaction();
						}

					}

				}

				return null;
			}

		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

	}

	private void SearchFilterParse(String response, boolean failre) {

		// broadcast_flag=false;
		System.out.println("filter parse");

		MapBackend detail = (new Gson()).fromJson(response.toString(), MapBackend.class);

		if (detail.rental_offerings.size() > 0) {
			appPref.SaveData("bysearch", "yes");
			myMap.setMyLocationEnabled(true);
			// myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
			// detail.rental_offerings.get(0).rental_complex_latitude,
			// detail.rental_offerings.get(0).rental_complex_longitude), 5));

			new Delete().from(PropertyTable.class).execute();

			for (int i = 0; i < detail.rental_offerings.size(); i++) {

				image_url = "";

				if (detail.rental_offerings.get(i).primary_property_photo_url
						.size() > 0) {

					image_url = detail.rental_offerings.get(i).primary_property_photo_url
							.get(0).photo_full_url;

				} else {
					image_url = "missing.png";
				}

				ActiveAndroid.beginTransaction();
				try {
					PropertyTable dbobj = new PropertyTable(
							i,
							detail.rental_offerings.get(i).id,
							detail.rental_offerings.get(i).rental_complex_latitude,
							detail.rental_offerings.get(i).rental_complex_longitude,
							detail.rental_offerings.get(i).bedroom_count,
							detail.rental_offerings.get(i).monthly_rent_floor,
							detail.rental_offerings.get(i).monthly_rent_ceiling,
							detail.rental_offerings.get(i).headline,
							detail.rental_offerings.get(i).full_bathroom_count,
							detail.rental_offerings.get(i).rental_offering_type,
							detail.rental_offerings.get(i).customer_contact_email_address,
							detail.rental_offerings.get(i).rental_complex_name,
							detail.rental_offerings.get(i).rental_complex_full_address,
							detail.rental_offerings.get(i).rental_complex_street_name,
							detail.rental_offerings.get(i).rental_complex_cross_street_name,
							detail.rental_offerings.get(i).starred,
							detail.rental_offerings.get(i).buzzer_intercom,
							detail.rental_offerings.get(i).central_air,
							detail.rental_offerings.get(i).deck_patio,
							detail.rental_offerings.get(i).dishwasher,
							detail.rental_offerings.get(i).doorman,
							detail.rental_offerings.get(i).elevator,
							detail.rental_offerings.get(i).fireplace,
							detail.rental_offerings.get(i).gym,
							detail.rental_offerings.get(i).hardwood_floor,
							detail.rental_offerings.get(i).new_appliances,
							detail.rental_offerings.get(i).parking_garage,
							detail.rental_offerings.get(i).parking_outdoor,
							detail.rental_offerings.get(i).pool,
							detail.rental_offerings.get(i).storage_space,
							detail.rental_offerings.get(i).vaulted_ceiling,
							detail.rental_offerings.get(i).walkin_closet,
							detail.rental_offerings.get(i).washer_dryer,
							detail.rental_offerings.get(i).yard_private,
							detail.rental_offerings.get(i).yard_shared,
							detail.rental_offerings.get(i).property_manager_accepts_cash,
							detail.rental_offerings.get(i).property_manager_accepts_checks,
							detail.rental_offerings.get(i).property_manager_accepts_credit_cards_offline,
							detail.rental_offerings.get(i).property_manager_accepts_online_payments,
							detail.rental_offerings.get(i).property_manager_accepts_money_orders,
							image_url,
							detail.rental_offerings.get(i).salesy_description,
							detail.rental_offerings.get(i).starred_property_id);
					dbobj.save();

					ActiveAndroid.setTransactionSuccessful();
				} catch (Exception e) {
                    AppLogger.log(TAG, e);
				} finally {
					ActiveAndroid.endTransaction();
				}

			}

			setmarkersFromDB();
		} else {
			DialogManager.showCrouton(activity, "No results");
		}

	}

	private void NormalMapParse(final String response, Boolean failure) {

		AppLogger.log(TAG, "response is " + response);

		myMap.setMyLocationEnabled(true);

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {

				showProgressDialog(R.string.dialog_msg_loading);
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {

				MapBackend detail = (new Gson()).fromJson(response.toString(), MapBackend.class);

				if (detail.rental_offerings.size() > 0) {

					new Delete().from(PropertyTable.class).execute();
					for (int i = 0; i < detail.rental_offerings.size(); i++) {

						image_url = "";

						if (!ListUtils.isNullOrEmpty(detail.rental_offerings.get(i).primary_property_photo_url)) {

							for (int j = 0; j < detail.rental_offerings.get(i).primary_property_photo_url
									.size(); j++) {
								image_url = detail.rental_offerings.get(i).primary_property_photo_url
										.get(0).photo_full_url;

							}
						} else {
							image_url = "missing.png";
						}

						try {

							ActiveAndroid.beginTransaction();
							PropertyTable dbobj = new PropertyTable(
									i,
									detail.rental_offerings.get(i).id,
									detail.rental_offerings.get(i).rental_complex_latitude,
									detail.rental_offerings.get(i).rental_complex_longitude,
									detail.rental_offerings.get(i).bedroom_count,
									detail.rental_offerings.get(i).monthly_rent_floor,
									detail.rental_offerings.get(i).monthly_rent_ceiling,
									detail.rental_offerings.get(i).headline,
									detail.rental_offerings.get(i).full_bathroom_count,
									detail.rental_offerings.get(i).rental_offering_type,
									detail.rental_offerings.get(i).customer_contact_email_address,
									detail.rental_offerings.get(i).rental_complex_name,
									detail.rental_offerings.get(i).rental_complex_full_address,
									detail.rental_offerings.get(i).rental_complex_street_name,
									detail.rental_offerings.get(i).rental_complex_cross_street_name,
									detail.rental_offerings.get(i).starred,
									detail.rental_offerings.get(i).buzzer_intercom,
									detail.rental_offerings.get(i).central_air,
									detail.rental_offerings.get(i).deck_patio,
									detail.rental_offerings.get(i).dishwasher,
									detail.rental_offerings.get(i).doorman,
									detail.rental_offerings.get(i).elevator,
									detail.rental_offerings.get(i).fireplace,
									detail.rental_offerings.get(i).gym,
									detail.rental_offerings.get(i).hardwood_floor,
									detail.rental_offerings.get(i).new_appliances,
									detail.rental_offerings.get(i).parking_garage,
									detail.rental_offerings.get(i).parking_outdoor,
									detail.rental_offerings.get(i).pool,
									detail.rental_offerings.get(i).storage_space,
									detail.rental_offerings.get(i).vaulted_ceiling,
									detail.rental_offerings.get(i).walkin_closet,
									detail.rental_offerings.get(i).washer_dryer,
									detail.rental_offerings.get(i).yard_private,
									detail.rental_offerings.get(i).yard_shared,
									detail.rental_offerings.get(i).property_manager_accepts_cash,
									detail.rental_offerings.get(i).property_manager_accepts_checks,
									detail.rental_offerings.get(i).property_manager_accepts_credit_cards_offline,
									detail.rental_offerings.get(i).property_manager_accepts_online_payments,
									detail.rental_offerings.get(i).property_manager_accepts_money_orders,
									image_url,
									detail.rental_offerings.get(i).salesy_description,
									detail.rental_offerings.get(i).starred_property_id);
							dbobj.save();

							ActiveAndroid.setTransactionSuccessful();
						} catch (Exception e) {
							AppLogger.log(TAG, e);
						} finally {
							ActiveAndroid.endTransaction();
						}

					}

				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				try {
					hideProgressDialog();
					setmarkersFromDB();
				} catch (Exception e) {
					AppLogger.log(TAG, e);
				}

				super.onPostExecute(result);
			}

		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

	}

//	@Override
//	public void error(String response, int value) {
//		DialogManager.showCrouton(activity, "failure");
//	}

	private void supportmap() {

		supportMapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.map, supportMapFragment);
		fragmentTransaction.commit();

	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			if (myMap == null) {
				myMap = supportMapFragment.getMap();

				if (appPref.getData("bysearch").equals("yes")) {
					AppLogger.log(TAG, "map jumbo search");
					setmarkersFromDB();
					StaticClass.bySearch = false;
				} else {
					try {
						if (new Select().from(PropertyTable.class).execute().size() > 0) {

							new CountDownTimer(1000, 1000) {

								@Override
								public void onTick(long millisUntilFinished) {

								}

								@Override
								public void onFinish() {
									setmarkersFromDB();
									showPropertyInMapBackground();
								}
							}.start();

						} else {
							showPropertyInMap();
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}

			}
		} catch (Exception e) {
            AppLogger.log(TAG, e);
		}

	}

	public void showPropertyInMap() {
		// progressshow();
		String url = ApiManager.getPropertySearchUrl("");
		GlobalFunctions.getApiCall(getActivity(), url,
				AppPreferences.getAuthToken(),
				new GeekHttpResponseHandler() {

					@Override
					public void onBeforeStart() {

					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onSuccess(String content) {
						try {
                            NormalMapParse(content, true);
						} catch (Exception e) {
							AppLogger.log(TAG, e);
						}
					}

					@Override
					public void onAuthenticationFailed() {

					}
				});
		//asynkhttpGet(1, url, AppPreferences.getAuthToken(), true);
	}

	private void drawMarker(LatLng point, int bedroom_count, String y,
			int mark_count) {
		// Creating an instance of MarkerOptions

		MarkerOptions second = new MarkerOptions();

		if (bedroom_count > 4) {
			second.icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(
					R.drawable.marker, "4+")));

		} else {
			second.icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(
					R.drawable.marker, String.valueOf(bedroom_count))));

		}

		second.position(point);
		Marker mark_my = myMap.addMarker(second);
		mMarkers.put(mark_my.getId(), mark_count);
		// System.out
		// .println(" the bedroom count " + bedroom_count + " mark_count "
		// + mark_count + " marker id " + mark_my.getId());

	}

	private Bitmap writeTextOnDrawable(int drawableId, String text) {

		Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
				.copy(Bitmap.Config.ARGB_8888, true);

		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.WHITE);
		paint.setTypeface(tf);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(convertToPixels(getActivity(), 9));

		Rect textRect = new Rect();
		paint.getTextBounds(text, 0, text.length(), textRect);

		Canvas canvas = new Canvas(bm);

		// If the text is bigger than the canvas , reduce the font size
		if (textRect.width() >= (canvas.getWidth() - 5)) // the padding on
															// either sides is
															// considered as 4,
															// so as to
															// appropriately fit
															// in the text
			paint.setTextSize(convertToPixels(getActivity(), 8)); // Scaling
																	// needs to
		// be used for
		// different dpi's

		// Calculate the positions
		int xPos = (canvas.getWidth() / 2) - 2; // -2 is for regulating the x
												// position offset

		// "- ((paint.descent() + paint.ascent()) / 2)" is the distance from the
		// baseline to the center.
		int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint
				.ascent()) / 2));

		canvas.drawText(text, xPos, yPos, paint);

		return bm;
	}

	public static int convertToPixels(Context context, int nDP) {
		final float conversionScale = context.getResources()
				.getDisplayMetrics().density;

		return (int) ((nDP * conversionScale) + 0.5f);

	}

	private void setmarkersFromDB() {


		Select select = new Select();
		List<PropertyTable> people = select.all().from(PropertyTable.class).execute();

		myMap.clear();
		mMarkers = null;
		mMarkers = new HashMap<String, Integer>();
		// mMarkers.clear();

		if (people.size() > 0) {
			for (int i = 0; i < people.size(); i++) {
				LatLng point = new LatLng(
						people.get(i).rental_complex_latitude,
						people.get(i).rental_complex_longitude);

				if (i == 0) {
					myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(people.get(0).rental_complex_latitude,
									people.get(0).rental_complex_longitude), 11));
				}

				drawMarker(point, people.get(i).bedroom_count,
						people.get(i).monthly_rent_floor + "-"
								+ people.get(i).monthly_rent_ceiling, i);
			}

		} else {
			DialogManager.showCrouton(activity, "No properties");
		}

		myMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				// Fragment innerlist = new ListInnerPage();
				// Bundle args = new Bundle();
				//
				// args.putInt("Count", Integer.parseInt(arg0.getTitle()));
				// innerlist.setArguments(args);
				//
				// nextfragment(innerlist, true, R.id.container);
				// addfragment(innerlist, true, R.id.container);

				// show dialog on click
				// new BottomDialog(getActivity());

				int id = mMarkers.get(arg0.getId());

				System.out.println("the clicked marked id is " + id);

				BottomDialog testAsyncTask = new BottomDialog(id,
						getActivity(), new FragmentCallback() {

							@Override
							public void onTaskDone(final int marker_value) {
								Fragment innerlist = new ListInnerPage();
								Bundle args = new Bundle();

								args.putInt("Count", marker_value);
								innerlist.setArguments(args);

                                nextfragment(innerlist, true, R.id.container);
							}
						});

				return false;
			}
		});

	}

	private void SearchViaLocation(String location) {

		broadcast_flag = true;

		String url = ApiManager.getPropertySearchUrl(location);

		System.out.println("the search url map is " + url);

		if (!location.equals("")) {
			GlobalFunctions.getApiCall(getActivity(), url,
					AppPreferences.getAuthToken(),
					new GeekHttpResponseHandler() {

						@Override
						public void onBeforeStart() {

						}

						@Override
						public void onFinish() {

						}

						@Override
						public void onSuccess(String content) {
							try {
                                SearchFilterParse(content, true);
							} catch (Exception e) {
								AppLogger.log(TAG, e);
							}
						}

						@Override
						public void onAuthenticationFailed() {

						}
					});
			//asynkhttpGet(2, url, AppPreferences.getAuthToken(), true);
		} else {
			toast("Select Filters");
		}

	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			System.out.println("inside on recieve ");

			Bundle b = intent.getExtras();
			if (b != null) {
				String loc = b.getString("params");
				System.out.println("the location si " + loc);
				// if(!broadcast_flag)
				SearchViaLocation(loc);

			}

		}
	};

	// Interface of clicked item from the dialog

	public interface FragmentCallback {
		public void onTaskDone(int mark_value);
	}

	@Override
	public void onDestroy() {


		getActivity().unregisterReceiver(receiver);
		super.onDestroy();
	}

	public void showPropertyInMapBackground() {
		// progressshow();
		String url = ApiManager.getPropertySearchUrl("");
        GlobalFunctions.getApiCall(getActivity(), url,
                AppPreferences.getAuthToken(),
                new GeekHttpResponseHandler() {

                    @Override
                    public void onBeforeStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onSuccess(String content) {
                        try {
                            SearchFilterParse(content, true);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
        //asynkhttpGet(3, url, AppPreferences.getAuthToken(), false);
	}

}
