package com.luttu.fragmentutils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luttu.luttulibrary.R;
import com.luttu.nettoast.Crouton;
import com.luttu.nettoast.Style;

/****
 * extend LuttuBaseFragment for fragment operations.
 * */
public class LuttuBaseFragment extends Fragment {
	public AppPrefes appPrefes;
	private FrameLayout containerlogin;
	private LinearLayout toplay_click_cancel;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		init();
	}

	/****
	 * 
	 * initializing.
	 * 
	 * */
	private void init() {

		try {
			LuttuBaseFragmentActivity mainActivity = (LuttuBaseFragmentActivity) getActivity();
			appPrefes = mainActivity.appPrefes;
			containerlogin = mainActivity.containerlogin;
			toplay_click_cancel=mainActivity.toplay_click_cancel;
		} catch (Exception e) {
			// TODO: handle exception

		}
	}

	/****
	 * 
	 * show ProgressDialog
	 * 
	 * */
	protected void progressshow() {

		if (containerlogin != null) {
			containerlogin.setVisibility(View.VISIBLE);
			if(toplay_click_cancel!=null)
			{
				toplay_click_cancel.setVisibility(View.VISIBLE);
			}
			
		} else {
			init();

			//containerlogin.setVisibility(View.VISIBLE);
		}
		
		
	}

	/****
	 * 
	 * hide your keyboard
	 * 
	 * */
	protected void hidekey() {

		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
				.getWindowToken(), 0);
	}

	/****
	 * 
	 * dismiss ProgressDialog
	 * 
	 * */
	protected void progresscancel() {

		if (containerlogin != null) {
			if(toplay_click_cancel!=null)
			{
				toplay_click_cancel.setVisibility(View.INVISIBLE);
			}
			containerlogin.setVisibility(View.GONE);
		}
	}

	/****
	 * 
	 * check validate EditText
	 * 
	 * */
	protected boolean checkvalidate(EditText... editTexts) {

		for (int i = 0; i < editTexts.length; i++) {
			if (editTexts[i].getText().toString().equals("")) {
				toast(editTexts[i].getTag().toString());
				return false;
			}
		}
		return true;
	}

	/****
	 * 
	 * clear focus EditText
	 * 
	 * */
	protected void clearfocus(EditText... editTexts) {

		for (int i = 0; i < editTexts.length; i++) {
			if (editTexts[i].getText().toString().equals("")) {
				editTexts[i].clearFocus();
			}
		}
	}

	/****
	 * 
	 * email validation
	 * 
	 * */
	protected boolean isValidEmail(EditText editText) {
		CharSequence target = editText.getText().toString();
		return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}

	/****
	 * 
	 * confirm password authentication.
	 * 
	 * */
	protected boolean checkpassword(EditText editText, EditText editText1) {

		String pass1 = editText.getText().toString();
		String pass2 = editText1.getText().toString();
		if (pass1.equals(pass2)) {
			return true;
		}
		toast("Password mismatch");
		return false;
	}

	/****
	 * 
	 * custom font to views
	 * 
	 * */
	protected void changeFonts(ViewGroup root, String font) {
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), font);
		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFonts((ViewGroup) v, font);
			}
		}
	}

	/****
	 * 
	 * replace a fragment.
	 * 
	 * */
	public void nextfragment(Fragment fragment, boolean stack, int id) {

		if (getActivity() == null) {
			return;
		}
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		ft.setCustomAnimations(R.anim.one_, R.anim.two_, R.anim.three_,
				R.anim.four_);
		ft.replace(id, fragment);
		if (stack)
			ft.addToBackStack(fragment.getClass().getName().toString());
		ft.commitAllowingStateLoss();
	}

	/****
	 * 
	 * replace child fragment.(Using nested fragments).
	 * 
	 * */
	public void nextchildfragment(Fragment fragment, boolean stack, int id) {

		if (getActivity() == null) {
			return;
		}
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.replace(id, fragment);
		if (stack)
			ft.addToBackStack(fragment.getClass().getName().toString());
		ft.commitAllowingStateLoss();
	}

	/****
	 * 
	 * replace fragment with animation
	 * 
	 * */
	public void nextfragmentanimation(Fragment fragment, boolean stack, int id) {

		if (getActivity() == null) {
			return;
		}
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		ft.setCustomAnimations(R.anim.one_, R.anim.two_, R.anim.three_,
				R.anim.four_);
		ft.replace(id, fragment);
		if (stack)
			ft.addToBackStack(fragment.getClass().getName().toString());
		ft.commitAllowingStateLoss();
	}

	/****
	 * 
	 * addfragment.
	 * 
	 * */
	public void addfragment(Fragment fragment, boolean stack, int id) {

		if (getActivity() == null) {
			return;
		}
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		ft.setCustomAnimations(R.anim.one_, R.anim.two_, R.anim.three_,
				R.anim.four_);
		ft.add(id, fragment);
		if (stack)
			ft.addToBackStack(fragment.getClass().getName().toString());
		ft.commitAllowingStateLoss();
	}

	/****
	 * 
	 * custom font to TextViews
	 * 
	 * */
	protected void changefont(TextView textView, String font) {

		Typeface mFont = Typeface.createFromAsset(getActivity().getAssets(),
				font);
		textView.setTypeface(mFont);
	}

	/****
	 * 
	 * to show normal toast.
	 * 
	 * */
	protected void normaltoast(String msg) {

		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}

	/****
	 * 
	 * to show success toast.
	 * 
	 * */
	protected void toastsuccess(String msg) {

		Crouton crouton = Crouton.makeText(getActivity(), msg, Style.CONFIRM);
		crouton.show();
	}

	/****
	 * 
	 * only use log.don't use sysout.
	 * 
	 * */
	protected void log(String message) {

		Log.d("SICS", message);
	}

	/****
	 * 
	 * to show error toast.
	 * 
	 * */
	protected void toast(String croutonText) {
		Crouton crouton = Crouton.makeText(getActivity(), croutonText,
				Style.ALERT);
		crouton.show();
	}

	/****
	 * 
	 * normal onbackpressed.
	 * 
	 * */
	protected void onback() {

		try {
			LuttuBaseFragmentActivity mainActivity = (LuttuBaseFragmentActivity) getActivity();
			mainActivity.backpress();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
