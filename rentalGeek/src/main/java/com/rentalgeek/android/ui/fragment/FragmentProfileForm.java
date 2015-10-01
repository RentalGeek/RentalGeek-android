package com.rentalgeek.android.ui.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.ErrorObj;
import com.rentalgeek.android.backend.ProfileIdFindBackend;
import com.rentalgeek.android.backend.UserProfile;
import com.rentalgeek.android.backend.model.Profile;
import com.rentalgeek.android.database.ProfileTable;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityCreateProfile;
import com.rentalgeek.android.ui.activity.ActivityGeekScore;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.activity.ActivityPayment;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ListUtils;
import com.rentalgeek.android.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentProfileForm extends GeekBaseFragment implements Validator.ValidationListener, AdapterView.OnItemClickListener {

    private static final String TAG = "FragmentProfileForm";

    private int position;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    // Browser key required for these requests
    private static final String API_KEY = "AIzaSyDuVB1GHSKyz51m1w4VGs_XTyxVlK01INY";

    @InjectView(R.id.layoutForm1)
    LinearLayout layoutForm1;

    @InjectView(R.id.layoutForm2)
    LinearLayout layoutForm2;

    @InjectView(R.id.layoutForm3)
    LinearLayout layoutForm3;

    @InjectView(R.id.layoutForm4)
    LinearLayout layoutForm4;

    @InjectView(R.id.layoutForm5)
    LinearLayout layoutForm5;

    @InjectView(R.id.layoutForm6)
    LinearLayout layoutForm6;

    @InjectView(R.id.profile_layout_header)
    LinearLayout profile_layout_header;

    @InjectView(R.id.eviction_description_layout)
    LinearLayout eviction_description_layout;

    @InjectView(R.id.felony_description_layout)
    LinearLayout felony_description_layout;

    @InjectView(R.id.submit)
    Button submit;

    @InjectView(R.id.back)
    Button back;

    @Required(order = 1, message = "Please fill all mandatory fields")
    @InjectView(R.id.first_name)
    EditText first_name;

    @Required(order = 2, message = "Please fill all mandatory fields")
    @InjectView(R.id.last_name)
    EditText last_name;

    @InjectView(R.id.dob)
    public DatePicker dob;

    @InjectView(R.id.drivers_license)
    public EditText drivers_license;

    @InjectView(R.id.drivers_license_state)
    public Spinner drivers_license_state;

    @Required(order = 4, message = "Please fill all mandatory fields")
    @TextRule(minLength = 10, maxLength = 10, message = "Please use 10 digit phone number", order = 5)
    @Regex(order = 6, pattern = "[0-9]{10}", message = "Please use 10 digit phone number")
    @InjectView(R.id.phone_number)
    public EditText phone_number;

    @InjectView(R.id.pets_description)
    public EditText pets_description;

    @InjectView(R.id.vehicle_description)
    public EditText vehicle_description;

    @Select(order = 7, message = "Please fill all mandatory fields")
    @InjectView(R.id.was_evicted)
    public Spinner was_evicted;

    @InjectView(R.id.eviction_description)
    public EditText eviction_description;

    @Select(order = 8, message = "Please fill all mandatory fields")
    @InjectView(R.id.was_felon)
    public Spinner was_felon;

    @InjectView(R.id.felony_description)
    public EditText felony_description;

    @Required(order = 9, message = "Please fill all mandatory fields")
    @InjectView(R.id.character_reference_name)
    public EditText character_reference_name;

    @Required(order = 10, message = "Please fill all mandatory fields")
    @InjectView(R.id.character_reference_phone)
    public EditText character_reference_phone;

    @Required(order = 11, message = "Please fill all mandatory fields")
    @InjectView(R.id.emergency_contact_name)
    public EditText emergency_contact_name;

    @TextRule(order = 12, minLength = 10, maxLength = 10, message = "Should be 10 digits")
    @Regex(order = 13, pattern = "[0-9]{10}", message = "Please use 10 digit phone number")
    @Required(order = 14, message = "Please fill all mandatory fields")
    @InjectView(R.id.emergency_contact_phone)
    public EditText emergency_contact_phone;

    @Required(order = 15, message = "Please fill all mandatory fields")
    @InjectView(R.id.current_home_address)
    public AutoCompleteTextView current_home_address;

    @InjectView(R.id.current_move_date)
    public DatePicker current_move_date;

    @InjectView(R.id.move_reason)
    public EditText move_reason;

    @Required(order = 17, message = "Please fill all mandatory fields")
    @InjectView(R.id.current_home_owner)
    public EditText current_home_owner;

    @Required(order = 18, message = "Please fill all mandatory fields")
    @InjectView(R.id.current_home_owner_phone)
    public EditText current_home_owner_phone;

    @InjectView(R.id.previous_home_address)
    public EditText previous_home_address;

    @InjectView(R.id.previous_move_in_date)
    public DatePicker previous_move_in_date;

    @InjectView(R.id.previous_move_out_date)
    public DatePicker previous_move_out_date;

    @InjectView(R.id.previous_home_owner)
    public EditText previous_home_owner;

    @InjectView(R.id.previous_home_owner_phone)
    public EditText previous_home_owner_phone;

    @InjectView(R.id.current_supervisor)
    public EditText current_supervisor;

    @Select(order = 19, message = "Please fill all mandatory fields")
    @InjectView(R.id.employment_status)
    public Spinner employment_status;

    @Required(order = 20, message = "Please fill all mandatory fields")
    @InjectView(R.id.cosigner_name)
    public EditText cosigner_name;

    @Required(order = 21, message = "Please fill all mandatory fields")
    @Email(order = 22, message = "Must be in email format")
    @InjectView(R.id.cosigner_email)
    public EditText cosigner_email;

    @InjectView(R.id.desired_move_date)
    public DatePicker desired_move_date;

    private Validator validator;

    AppPrefes appPref;

    ProfileTable profdets;

    protected GooglePlacesAutocompleteAdapter placesAdapter;

    private SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static FragmentProfileForm newInstance(int pos) {
        FragmentProfileForm fragment = new FragmentProfileForm();
        Bundle args = new Bundle();
        args.putInt(Common.KEY_POSITION, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(Common.KEY_POSITION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_form, container, false);
        ButterKnife.setDebug(true);
        ButterKnife.inject(this, v);

        if (!SessionManager.Instance.hasPayed()) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage(getActivity().getResources().getString(R.string.geek_go));
            builder1.setTitle("Alert");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Go to payment",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            activity.finish();
                            Navigation.navigateActivity(activity, ActivityPayment.class, false);
                        }
                    });

            builder1.setNegativeButton("Home",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            activity.finish();
                            Navigation.navigateActivity(activity, ActivityHome.class, false);
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();

        }

        appPref = new AppPrefes(getActivity(), "rentalgeek");
        validator = new Validator(this);
        validator.setValidationListener(this);

        String[] states = RentalGeekApplication.getStringArray(R.array.state_list);
        ArrayAdapter<String> states_adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,R.id.item_text,states);
        drivers_license_state.setAdapter(states_adapter);
        
        String[] booleans = RentalGeekApplication.getStringArray(R.array.tru);
        ArrayAdapter<String> booleans_adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,R.id.item_text,booleans);
        was_evicted.setAdapter(booleans_adapter);
        was_felon.setAdapter(booleans_adapter);

        String[] employment_statuses = RentalGeekApplication.getStringArray(R.array.empl);
        ArrayAdapter<String> employment_status_adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,R.id.item_text,employment_statuses);
        employment_status.setAdapter(employment_status_adapter);

        placesAdapter = new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item);
        current_home_address.setAdapter(placesAdapter);
        current_home_address.setOnItemClickListener(this);
        current_home_address.setValidator(new AutoValidator());
        current_home_address.setOnFocusChangeListener(new FocusListener());
        
        wasEverEnvicted();
        isFelon();

        fetchProfileData();
        
        switchFormVisibility();

        return v;
    }

    protected void switchFormVisibility() {
        switch (this.position) {
            case 1:
                layoutForm1.setVisibility(View.VISIBLE);
                profile_layout_header.setVisibility(View.VISIBLE);
                submit.setText("Next");
                back.setVisibility(View.INVISIBLE);
                break;
            case 2:
                layoutForm2.setVisibility(View.VISIBLE);
                profile_layout_header.setVisibility(View.GONE);
                submit.setText("Next");
                back.setVisibility(View.VISIBLE);
                break;
            case 3:
                layoutForm3.setVisibility(View.VISIBLE);
                profile_layout_header.setVisibility(View.GONE);
                submit.setText("Next");
                back.setVisibility(View.VISIBLE);
                break;
            case 4:
                layoutForm4.setVisibility(View.VISIBLE);
                profile_layout_header.setVisibility(View.GONE);
                submit.setText("Next");
                back.setVisibility(View.VISIBLE);
                break;
            case 5:
                layoutForm5.setVisibility(View.VISIBLE);
                profile_layout_header.setVisibility(View.GONE);
                submit.setText("Next");
                back.setVisibility(View.VISIBLE);
                break;
            case 6:
                layoutForm6.setVisibility(View.VISIBLE);
                profile_layout_header.setVisibility(View.GONE);
                submit.setText("Submit");
                back.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    // setting profile data to view
    private void setProfileData(String response) {
        try {
            // setting the values from the server
            response = response.replaceAll("null", " \" \"");
            AppLogger.log(TAG, "profile get response " + response);
            UserProfile detail = (new Gson()).fromJson(response, UserProfile.class);

            if (detail != null && detail.profiles != null && detail.profiles.size() > 0) {
                Profile profile = detail.profiles.get(0);

                String geekScore = profile.geek_score;
                if (!TextUtils.isEmpty(geekScore)) {
                    appPref.SaveData("geek_score", geekScore);
                }
                Navigation.navigateActivity(getActivity(), ActivityGeekScore.class);
            }
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }

    }

    // parsing patch user response
    private void patchedUserParse(String response) {
        try {
            ProfileIdFindBackend detail = (new Gson()).fromJson(response, ProfileIdFindBackend.class);

            if (detail != null) {

                if (detail.profile != null) {

                    toast("Profile Updated Successfully");
                    //hidekey();

                    if (SessionManager.Instance.hasPayed()) {

                        Navigation.navigateActivity(getActivity(), ActivityGeekScore.class);

                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage(getActivity().getResources().getString(R.string.geek_go));
                        builder1.setTitle("Alert");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Go to payment",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Navigation.navigateActivity(activity, ActivityGeekScore.class, true);
                                    }
                                });

                        builder1.setNegativeButton("Home",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Navigation.navigateActivity(activity, ActivityHome.class, true);
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }

                }
            }
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }

    // parsing create user response
    private void createUserParse(String response) {
        try {
            ProfileIdFindBackend detail = (new Gson()).fromJson(response, ProfileIdFindBackend.class);

            if (detail != null) {
                if (detail.profile != null) {
                    SessionManager.Instance.getCurrentUser().profile_id = detail.profile.id;
                    System.out.println("profile id is " + detail.profile.id);
                    //callPatchUpdateLink(detail.profile.id);
                    toast("Profile Updated Successfully");

                    if (SessionManager.Instance.hasPayed()) {

                        Navigation.navigateActivity(getActivity(), ActivityGeekScore.class);

                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage(getActivity().getResources().getString(R.string.geek_go));
                        builder1.setTitle("Alert");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Go to payment",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        activity.finish();
                                        Navigation.navigateActivity(activity, ActivityGeekScore.class, true);
                                    }
                                });

                        builder1.setNegativeButton("Home",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        activity.finish();
                                        Navigation.navigateActivity(activity, ActivityHome.class, true);
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }

    }

    private RequestParams buildParams(int position, RequestParams params) throws ParseException {

        Calendar calendar = Calendar.getInstance();

        switch(position) {
            case 1:

                //These are required fields so no need to check
                params.put("profile[first_name]", first_name.getText().toString());
                params.put("profile[last_name]", last_name.getText().toString());

                calendar.set(dob.getYear(), dob.getMonth(), dob.getDayOfMonth());
                Date date_dob = calendar.getTime();
                params.put("profile[born_on]",date_format.format(date_dob));

                System.out.println(String.format("First name: %s", first_name.getText().toString()));
                System.out.println(String.format("Last name: %s", last_name.getText().toString()));
                System.out.println(String.format("Born on: %s", date_format.format(date_dob)));

                //Optional fields, so let's check
                if( ! StringUtils.isTrimEmpty(drivers_license) && ! StringUtils.isNotNullAndEquals((String)drivers_license_state.getSelectedItem(),"States") ) {
                    params.put("profile[drivers_license_number]",drivers_license.getText().toString());
                    params.put("profile[drivers_license_state]", (String)drivers_license_state.getSelectedItem());

                    System.out.println(String.format("Drivers License Number: %s", drivers_license.getText().toString()));
                    System.out.println(String.format("Drivers License State: %s", (String) drivers_license_state.getSelectedItem()));
                }

                break;
            case 2:
                //These are required fields so no need to check
                params.put("profile[phone_number]", phone_number.getText().toString());

                System.out.println(String.format("Phone number: %s",phone_number.getText().toString()));

                //Optional fields, let's check
                if ( ! StringUtils.isTrimEmpty(pets_description) ) {
                    params.put("profile[pets_description]", pets_description.getText().toString());
                    System.out.println(String.format("Pet description: %s", pets_description.getText().toString()));
                }

                if ( ! StringUtils.isTrimEmpty(vehicle_description) ) {
                    params.put("profile[vehicles_description]", vehicle_description.getText().toString());
                    System.out.println(String.format("Vehicle description: %s", vehicle_description.getText().toString()));
                }

                if ( ! StringUtils.isNotNullAndEquals((String)was_evicted.getSelectedItem(),"Select") ) {
                    System.out.println(String.format("Evicted: %s",(String) was_evicted.getSelectedItem()));

                    if( ((String)was_evicted.getSelectedItem()).equals("Yes") ) {
                        params.put("profile[was_ever_evicted_explanation]", eviction_description.getText().toString());
                        params.put("profile[was_ever_evicted]", "true");
                        System.out.println(String.format("Evicted description: %s", eviction_description.getText().toString()));
                    }

                    else if ( ((String)was_evicted.getSelectedItem()).equals("No") ) {
                        params.put("profile[was_ever_evicted]", "false");
                    }
                }

                break;
            case 3:
                //These are required fields so no need to check
                if ( StringUtils.isNotNullAndEquals((String)was_felon.getSelectedItem(),"Select") ) {
                    System.out.println(String.format("Felon: %s",(String) was_felon.getSelectedItem()));

                    if( ((String)was_felon.getSelectedItem()).equals("Yes") ) {
                        params.put("profile[is_felon_explanation]", felony_description.getText().toString());
                        params.put("profile[is_felon]", "true");
                        System.out.println(String.format("Felon description: %s", felony_description.getText().toString()));
                    }

                    else if ( ((String)was_felon.getSelectedItem()).equals("No") ) {
                        params.put("profile[is_felon]", "false");
                    }
                }

                params.put("profile[character_reference_name]",character_reference_name.getText().toString());
                params.put("profile[character_reference_contact_info]",character_reference_phone.getText().toString());
                params.put("profile[emergency_contact_name]",emergency_contact_name.getText().toString());
                params.put("profile[emergency_contact_phone_number]",emergency_contact_phone.getText().toString());

                System.out.println(String.format("Character reference name: %s", character_reference_name.getText().toString()));
                System.out.println(String.format("Character reference phone: %s",character_reference_phone.getText().toString()));
                System.out.println(String.format("Emergency contact name: %s",emergency_contact_name.getText().toString()));
                System.out.println(String.format("Emergency contact phone:",emergency_contact_phone.getText().toString()));

                break;
            case 4:
                //These are required fields so no need to check
                params.put("profile[current_home_street]", String.format("%s %s", Common.streetNumber, Common.streetName));
                params.put("profile[current_home_city]", Common.city);
                params.put("profile[current_home_state]", Common.state);
                params.put("profile[current_home_zipcode]", Common.zip);

                calendar.set(current_move_date.getYear(), current_move_date.getMonth(), current_move_date.getDayOfMonth());
                Date date_move = calendar.getTime();
                params.put("profile[current_home_moved_in_on]", date_format.format(date_move));

                params.put("profile[current_home_owner]", current_home_owner.getText().toString());
                params.put("profile[current_home_owner_contact_info]", current_home_owner_phone.getText().toString());

                System.out.println(String.format("Address: %s %s, %s %s, %s", Common.streetNumber, Common.streetName, Common.city, Common.state, Common.zip));
                System.out.println(String.format("Current home move in date: %s",date_format.format(date_move)));

                //Optional fields, let's check
                if ( ! StringUtils.isTrimEmpty(move_reason)) {
                    params.put("profile[current_home_dissatisfaction_explanation]", move_reason.getText().toString());
                    System.out.println(String.format("Reason for moving: %s",move_reason.getText().toString()));
                }

                break;
            case 5:

                //All are optional
                if ( ! StringUtils.isTrimEmpty(previous_home_address)) {
                    params.put("profile[previous_home_street_address]", previous_home_address.getText().toString());

                    calendar.set(previous_move_in_date.getYear(), previous_move_in_date.getMonth(), previous_move_in_date.getDayOfMonth());
                    Date date_move_in = calendar.getTime();
                    params.put("profile[previous_home_moved_in_on]", date_format.format(date_move_in));

                    calendar.set(previous_move_out_date.getYear(), previous_move_out_date.getMonth(), previous_move_out_date.getDayOfMonth());
                    Date date_move_out = calendar.getTime();
                    params.put("profile[previous_home_moved_out]", date_format.format(date_move_out));

                    params.put("profile[previous_home_owner]",previous_home_owner.getText().toString());
                    params.put("profile[previous_home_owner_contact_info]",previous_home_owner_phone.getText().toString());

                    System.out.println(String.format("Previous home address: %s", previous_home_address.getText().toString()));
                    System.out.println(String.format("Previous home move in date: %s", date_format.format(date_move_in)));
                    System.out.println(String.format("Previous home move out date: %s", date_format.format(date_move_out)));
                    System.out.println(String.format("Previous home move owner: %s", previous_home_owner.getText().toString()));
                    System.out.println(String.format("Previous home move owner phone: %s", previous_home_owner_phone.getText().toString()));
                }

                break;
            case 6:
                //These are required fields
                params.put("profile[employment_status]", (String) employment_status.getSelectedItem());
                params.put("profile[cosigner_name]", cosigner_name.getText().toString());
                params.put("profile[cosigner_email_address]", cosigner_email.getText().toString());

                calendar.set(desired_move_date.getYear(), desired_move_date.getMonth(), desired_move_date.getDayOfMonth());
                Date date_desired_date = calendar.getTime();
                params.put("profile[desires_to_move_in_on]", date_format.format(date_desired_date));

                System.out.println(String.format("Employment status: %s", (String) employment_status.getSelectedItem()));
                System.out.println(String.format("Cosigner name: %s",cosigner_name.getText().toString()));
                System.out.println(String.format("Cosigner email: %s",cosigner_email.getText().toString()));
                System.out.println(String.format("Desired move date: %s",date_format.format(date_desired_date)));

                //Optional fields, let's check
                if( !StringUtils.isTrimEmpty(current_supervisor) ) {
                    params.put("profile[current_employment_supervisor]", current_supervisor.getText().toString());
                    System.out.println(String.format("Employment surpervisor: %s", current_supervisor.getText().toString()));
                }

                break;
        }

        return params;
    }

    private RequestParams buildRequestParams(boolean getAll) throws ParseException {

        RequestParams params = new RequestParams();

        if (getAll) {
            params = buildParams(1, params);
            params = buildParams(2, params);
            params = buildParams(3, params);
            params = buildParams(4, params);
            params = buildParams(5, params);
            params = buildParams(6, params);
        } else {
            params = buildParams(position, params);
        }

        return params;
    }

    // Calling Patch request to update the profile
    private void callPatchUpdateLink(String id) {
        try {

            String url = ApiManager.getProfile(id);

            RequestParams params = buildRequestParams(false);

            params.put("profile[user_id]", appPref.getData("Uid"));

            GlobalFunctions.putApiCall(getActivity(), url, params,
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
                                patchedUserParse(content);
                            } catch (Exception e) {
                                AppLogger.log(TAG, e);
                            }
                        }

                        @Override
                        public void onAuthenticationFailed() {

                        }
                    });

        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }

    }

    public void error(String response, int value) {

            try {
                ErrorObj sess = (new Gson()).fromJson(response, ErrorObj.class);

                if (sess != null) {
                    toasts("Phone Number", sess.errors.phone_number);
                    toasts("Birth Date", sess.errors.born_on);
                    toasts("Emergency Contact", sess.errors.emergency_contact_phone_number);
                    toasts("Current Home Move-in Date", sess.errors.current_home_moved_in_on);
                    toasts("Previous Home Date", sess.errors.previous_home_moved_in_on);
                    toasts("Previous Employer Email", sess.errors.previous_employment_employer_email_address);
                    toasts("Previous Employer Phone", sess.errors.previous_employment_employer_phone_number);
                    toasts("Employer Email", sess.errors.current_employment_employer_email_address);
                    toasts("Employer Phone", sess.errors.current_employment_employer_phone_number);
                    toasts("Cosigner Email Address", sess.errors.cosigner_email_address);
                    toasts("Move-in Date", sess.errors.desires_to_move_in_on);
                    toasts("SSN", sess.errors.ssn);
                }

            } catch (Exception e) {
                AppLogger.log(TAG, e);
            }


    }

    private void toasts(String field, List<String> message) {

        try {
            if (!ListUtils.isNullOrEmpty(message))
                showAlert(String.format("%s: %s", field, message.get(0)));
        } catch (IndexOutOfBoundsException e) {
            AppLogger.log(e);
        } catch (NullPointerException e) {
            AppLogger.log(e);
        }
    }

    // Check on validation fail
    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {

        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        }

        else if ( failedView instanceof Spinner ) {
            failedView.requestFocusFromTouch();
            TextView selectedView = (TextView)(((Spinner) failedView).getSelectedView().findViewById(R.id.item_text));
            selectedView.setError(message);
        }
    }

    // After validation success
    @Override
    public void onValidationSucceeded() {

        boolean profileExists = SessionManager.Instance.hasProfile();

        if (!profileExists) {
            if (position < 6) {
                // Just navigate to next screen
                ActivityCreateProfile activity = (ActivityCreateProfile) getActivity();
                activity.flipPager(position);
            } else {
                createProfile();
            }

        } else {
            if (!profileExists) {
                createProfile();
            } else {
                callPatchUpdateLink(SessionManager.Instance.getDefaultProfileId());
            }
        }


    }

    // Validate view and procced
    @OnClick(R.id.submit)
    public void ProfileSubmit() {
        validator.validate();
    }

    @OnClick(R.id.back)
    public void clickback() {
        ActivityCreateProfile activity = (ActivityCreateProfile) getActivity();
        activity.flipPager(position - 2);
    }

    private void wasEverEnvicted() {

        was_evicted.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {
                    eviction_description_layout.setVisibility(View.VISIBLE);
                } else {
                    eviction_description_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void isFelon() {

        was_felon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (position == 1) {
                    felony_description_layout.setVisibility(View.VISIBLE);
                } else {
                    felony_description_layout.setVisibility(View.GONE);
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
            final RequestParams params =  buildRequestParams(true);

            params.put("profile[user_id]", appPref.getData("Uid"));

            /**
            String url = ApiManager.getProfile("");

            GlobalFunctions.postApiCall(getActivity(), url, params,
                    AppPreferences.getAuthToken(),
                    new GeekHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            showProgressDialog(R.string.dialog_msg_loading);
                        }

                        @Override
                        public void onFinish() {
                            hideProgressDialog();
                        }

                        @Override
                        public void onSuccess(String content) {
                            try {
                                createUserParse(content);
                            } catch (Exception e) {
                                AppLogger.log(TAG, e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable ex, String failureResponse) {
                            super.onFailure(ex, failureResponse);
                            error(failureResponse, 0);

                            //DialogManager.showCrouton(activity, failureResponse);
                        }

                        @Override
                        public void onAuthenticationFailed() {

                        }
                    });
             **/

        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }

    }

    // fetch user profile info
    private void fetchProfileData() {

        String url = ApiManager.getProfile("");
        GlobalFunctions.getApiCall(getActivity(), url,
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
                            setProfileData(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
    }

    // email checking function
    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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
            AppLogger.log(TAG, e);
        }

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        String ref = (resultRefs != null && resultRefs.size() > position) ? resultRefs.get(position) : "";
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        String place = getPlaceDetails(ref);
    }

    ArrayList<String> resultRefs = null;


    public String getPlaceDetails(String ref) {

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            URL url = new URL(getPlaceDetailsUrl(ref));

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            AppLogger.log(TAG, e);
            return "";
        } catch (IOException e) {
            AppLogger.log(TAG, e);
            return "";
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());

            JSONObject result = jsonObj.getJSONObject("result");
            JSONArray addressComponents = result.getJSONArray("address_components");

            Common.streetNumber = getAddressComponent(addressComponents, "street_number");
            Common.streetName = getAddressComponent(addressComponents, "route");
            Common.city = getAddressComponent(addressComponents, "locality");
            Common.state = getAddressComponent(addressComponents, "administrative_area_level_1");
            Common.zip = getAddressComponent(addressComponents, "postal_code");

        } catch (JSONException e) {
            AppLogger.log(TAG, e);
        }

        return "";
    }

    public String getAddressComponent(JSONArray addressComponents, String type) {
        if (addressComponents == null || addressComponents.length() == 0) return "";
        try {
            for (int i=0; i<addressComponents.length(); i++) {
                JSONObject jsonObject = addressComponents.getJSONObject(i);
                JSONArray typesObject = jsonObject.getJSONArray("types");
                for (int j=0; j<typesObject.length();j++) {
                    String typeValue = typesObject.getString(j);
                    if (typeValue.equals(type)) {
                        return jsonObject.getString("long_name");
                    }
                }
            }
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
        return "";
    }

    public ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:us");
            sb.append("&types=address");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            AppLogger.log(TAG, e);
            return resultList;
        } catch (IOException e) {
            AppLogger.log(TAG, e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            resultRefs = new ArrayList<String>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                resultRefs.add(predsJsonArray.getJSONObject(i).getString("reference"));
            }
        } catch (JSONException e) {
            AppLogger.log(TAG, e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    private String getPlaceDetailsUrl(String ref){

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key="+API_KEY;

        // reference of place
        String reference = "reference="+ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference+"&"+sensor+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/"+output+"?"+parameters;

        return url;
    }


    protected class AutoValidator implements AutoCompleteTextView.Validator {

        @Override
        public boolean isValid(CharSequence text) {

            if (!ListUtils.isNullOrEmpty(placesAdapter.resultList)) {
                String[] validWords = placesAdapter.resultList.toArray(new String[placesAdapter.resultList.size()]);
                Log.v("Test", "Checking if valid: " + text);
                //Arrays.sort(validWords);
                String searchFor = text.toString();
                if (TextUtils.isEmpty(searchFor)) return false;

                for (int i=0; i<placesAdapter.resultList.size(); i++) {
                    String result = placesAdapter.resultList.get(i);
                    if (searchFor.equals(result)) return true;
                }
//                if (Arrays.binarySearch(validWords, text.toString()) > 0) {
//                    return true;
//                }
            }

            return false;
        }

        @Override
        public CharSequence fixText(CharSequence invalidText) {
            Log.v("Test", "Returning fixed text");

            /* I'm just returning an empty string here, so the field will be blanked,
             * but you could put any kind of action here, like popping up a dialog?
             *
             * Whatever value you return here must be in the list of valid words.
             */
            return "";
        }
    }

    class FocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            AppLogger.log("Test", "Focus changed");
            if (v.getId() == R.id.ed_home_addr && !hasFocus) {
                AppLogger.log(TAG, "Performing validation");
                ((AutoCompleteTextView)v).performValidation();
            }
        }
    }
}
