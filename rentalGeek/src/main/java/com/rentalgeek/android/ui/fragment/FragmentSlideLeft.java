package com.rentalgeek.android.ui.fragment;

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

import com.activeandroid.query.Delete;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.database.ProfileTable;
import com.rentalgeek.android.database.PropertyTable;
import com.rentalgeek.android.geekvision.GeekVision;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.activity.ActivityTutorials;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FragmentSlideLeft extends GeekBaseFragment {

    private static final String TAG = "FragmentSlideLeft";

    @InjectView(R.id.slidelist)
	ListView list;

	AppPrefes appPref;

	ItemAdapter adapter;

	String[] fiilliste;

	ArrayList<String> listitems;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_slideleft, container, false);
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
					//((ActivityHome) getActivity()).closedrawer();
					nextfragment(new FragmentGeekScoreMain(), false, R.id.container);
					break;
				case 1:
					appPref.SaveData("map_list", "");
					//((ActivityHome) getActivity()).closedrawer();
					nextfragment(new FragmentStarredProperties(), false, R.id.container);
					break;
				case 2:
					appPref.SaveData("map_list", "");
					//((ActivityHome) getActivity()).closedrawer();
					nextfragment(new GeekVision(), false, R.id.container);
					break;
				case 3:
//					logout();
					appPref.SaveData("map_list", "");
					//((ActivityHome) getActivity()).closedrawer();
					nextfragment(new FragmentCosignerApp1(), false, R.id.container);
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
	public void onDestroyView() {

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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getActivity().getLayoutInflater().inflate(R.layout.slide_single, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.main_item);
				holder.slide_in = (LinearLayout) view.findViewById(R.id.slide_in);
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

		getActivity().getSupportFragmentManager().beginTransaction()
				.add(R.id.container, fragment).commitAllowingStateLoss();
	}

	private void logout() {


		// CallLogoutLink();

		new Delete().from(ProfileTable.class).execute();
		new Delete().from(PropertyTable.class).execute();
		PersistentCookieStore mCookieStore = new PersistentCookieStore(getActivity());
		mCookieStore.clear();
		//TODO: need to implements this in newer fb lib?
//		Session session = Session.getActiveSession();
//		if (session != null)
//			session.closeAndClearTokenInformation();
		appPref.deleteAll();
		appPref.SaveData("first", "");
		getActivity().finish();
		Intent intent = new Intent(getActivity(),ActivityTutorials.class);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.three_, R.anim.four_);

	}

	private void CallLogoutLink() {


		RequestParams params = new RequestParams();
		params.put("user[id]", appPref.getData("Uid"));
		// params.put("user[email]", a);
		// params.put("user[password]", b);

        GlobalFunctions.postApiCall(getActivity(), ApiManager.getSignOut(), params,
                AppPreferences.getAuthToken(),
                new GeekHttpResponseHandler() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onSuccess(String content) {
                        try {

                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
        //asynkhttp(params, 1, ApiManager.getSignOut(), AppPreferences.getAuthToken(), true);

	}

	private void showLogoutAlert() {


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
