package com.rentalgeek.android.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.activeandroid.query.Delete;
import com.rentalgeek.android.R;
import com.rentalgeek.android.database.PropertyTable;
import com.rentalgeek.android.utils.ConnectionDetector;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseAbstract;

/**
 * 
 * @author George
 * 
 * @purpose Right Slide Menu
 *
 */
public class SlideRight extends LuttuBaseAbstract {

	// BedRoom Buttons
	@InjectView(R.id.bt_studio)
	Button studio;

	ConnectionDetector con;

	AppPrefes appPref;

	@InjectView(R.id.bt_bedone)
	Button bt_bedone;

	@InjectView(R.id.bt_bedtwo)
	Button bt_bedtwo;

	@InjectView(R.id.bt_bedthree)
	Button bt_bedthree;

	@InjectView(R.id.bt_bedfour)
	Button bt_bedfour;

	// Bathroom Buttons
	@InjectView(R.id.bt_bathone)
	Button bt_bathone;

	@InjectView(R.id.bt_bathtwo)
	Button bt_bathtwo;

	@InjectView(R.id.bt_baththree)
	Button bt_baththree;

	@InjectView(R.id.bt_bathfour)
	Button bt_bathfour;

	@InjectView(R.id.edittextmaplocation)
	EditText ed_search;

	String search_params;
	String params_strings = "";

	StringBuilder sb;

	@InjectView(R.id.price_seek)
	SeekBar price_seek;

	@InjectView(R.id.price_range)
	TextView price_range;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.slideright, container, false);
		ButterKnife.inject(this, v);
		params_strings = "";
		con = new ConnectionDetector(getActivity());
		appPref = new AppPrefes(getActivity(), "rentalgeek");
		PriceSeek();

		return v;
	}

	private void PriceSeek() {
		// TODO Auto-generated method stub

		price_seek.setOnTouchListener(new ListView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow Drawer to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(true);
					break;

				case MotionEvent.ACTION_UP:
					// Allow Drawer to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(false);
					break;
				}

				// Handle seekbar touch events.
				v.onTouchEvent(event);
				return true;
			}
		});

		price_seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

				sb = null;
				sb = new StringBuilder();
				sb.append("$");
				sb.append(checkRange(progress));
				price_range.setText(sb.toString());
			}

		});

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

	@OnClick(R.id.search_loc)
	public void SearchLocation() {

		Fragment f = getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.container);
		if (f instanceof Map) {

			if (con.isConnectingToInternet()) {
				searchInmap();
			} else {
				toast("No Connection");
			}

		} else if (f instanceof ListViewDetails) {

			if (con.isConnectingToInternet()) {
				searchInlist();
			} else {
				toast("No Connection");
			}

		} else {
			toast("Please navigate to List or Map page and search");
		}

		// if (appPref.getData("map_list").equals("map"))
		// {
		// searchInmap();
		// }
		// else if(appPref.getData("map_list").equals("list"))
		// {
		// searchInlist();
		// }
		// else
		// {
		// toast("Please navigate to List or Map page and search");
		// }

	}

	private Integer checkRange(int range) {
		// TODO Auto-generated method stub
		if (range > 0 && range <= 10)
			return 100;
		else if (range > 10 && range <= 20)
			return 500;
		else if (range > 20 && range <= 30)
			return 500;
		else if (range > 30 && range <= 40)
			return 700;
		else if (range > 40 && range <= 50)
			return 1000;
		else if (range > 50 && range <= 60)
			return 1500;
		else if (range > 60 && range <= 70)
			return 2000;
		else if (range > 70 && range <= 80)
			return 2500;
		else if (range > 70 && range <= 80)
			return 3000;
		else if (range > 80 && range <= 90)
			return 3500;
		else if (range > 90 && range <= 1000)
			return 4000;
		else
			return 0;
	}

	@OnClick({ R.id.bt_studio, R.id.bt_bedone, R.id.bt_bedtwo,
			R.id.bt_bedthree, R.id.bt_bedfour })
	public void BedroomClick(View v) {

		switch (v.getId()) {
		case R.id.bt_studio:

			if (compareColor(studio.getTag().toString(), "#4181cb")) {
				replaceBedroomTag(0);
				params_strings = params_strings + "&search[bedroom]=0";
				setColorToBedroom(studio, bt_bedone, bt_bedtwo, bt_bedthree,
						bt_bedfour);
			} else {
				replaceWord("\\&search\\[bedroom\\]=0");
				removeColor(studio);

			}

			break;
		case R.id.bt_bedone:
			if (compareColor(bt_bedone.getTag().toString(), "#4181cb")) {
				replaceBedroomTag(1);
				params_strings = params_strings + "&search[bedroom]=1";
				setColorToBedroom(bt_bedone, studio, bt_bedtwo, bt_bedthree,
						bt_bedfour);
			} else {
				replaceWord("\\&search\\[bedroom\\]=1");
				removeColor(bt_bedone);

			}
			break;

		case R.id.bt_bedtwo:
			if (compareColor(bt_bedtwo.getTag().toString(), "#4181cb")) {
				replaceBedroomTag(2);
				params_strings = params_strings + "&search[bedroom]=2";
				setColorToBedroom(bt_bedtwo, studio, bt_bedone, bt_bedthree,
						bt_bedfour);
			} else {
				replaceWord("\\&search\\[bedroom\\]=2");
				removeColor(bt_bedtwo);

			}
			break;

		case R.id.bt_bedthree:
			if (compareColor(bt_bedthree.getTag().toString(), "#4181cb")) {
				replaceBedroomTag(3);
				params_strings = params_strings + "&search[bedroom]=3";
				setColorToBedroom(bt_bedthree, studio, bt_bedone, bt_bedtwo,
						bt_bedfour);
			} else {
				replaceWord("\\&search\\[bedroom\\]=3");
				removeColor(bt_bedthree);

			}
			break;

		case R.id.bt_bedfour:
			if (compareColor(bt_bedfour.getTag().toString(), "#4181cb")) {
				replaceBedroomTag(4);
				params_strings = params_strings + "&search[bedroom]=4";
				setColorToBedroom(bt_bedfour, studio, bt_bedone, bt_bedtwo,
						bt_bedthree);
			} else {
				replaceWord("\\&search\\[bedroom\\]=4");
				removeColor(bt_bedfour);

			}
			break;
		}

	}

	@OnClick({ R.id.bt_bathone, R.id.bt_bathtwo, R.id.bt_baththree,
			R.id.bt_bathfour })
	public void Bathroom(View v) {

		switch (v.getId()) {

		case R.id.bt_bathone:
			replaceTag(1);
			if (compareColor(bt_bathone.getTag().toString(), "#4181cb")) {

				params_strings = params_strings + "&search[bathroom]=1";
				setColorBathroom(bt_bathone, bt_bathtwo, bt_baththree,
						bt_bathfour);
			} else {
				replaceWord("\\&search[bathroom]=1");
				removeColor(bt_bathtwo);

			}
			break;
		case R.id.bt_bathtwo:
			replaceTag(2);
			if (compareColor(bt_bathtwo.getTag().toString(), "#4181cb")) {

				params_strings = params_strings + "&search[bathroom]=2";
				setColorBathroom(bt_bathtwo, bt_bathone, bt_baththree,
						bt_bathfour);
			} else {
				replaceWord("\\&search\\[bathroom\\]=2");
				removeColor(bt_bathtwo);

			}
			break;
		case R.id.bt_baththree:
			replaceTag(3);
			if (compareColor(bt_baththree.getTag().toString(), "#4181cb")) {

				params_strings = params_strings + "&search[bathroom]=3";
				setColorBathroom(bt_baththree, bt_bathone, bt_bathtwo,
						bt_bathfour);
			} else {
				replaceWord("\\&search\\[bathroom\\]=3");
				removeColor(bt_baththree);

			}
			break;
		case R.id.bt_bathfour:
			replaceTag(4);
			if (compareColor(bt_bathfour.getTag().toString(), "#4181cb")) {

				params_strings = params_strings + "&search[bathroom]=4";
				setColorBathroom(bt_bathfour, bt_bathone, bt_bathtwo,
						bt_baththree);
			} else {
				replaceWord("\\&search\\[bathroom\\]=4");
				removeColor(bt_bathfour);

			}
			break;

		}

	}

	private boolean compareColor(String first_color, String second_color) {
		// TODO Auto-generated method stub

		if (first_color.equals(second_color)) {
			return true;
		} else {

			return false;
		}

	}

	private void replaceWord(String string) {
		// TODO Auto-generated method stub
		params_strings = params_strings.replaceAll(string, "");

	}

	private void replaceTag(int i) {
		// TODO Auto-generated method stub

		switch (i) {
		case 1:
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=2", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=3", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=4", "");
			break;
		case 2:
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=1", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=3", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=4", "");
			break;
		case 3:
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=1", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=2", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=4", "");
			break;
		case 4:
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=1", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=3", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bathroom\\]=2", "");
			break;
		default:
			break;

		}

	}

	private void replaceBedroomTag(int i) {
		// TODO Auto-generated method stub

		switch (i) {

		case 0:

			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=1", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=2", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=3", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=4", "");
			break;

		case 1:
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=0", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=2", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=3", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=4", "");
			break;
		case 2:
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=0", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=1", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=3", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=4", "");
			break;
		case 3:
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=0", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=1", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=2", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=4", "");
			break;
		case 4:
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=0", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=1", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=3", "");
			params_strings = params_strings.replaceAll(
					"\\&search\\[bedroom\\]=2", "");
			break;
		default:
			break;

		}

	}

	private void setColorToBedroom(Button b1, Button b2, Button b3, Button b4,
			Button b5) {

		b1.setTag("#0000A0");
		b2.setTag("#4181cb");
		b3.setTag("#4181cb");
		b4.setTag("#4181cb");
		b5.setTag("#4181cb");
		b1.setBackgroundColor(getActivity().getResources().getColor(
				R.color.dark_blue));
		b2.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));
		b3.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));
		b4.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));
		b5.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));
	}

	private void setColorBathroom(Button b1, Button b2, Button b3, Button b4) {
		b1.setTag("#0000A0");
		b2.setTag("#4181cb");
		b3.setTag("#4181cb");
		b4.setTag("#4181cb");

		b1.setBackgroundColor(getActivity().getResources().getColor(
				R.color.dark_blue));
		b2.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));
		b3.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));
		b4.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));

	}

	private void removeColor(Button b) {
		// TODO Auto-generated method stub

		b.setTag("#4181cb");
		b.setBackgroundColor(getActivity().getResources()
				.getColor(R.color.blue));
	}

	// remove after search
	private void removeEverything() {
		// TODO Auto-generated method stub

		ed_search.setText("");

		price_seek.setProgress(0);

		bt_bathone.setTag("#4181cb");
		bt_bathone.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));

		bt_bathtwo.setTag("#4181cb");
		bt_bathtwo.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));

		bt_bathtwo.setTag("#4181cb");
		bt_bathtwo.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));

		bt_baththree.setTag("#4181cb");
		bt_baththree.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));

		bt_bathfour.setTag("#4181cb");
		bt_bathfour.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));

		studio.setTag("#4181cb");
		studio.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));

		bt_bedone.setTag("#4181cb");
		bt_bedone.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));

		bt_bedtwo.setTag("#4181cb");
		bt_bedtwo.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));

		bt_bedthree.setTag("#4181cb");
		bt_bedthree.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));

		bt_bedfour.setTag("#4181cb");
		bt_bedfour.setBackgroundColor(getActivity().getResources().getColor(
				R.color.blue));

		params_strings = null;
		params_strings = "";

		((HomeActivity) getActivity()).closedrawer();
	}

	private void searchInmap() {
		// TODO Auto-generated method stub
		((HomeActivity) getActivity()).closedrawer();
		if (ed_search.getText().toString().equals("")) {

			System.out.println("empty edittext");

			if (price_seek.getProgress() != 0) {
				params_strings = params_strings + "&search[minPrice]=100&"
						+ "search[maxPrice]="
						+ checkRange(price_seek.getProgress());
			}

			Intent refreshIntent = new Intent("search");
			Bundle args = new Bundle();
			args.putString("params", params_strings.toString().trim());
			refreshIntent.putExtras(args);
			getActivity().sendBroadcast(refreshIntent);
			removeEverything();

		} else {

			System.out.println("no empty edittext");
			params_strings = params_strings + "&search[location]="
					+ ed_search.getText().toString().trim();
			if (price_seek.getProgress() != 0) {
				params_strings = params_strings + "&search[minPrice]=100&"
						+ "search[maxPrice]="
						+ checkRange(price_seek.getProgress());
			}

			Intent refreshIntent = new Intent("search");
			Bundle args = new Bundle();
			args.putString("params", params_strings.toString().trim());
			refreshIntent.putExtras(args);
			getActivity().sendBroadcast(refreshIntent);
			removeEverything();
		}
	}

	private void searchInlist() {
		// TODO Auto-generated method stub
		((HomeActivity) getActivity()).closedrawer();
		if (ed_search.getText().toString().equals("")) {

			if (price_seek.getProgress() != 0) {
				params_strings = params_strings + "&search[minPrice]=100&"
						+ "search[maxPrice]="
						+ checkRange(price_seek.getProgress());
			}

			Intent refreshIntent = new Intent("searchlist");
			Bundle args = new Bundle();
			args.putString("params", params_strings.toString().trim());
			refreshIntent.putExtras(args);
			getActivity().sendBroadcast(refreshIntent);
			removeEverything();

		} else {

			if (price_seek.getProgress() != 0) {
				params_strings = params_strings + "&search[minPrice]=100&"
						+ "search[maxPrice]="
						+ checkRange(price_seek.getProgress());
			}

			params_strings = params_strings + "&search[location]="
					+ ed_search.getText().toString().trim();
			Intent refreshIntent = new Intent("searchlist");
			Bundle args = new Bundle();
			args.putString("params", params_strings.toString().trim());
			refreshIntent.putExtras(args);
			getActivity().sendBroadcast(refreshIntent);
			removeEverything();
		}

	}

	@OnClick(R.id.clear_search)
	public void ClearSearchDet() {
		appPref.SaveData("bysearch", "no");
		removeEverything();
		new Delete().from(PropertyTable.class).execute();
		nextfragment(new Map(), false, R.id.container);
		((HomeActivity)getActivity()).selectorShift();
		appPref.SaveData("map_list", "map");

	}
}
