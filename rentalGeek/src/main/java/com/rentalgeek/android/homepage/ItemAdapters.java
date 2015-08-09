package com.rentalgeek.android.homepage;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.rentalgeek.android.R;
import com.rentalgeek.android.backend.AddStarBack;
import com.rentalgeek.android.database.PropertyTable;
import com.rentalgeek.android.pojos.PropertyListPojo;
import com.rentalgeek.android.utils.StaticClass;
import com.bumptech.glide.Glide;
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

/**
 * 
 * @author George
 * 
 * @purpose Item Adapter class loads the each offerings to the ListView
 *
 */
public class ItemAdapters extends ArrayAdapter<PropertyListPojo.PropertyList> {

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
			ImageView mainImage = (ImageView) main_list
					.findViewById(R.id.main_image);
			ImageView isStarred = (ImageView) main_list
					.findViewById(R.id.is_starred);
			TextView priceRange = (TextView) main_list
					.findViewById(R.id.price_range);
			TextView streetName = (TextView) main_list
					.findViewById(R.id.street_name);
			TextView bedcount = (TextView) main_list
					.findViewById(R.id.bedcount);
			TextView showerCount = (TextView) main_list
					.findViewById(R.id.shower_count);
			return new ViewHolder(main_list, mainImage, isStarred, priceRange,
					streetName, bedcount, showerCount);
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		if (convertView == null) {
			View view = mInflater.inflate(R.layout.listview_single, parent,
					false);
			vh = ViewHolder.create((RelativeLayout) view);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final PropertyListPojo.PropertyList item = getItem(position);

		// TODOBind your data to the views here
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub

				if (item.starred) {
					DislikeMe(item.id, position);
				} else {
					starProperty(item.id, position);
				}

			}
		});

		return vh.main_list;
	}

	private LayoutInflater mInflater;

	// Constructors
	public ItemAdapters(Context context,
			List<PropertyListPojo.PropertyList> objects) {

		super(context, 0, objects);
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		appPref = new AppPrefes(context, "rentalgeek");
		client = new AsyncHttpClient();
	}

	public ItemAdapters(Context context, PropertyListPojo.PropertyList[] objects) {
		super(context, 0, objects);
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		appPref = new AppPrefes(context, "rentalgeek");
		client = new AsyncHttpClient();
	}

	public void addfragment(Fragment fragment, boolean stack, int id, Context c) {
		// TODO Auto-generated method stub
		if (c == null) {
			return;
		}
		FragmentTransaction ft = ((HomeActivity) c).getSupportFragmentManager()
				.beginTransaction();
		ft.add(id, fragment);
		if (stack)
			ft.addToBackStack(fragment.getClass().getName().toString());
		ft.commitAllowingStateLoss();
	}

	// Call the server to like
	public void starProperty(final int itemid, final int clickposition) {
		new GetSSl().getssl(client);
		mCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(mCookieStore);
		RequestParams params = new RequestParams();

		String Uid = appPref.getData("Uid");
		System.out.println("" + Uid + " rental offering id " + itemid);
		params.put("starred_property[applicant_id]", Uid);
		params.put("starred_property[rental_offering_id]", itemid + "");

		String url = StaticClass.headlink + "/v2/starred_properties";

		GlobalFunctions.postApiCall(context, url, params, client,
				new HttpResponseHandler() {

					@Override
					public void handle(String response, boolean failre) {
						try {
							// TODO Auto-generated method stub
							System.out.println("dialog response " + response);

							if (failre) {

								AddStarBack detail = (new Gson()).fromJson(
										response.toString(), AddStarBack.class);

								Crouton crouton = Crouton.makeText(
										(HomeActivity) context,
										"Added to favorites", Style.ALERT);
								crouton.show();

								PropertyListPojo.PropertyList propobj = getItem(clickposition);
								propobj.starred = true;

								prop = new Select().from(PropertyTable.class)
										.where("uid = ?", itemid)
										.executeSingle();
								prop.starred = true;
								prop.starred_property_id = detail.starred_property.id;
								prop.save();
								notifyDataSetChanged();
							} else {

								Crouton crouton = Crouton.makeText(
										(HomeActivity) context,
										"Action Failed", Style.ALERT);
								crouton.show();

							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}

	// Dislike the property
	public void DislikeMe(int itemids, int clickposition) {

		final PropertyListPojo.PropertyList propobj = getItem(clickposition);
		final PropertyTable	propy = new Select().from(PropertyTable.class).where("uid = ?", itemids)
				.executeSingle();

		if (propy.starred_property_id != null) {
			new GetSSl().getssl(client);
			mCookieStore = new PersistentCookieStore(context);
			client.setCookieStore(mCookieStore);

			String links = StaticClass.headlink + "/v2/starred_properties/"
					+ propy.starred_property_id;
			GlobalFunctions.deleteApiCall(context, links, client,
					new HttpResponseHandler() {

						@Override
						public void handle(String response, boolean failre) {
							// TODO Auto-generated method stub
							System.out.println("remove star response "+response);
							if (failre && context != null) {
								propobj.starred = false;
								propy.starred = false;
								propy.save();
								propy.starred_property_id = null;
								notifyDataSetChanged();
							} else if (context != null) {

							}

						}
					});
		}

	}

}
