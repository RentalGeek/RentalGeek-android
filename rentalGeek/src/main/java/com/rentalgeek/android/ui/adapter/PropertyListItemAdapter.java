package com.rentalgeek.android.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.AddStarBack;
import com.rentalgeek.android.database.PropertyTable;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.PropertyListPojo;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.fragment.ListInnerPage;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.util.List;


public class PropertyListItemAdapter extends ArrayAdapter<PropertyListPojo.PropertyList> {

    private static final String TAG = PropertyListItemAdapter.class.getSimpleName();

    Context context;
	AppPrefes appPref;
	AsyncHttpClient client;
	PersistentCookieStore mCookieStore;
	PropertyTable prop;

	private static class ViewHolder {

		public final RelativeLayout main_list;
		public final ImageView mainImage;
		public final ImageView isStarred;
		public final TextView priceRange;
		public final TextView streetName;
		public final TextView bedcount;
		public final TextView showerCount;

		private ViewHolder(RelativeLayout main_list, ImageView mainImage,
				ImageView isStarred, TextView priceRange, TextView streetName,
				TextView bedcount, TextView showerCount) {
			this.main_list = main_list;
			this.mainImage = mainImage;
			this.isStarred = isStarred;
			this.priceRange = priceRange;
			this.streetName = streetName;
			this.bedcount = bedcount;
			this.showerCount = showerCount;
		}

		public static ViewHolder create(RelativeLayout main_list) {
			ImageView mainImage = (ImageView) main_list.findViewById(R.id.main_image);
			ImageView isStarred = (ImageView) main_list.findViewById(R.id.is_starred);
			TextView priceRange = (TextView) main_list.findViewById(R.id.price_range);
			TextView streetName = (TextView) main_list.findViewById(R.id.street_name);
			TextView bedcount = (TextView) main_list.findViewById(R.id.bedcount);
			TextView showerCount = (TextView) main_list.findViewById(R.id.shower_count);
			return new ViewHolder(main_list, mainImage, isStarred, priceRange,
					                streetName, bedcount, showerCount);
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		if (convertView == null) {
			View view = mInflater.inflate(R.layout.listview_single, parent, false);
			vh = ViewHolder.create((RelativeLayout) view);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final PropertyListPojo.PropertyList item = getItem(position);

		if (item.starred) {
			vh.isStarred.setImageResource(R.drawable.star_select);
		} else {
			vh.isStarred.setImageResource(R.drawable.star);
		}

		Glide.with(context).load(item.prop_image).placeholder(R.drawable.white)
				.error(R.drawable.banner).into(vh.mainImage);

		vh.streetName.setText(item.headline);
		vh.priceRange.setText(item.monthly_rent_floor + "");
		vh.bedcount.setText(item.bedroom_count + "");
		vh.showerCount.setText(item.full_bathroom_count + "");

		vh.mainImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				appPref.SaveIntData("listp", position);
				System.out.println("the clicked pos " + position);

				Fragment innerlist = new ListInnerPage();
				Bundle args = new Bundle();
				args.putInt("Count", item.count);
				innerlist.setArguments(args);
				addfragment(innerlist, true, R.id.container, context);

			}
		});

		vh.isStarred.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (item.starred) {
					DislikeMe(item.id, position);
				} else {
					starProperty(item, position);
				}

			}
		});

		return vh.main_list;
	}

	private LayoutInflater mInflater;

	// Constructors
	public PropertyListItemAdapter(Context context, List<PropertyListPojo.PropertyList> objects) {
		super(context, 0, objects);
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		appPref = new AppPrefes(context, "rentalgeek");
		client = new AsyncHttpClient();
	}

	public PropertyListItemAdapter(Context context, PropertyListPojo.PropertyList[] objects) {
		super(context, 0, objects);
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		appPref = new AppPrefes(context, "rentalgeek");
		client = new AsyncHttpClient();
	}

	public void addfragment(Fragment fragment, boolean stack, int id, Context c) {

		if (c == null) {
			return;
		}
		FragmentTransaction ft = ((ActivityHome) c).getSupportFragmentManager().beginTransaction();
		ft.add(id, fragment);
		if (stack)
			ft.addToBackStack(fragment.getClass().getName().toString());
		ft.commitAllowingStateLoss();
	}

	// Call the server to like
	public void starProperty(final PropertyListPojo.PropertyList item, final int clickposition) {
		//new GetSSl().getssl(client);
		mCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(mCookieStore);
		RequestParams params = new RequestParams();

		String Uid = appPref.getData("Uid");
		System.out.println("" + Uid + " rental offering id " + item.id);
		params.put("starred_property[user_id]", Uid);

		params.put("starred_property[rental_offering_id]", item.id + "");

//        params.put("starred_property[property_address]", );
//        params.put("starred_property[bedroom_count]", item.bedroom_count);
 //       params.put("starred_property[full_bathroom_count]", item.full_bathroom_count);
//        params.put("starred_property[square_footage_floor]", );
 //       params.put("starred_property[monthly_rent_floor]", item.monthly_rent_floor);
//        params.put("starred_property[salesy_description]", );
//        params.put("starred_property[sold_out]", );


//                detail.starred_properties.get(i).id,
//                detail.starred_properties.get(i).user_id,
//                detail.starred_properties.get(i).rental_offering_id,
//                detail.starred_properties.get(i).property_address,
//                detail.starred_properties.get(i).bedroom_count,
//                detail.starred_properties.get(i).full_bathroom_count,
//                detail.starred_properties.get(i).square_footage_floor,
//                detail.starred_properties.get(i).monthly_rent_floor,
//                detail.starred_properties.get(i).salesy_description,
//                image_url,
//                detail.starred_properties.get(i).sold_out

                            String url = ApiManager.getStarredPrpoertiesUrl("");

		GlobalFunctions.postApiCall(context, url, params, AppPreferences.getAuthToken(),
				new GeekHttpResponseHandler() {

                    @Override
                    public void onSuccess(String content) {
						try {

							System.out.println("dialog response " + content);

                            AddStarBack detail = (new Gson()).fromJson(content.toString(), AddStarBack.class);

							DialogManager.showCrouton((AppCompatActivity) context, "Added to favorites");

                            PropertyListPojo.PropertyList propobj = getItem(clickposition);
                            propobj.starred = true;

                            prop = new Select().from(PropertyTable.class)
                                    .where("uid = ?", item.id)
                                    .executeSingle();
                            prop.starred = true;
                            prop.starred_property_id = detail.starred_property.id;
                            prop.save();

						} catch (Exception e) {
                            AppLogger.log(TAG, e);
						}

					}

					@Override
					public void onAuthenticationFailed() {

					}
				});

	}

	// Dislike the property
	public void DislikeMe(int itemids, int clickposition) {

		final PropertyListPojo.PropertyList propobj = getItem(clickposition);
		final PropertyTable	propy = new Select().from(PropertyTable.class).where("uid = ?", itemids).executeSingle();

		if (propy.starred_property_id != null) {

			//new GetSSl().getssl(client);
			mCookieStore = new PersistentCookieStore(context);
			client.setCookieStore(mCookieStore);

			String links = ApiManager.getStarredPrpoertiesUrl(propy.starred_property_id);

            RequestParams params = new RequestParams();
            params.put("starred_property[rental_offering_id]", propy.starred_property_id);


			GlobalFunctions.deleteApiCall(context, links, AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {

                }

                @Override
                public void onSuccess(String content) {
                    try {
                        propobj.starred = false;
						propy.starred = false;
						propy.save();
						propy.starred_property_id = null;
						notifyDataSetChanged();
                    } catch (Exception e) {
                        AppLogger.log(TAG, e);
                    }
                }

                @Override
                public void onAuthenticationFailed() {

                }
			});

		}

	}

}
