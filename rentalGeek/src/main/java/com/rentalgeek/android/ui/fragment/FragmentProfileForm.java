package com.rentalgeek.android.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
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
import com.rentalgeek.android.backend.model.Profile;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityCreateProfile;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.activity.ActivityPayment;
import com.rentalgeek.android.ui.adapter.PlaceAutocompleteAdapter;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.ui.view.AutoCompleteAddressListener;
import com.rentalgeek.android.ui.view.ProfileFieldBinarySelect;
import com.rentalgeek.android.ui.view.ProfileFieldDateChange;
import com.rentalgeek.android.ui.view.ProfileFieldSelect;
import com.rentalgeek.android.ui.view.ProfileFieldTextWatcher;
import com.rentalgeek.android.utils.ListUtils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentProfileForm extends GeekBaseFragment implements Validator.ValidationListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "FragmentProfileForm";

    private int position;

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

    @Required(order = 3, message = "Please fill all mandatory fields")
    @TextRule(minLength = 9, message = "Please enter a valid 9 digit SSN", order = 4)
    @InjectView(R.id.ssn)
    public EditText ssn;

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
    public AutoCompleteTextView previous_home_address;

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

    GoogleApiClient googleApiClient;

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

        googleApiClient = new GoogleApiClient.Builder(getActivity()).addApi(Places.GEO_DATA_API).build();

        setup();

        switchFormVisibility();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    private void setup() {
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

        first_name.addTextChangedListener(new ProfileFieldTextWatcher(first_name));
        last_name.addTextChangedListener(new ProfileFieldTextWatcher(last_name));
        ssn.addTextChangedListener(new ProfileFieldTextWatcher(ssn));
        drivers_license.addTextChangedListener(new ProfileFieldTextWatcher(drivers_license));
        phone_number.addTextChangedListener(new ProfileFieldTextWatcher(phone_number));
        pets_description.addTextChangedListener(new ProfileFieldTextWatcher(pets_description));
        vehicle_description.addTextChangedListener(new ProfileFieldTextWatcher(vehicle_description));
        eviction_description.addTextChangedListener(new ProfileFieldTextWatcher(eviction_description));
        felony_description.addTextChangedListener(new ProfileFieldTextWatcher(felony_description));
        character_reference_name.addTextChangedListener(new ProfileFieldTextWatcher(character_reference_name));
        character_reference_phone.addTextChangedListener(new ProfileFieldTextWatcher(character_reference_phone));
        emergency_contact_name.addTextChangedListener(new ProfileFieldTextWatcher(emergency_contact_name));
        emergency_contact_phone.addTextChangedListener(new ProfileFieldTextWatcher(emergency_contact_phone));
        move_reason.addTextChangedListener(new ProfileFieldTextWatcher(move_reason));
        current_home_owner.addTextChangedListener(new ProfileFieldTextWatcher(current_home_owner));
        current_home_owner_phone.addTextChangedListener(new ProfileFieldTextWatcher(current_home_owner_phone));
        previous_home_owner.addTextChangedListener(new ProfileFieldTextWatcher(previous_home_owner));
        previous_home_owner_phone.addTextChangedListener(new ProfileFieldTextWatcher(previous_home_owner_phone));
        current_supervisor.addTextChangedListener(new ProfileFieldTextWatcher(current_supervisor));
        cosigner_name.addTextChangedListener(new ProfileFieldTextWatcher(cosigner_name));
        cosigner_email.addTextChangedListener(new ProfileFieldTextWatcher(cosigner_email));

        DateTime now = DateTime.now();

        //Not sure why month index are wrong, subtract 1
        dob.init(now.getYear(), now.getMonthOfYear()-1, now.getDayOfMonth(), new ProfileFieldDateChange(dob));
        current_move_date.init(now.getYear(), now.getMonthOfYear()-1, now.getDayOfMonth(), new ProfileFieldDateChange(current_move_date));
        previous_move_in_date.init(now.getYear(), now.getMonthOfYear()-1, now.getDayOfMonth(), new ProfileFieldDateChange(previous_move_in_date));
        previous_move_out_date.init(now.getYear(), now.getMonthOfYear()-1, now.getDayOfMonth(), new ProfileFieldDateChange(previous_move_out_date));
        desired_move_date.init(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth(), new ProfileFieldDateChange(desired_move_date));

        drivers_license_state.setOnItemSelectedListener(new ProfileFieldSelect(drivers_license_state));
        was_evicted.setOnItemSelectedListener(new ProfileFieldBinarySelect(was_evicted,eviction_description_layout));
        was_felon.setOnItemSelectedListener(new ProfileFieldBinarySelect(was_felon,felony_description_layout));
        employment_status.setOnItemSelectedListener(new ProfileFieldSelect(employment_status));

        PlaceAutocompleteAdapter current_home_address_adapter = new PlaceAutocompleteAdapter(getActivity(),googleApiClient);
        current_home_address.setOnItemClickListener(new AutoCompleteAddressListener(current_home_address));
        current_home_address.setAdapter(current_home_address_adapter);

        PlaceAutocompleteAdapter previous_home_address_adapter = new PlaceAutocompleteAdapter(getActivity(),googleApiClient);
        previous_home_address.setOnItemClickListener(new AutoCompleteAddressListener(previous_home_address));
        previous_home_address.setAdapter(previous_home_address_adapter);

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

    private RequestParams buildRequestParams() {

        RequestParams params = new RequestParams();

        Profile profile = SessionManager.Instance.getDefaultProfile();

        String format = "profile[%s]";

        params.put("profile[user_id]", appPref.getData("Uid"));

        for( String field : profile.getFieldNames() ) {

            Object value = profile.get(field);

            if( value != null && ! value.toString().isEmpty() )
                params.put(String.format(format,field),value.toString());
        }

        return params;
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

        if (position >= 1 && position < 6 ) {
            // Just navigate to next screen
            ActivityCreateProfile activity = (ActivityCreateProfile) getActivity();
            activity.flipPager(position);
        } else if ( position == 6) {
            createProfile();
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

    // Create profile params and Api call
    public void createProfile() {
        try {

            final RequestParams params =  buildRequestParams();

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
                                System.out.println(content);
                            } catch (Exception e) {
                                AppLogger.log(TAG, e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable ex, String failureResponse) {
                            super.onFailure(ex, failureResponse);
                            error(failureResponse, 0);
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            System.out.println("Failed authentication." +"");
                        }
                    });
        } catch (Exception e) {
            AppLogger.log(TAG, e);
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
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
