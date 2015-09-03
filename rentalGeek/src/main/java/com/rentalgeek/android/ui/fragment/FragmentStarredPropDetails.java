package com.rentalgeek.android.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint.Align;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.ApplyBackend;
import com.rentalgeek.android.backend.ApplyError;
import com.rentalgeek.android.backend.StarredInnerBacked;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.dialog.MoreAmenitiesDialog;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ConnectionDetector;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ir.noghteh.JustifiedTextView;

/**
 * 
 * @author George
 * 
 * @purpose Fragment Which shows the details about the details of the user
 *          favorite properties
 *
 */
public class FragmentStarredPropDetails extends GeekBaseFragment {

	private static final String TAG = "FragmentStarredPropDetails";

	@InjectView(R.id.price_range_inner)
	TextView price_range_inner;
	
	@InjectView(R.id.amen_tag)
	TextView amen_tag;

	@InjectView(R.id.street_name_inner)
	TextView street_name_inner;
	
	@InjectView(R.id.amenisties_lay)
	LinearLayout amenisties_lay;

	ArrayList<String> amenities = new ArrayList<String>();

	StarredInnerBacked detail;

	@InjectView(R.id.bed_inner)
	TextView bed_inner;

	@InjectView(R.id.shower_inner)
	TextView shower_inner;

	@InjectView(R.id.like)
	ImageView dislike;

	@InjectView(R.id.apply)
	ImageView apply;

	@InjectView(R.id.am4)
	JustifiedTextView description;

	@InjectView(R.id.like_tag)
	TextView like_tag;

	@InjectView(R.id.main_image)
	ImageView main_image;

	@InjectView(R.id.am0)
	TextView am0;

	@InjectView(R.id.am1)
	TextView am1;

	@InjectView(R.id.am2)
	TextView am2;

	@InjectView(R.id.am3)
	TextView am3;

	@InjectView(R.id.star_img)
	ImageView star_img;

	AppPrefes appPref;

	Context StarredPropDetailsContext;
	ConnectionDetector con;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View v = inflater.inflate(R.layout.listview_inner, container, false);
		ButterKnife.inject(this, v);
		like_tag.setText("Dislike");
		appPref = new AppPrefes(getActivity(), "rentalgeek");
		Bundle b = getArguments();
		like_tag.setTag(b.getString("star_id").toString());
		con = new ConnectionDetector(getActivity());

		if (con.isConnectingToInternet()) {
			detailsFetchServer(b.getString("rental_id"));
		} else {
			toast("No Connection");
		}

		StarredPropDetailsContext = getActivity();
		return v;

	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
		ButterKnife.reset(this);
	}

	private void detailsFetchServer(String id) {
		String url = ApiManager.getPropertySearchUrl(id);
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
							DetailsParse(content);
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

/*	@Override
	public void parseresult(String response, boolean success, int value) {


		switch (value) {
		case 1:
			DetailsParse(response);
			break;
		case 2:
			removeStar(response);
			break;
		case 3:
			ApplyParse(response);
			break;
		default:
			break;
		}

	}*/

	private void ApplyParse(String response) {

		System.out.println("the apply response id " + response);

		ApplyBackend detail = (new Gson()).fromJson(response.toString(), ApplyBackend.class);
		
		if (detail.apply == null) {

			if (detail.messsage != null)
				toast("Already Applied to the property");

		} else {
			toast("Successfully Applied to the property");
		}


	}

	private void DetailsParse(String response) {
		try {

			System.out.println("response inner list fav " + response);
			detail = (new Gson()).fromJson(response.toString(),
					StarredInnerBacked.class);

			if (detail.rental_offering != null) {

				if (detail.rental_offering.starred) {
					Picasso.with(getActivity()).load(R.drawable.star_select)
							.into(star_img);
					Picasso.with(getActivity()).load(R.drawable.like)
					.into(dislike);
				} else {
					Picasso.with(getActivity()).load(R.drawable.star)
							.into(star_img);
				}

				if (detail.rental_offering.primary_property_photo_url.size() > 0) {
					if (detail.rental_offering.primary_property_photo_url
							.get(0).primary_photo) {
						Picasso.with(getActivity())
								.load(detail.rental_offering.primary_property_photo_url
										.get(0).photo_full_url)
								.placeholder(R.drawable.white)
								.error(R.drawable.banner).into(main_image);
					}
				}

				price_range_inner
						.setText(detail.rental_offering.monthly_rent_floor);
				shower_inner
						.setText(detail.rental_offering.full_bathroom_count);
				bed_inner.setText(detail.rental_offering.bedroom_count);
				street_name_inner.setText(detail.rental_offering.headline);

				description.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
				description.setAlignment(Align.LEFT);
				description.setLineSpacing(5);
				description
						.setText(detail.rental_offering.salesy_description
								+ ". You can reach the property manager at "
								+ detail.rental_offering.customer_contact_email_address);

				parseAmenities();
			}
		} catch (Exception e) {
			AppLogger.log(TAG, e);
		}

	}

/*	@Override
	public void error(String response, int value) {

		System.out.println("the response " + response);
		switch (value) {
		case 3:
			ErrorApply(response);
			break;
		case 2:
			toast("Already removed");
			break;
		default:
			toast("Something went wrong");
			break;
		}
	}*/

	private void ErrorApply(String response) {


		try {
			ApplyError detail = (new Gson()).fromJson(response.toString(),
					ApplyError.class);

			if (detail != null && !detail.success) {
				
				if (detail.message.contains("FragmentProfile")) {
					messageProfile("Complete your profile in order to apply");

				} else if (detail.message.contains("FragmentPayment")) {
					messagePayment("You must create your geekscore to apply");

				}
				
				
			}
		} catch (Exception e) {
			AppLogger.log(TAG, e);
		}

	}

	@OnClick(R.id.show_mre)
	public void show_mre() {
		if (detail.rental_offering != null) {

			if (amenities.size() > 4) {
				new MoreAmenitiesDialog(amenities, StarredPropDetailsContext);
			} else {
				toast("No more amenities available");
			}

		}
	}

	@OnClick({R.id.like,R.id.star_img})
	public void Dislike() {

		String url = ApiManager.getStarredPrpoertiesUrl(like_tag.getTag().toString());

		AppLogger.log(TAG, "the tag in unstar " + url);

        GlobalFunctions.deleteApiCall(getActivity(), url,
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
                            removeStar(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
        //asynkhttpDelete(2, url, AppPreferences.getAuthToken(), true);
	}

	private void removeStar(String response) {


		toast("Property removed");
		Picasso.with(getActivity()).load(R.drawable.star).into(star_img);
		goBack();

	}

	private void goBack() {

		Intent refreshIntent = new Intent("dislikeref");
		Bundle args = new Bundle();
		args.putString("remove_params", like_tag.getTag().toString().trim());
		refreshIntent.putExtras(args);
		getActivity().sendBroadcast(refreshIntent);
		FragmentManager fm = getActivity().getSupportFragmentManager();

		fm.popBackStack();

	}

	public void parseAmenities() {

		if (detail.rental_offering != null) {

			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {

					if (detail.rental_offering.buzzer_intercom) {
						amenities.add("Buzzer Intercom");
					}

					if (detail.rental_offering.central_air) {
						amenities.add("Central Air");
					}

					if (detail.rental_offering.deck_patio) {
						amenities.add("Deck Patio");
					}

					if (detail.rental_offering.dishwasher) {
						amenities.add("Dishwasher");
					}

					if (detail.rental_offering.doorman) {
						amenities.add("Doorman");
					}

					if (detail.rental_offering.elevator) {
						amenities.add("Elevator");
					}

					if (detail.rental_offering.fireplace) {
						amenities.add("Fireplace");
					}

					if (detail.rental_offering.gym) {
						amenities.add("Gym");
					}

					if (detail.rental_offering.hardwood_floor) {
						amenities.add("Hardwood Floor");
					}

					if (detail.rental_offering.new_appliances) {
						amenities.add("New Appliances");
					}

					if (detail.rental_offering.parking_garage) {
						amenities.add("Parking Garage");
					}

					if (detail.rental_offering.parking_outdoor) {
						amenities.add("Parking Outdoor");
					}

					if (detail.rental_offering.pool) {
						amenities.add("Pool");
					}
					if (detail.rental_offering.storage_space) {
						amenities.add("Storage Space");
					}
					if (detail.rental_offering.walkin_closet) {
						amenities.add("Walkin Closet");
					}
					if (detail.rental_offering.washer_dryer) {
						amenities.add("Washer Dryer");
					}
					if (detail.rental_offering.yard_private) {
						amenities.add("Yard Private");
					}
					if (detail.rental_offering.yard_shared) {
						amenities.add("Yard Shared");
					}
					if (detail.rental_offering.property_manager_accepts_cash) {
						amenities.add("Manager Accepts Cash");
					}
					if (detail.rental_offering.property_manager_accepts_checks) {
						amenities.add("Accepts Checks");
					}
					if (detail.rental_offering.property_manager_accepts_online_payments) {
						amenities.add("Online FragmentPayment");
					}
					if (detail.rental_offering.property_manager_accepts_money_orders) {
						amenities.add("Money Order");
					}
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {

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
					
					if(amenities.size()>4)
					{
						setAminities();
					}
					else
					{
						amen_tag.setVisibility(View.GONE);
					}

				};

			}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		}

	}

	@OnClick(R.id.apply)
	public void Apply() {
		RequestParams params = new RequestParams();
		params.put("apply[applicable_id]", appPref.getData("Uid"));
		params.put("apply[rental_offering_id]",
				String.valueOf(detail.rental_offering.id));
		params.put("apply[applicable_type]", "Applicant");

        GlobalFunctions.postApiCall(activity, ApiManager.getApplyUrl(), params,  AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

            @Override
            public void onBeforeStart() {
                super.onBeforeStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                ApplyParse(content);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
            }
        });
		//asynkhttp(params, 3, ApiManager.getApplyUrl(), AppPreferences.getAuthToken(), true);
	}
	
	private void setAminities() {

		try {
			final TextView[] myTextViews = new TextView[amenities.size()]; // create an empty array;

			for (int i = 0; i < amenities.size(); i++) {
			    // create a new textview
			    final TextView rowTextView = new TextView(getActivity());
			    
			    // set some properties of rowTextView or something
			    LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    params.setMargins(4, 4, 4, 4);
			    rowTextView.setText(getActivity().getResources().getString(R.string.bullet)+"  "+amenities.get(i));
			    

			    // add the textview to the linearlayout
			    amenisties_lay.addView(rowTextView);

			    // save a reference to the textview for later
			    myTextViews[i] = rowTextView;
			}
		} catch (Exception e) {
			AppLogger.log(TAG, e);
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
