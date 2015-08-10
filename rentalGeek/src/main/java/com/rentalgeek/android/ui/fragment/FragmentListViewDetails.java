package com.rentalgeek.android.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseAbstract;
import com.rentalgeek.android.R;
import com.rentalgeek.android.backend.MapBackend;
import com.rentalgeek.android.database.PropertyTable;
import com.rentalgeek.android.pojos.PropertyListPojo;
import com.rentalgeek.android.ui.adapter.PropertyListItemAdapter;
import com.rentalgeek.android.utils.RandomUtils;
import com.rentalgeek.android.utils.StaticClass;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 
 * @author George
 * 
 * @purpose Class which handles the list view , rental offerings
 *
 */
public class FragmentListViewDetails extends LuttuBaseAbstract {

	@InjectView(R.id.slidelist) 
	ListView list;
	
	AppPrefes appPref;

	String image_url;
	private List<PropertyListPojo.PropertyList> mainlist = new ArrayList<PropertyListPojo.PropertyList>();

	// ItemAdapter adapter;
	PropertyListItemAdapter adapters;

	int[] fiilliste;
	int width;

	ArrayList<Integer> listitems;
	ArrayList<String> street_name;
	TypedArray images;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater
				.inflate(R.layout.property_list_main, container, false);
		ButterKnife.inject(this, v);
		appPref=new AppPrefes(getActivity(), "rentalgeek");
		listitems = new ArrayList<Integer>();
		getActivity().registerReceiver(likereceiver,
				new IntentFilter("likeref"));
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		getActivity()
				.registerReceiver(receiver, new IntentFilter("searchlist"));
		fetchFromDB();

		return v;
	}

	private void fetchFromDB() {
		// TODO Auto-generated method stub

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			}

		};

		PropertyListPojo item = new PropertyListPojo();
		Select select = new Select();
		List<PropertyTable> propertytobj = select.all()
				.from(PropertyTable.class).execute();
		if (propertytobj.size() > 0) {
			for (int i = 0; i < propertytobj.size(); i++) {

				mainlist.add(item.new PropertyList(propertytobj.get(i).count,
						propertytobj.get(i).rental_complex_latitude,
						propertytobj.get(i).rental_complex_longitude,
						propertytobj.get(i).bedroom_count,
						propertytobj.get(i).monthly_rent_floor, propertytobj
								.get(i).monthly_rent_ceiling, propertytobj
								.get(i).headline,
						propertytobj.get(i).full_bathroom_count, propertytobj
								.get(i).starred, propertytobj.get(i).uid,
						propertytobj.get(i).property_image));
			}

			adapters = null;
			adapters = new PropertyListItemAdapter(getActivity(), mainlist);

			list.setAdapter(adapters);

		} else {
			toast("No results");
		}

	}

	@Override
	public void parseresult(String response, boolean success, int value) {
		// TODO Auto-generated method stub

		switch (value) {
		case 1:
			ParseLocation(response);
			break;

		default:
			break;
		}

	}

	private void ParseLocation(String response) {
		// TODO Auto-generated method stub

		try {
			// TODO Auto-generated method stub

			MapBackend detail = (new Gson()).fromJson(response.toString(),
					MapBackend.class);
			if (detail.rental_offerings.size() > 0) {
				appPref.SaveData("bysearch", "yes");
				StaticClass.bySearch = true;
				new Delete().from(PropertyTable.class).execute();

				ActiveAndroid.beginTransaction();
				try {
					for (int i = 0; i < detail.rental_offerings.size(); i++) {

						image_url = null;
						image_url = "";

						if (detail.rental_offerings.get(i).primary_property_photo_url
								.size() > 0) {

							image_url = detail.rental_offerings.get(i).primary_property_photo_url
									.get(0).photo_full_url;

						} else {
							image_url = "missing.png";
						}

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

					}
					ActiveAndroid.setTransactionSuccessful();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					ActiveAndroid.endTransaction();
				}

				mainlist.clear();
				adapters.notifyDataSetChanged();
				fetchFromDB();
			} else {
				toast("No results");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			toast("No locations found");
		}

	}

	@Override
	public void error(String response, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ButterKnife.reset(this);
		getActivity().unregisterReceiver(receiver);
		getActivity().unregisterReceiver(likereceiver);
	}

	public Integer random() {
		RandomUtils random = new RandomUtils();
		return random.nextInt(0, 6);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			System.out.println("inside on recieve ");

			Bundle b = intent.getExtras();
			if (b != null) {
				String loc = b.getString("params");
				System.out.println("the location si " + loc);
				SearchViaLocationList(loc);

			}

		}

	};

	BroadcastReceiver likereceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			

			Bundle b = intent.getExtras();
			if (b != null) {
				if (adapters != null) {
					if (mainlist != null) {
						int pos = appPref.getIntData("listp");
						System.out.println("inside on recieve POS"+pos);
						
						
						if(b.getBoolean("remove_params"))
						{
							mainlist.get(pos).setStarred(true);
							adapters.notifyDataSetChanged();
						}
						else
						{
							mainlist.get(pos).setStarred(false);
							adapters.notifyDataSetChanged();
						}
						
					}
				}

			}

		}

	};

	private void SearchViaLocationList(String loc) {
		// TODO Auto-generated method stub

		if (!loc.equals("")) {
			System.out.println("the search url list is " + StaticClass.headlink
					+ "/v2/rental_offerings.json?" + loc);

			asynkhttpGet(1, StaticClass.headlink + "/v2/rental_offerings.json?"
					+ loc, true);

		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

}
