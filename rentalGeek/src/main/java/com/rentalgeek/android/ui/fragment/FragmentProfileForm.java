package com.rentalgeek.android.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.backend.model.Profile;
import com.rentalgeek.android.backend.model.User;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.SubmitProfileEvent;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.activity.ActivityCreateProfile;
import com.rentalgeek.android.ui.adapter.PlaceAutocompleteAdapter;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.ui.view.AutoCompleteAddressListener;
import com.rentalgeek.android.ui.view.ProfileFieldBinarySelect;
import com.rentalgeek.android.ui.view.ProfileFieldDateChange;
import com.rentalgeek.android.ui.view.ProfileFieldSelect;
import com.rentalgeek.android.ui.view.ProfileFieldTextWatcher;
import com.rentalgeek.android.ui.view.UserFieldTextWatcher;
import com.rentalgeek.android.utils.GeekGson;
import com.rentalgeek.android.utils.ListUtils;
import com.rentalgeek.android.utils.OkAlert;
import com.rentalgeek.android.utils.ResponseParser;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.rentalgeek.android.constants.IntentKey.RESUBMITTING_PROFILE;
import static com.rentalgeek.android.constants.IntentKey.PROFILE_POSITION;

public class FragmentProfileForm extends GeekBaseFragment implements Validator.ValidationListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "FragmentProfileForm";
    private int position;
    private boolean resubmittingProfile = false;

    @InjectView(R.id.layoutForm1) LinearLayout layoutForm1;
    @InjectView(R.id.layoutForm2) LinearLayout layoutForm2;
    @InjectView(R.id.layoutForm3) LinearLayout layoutForm3;
    @InjectView(R.id.layoutForm4) LinearLayout layoutForm4;
    @InjectView(R.id.layoutForm5) LinearLayout layoutForm5;
    @InjectView(R.id.layoutForm6) LinearLayout layoutForm6;
    @InjectView(R.id.profile_layout_header) LinearLayout profile_layout_header;
    @InjectView(R.id.eviction_description_layout) LinearLayout eviction_description_layout;
    @InjectView(R.id.felony_description_layout) LinearLayout felony_description_layout;
    @InjectView(R.id.submit) Button submit;
    @InjectView(R.id.back) Button back;

    @Required(order = 1, message = "Please fill all mandatory fields")
    @InjectView(R.id.first_name) EditText first_name;

    @Required(order = 2, message = "Please fill all mandatory fields")
    @InjectView(R.id.last_name) EditText last_name;

    @InjectView(R.id.dob) public DatePicker dob;

    @InjectView(R.id.drivers_license) public EditText drivers_license;

    @Required(order = 3, message = "Please fill all mandatory fields")
    @TextRule(minLength = 9, message = "Please enter a valid 9 digit SSN", order = 4)
    @InjectView(R.id.ssn) public EditText ssn;

    @InjectView(R.id.drivers_license_state) public Spinner drivers_license_state;

    @Required(order = 4, message = "Please fill all mandatory fields")
    @TextRule(minLength = 10, maxLength = 10, message = "Please use 10 digit phone number", order = 5)
    @Regex(order = 6, pattern = "[0-9]{10}", message = "Please use 10 digit phone number")
    @InjectView(R.id.phone_number) public EditText phone_number;

    @InjectView(R.id.pets_description) public EditText pets_description;

    @InjectView(R.id.vehicle_description) public EditText vehicle_description;

    @Select(order = 7, message = "Please fill all mandatory fields")
    @InjectView(R.id.was_evicted) public Spinner was_evicted;

    @InjectView(R.id.eviction_description) public EditText eviction_description;

    @Select(order = 8, message = "Please fill all mandatory fields")
    @InjectView(R.id.was_felon) public Spinner was_felon;

    @InjectView(R.id.felony_description) public EditText felony_description;

    @Required(order = 9, message = "Please fill all mandatory fields")
    @InjectView(R.id.character_reference_name) public EditText character_reference_name;

    @Required(order = 10, message = "Please fill all mandatory fields")
    @InjectView(R.id.character_reference_phone) public EditText character_reference_phone;

    @Required(order = 11, message = "Please fill all mandatory fields")
    @InjectView(R.id.emergency_contact_name) public EditText emergency_contact_name;

    @TextRule(order = 12, minLength = 10, maxLength = 10, message = "Should be 10 digits")
    @Regex(order = 13, pattern = "[0-9]{10}", message = "Please use 10 digit phone number")
    @Required(order = 14, message = "Please fill all mandatory fields")
    @InjectView(R.id.emergency_contact_phone) public EditText emergency_contact_phone;

    @Required(order = 15, message = "Please fill all mandatory fields")
    @InjectView(R.id.current_home_address) public AutoCompleteTextView current_home_address;

    @InjectView(R.id.current_move_date) public DatePicker current_move_date;

    @InjectView(R.id.move_reason) public EditText move_reason;

    @Required(order = 17, message = "Please fill all mandatory fields")
    @InjectView(R.id.current_home_owner) public EditText current_home_owner;

    @Required(order = 18, message = "Please fill all mandatory fields")
    @InjectView(R.id.current_home_owner_phone) public EditText current_home_owner_phone;

    @InjectView(R.id.previous_home_address) public AutoCompleteTextView previous_home_address;

    @InjectView(R.id.previous_move_in_date) public DatePicker previous_move_in_date;

    @InjectView(R.id.previous_move_out_date) public DatePicker previous_move_out_date;

    @InjectView(R.id.previous_home_owner) public EditText previous_home_owner;

    @InjectView(R.id.previous_home_owner_phone) public EditText previous_home_owner_phone;

    @InjectView(R.id.current_supervisor) public EditText current_supervisor;

    @Select(order = 19, message = "Please fill all mandatory fields")
    @InjectView(R.id.employment_status) public Spinner employment_status;

    @Required(order = 20, message = "Please fill all mandatory fields")
    @InjectView(R.id.cosigner_name) public EditText cosigner_name;

    @Required(order = 21, message = "Please fill all mandatory fields")
    @Email(order = 22, message = "Must be in email format")
    @InjectView(R.id.cosigner_email) public EditText cosigner_email;

    @InjectView(R.id.desired_move_date) public DatePicker desired_move_date;

    private Validator validator;
    AppPrefes appPref;
    GoogleApiClient googleApiClient;

    ArrayAdapter<String> booleans_adapter;
    ArrayAdapter<String> states_adapter;
    ArrayAdapter<String> employment_status_adapter;

    private SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static FragmentProfileForm newInstance(int pos, boolean resubmittingProfile) {
        Bundle bundle = new Bundle();
        bundle.putInt(PROFILE_POSITION, pos);
        bundle.putBoolean(RESUBMITTING_PROFILE, resubmittingProfile);
        FragmentProfileForm fragment = new FragmentProfileForm();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        if (extras != null) {
            this.position = extras.getInt(PROFILE_POSITION);
            this.resubmittingProfile = extras.getBoolean(RESUBMITTING_PROFILE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_form, container, false);
        ButterKnife.inject(this, v);

        appPref = new AppPrefes(getActivity(), "rentalgeek");
        validator = new Validator(this);
        validator.setValidationListener(this);

        googleApiClient = new GoogleApiClient.Builder(getActivity()).addApi(Places.GEO_DATA_API).build();

        setup();
        initialize();
        switchFormVisibility();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    private void initialize() {
        Profile profile = SessionManager.Instance.getDefaultProfile();
        User user = SessionManager.Instance.getCurrentUser();

        if (profile == null)
            return;
        else {
            //Page 1
            first_name.setText(user.first_name);
            last_name.setText(user.last_name);

            if (profile.get("born_on") != null) {
                DateTime datetime = DateTime.parse((String) profile.get("born_on"));
                dob.updateDate(datetime.getYear(), datetime.getMonthOfYear() - 1, datetime.getDayOfMonth());
            }

            if (profile.get("ssn") != null) {
                ssn.setText((String) profile.get("ssn"));
            }

            if (profile.get("drivers_license_number") != null) {
                drivers_license.setText((String) profile.get("drivers_license_number"));
            }

            if (profile.get("drivers_license_state") != null) {
                int position = states_adapter.getPosition((String) profile.get("drivers_license_state"));
                drivers_license_state.setSelection(position);
            }

            //Page 2
            if (profile.get("phone_number") != null) {
                phone_number.setText(profile.get("phone_number").toString());
            }

            if (profile.get("pets_description") != null) {
                pets_description.setText(profile.get("pets_description").toString());
            }

            if (profile.get("vehicles_description") != null) {
                vehicle_description.setText(profile.get("vehicles_description").toString());
            }

            if (profile.get("was_ever_evicted") != null) {
                int position;
                String value = profile.get("was_ever_evicted").toString();

                if (value.equals("false"))
                    position = 2;
                else {
                    position = 1;
                    eviction_description_layout.setVisibility(View.VISIBLE);
                }

                was_evicted.setSelection(position);
            }

            if (profile.get("was_ever_evicted_explanation") != null) {
                eviction_description.setText(profile.get("was_ever_evicted_explanation").toString());
            }

            //Page 3
            if (profile.get("is_felon") != null) {
                int position;
                String value = profile.get("is_felon").toString();

                if (value.equals("false"))
                    position = 2;
                else {
                    position = 1;
                    felony_description_layout.setVisibility(View.VISIBLE);
                }

                was_felon.setSelection(position);
            }

            if (profile.get("is_felon_description") != null) {
                felony_description.setText(profile.get("is_felon_description").toString());
            }

            if (profile.get("character_reference_name") != null) {
                character_reference_name.setText(profile.get("character_reference_name").toString());
            }

            if (profile.get("character_reference_contact_info") != null) {
                character_reference_phone.setText(profile.get("character_reference_contact_info").toString());
            }

            if (profile.get("emergency_contact_name") != null) {
                emergency_contact_name.setText(profile.get("emergency_contact_name").toString());
            }

            if (profile.get("emergency_contact_phone_number") != null) {
                emergency_contact_phone.setText(profile.get("emergency_contact_phone_number").toString());
            }

            //Page 4
            if (profile.get("current_home_street") != null &&
                profile.get("current_home_city") != null &&
                profile.get("current_home_state") != null &&
                profile.get("current_home_zipcode") != null) {

                String street = profile.get("current_home_street").toString();
                String city = profile.get("current_home_city").toString();
                String state = profile.get("current_home_state").toString();
                String zipcode = profile.get("current_home_zipcode").toString();

                String full_address = String.format("%s, %s, %s %s", street, city, state, zipcode);
                current_home_address.setText(full_address);
            }

            if (profile.get("current_home_moved_in_on") != null) {
                DateTime datetime = DateTime.parse((String) profile.get("current_home_moved_in_on"));
                current_move_date.updateDate(datetime.getYear(), datetime.getMonthOfYear(), datetime.getDayOfMonth());
            }

            if (profile.get("current_home_dissatisfaction_explanation") != null) {
                move_reason.setText(profile.get("current_home_dissatisfaction_explanation").toString());
            }

            if (profile.get("current_home_owner") != null) {
                current_home_owner.setText(profile.get("current_home_owner").toString());
            }

            if (profile.get("current_home_owner_contact_info") != null) {
                current_home_owner_phone.setText(profile.get("current_home_owner_contact_info").toString());
            }

            //Page 5
            if (profile.get("previous_home_street") != null &&
                profile.get("previous_home_city") != null &&
                profile.get("previous_home_state") != null &&
                profile.get("previous_home_zipcode") != null) {

                String street = profile.get("previous_home_street").toString();
                String city = profile.get("previous_home_city").toString();
                String state = profile.get("previous_home_state").toString();
                String zipcode = profile.get("previous_home_zipcode").toString();

                String full_address = String.format("%s, %s, %s %s", street, city, state, zipcode);
                previous_home_address.setText(full_address);
            }

            if (profile.get("previous_home_moved_in_on") != null) {
                DateTime datetime = DateTime.parse((String) profile.get("previous_home_moved_in_on"));
                previous_move_in_date.updateDate(datetime.getYear(), datetime.getMonthOfYear(), datetime.getDayOfMonth());
            }

            if (profile.get("previous_home_moved_out") != null) {
                DateTime datetime = DateTime.parse((String) profile.get("previous_home_moved_out"));
                previous_move_out_date.updateDate(datetime.getYear(), datetime.getMonthOfYear(), datetime.getDayOfMonth());
            }

            if (profile.get("previous_home_owner") != null) {
                previous_home_owner.setText(profile.get("previous_home_owner").toString());
            }

            if (profile.get("previous_home_owner_contact_info") != null) {
                previous_home_owner_phone.setText(profile.get("previous_home_owner_contact_info").toString());
            }

            //Page 6
            if (profile.get("current_employment_supervisor") != null) {
                current_supervisor.setText(profile.get("current_employment_supervisor").toString());
            }

            if (profile.get("employment_status") != null) {
                int position = employment_status_adapter.getPosition(profile.get("employment_status").toString());
                employment_status.setSelection(position);
            }

            if (profile.get("cosigner_name") != null) {
                cosigner_name.setText(profile.get("cosigner_name").toString());
            }

            if (profile.get("cosigner_email_address") != null) {
                cosigner_email.setText(profile.get("cosigner_email_address").toString());
            }

            if (profile.get("desires_to_move_in_on") != null) {
                DateTime datetime = DateTime.parse((String) profile.get("desires_to_move_in_on"));
                desired_move_date.updateDate(datetime.getYear(), datetime.getMonthOfYear(), datetime.getDayOfMonth());
            }

        }
    }

    private void setup() {
        String[] booleans = RentalGeekApplication.getStringArray(R.array.tru);
        String[] states = RentalGeekApplication.getStringArray(R.array.state_list);
        String[] employment_statuses = RentalGeekApplication.getStringArray(R.array.empl);

        booleans_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.item_text, booleans);
        states_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.item_text, states);
        employment_status_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.item_text, employment_statuses);

        DateTime now = DateTime.now();

        //Not sure why month index are wrong, subtract 1
        int maxYear = now.getYear() - 16;
        int maxMonth = now.getMonthOfYear();
        int maxDay = now.getDayOfMonth();

        now = new DateTime(maxYear, maxMonth, maxDay, 0, 0);

        if (position == 0) {
            first_name.addTextChangedListener(new UserFieldTextWatcher(first_name));
            last_name.addTextChangedListener(new UserFieldTextWatcher(last_name));
            dob.init(maxYear, maxMonth, maxDay, new ProfileFieldDateChange(dob));
            dob.setMaxDate(now.getMillis());
            ssn.addTextChangedListener(new ProfileFieldTextWatcher(ssn));
            drivers_license.addTextChangedListener(new ProfileFieldTextWatcher(drivers_license));
            drivers_license_state.setOnItemSelectedListener(new ProfileFieldSelect(drivers_license_state));
            drivers_license_state.setAdapter(states_adapter);
        } else if (position == 1) {
            phone_number.addTextChangedListener(new ProfileFieldTextWatcher(phone_number));
            pets_description.addTextChangedListener(new ProfileFieldTextWatcher(pets_description));
            vehicle_description.addTextChangedListener(new ProfileFieldTextWatcher(vehicle_description));
            was_evicted.setOnItemSelectedListener(new ProfileFieldBinarySelect(was_evicted, eviction_description_layout));
            was_evicted.setAdapter(booleans_adapter);
            eviction_description.addTextChangedListener(new ProfileFieldTextWatcher(eviction_description));
        } else if (position == 2) {
            was_felon.setOnItemSelectedListener(new ProfileFieldBinarySelect(was_felon, felony_description_layout));
            was_felon.setAdapter(booleans_adapter);
            felony_description.addTextChangedListener(new ProfileFieldTextWatcher(felony_description));
            character_reference_name.addTextChangedListener(new ProfileFieldTextWatcher(character_reference_name));
            character_reference_phone.addTextChangedListener(new ProfileFieldTextWatcher(character_reference_phone));
            emergency_contact_name.addTextChangedListener(new ProfileFieldTextWatcher(emergency_contact_name));
            emergency_contact_phone.addTextChangedListener(new ProfileFieldTextWatcher(emergency_contact_phone));
        } else if (position == 3) {
            PlaceAutocompleteAdapter current_home_address_adapter = new PlaceAutocompleteAdapter(getActivity(), googleApiClient);
            current_home_address.setOnItemClickListener(new AutoCompleteAddressListener(current_home_address));
            current_home_address.setAdapter(current_home_address_adapter);
            current_move_date.init(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth(), new ProfileFieldDateChange(current_move_date));
            move_reason.addTextChangedListener(new ProfileFieldTextWatcher(move_reason));
            current_home_owner.addTextChangedListener(new ProfileFieldTextWatcher(current_home_owner));
            current_home_owner_phone.addTextChangedListener(new ProfileFieldTextWatcher(current_home_owner_phone));
        } else if (position == 4) {
            PlaceAutocompleteAdapter previous_home_address_adapter = new PlaceAutocompleteAdapter(getActivity(), googleApiClient);
            previous_home_address.setOnItemClickListener(new AutoCompleteAddressListener(previous_home_address));
            previous_home_address.setAdapter(previous_home_address_adapter);
            previous_move_in_date.init(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth(), new ProfileFieldDateChange(previous_move_in_date));
            previous_move_out_date.init(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth(), new ProfileFieldDateChange(previous_move_out_date));
            previous_home_owner.addTextChangedListener(new ProfileFieldTextWatcher(previous_home_owner));
            previous_home_owner_phone.addTextChangedListener(new ProfileFieldTextWatcher(previous_home_owner_phone));
        } else if (position == 5) {
            current_supervisor.addTextChangedListener(new ProfileFieldTextWatcher(current_supervisor));
            employment_status.setOnItemSelectedListener(new ProfileFieldSelect(employment_status));
            employment_status.setAdapter(employment_status_adapter);
            cosigner_name.addTextChangedListener(new ProfileFieldTextWatcher(cosigner_name));
            cosigner_email.addTextChangedListener(new ProfileFieldTextWatcher(cosigner_email));
            desired_move_date.init(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth(), new ProfileFieldDateChange(desired_move_date));
        }
    }

    protected void switchFormVisibility() {
        switch (this.position) {
            case 0:
                layoutForm1.setVisibility(View.VISIBLE);
                profile_layout_header.setVisibility(View.VISIBLE);
                submit.setText("Next");
                back.setVisibility(View.INVISIBLE);
                break;
            case 1:
                layoutForm2.setVisibility(View.VISIBLE);
                profile_layout_header.setVisibility(View.GONE);
                submit.setText("Next");
                back.setVisibility(View.VISIBLE);
                break;
            case 2:
                layoutForm3.setVisibility(View.VISIBLE);
                profile_layout_header.setVisibility(View.GONE);
                submit.setText("Next");
                back.setVisibility(View.VISIBLE);
                break;
            case 3:
                layoutForm4.setVisibility(View.VISIBLE);
                profile_layout_header.setVisibility(View.GONE);
                submit.setText("Next");
                back.setVisibility(View.VISIBLE);
                break;
            case 4:
                layoutForm5.setVisibility(View.VISIBLE);
                profile_layout_header.setVisibility(View.GONE);
                submit.setText("Next");
                back.setVisibility(View.VISIBLE);
                break;
            case 5:
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
        User user = SessionManager.Instance.getCurrentUser();

        String format = "user[profile_attributes][%s]";

        for (String field : profile.getFieldNames()) {
            Object value = profile.get(field);

            if (value != null && !value.toString().isEmpty()) {
                params.put(String.format(format, field), value.toString());
            }
        }

        params.put("user[first_name]", user.first_name);
        params.put("user[last_name]", user.last_name);

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
        } else if (failedView instanceof Spinner) {
            failedView.requestFocusFromTouch();
            TextView selectedView = (TextView) (((Spinner) failedView).getSelectedView().findViewById(R.id.item_text));
            selectedView.setError(message);
        }
    }

    // After validation success
    @Override
    public void onValidationSucceeded() {
        if (position == 0) {
            User user = SessionManager.Instance.getCurrentUser();
            AppPreferences.putFirstName(user.first_name);
            AppPreferences.putLastName(user.last_name);
        }

        if (position >= 0 && position <= 4) {
            // Just navigate to next screen
            ActivityCreateProfile activity = (ActivityCreateProfile) getActivity();
            activity.flipPager(position + 1);
            AppPreferences.putProfile(GeekGson.getInstance().toJson(SessionManager.Instance.getDefaultProfile()));
        } else if (position == 5) {
            createProfile();
        }

        AppPreferences.putProfilePage(Integer.toString(position));
    }

    // Validate view and procced
    @OnClick(R.id.submit)
    public void ProfileSubmit() {
        validator.validate();
    }

    @OnClick(R.id.back)
    public void clickback() {
        ActivityCreateProfile activity = (ActivityCreateProfile) getActivity();
        activity.flipPager(position - 1);
    }

    // Create profile params and Api call
    public void createProfile() {
        try {
            final RequestParams params = buildRequestParams();
            System.out.println(params);

            String url = ApiManager.specificUserUrl(SessionManager.Instance.getCurrentUser().id);

            GlobalFunctions.putApiCall(getActivity(), url, params,
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
                            LoginBackend user = GeekGson.getInstance().fromJson(content, LoginBackend.class);
                            SessionManager.Instance.onUserLoggedIn(user);
                            AppPreferences.removeProfile();
                            AppEventBus.post(new SubmitProfileEvent(resubmittingProfile));
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable ex, String failureResponse) {
                        System.out.println(failureResponse);
                        super.onFailure(ex, failureResponse);
                        ResponseParser.ErrorMsg errorMsg = new ResponseParser().humanizedErrorMsg(failureResponse);
                        OkAlert.show(getActivity(), errorMsg.title, errorMsg.msg);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        System.out.println("Failed authentication." + "");
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

}
