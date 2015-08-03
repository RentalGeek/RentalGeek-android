package com.app.rentalgeek.homepage;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.activeandroid.query.Delete;
import com.app.rentalgeek.R;
import com.app.rentalgeek.database.ProfileTable;
import com.app.rentalgeek.database.PropertyTable;
import com.app.rentalgeek.geekscores.GeekScoreMain;
import com.app.rentalgeek.geekvision.GeekVision;
import com.app.rentalgeek.profile.Profile;
import com.app.rentalgeek.starredprop.StarredProperties;
import com.app.rentalgeek.tutorials.Tutorials;
import com.app.rentalgeek.utils.StaticClass;
import com.facebook.Session;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseAbstract;

/**
 * 
 * @author George
 * 
 * @purpose Left Slide Menu
 *
 */

public class SlideLeft extends LuttuBaseAbstract {
 
	@InjectView(R.id.slidelist)
	ListView list;

	AppPrefes appPref;

	ItemAdapter adapter;

	String[] fiilliste;

	ArrayList<String> listitems;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.slideleft, container, false);
		ButterKnife.inject(this, v);
		listitems = new ArrayList<String>();
		appPref = new AppPrefes(getActivity(), "rentalgeek");
		fiilliste = getResources().getStringArray(R.array.slidelist);

		for (String f : fiilliste) {
			listitems.add(f);
		}

		adapter = new ItemAdapter();

		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					appPref.SaveData("map_list", "");
					((HomeActivity) getActivity()).closedrawer();
					nextfragment(new GeekScoreMain(), false, R.id.container);
					break;
				case 1:
					appPref.SaveData("map_list", "");
					((HomeActivity) getActivity()).closedrawer();
					nextfragment(new StarredProperties(), false, R.id.container);
					break;
				case 2:
					appPref.SaveData("map_list", "");
					((HomeActivity) getActivity()).closedrawer();
					nextfragment(new GeekVision(), false, R.id.container);
					break;
				case 3:
					appPref.SaveData("map_list", "");
					((HomeActivity) getActivity()).closedrawer();
					nextfragment(new Profile(), false, R.id.container);
					break;
				case 4:
					logout();
//					showLogoutAlert();
					break;
				default:
					System.out.println("position  " + position);
					break;
				}

			}
		});
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

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	// ----------------------------------
	class ItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listitems.size();
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
						R.layout.slide_single, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.main_item);
				holder.slide_in = (LinearLayout) view
						.findViewById(R.id.slide_in);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.text.setText(listitems.get(position));
			return view;
		}
	}

	private static class ViewHolder {
		TextView text;
		Button button;
		LinearLayout slide_in;

	}

	public void addfg(Fragment fragment) {
		// TODO Auto-generated method stub
		getActivity().getSupportFragmentManager().beginTransaction()
				.add(R.id.container, fragment).commitAllowingStateLoss();
	}

	private void logout() {
		// TODO Auto-generated method stub

		// CallLogoutLink();

		new Delete().from(ProfileTable.class).execute();
		new Delete().from(PropertyTable.class).execute();
		PersistentCookieStore mCookieStore = new PersistentCookieStore(
				getActivity());
		mCookieStore.clear();
		Session session = Session.getActiveSession();
		if (session != null)
			session.closeAndClearTokenInformation();
		appPref.deleteAll();
		appPref.SaveData("first", "");
		getActivity().finish();
		Intent intent = new Intent(getActivity(),Tutorials.class);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.three_, R.anim.four_);

	}

	private void CallLogoutLink() {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		params.put("applicant[id]", appPref.getData("Uid"));
		// params.put("applicant[email]", a);
		// params.put("applicant[password]", b);
		asynkhttp(params, 1,
				StaticClass.headlink + "/applicants/sign_out.json", true);

	}

	private void showLogoutAlert() {
		// TODO Auto-generated method stub

		AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity(),
				(R.style.MyDialog));
		builder1.setMessage("Are you sure you want to log out?");
		builder1.setCancelable(true);
		builder1.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						logout();

					}

				});
		builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert11 = builder1.create();
		alert11.show();

	}
}
