package com.rentalgeek.android.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseAbstract;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.StarredBacked;
import com.rentalgeek.android.pojos.StarredListPojo;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ConnectionDetector;
import com.rentalgeek.android.utils.RandomUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 
 * @author George
 * 
 * @purpose Fragment which handles the Starred Property List
 *
 */
public class FragmentStarredProperties extends LuttuBaseAbstract {
 
	@InjectView(R.id.starred_list)
	ListView list;

	AppPrefes appPref;

	ConnectionDetector con;

	private List<StarredListPojo.StarredList> mainlist = new ArrayList<StarredListPojo.StarredList>();

	ItemAdapter adapter;

	int[] fiilliste;

	ArrayList<Integer> listitems;
	ArrayList<String> street_name;
	TypedArray images;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater
				.inflate(R.layout.fragment_starred_properties, container, false);
		ButterKnife.inject(this, v);
		listitems = new ArrayList<Integer>();
		con = new ConnectionDetector(getActivity());
		appPref = new AppPrefes(getActivity(), "rentalgeek");
		getActivity()
				.registerReceiver(receiver, new IntentFilter("dislikeref"));
		if (con.isConnectingToInternet()) {
			fetchFromServer();
		} else {
			toast("No connection");
		}

		return v;
	}

	private void fetchFromServer() {


		asynkhttpGet(2, ApiManager.getStarredPrpoertiesUrl(""), AppPreferences.getAuthToken(),//StaticClass.headlink + "/v2/starred_properties.json",
				true);

	}

	@Override
	public void parseresult(String response, boolean success, int value) {

		switch (value) {
		case 1:
			removeStar(response);
			break;
		case 2:
			loadList(response);
		default:
			break;
		}

	}

	private void loadList(String response) {
		try {

			System.out.println("starred response " + response);

			StarredBacked detail = (new Gson()).fromJson(response.toString(),
					StarredBacked.class);
			if (detail.starred_properties.size() > 0) {

				StarredListPojo item = new StarredListPojo();
				for (int i = 0; i < detail.starred_properties.size(); i++) {

					String image_url="";
					if(detail.starred_properties.get(i).image.size()>0)
					{
						for (int j = 0; j < detail.starred_properties.get(i).image
								.size(); j++) {
							image_url = detail.starred_properties.get(i).image
									.get(0).photo_full_url;

						}
					}
					else
					{
						image_url="missing.png";
					}
					
					mainlist.add(item.new StarredList(
							detail.starred_properties.get(i).id,
							detail.starred_properties.get(i).user_id,
							detail.starred_properties.get(i).rental_offering_id,
							detail.starred_properties.get(i).property_address,
							detail.starred_properties.get(i).bedroom_count,
							detail.starred_properties.get(i).full_bathroom_count,
							detail.starred_properties.get(i).square_footage_floor,
							detail.starred_properties.get(i).monthly_rent_floor,
							detail.starred_properties.get(i).salesy_description,
							image_url,
							detail.starred_properties.get(i).sold_out));
					adapter = new ItemAdapter();
					list.setAdapter(adapter);
				}

			} else {
				toast("You have no favorites");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void removeStar(String response) {


		toast("Property removed");

	}

	@Override
	public void error(String response, int value) {


	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
		getActivity().unregisterReceiver(receiver);
		ButterKnife.reset(this);
	}

	// ----------------------------------ADAPTER CLASS---------------------
	class ItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mainlist.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getActivity().getLayoutInflater().inflate(
						R.layout.listview_single, parent, false);
				holder = new ViewHolder();
				holder.main_image = (ImageView) view
						.findViewById(R.id.main_image);
				holder.street_name = (TextView) view
						.findViewById(R.id.street_name);
				holder.is_starred = (ImageView) view
						.findViewById(R.id.is_starred);
				holder.price = (TextView) view.findViewById(R.id.price_range);
				holder.bed_count = (TextView) view.findViewById(R.id.bedcount);
				holder.shower_count = (TextView) view
						.findViewById(R.id.shower_count);
				holder.lay = (RelativeLayout) view.findViewById(R.id.main_list);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			int ppp = random();
			// holder.main_image.setImageResource(fiilliste[ppp]);
			holder.is_starred.setTag(mainlist.get(position).id);
			Picasso.with(getActivity()).load(mainlist.get(position).image)
					.placeholder(R.drawable.white).error(R.drawable.banner)
					.into(holder.main_image);
			holder.main_image.setTag(ppp);
			holder.street_name.setText(mainlist.get(position).property_address);
			holder.price
					.setText(mainlist.get(position).monthly_rent_floor + "");
			holder.bed_count.setText(mainlist.get(position).bedroom_count + "");
			holder.shower_count
					.setText(mainlist.get(position).full_bathroom_count + "");

			holder.is_starred.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {


					RemoveStar(holder.is_starred.getTag().toString());
					mainlist.remove(position);
					notifyDataSetChanged();

				}

			});

			holder.lay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {


					Fragment innerlist = new FragmentStarredPropDetails();
					Bundle args = new Bundle();

					args.putString("star_id", holder.is_starred.getTag()
							.toString());
					args.putString("rental_id",
							mainlist.get(position).rental_offering_id);

					args.putString("img_pos", holder.main_image.getTag()
							.toString());
					System.out.println("the value  "
							+ (Integer) holder.main_image.getTag());

					innerlist.setArguments(args);
					appPref.SaveIntData("click_pos", position);
					addfragment(innerlist, true, R.id.container);

				}
			});

			return view;
		}
	}

	private static class ViewHolder {
		ImageView main_image;
		TextView street_name;
		TextView price;
		TextView bed_count;
		TextView shower_count;
		RelativeLayout lay;
		ImageView is_starred;

	}

	public Integer random() {
		RandomUtils random = new RandomUtils();
		return random.nextInt(0, 6);
	}

	private void RemoveStar(String id) {
		asynkhttpDelete(1, ApiManager.getStarredPrpoertiesUrl(id), AppPreferences.getAuthToken(), true);
	}

	// Broadcast that refreshes the list view
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			try {
				Bundle b = intent.getExtras();
				if (b != null) {
					System.out.println("inside on recieve "
							+ appPref.getIntData("click_pos"));
					int pos = appPref.getIntData("click_pos");
					mainlist.remove(pos);
					adapter.notifyDataSetChanged();

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

}
