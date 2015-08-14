package com.rentalgeek.android.ui.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseAbstract;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.ErrorObj;
import com.rentalgeek.android.backend.ProfileIdFindBackend;
import com.rentalgeek.android.backend.ProfilePost;
import com.rentalgeek.android.database.ProfileTable;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.utils.ConnectionDetector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 
 * @author George
 * 
 * @purpose Fragment which deals with the profile of a Rental geek user.
 *
 */
public class FragmentProfile extends LuttuBaseAbstract implements ValidationListener,
		OnFocusChangeListener,
		android.widget.AdapterView.OnItemSelectedListener {

	public static final String DATEPICKER_TAG = "datepicker";

	// View Injection and View Validations
	boolean iseveryThing;
	@InjectView(R.id.evdesc)
	RelativeLayout evdesc;

	@Required(order = 1, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_first_name)
	EditText ed_first_name;

	@Required(order = 2, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_last_name)
	EditText ed_last_name;

	@InjectView(R.id.is_felon_lay)
	RelativeLayout is_felon_lay;

	@Required(order = 3, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_born_on)
	public EditText edBornOn;

	@InjectView(R.id.ed_license)
	public EditText edLicense;

	@InjectView(R.id.ed_license_state)
	public Spinner ed_license_state;

	@Required(order = 4, message = "Please fill all mandatory fields")
	@TextRule(minLength = 10, maxLength = 10, message = "Please use 10 digit phone number", order = 5)
	@Regex(order = 6, pattern = "[0-9]{10}", message = "Please use 10 digit phone number")
	@InjectView(R.id.ed_ph)
	public EditText edPh;

	@InjectView(R.id.ed_desc_pets)
	public EditText edDescPets;

	@InjectView(R.id.ed_desc_vehicle)
	public EditText edDescVehicle;

	@Select(order = 7, message = "please select are you envicted")
	@InjectView(R.id.ed_was_envicted)
	public Spinner ed_was_envicted;

	@InjectView(R.id.ed_envicted_desc)
	public EditText edEnvicted;

	@Select(order = 8, message = "Please select are you felon")
	@InjectView(R.id.ed_felon)
	public Spinner edFelon;

	@InjectView(R.id.ed_felon_desc)
	public EditText edFelonDesc;

	@Required(order = 9, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_char_ref)
	public EditText character_reference_name;

	@Required(order = 10, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_char_ref_conifo)
	public EditText character_reference_contact_info;

	@Required(order = 11, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_emer_conifo)
	public EditText emergency_contact_name;

	@TextRule(order = 12, minLength = 10, maxLength = 10, message = "Should be 10 digits")
	@Regex(order = 13, pattern = "[0-9]{10}", message = "Please use 10 digit phone number")
	@Required(order = 14, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_emer_ph)
	public EditText emergency_contact_phone_number;

	@Required(order = 15, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_home_addr)
	public EditText current_home_street_address;

	@Required(order = 16, message = "Please fill")
	@InjectView(R.id.ed_curr_home_mov_in)
	public EditText current_home_moved_in_on;

	@InjectView(R.id.ed_curr_home_diss)
	public EditText edCurrHomeDiss;

	@Required(order = 17, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_curr_home_own)
	public EditText current_home_owner;
	ActivityHome contexto;

	@Required(order = 18, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_curr_own_cont)
	public EditText current_home_owner_contact_info;

	@InjectView(R.id.ed_prev_own_stree)
	public EditText previous_home_street_address;

	@InjectView(R.id.ed_prev_hme_movedin)
	public EditText previous_home_moved_in_on;

	@InjectView(R.id.ed_prev_hme_movedout)
	public EditText previous_home_moved_out;

	@InjectView(R.id.ed_prev_hme_own)
	public EditText previous_home_owner;

	@InjectView(R.id.ed_prev_hme_own_cnt)
	public EditText previous_home_owner_contact_info;

	@Select(order = 19, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_empl_sta)
	public Spinner employment_status;

	@InjectView(R.id.ed_empl_super)
	public EditText current_employment_supervisor;

	@Required(order = 20, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_cosigner)
	public EditText cosigner_name;

	@Required(order = 21, message = "Please fill all mandatory fields")
	@InjectView(R.id.ed_cosigner_email)
	public EditText cosigner_email_address;

	@InjectView(R.id.ed_desir_mov)
	public EditText desires_to_move_in_on;

	private Validator validator;
	AppPrefes appPref;
	ConnectionDetector con;

	ProfileTable profdets;

	private DatePickerDialog toDatePickerDialog;

	private SimpleDateFormat dateFormatter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_profile, container, false);
		ButterKnife.inject(this, v);
		validator = new Validator(this);
		validator.setValidationListener(this);
		wasEverEnvicted(); // hiding the envicted desc lay according to the
							// selection
		isFelon(); // hiding the is felon lay according to the selection
		appPref = new AppPrefes(getActivity(), "rentalgeek");
		con = new ConnectionDetector(getActivity());
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		contexto = (ActivityHome) getActivity();
		registerOnFocusChangeListeners();
		if (con.isConnectingToInternet()) {

			fetchProfileData();
		} else {
			toast("No Connection");
		}

		return v;
	}

	private void registerOnFocusChangeListeners() {

		com.activeandroid.query.Select select = new com.activeandroid.query.Select();
		List<ProfileTable> profcont = select.all().from(ProfileTable.class)
				.execute();

		if (profcont.size() > 0) {
			profdets = new com.activeandroid.query.Select()
					.from(ProfileTable.class)
					.where("uid = ?", appPref.getData("Uid")).executeSingle();
		} else {
			profdets = new ProfileTable();
			profdets.uid = appPref.getData("Uid");
		}

		edEnvicted.setOnFocusChangeListener(this);
		ed_first_name.setOnFocusChangeListener(this);
		ed_last_name.setOnFocusChangeListener(this);
		edLicense.setOnFocusChangeListener(this);
		edPh.setOnFocusChangeListener(this);
		edDescPets.setOnFocusChangeListener(this);
		edDescVehicle.setOnFocusChangeListener(this);
		edEnvicted.setOnFocusChangeListener(this);
		edFelonDesc.setOnFocusChangeListener(this);
		character_reference_name.setOnFocusChangeListener(this);
		character_reference_contact_info.setOnFocusChangeListener(this);
		emergency_contact_name.setOnFocusChangeListener(this);
		emergency_contact_phone_number.setOnFocusChangeListener(this);
		current_home_street_address.setOnFocusChangeListener(this);
		current_home_moved_in_on.setOnFocusChangeListener(this);
		edCurrHomeDiss.setOnFocusChangeListener(this);
		current_home_owner.setOnFocusChangeListener(this);
		current_home_owner_contact_info.setOnFocusChangeListener(this);
		previous_home_street_address.setOnFocusChangeListener(this);
		previous_home_moved_in_on.setOnFocusChangeListener(this);
		previous_home_moved_out.setOnFocusChangeListener(this);
		previous_home_owner.setOnFocusChangeListener(this);
		previous_home_owner_contact_info.setOnFocusChangeListener(this);
		current_employment_supervisor.setOnFocusChangeListener(this);
		cosigner_name.setOnFocusChangeListener(this);
		cosigner_email_address.setOnFocusChangeListener(this);
		desires_to_move_in_on.setOnFocusChangeListener(this);
		ed_license_state.setOnItemSelectedListener(this);
		employment_status.setOnItemSelectedListener(this);

	}

	@Override
	public void parseresult(String response, boolean success, int value) {


		switch (value) {
		case 1:
			createUserParse(response);
			break;
		case 2:
			patchedUserParse(response);
			break;
		case 3:
			setProfileData(response);
			break;
		default:
			break;
		}
	}

	// setting profile data to view
	private void setProfileData(String response) {
		try {


			// setting the values from the server
			response = response.replaceAll("null", " \" \"");
			System.out.println("profile get response " + response);
			ProfilePost detail = (new Gson()).fromJson(response,
					ProfilePost.class);

			if (detail != null) {
				if (detail.profiles != null && detail.profiles.size() > 0) {

					System.out.println("profile id "
							+ detail.profiles.get(0).id);
					appPref.SaveData("prof_id", detail.profiles.get(0).id);
					System.out.println("profile born_on "
							+ detail.profiles.get(0).born_on);

					ed_first_name.setText(detail.profiles.get(0).first_name);
					ed_last_name.setText(detail.profiles.get(0).last_name);

					if (!detail.profiles.get(0).born_on.equals(" ")) {
						Date date = new SimpleDateFormat("yyyy-MM-dd")
								.parse(detail.profiles.get(0).born_on);
						String dateString = new SimpleDateFormat("MM-dd-yyyy")
								.format(date);
						edBornOn.setText(dateString);
					}

					edLicense
							.setText(detail.profiles.get(0).drivers_license_number);

					ArrayAdapter<CharSequence> adapter = ArrayAdapter
							.createFromResource(getActivity(),
									R.array.state_list,
									android.R.layout.simple_spinner_item);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					ed_license_state.setAdapter(adapter);
					if (!detail.profiles.get(0).drivers_license_state
							.equals(null)) {
						int spinnerPostion = adapter
								.getPosition(detail.profiles.get(0).drivers_license_state);
						ed_license_state.setSelection(spinnerPostion);
						spinnerPostion = 0;
					}
					edPh.setText(detail.profiles.get(0).phone_number);

					edDescPets.setText(detail.profiles.get(0).pets_description);
					edDescVehicle
							.setText(detail.profiles.get(0).vehicles_description);

					if (detail.profiles.get(0).was_ever_evicted.equals("false")) {
						ed_was_envicted.setSelection(2);
					} else {
						ed_was_envicted.setSelection(1);
					}

					edEnvicted
							.setText(detail.profiles.get(0).was_ever_evicted_explanation);

					if (detail.profiles.get(0).is_felon.equals("false")) {
						edFelon.setSelection(2);
					} else {
						edFelon.setSelection(1);
					}
					edFelonDesc
							.setText(detail.profiles.get(0).is_felon_explanation);
					character_reference_name
							.setText(detail.profiles.get(0).character_reference_name);
					character_reference_contact_info.setText(detail.profiles
							.get(0).character_reference_contact_info);
					emergency_contact_name
							.setText(detail.profiles.get(0).emergency_contact_name);
					emergency_contact_phone_number.setText(detail.profiles
							.get(0).emergency_contact_phone_number);
					current_home_street_address
							.setText(detail.profiles.get(0).current_home_street_address);

					if (!detail.profiles.get(0).current_home_moved_in_on
							.equals(" ")) {
						Date date2 = new SimpleDateFormat("yyyy-MM-dd")
								.parse(detail.profiles.get(0).current_home_moved_in_on);
						String dateString2 = new SimpleDateFormat("MM-dd-yyyy")
								.format(date2);
						current_home_moved_in_on.setText(dateString2);
					}

					edCurrHomeDiss
							.setText(detail.profiles.get(0).current_home_dissatisfaction_explanation);
					current_home_owner
							.setText(detail.profiles.get(0).current_home_owner);
					current_home_owner_contact_info.setText(detail.profiles
							.get(0).current_home_owner_contact_info);
					previous_home_street_address
							.setText(detail.profiles.get(0).previous_home_street_address);

					if (!detail.profiles.get(0).previous_home_moved_in_on
							.equals(" ")) {
						Date date3 = new SimpleDateFormat("yyyy-MM-dd")
								.parse(detail.profiles.get(0).previous_home_moved_in_on);
						String dateString3 = new SimpleDateFormat("MM-dd-yyyy")
								.format(date3);
						previous_home_moved_in_on.setText(dateString3);
					}

					if (!detail.profiles.get(0).previous_home_moved_out
							.equals(" ")) {
						Date date4 = new SimpleDateFormat("yyyy-MM-dd")
								.parse(detail.profiles.get(0).previous_home_moved_out);
						String dateString4 = new SimpleDateFormat("MM-dd-yyyy")
								.format(date4);
						previous_home_moved_out.setText(dateString4);
					}

					previous_home_owner
							.setText(detail.profiles.get(0).previous_home_owner);
					previous_home_owner_contact_info.setText(detail.profiles
							.get(0).previous_home_owner_contact_info);
					if (detail.profiles.get(0).employment_status
							.equals("employed")) {
						employment_status.setSelection(1);
					} else {
						employment_status.setSelection(2);
					}
					current_employment_supervisor.setText(detail.profiles
							.get(0).current_employment_supervisor);

					cosigner_name.setText(detail.profiles.get(0).cosigner_name);

					cosigner_email_address
							.setText(detail.profiles.get(0).cosigner_email_address);

					if (!detail.profiles.get(0).desires_to_move_in_on
							.equals(" ")) {
						Date date5 = new SimpleDateFormat("yyyy-MM-dd")
								.parse(detail.profiles.get(0).desires_to_move_in_on);
						String dateString5 = new SimpleDateFormat("MM-dd-yyyy")
								.format(date5);
						desires_to_move_in_on.setText(dateString5);
					}

				} else {
					setProfdetailsFromLocalDb();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setProfdetailsFromLocalDb() {


		com.activeandroid.query.Select select = new com.activeandroid.query.Select();
		List<ProfileTable> profs = select.all().from(ProfileTable.class)
				.execute();

		if (profs.size() > 0) {
			try {

				ed_first_name.setText(profs.get(0).firstname);
				ed_last_name.setText(profs.get(0).lastname);
				edBornOn.setText(profs.get(0).born_on);
				edLicense.setText(profs.get(0).drivers_license_number);
				edPh.setText(profs.get(0).phone_number);
				edDescPets.setText(profs.get(0).pets_description);
				edDescVehicle.setText(profs.get(0).vehicles_description);
				character_reference_name
						.setText(profs.get(0).character_reference_name);
				character_reference_contact_info
						.setText(profs.get(0).character_reference_contact_info);
				emergency_contact_name
						.setText(profs.get(0).emergency_contact_name);
				emergency_contact_phone_number
						.setText(profs.get(0).emergency_contact_phone_number);
				current_home_street_address
						.setText(profs.get(0).current_home_street_address);
				current_home_moved_in_on
						.setText(profs.get(0).current_home_moved_in_on);
				edCurrHomeDiss
						.setText(profs.get(0).current_home_dissatisfaction_explanation);
				current_home_owner.setText(profs.get(0).current_home_owner);
				current_home_owner_contact_info
						.setText(profs.get(0).current_home_owner_contact_info);
				previous_home_street_address
						.setText(profs.get(0).previous_home_street_address);
				previous_home_owner.setText(profs.get(0).previous_home_owner);
				previous_home_owner_contact_info
						.setText(profs.get(0).previous_home_owner_contact_info);
				current_employment_supervisor
						.setText(profs.get(0).current_employment_supervisor);
				cosigner_name.setText(profs.get(0).cosigner_name);
				cosigner_email_address
						.setText(profs.get(0).cosigner_email_address);
				emergency_contact_phone_number
						.setText(profs.get(0).emergency_contact_phone_number);
				desires_to_move_in_on
						.setText(profs.get(0).desires_to_move_in_on);
				previous_home_moved_in_on
						.setText(profs.get(0).previous_home_moved_in_on);
				previous_home_moved_out
						.setText(profs.get(0).previous_home_moved_out);

				System.out.println("employment status seeting"
						+ profs.get(0).employment_status);
				if (profs.get(0).employment_status.equals("employed")) {
					employment_status.setSelection(1);
				} else {
					employment_status.setSelection(2);
				}

				if (profs.get(0).is_felon.equals("No")) {
					edFelon.setSelection(2);
				} else {
					edFelon.setSelection(1);
				}

				edFelonDesc.setText(profs.get(0).is_felon_explanation);

				if (profs.get(0).was_ever_evicted.equals("No")) {
					ed_was_envicted.setSelection(2);
					System.out.println("was ever evicted no"
							+ profs.get(0).was_ever_evicted);
				} else {
					System.out.println("was ever evicted yes"
							+ profs.get(0).was_ever_evicted);
					ed_was_envicted.setSelection(1);
				}

				edEnvicted.setText(profs.get(0).was_ever_evicted_explanation);

				ArrayAdapter<CharSequence> adapter = ArrayAdapter
						.createFromResource(getActivity(), R.array.state_list,
								android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				ed_license_state.setAdapter(adapter);
				System.out.println("the value of state out "
						+ profs.get(0).drivers_license_state);
				if (!profs.get(0).drivers_license_state.equals(null)) {
					System.out.println("the value of state "
							+ profs.get(0).drivers_license_state);
					int spinnerPostion = adapter
							.getPosition(profs.get(0).drivers_license_state);
					ed_license_state.setSelection(spinnerPostion);
					spinnerPostion = 0;
				}
			} catch (Exception e) {
				System.out.println("was ever evicted   catch" + e);
			}

		} else {

		}

	}

	// parsing patch user response
	private void patchedUserParse(String response) {
		try {

			ProfileIdFindBackend detail = (new Gson()).fromJson(response,
					ProfileIdFindBackend.class);

			if (detail != null) {

				if (detail.profile != null) {
					appPref.SaveData("prof_id", detail.profile.id);
					toastsuccess("FragmentProfile Updated Successfully");
					hidekey();

					if (appPref.getIntData("payed") == 200) {

						nextfragment(new FragmentFinalGeekScore(), false,
								R.id.container);

					} else {
						AlertDialog.Builder builder1 = new AlertDialog.Builder(
								getActivity());
						builder1.setMessage(getActivity().getResources()
								.getString(R.string.geek_go));
						builder1.setTitle("Alert");
						builder1.setCancelable(true);
						builder1.setPositiveButton("Go to payment",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										dialog.cancel();
										nextfragment(new FragmentGeekScoreMain(),
												false, R.id.container);
									}
								});

						builder1.setNegativeButton("Home",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										nextfragment(new FragmentListViewDetails(),
												false, R.id.container);
									}
								});
						AlertDialog alert11 = builder1.create();
						alert11.show();
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// parsing create user response
	private void createUserParse(String response) {
		try {


			ProfileIdFindBackend detail = (new Gson()).fromJson(response,
					ProfileIdFindBackend.class);

			if (detail != null) {
				if (detail.profile != null) {

					appPref.SaveData("prof_id", detail.profile.id);

					System.out.println("profile id is " + detail.profile.id);
					callPatchUpdateLink(detail.profile.id);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Calling Patch request to update the profile
	private void callPatchUpdateLink(String id) {
		try {


			String url = ApiManager.getProfile(id);
			iseveryThing = false;

			final RequestParams params = new RequestParams();
			
			if(!ed_first_name.getText().toString().equals(""))
			{
				params.put("profile[first_name]", ed_first_name
						.getText().toString().trim());
			}
			
			if(!ed_last_name.getText().toString().equals(""))
			{
				params.put("profile[last_name]", ed_last_name
						.getText().toString().trim());
			}

			if (!edBornOn.getText().toString().trim().equals("")) {
				Date date = new SimpleDateFormat("MM-dd-yyyy").parse(edBornOn
						.getText().toString().trim());
				String dateString1 = new SimpleDateFormat("yyyy-MM-dd")
						.format(date);

				params.put("profile[born_on]", dateString1);
			}

			if (!edLicense.getText().toString().trim().equals(""))
				params.put("profile[drivers_license_number]", edLicense
						.getText().toString().trim());

			if (!ed_license_state.getSelectedItem().toString().trim()
					.equals("States"))
				params.put("profile[drivers_license_state]", ed_license_state
						.getSelectedItem().toString().trim());

			if (!edPh.getText().toString().trim().equals(""))
				params.put("profile[phone_number]", edPh.getText().toString()
						.trim());

			if (!edDescPets.getText().toString().trim().equals(""))
				params.put("profile[pets_description]", edDescPets.getText()
						.toString().trim());

			if (!edDescVehicle.getText().toString().trim().equals(""))
				params.put("profile[vehicles_description]", edDescVehicle
						.getText().toString().trim());

			if (ed_was_envicted.getSelectedItem().toString().equals("Yes")) {
				params.put("profile[was_ever_evicted]", "true");
			} else {
				params.put("profile[was_ever_evicted]", "false");
			}

			if (!edEnvicted.getText().toString().trim().equals(""))
				params.put("profile[was_ever_evicted_explanation]", edEnvicted
						.getText().toString().trim());

			if (edFelon.getSelectedItem().toString().equals("Yes")) {
				params.put("profile[is_felon]", "true");
			} else {
				params.put("profile[is_felon]", "false");
			}

			if (!edFelonDesc.getText().toString().trim().equals(""))
				params.put("profile[is_felon_explanation]", edFelonDesc
						.getText().toString().trim());
			if (!character_reference_name.getText().toString().trim()
					.equals(""))
				params.put("profile[character_reference_name]",
						character_reference_name.getText().toString().trim());

			if (!character_reference_contact_info.getText().toString().trim()
					.equals(""))
				params.put("profile[character_reference_contact_info]",
						character_reference_contact_info.getText().toString()
								.trim());

			if (!emergency_contact_name.getText().toString().trim().equals(""))
				params.put("profile[emergency_contact_name]",
						emergency_contact_name.getText().toString().trim());

			if (!emergency_contact_phone_number.getText().toString().trim()
					.equals(""))
				params.put("profile[emergency_contact_phone_number]",
						emergency_contact_phone_number.getText().toString()
								.trim());

			if (!current_home_street_address.getText().toString().trim()
					.equals(""))
				params.put("profile[current_home_street_address]",
						current_home_street_address.getText().toString().trim());

			if (!current_home_moved_in_on.getText().toString().trim()
					.equals("")) {

				Date date = new SimpleDateFormat("MM-dd-yyyy")
						.parse(current_home_moved_in_on.getText().toString()
								.trim());
				String dateString3 = new SimpleDateFormat("yyyy-MM-dd")
						.format(date);
				params.put("profile[current_home_moved_in_on]", dateString3);

			}

			if (!edCurrHomeDiss.getText().toString().trim().equals(""))
				params.put("profile[current_home_dissatisfaction_explanation]",
						edCurrHomeDiss.getText().toString().trim());

			if (!current_home_owner.getText().toString().trim().equals(""))
				params.put("profile[current_home_owner]", current_home_owner
						.getText().toString().trim());

			if (!current_home_owner_contact_info.getText().toString().trim()
					.equals(""))
				params.put("profile[current_home_owner_contact_info]",
						current_home_owner_contact_info.getText().toString()
								.trim());

			if (!previous_home_street_address.getText().toString().trim()
					.equals(""))
				params.put("profile[previous_home_street_address]",
						previous_home_street_address.getText().toString()
								.trim());

			if (!previous_home_moved_in_on.getText().toString().trim()
					.equals("")) {
				Date date = new SimpleDateFormat("MM-dd-yyyy")
						.parse(previous_home_moved_in_on.getText().toString()
								.trim());
				String dateString3 = new SimpleDateFormat("yyyy-MM-dd")
						.format(date);

				params.put("profile[previous_home_moved_in_on]", dateString3);
			}

			if (!previous_home_moved_out.getText().toString().trim().equals("")) {
				Date date = new SimpleDateFormat("MM-dd-yyyy")
						.parse(previous_home_moved_out.getText().toString()
								.trim());
				String dateString4 = new SimpleDateFormat("yyyy-MM-dd")
						.format(date);

				params.put("profile[previous_home_moved_out]", dateString4);
			}

			if (!previous_home_owner.getText().toString().trim().equals(""))
				params.put("profile[previous_home_owner]", previous_home_owner
						.getText().toString().trim());

			if (!previous_home_owner_contact_info.getText().toString().trim()
					.equals(""))
				params.put("profile[previous_home_owner_contact_info]",
						previous_home_owner_contact_info.getText().toString()
								.trim());

			params.put("profile[employment_status]", employment_status
					.getSelectedItem().toString().trim());

			if (!current_employment_supervisor.getText().toString().trim()
					.equals(""))
				params.put("profile[current_employment_supervisor]",
						current_employment_supervisor.getText().toString()
								.trim());

			if (!cosigner_name.getText().toString().trim().equals(""))
				params.put("profile[cosigner_name]", cosigner_name.getText()
						.toString().trim());

			if (!cosigner_email_address.getText().toString().trim().equals("")) {
				if (isValidEmail(cosigner_email_address.getText().toString()
						.trim())) {
					params.put("profile[cosigner_email_address]",
							cosigner_email_address.getText().toString().trim());
				} else {
					iseveryThing = true;
					cosigner_email_address
							.setError("Please enter a valid email");
					cosigner_email_address.requestFocus();
				}

			}

			if (!desires_to_move_in_on.getText().toString().trim().equals("")) {
				Date date = new SimpleDateFormat("MM-dd-yyyy")
						.parse(desires_to_move_in_on.getText().toString()
								.trim());
				String dateString5 = new SimpleDateFormat("yyyy-MM-dd")
						.format(date);

				params.put("profile[desires_to_move_in_on]", dateString5);
			}

			params.put("profile[user_id]", appPref.getData("Uid"));

			if (con.isConnectingToInternet()) {

				if (!iseveryThing) {
					// call a patch update from this position after creating a
					// profile
					asynkhttpPut(params, 2, url, appPref.getData("authentication_token"), true);
				} else {
					iseveryThing = false;
				}

			} else {
				toast("No Connection");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void error(String response, int value) {


		if (value == 2 || value == 1) {
			try {
				ErrorObj sess = (new Gson()).fromJson(response, ErrorObj.class);

				toasts(sess.errors.born_on);
				toasts(sess.errors.desires_to_move_in_on);
				toasts(sess.errors.emergency_contact_phone_number);
				toasts(sess.errors.ssn);
				toasts(sess.errors.previous_employment_employer_email_address);
				toasts(sess.errors.cosigner_email_address);
				toasts(sess.errors.previous_employment_employer_phone_number);
				toasts(sess.errors.current_employment_employer_email_address);
				toasts(sess.errors.current_employment_employer_phone_number);
				toasts(sess.errors.previous_home_moved_in_on);
				toasts(sess.errors.current_home_moved_in_on);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
		}

	}

	private void toasts(List<String> born_on) {

		try {
			showAlert(born_on.get(0));
		} catch (IndexOutOfBoundsException e) {
			AppLogger.log(e);
		} catch (NullPointerException e) {
			AppLogger.log(e);
		}
	}

	private List getErroList(
			List<com.rentalgeek.android.backend.ErrorArray.Error> al) {


		List<String> list = new ArrayList<String>();

		for (int i = 0; i < al.size(); i++) {

			list.add(al.get(i).message);

		}

		return list;

	}

	private void alertList(List<String> add) {


		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				getActivity());

		builderSingle.setTitle("Errors");
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.select_dialog_singlechoice);
		arrayAdapter.addAll(add);
		builderSingle.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builderSingle.show();

	}

	// Check on validation fail
	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {

		String message = failedRule.getFailureMessage();
		if (failedView instanceof EditText) {
			failedView.requestFocus();
			((EditText) failedView).setError(message);
		} else {
			// Toast.makeText(getActivity(), message,
			// Toast.LENGTH_SHORT).show();
			toast(message);
			if (failedView instanceof Spinner) {
				((Spinner) failedView).requestFocusFromTouch();
			}
		}
	}

	// After validation success
	@Override
	public void onValidationSucceeded() {


		if (con.isConnectingToInternet()) {
			if (appPref.getData("prof_id").equals("")) {
				createProfile();
			} else {
				callPatchUpdateLink(appPref.getData("prof_id"));
			}
		} else {
			toast("No Connection");
		}

	}

	// Validate view and procced
	@OnClick(R.id.profile_submit)
	public void ProfileSubmit() {
		validator.validate();
	}

	@OnClick({ R.id.ed_desir_mov, R.id.ed_born_on, R.id.ed_prev_hme_movedout,
			R.id.ed_curr_home_mov_in, R.id.ed_prev_hme_movedin })
	public void edDesirMov(View view) {
		dialogDatepicker((EditText) view, view);
		edLicense.clearFocus();
	}

	private void wasEverEnvicted() {


		ed_was_envicted
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						if (!parent.getSelectedItem().toString()
								.equals("Select")) {
							profdets.was_ever_evicted = ed_was_envicted
									.getSelectedItem().toString();
							profdets.save();
						}

						if (position == 1) {
							evdesc.setVisibility(View.VISIBLE);
						} else {
							evdesc.setVisibility(View.GONE);
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

	}

	private void isFelon() {


		edFelon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				if (!parent.getSelectedItem().toString().equals("Select")) {
					profdets.is_felon = edFelon.getSelectedItem().toString();
					profdets.save();
				}
				if (position == 1) {
					is_felon_lay.setVisibility(View.VISIBLE);
				} else {
					is_felon_lay.setVisibility(View.GONE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	// Create profile params and Api call
	public void createProfile() {
		try {
			final RequestParams params = new RequestParams();

			if(!ed_first_name.getText().toString().equals(""))
			{
				params.put("profile[first_name]", ed_first_name
						.getText().toString().trim());
			}
			
			if(!ed_last_name.getText().toString().equals(""))
			{
				params.put("profile[last_name]", ed_last_name
						.getText().toString().trim());
			}
			
			if (!edBornOn.getText().toString().trim().equals("")) {
				Date date = new SimpleDateFormat("MM-dd-yyyy").parse(edBornOn
						.getText().toString().trim());
				String dateString2 = new SimpleDateFormat("yyyy-MM-dd")
						.format(date);
				params.put("profile[born_on]", dateString2);
			}

			if (!edLicense.getText().toString().trim().equals(""))
				params.put("profile[drivers_license_number]", edLicense
						.getText().toString().trim());

			if (!ed_license_state.getSelectedItem().toString().trim()
					.equals("States"))
				params.put("profile[drivers_license_state]", ed_license_state
						.getSelectedItem().toString().trim());

			if (!edPh.getText().toString().trim().equals(""))
				params.put("profile[phone_number]", edPh.getText().toString()
						.trim());

			if (!edDescPets.getText().toString().trim().equals(""))
				params.put("profile[pets_description]", edDescPets.getText()
						.toString().trim());

			if (!edDescVehicle.getText().toString().trim().equals(""))
				params.put("profile[vehicles_description]", edDescVehicle
						.getText().toString().trim());

			params.put("profile[was_ever_evicted]", ed_was_envicted
					.getSelectedItem().toString());

			if (!edEnvicted.getText().toString().trim().equals(""))
				params.put("profile[was_ever_evicted_explanation]", edEnvicted
						.getText().toString().trim());

			if (edFelon.getSelectedItem().toString().equals("Yes")) {
				params.put("profile[is_felon]", "true");
			} else {
				params.put("profile[is_felon]", "false");
			}

			if (!edFelonDesc.getText().toString().trim().equals(""))
				params.put("profile[is_felon_explanation]", edFelonDesc
						.getText().toString().trim());
			if (!character_reference_name.getText().toString().trim()
					.equals(""))
				params.put("profile[character_reference_name]",
						character_reference_name.getText().toString().trim());

			if (!character_reference_contact_info.getText().toString().trim()
					.equals(""))
				params.put("profile[character_reference_contact_info]",
						character_reference_contact_info.getText().toString()
								.trim());

			if (!emergency_contact_name.getText().toString().trim().equals(""))
				params.put("profile[emergency_contact_name]",
						emergency_contact_name.getText().toString().trim());

			if (!emergency_contact_phone_number.getText().toString().trim()
					.equals(""))
				params.put("profile[emergency_contact_phone_number]",
						emergency_contact_phone_number.getText().toString()
								.trim());

			if (!current_home_street_address.getText().toString().trim()
					.equals(""))
				params.put("profile[current_home_street_address]",
						current_home_street_address.getText().toString().trim());

			if (!current_home_moved_in_on.getText().toString().trim()
					.equals("")) {
				Date date = new SimpleDateFormat("MM-dd-yyyy")
						.parse(current_home_moved_in_on.getText().toString()
								.trim());
				String dateString2 = new SimpleDateFormat("yyyy-MM-dd")
						.format(date);

				params.put("profile[current_home_moved_in_on]", dateString2);
			}

			if (!edCurrHomeDiss.getText().toString().trim().equals(""))
				params.put("profile[current_home_dissatisfaction_explanation]",
						edCurrHomeDiss.getText().toString().trim());

			if (!current_home_owner.getText().toString().trim().equals(""))
				params.put("profile[current_home_owner]", current_home_owner
						.getText().toString().trim());

			if (!current_home_owner_contact_info.getText().toString().trim()
					.equals(""))
				params.put("profile[current_home_owner_contact_info]",
						current_home_owner_contact_info.getText().toString()
								.trim());

			if (!previous_home_street_address.getText().toString().trim()
					.equals(""))
				params.put("profile[previous_home_street_address]",
						previous_home_street_address.getText().toString()
								.trim());

			if (!previous_home_moved_in_on.getText().toString().trim()
					.equals("")) {
				Date date = new SimpleDateFormat("MM-dd-yyyy")
						.parse(previous_home_moved_in_on.getText().toString()
								.trim());
				String dateString2 = new SimpleDateFormat("yyyy-MM-dd")
						.format(date);

				params.put("profile[previous_home_moved_in_on]", dateString2);
			}

			if (!previous_home_moved_out.getText().toString().trim().equals("")) {
				Date date = new SimpleDateFormat("MM-dd-yyyy")
						.parse(previous_home_moved_out.getText().toString()
								.trim());
				String dateString2 = new SimpleDateFormat("yyyy-MM-dd")
						.format(date);

				params.put("profile[previous_home_moved_out]", dateString2);
			}

			if (!previous_home_owner.getText().toString().trim().equals(""))
				params.put("profile[previous_home_owner]", previous_home_owner
						.getText().toString().trim());

			if (!previous_home_owner_contact_info.getText().toString().trim()
					.equals(""))
				params.put("profile[previous_home_owner_contact_info]",
						previous_home_owner_contact_info.getText().toString()
								.trim());

			params.put("profile[employment_status]", employment_status
					.getSelectedItem().toString().trim());

			if (!current_employment_supervisor.getText().toString().trim()
					.equals(""))
				params.put("profile[current_employment_supervisor]",
						current_employment_supervisor.getText().toString()
								.trim());

			if (!cosigner_name.getText().toString().trim().equals(""))
				params.put("profile[cosigner_name]", cosigner_name.getText()
						.toString().trim());

			if (!cosigner_email_address.getText().toString().trim().equals("")) {
				if (isValidEmail(cosigner_email_address.getText().toString()
						.trim())) {
					params.put("profile[cosigner_email_address]",
							cosigner_email_address.getText().toString().trim());
				} else {
					iseveryThing = true;
					cosigner_email_address
							.setError("Please enter a valid email");
					cosigner_email_address.requestFocus();
				}

			}

			if (!desires_to_move_in_on.getText().toString().trim().equals("")) {

				Date date = new SimpleDateFormat("MM-dd-yyyy")
						.parse(desires_to_move_in_on.getText().toString()
								.trim());
				String dateString2 = new SimpleDateFormat("yyyy-MM-dd")
						.format(date);

				params.put("profile[desires_to_move_in_on]", dateString2);
			}

			params.put("profile[user_id]", appPref.getData("Uid"));

			String url = ApiManager.getProfile("");

			if (con.isConnectingToInternet()) {

				if (!iseveryThing) {
					// call a patch update from this position after creating a
					// profile
					asynkhttp(params, 1, url, appPref.getData("authentication_token"), true);
				} else {
					toast("No Connection");
					iseveryThing = false;
				}

			} else {
				toast("No Connection");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// fetch user profile info
	private void fetchProfileData() {

		String url = ApiManager.getProfile("");
		asynkhttpGet(3, url, appPref.getData("authentication_token"), true);
	}

	// email checking function
	public boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	// check phone number
	private boolean isPhoneValid(int len) {

		if (len == 10) {
			return true;
		} else
			return false;

	}

	private void dialogDatepicker(final EditText edit, final View viewv) {

		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.date_select);
		final DatePicker datePicker = (DatePicker) dialog
				.findViewById(R.id.datePicker1);
		datePicker
				.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
		Button bt_ok = (Button) dialog.findViewById(R.id.bt_ok);
		Button bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int month = datePicker.getMonth() + 1;
				String val = String.format("%02d-%02d-%d", month,
						datePicker.getDayOfMonth(), datePicker.getYear());
				edit.setText(val);
				switch (viewv.getId()) {
				case R.id.ed_born_on:
					profdets.born_on = edit.getText().toString();
					profdets.save();
					break;
				case R.id.ed_curr_home_mov_in:
					profdets.current_home_moved_in_on = edit.getText()
							.toString();
					profdets.save();
					break;
				case R.id.ed_prev_hme_movedin:
					profdets.previous_home_moved_in_on = edit.getText()
							.toString();
					profdets.save();
					break;
				case R.id.ed_prev_hme_movedout:
					profdets.previous_home_moved_out = edit.getText()
							.toString();
					profdets.save();
					break;
				case R.id.ed_desir_mov:
					System.out.println("desires to move 2"
							+ edit.getText().toString());
					profdets.desires_to_move_in_on = edit.getText().toString();
					profdets.save();
					break;

				default:
					break;
				}

				dialog.cancel();
			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.cancel();
			}
		});
		dialog.show();
	}

	private void setDateTimeField() {

		Calendar newCalendar = Calendar.getInstance();
		toDatePickerDialog = new DatePickerDialog(getActivity(),
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {


					}

				}, newCalendar.get(Calendar.YEAR),
				newCalendar.get(Calendar.MONTH),
				newCalendar.get(Calendar.DAY_OF_MONTH));
	}

	private void showAlert(String message) {


		try {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					getActivity());
			builder1.setMessage(message);
			builder1.setTitle("Alert");
			builder1.setCancelable(true);
			builder1.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							dialog.cancel();
						}
					});
			AlertDialog alert11 = builder1.create();
			alert11.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Auto Save Functionality in FragmentProfile

	@Override
	public void onFocusChange(View v, boolean hasFocus) {


		if (v instanceof EditText) {

			EditText ed = (EditText) v;

			if (!hasFocus && !ed.getText().toString().equals("")) {
				switch (v.getId()) {
				case R.id.ed_born_on:
					profdets.born_on = ed.getText().toString();
					profdets.save();
					break;
				case R.id.ed_first_name:
					profdets.firstname = ed_first_name.getText().toString();
					profdets.save();
					break;
				case R.id.ed_last_name:
					profdets.lastname = ed_last_name.getText().toString();
					profdets.save();
					break;
				case R.id.ed_license:
					profdets.drivers_license_number = edLicense.getText()
							.toString();
					profdets.save();
					break;
				case R.id.ed_ph:
					profdets.phone_number = edPh.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_desc_pets:
					profdets.pets_description = edDescPets.getText().toString()
							.trim();
					profdets.save();
					break;
				case R.id.ed_desc_vehicle:
					profdets.vehicles_description = edDescVehicle.getText()
							.toString().trim();
					profdets.save();
					break;
				case R.id.ed_envicted_desc:
					profdets.was_ever_evicted_explanation = edEnvicted
							.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_felon_desc:
					profdets.is_felon_explanation = edFelonDesc.getText()
							.toString().trim();
					profdets.save();
					break;
				case R.id.ed_char_ref:
					profdets.character_reference_name = character_reference_name
							.getText().toString().trim();
					profdets.save();
					break;

				case R.id.ed_char_ref_conifo:
					profdets.character_reference_contact_info = character_reference_contact_info
							.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_emer_conifo:
					profdets.emergency_contact_name = emergency_contact_name
							.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_home_addr:
					profdets.current_home_street_address = current_home_street_address
							.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_curr_home_diss:
					profdets.current_home_dissatisfaction_explanation = edCurrHomeDiss
							.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_curr_home_own:
					profdets.current_home_owner = current_home_owner.getText()
							.toString().trim();
					profdets.save();
					break;
				case R.id.ed_curr_own_cont:
					profdets.current_home_owner_contact_info = current_home_owner_contact_info
							.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_prev_own_stree:
					profdets.previous_home_street_address = previous_home_street_address
							.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_prev_hme_own:
					profdets.previous_home_owner = previous_home_owner
							.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_prev_hme_own_cnt:
					profdets.previous_home_owner_contact_info = previous_home_owner_contact_info
							.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_empl_super:
					profdets.current_employment_supervisor = current_employment_supervisor
							.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_cosigner:
					profdets.cosigner_name = cosigner_name.getText().toString()
							.trim();
					profdets.save();
					break;
				case R.id.ed_cosigner_email:
					profdets.cosigner_email_address = cosigner_email_address
							.getText().toString().trim();
					profdets.save();
					break;
				case R.id.ed_emer_ph:
					profdets.emergency_contact_phone_number = emergency_contact_phone_number
							.getText().toString().trim();
					profdets.save();
					break;

				default:
					break;
				}
			}
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {

		System.out.println("the value of state  onItemSelected  "
				+ parent.getSelectedItem());
		if (parent instanceof Spinner
				&& !parent.getSelectedItem().equals("Select")) {
			switch (parent.getId()) {
			case R.id.ed_license_state:
				System.out.println("lisense state "
						+ ed_license_state.getSelectedItem().toString());
				profdets.drivers_license_state = ed_license_state
						.getSelectedItem().toString();
				profdets.save();
				break;
			case R.id.ed_empl_sta:
				System.out.println("employment_status "
						+ employment_status.getSelectedItem().toString());
				profdets.employment_status = employment_status
						.getSelectedItem().toString();
				profdets.save();
				break;

			default:
				break;
			}
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {


	}

}