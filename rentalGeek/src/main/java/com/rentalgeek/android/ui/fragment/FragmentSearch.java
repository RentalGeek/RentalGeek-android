package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.model.PropertyManager;
import com.rentalgeek.android.backend.model.PropertyManagerRoot;
import com.rentalgeek.android.constants.SharedPrefs;
import com.rentalgeek.android.mvp.search.SearchView;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.storage.PropertyManagementCache;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.ui.view.SearchOptionButton;
import com.rentalgeek.android.ui.view.SimpleSpinner;
import com.rentalgeek.android.utils.FilterParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class FragmentSearch extends GeekBaseFragment implements SearchView {

    @InjectViews({R.id.btn_bed0, R.id.btn_bed1, R.id.btn_bed2, R.id.btn_bed3, R.id.btn_bed4})
    List<SearchOptionButton> bedBtns;

    @InjectViews({R.id.btn_bath1, R.id.btn_bath2, R.id.btn_bath3, R.id.btn_bath4})
    List<SearchOptionButton> bathBtns;

    @InjectView(R.id.price_seek) SeekBar priceSeeker;
    @InjectView(R.id.rent_range) TextView rentRangeTextView;
    @InjectView(R.id.management_company_spinner) SimpleSpinner managementCompanySpinner;

    private static final String LOADING_TEXT = "Loading...";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        bedBtns = new ArrayList<>();
        bathBtns = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.inject(this, view);

        priceSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rentRangeTextView.setText("$" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setUpSpinner();
        rememberPreviousSearchSettings();

        return view;
    }

    private void rememberPreviousSearchSettings() {
        priceSeeker.setProgress(AppPreferences.getSearchMaxPrice());

        ArrayList<Integer> previouslySelectedButtonIds = AppPreferences.getSearchSelectedButtons();
        ArrayList<SearchOptionButton> allButtons = new ArrayList<>();

        for (SearchOptionButton button : bathBtns) {
            allButtons.add(button);
        }
        for (SearchOptionButton button : bedBtns) {
            allButtons.add(button);
        }

        for (SearchOptionButton button : allButtons) {
            for (int selectedButtonId : previouslySelectedButtonIds) {
                if (button.getId() == selectedButtonId) {
                    button.pressed();
                    break;
                }
            }
        }

        restorePreviousManagementCompanySelection();
    }

    private void restorePreviousManagementCompanySelection() {
        String currentlySelectedSpinnerItem = managementCompanySpinner.getSelectedItem().toString();
        if (!currentlySelectedSpinnerItem.equals(LOADING_TEXT)) {
            int indexToSelect = AppPreferences.getManagementCompanySelectionIndex();
            managementCompanySpinner.setSelection(indexToSelect);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.btn_bath1, R.id.btn_bath2, R.id.btn_bath3, R.id.btn_bath4})
    public void bathButtonPressed(SearchOptionButton button) {
        if (button.selected) {
            button.reset();
            FilterParams.INSTANCE.params.remove("bathrooms_count");
            AppPreferences.putSearchBathCount(SharedPrefs.NO_SELECTION);
        } else {
            for (SearchOptionButton b : bathBtns) {
                b.reset();
            }
            button.pressed();
        }
    }
    
    @OnClick({R.id.btn_bed0,R.id.btn_bed1, R.id.btn_bed2, R.id.btn_bed3, R.id.btn_bed4})
    public void bedButtonPressed(SearchOptionButton button) {
        if (button.selected) {
            button.reset();
            FilterParams.INSTANCE.params.remove("bedrooms_count");
            AppPreferences.putSearchBedCount(SharedPrefs.NO_SELECTION);
        } else {
            for (SearchOptionButton b : bedBtns) {
                b.reset();
            }
            button.pressed();
        }
    }

    @OnClick(R.id.reset_search)
    public void onResetClick() {
        priceSeeker.setProgress(1000);

        for (SearchOptionButton button : bedBtns) {
            button.reset();
        }

        for (SearchOptionButton button : bathBtns) {
            button.reset();
        }

        managementCompanySpinner.setSelection(0);
    }

    @OnClick(R.id.search_submit)
    public void onSubmitClick() {
        ArrayList<String> bathValues = new ArrayList<String>();
        ArrayList<Integer> bathIds = new ArrayList<>();
        ArrayList<String> bedValues = new ArrayList<String>();
        ArrayList<Integer> bedIds = new ArrayList<>();

        for (SearchOptionButton button : bedBtns) {
            if (button.isSelected()) {
                bedValues.add(button.getValue());
                FilterParams.INSTANCE.params.put("bedrooms_count", button.getValue());
                AppPreferences.putSearchBedCount(Integer.parseInt(button.getValue()));
                bedIds.add(button.getId());
            }
        }

        for (SearchOptionButton button : bathBtns) {
            if (button.isSelected()) {
                bathValues.add(button.getValue());
                FilterParams.INSTANCE.params.put("bathrooms_count", button.getValue());
                AppPreferences.putSearchBathCount(Integer.parseInt(button.getValue()));
                bathIds.add(button.getId());
            }
        }

        String selectedPropertyManagementCompany = managementCompanySpinner.getSelectedItem().toString();
        if (!selectedPropertyManagementCompany.equals("") && !selectedPropertyManagementCompany.equals(LOADING_TEXT)) {
            int selectedPropertyId = PropertyManagementCache.INSTANCE.getIdFromName(selectedPropertyManagementCompany);
            if (selectedPropertyId != PropertyManagementCache.ID_NOT_FOUND) {
                AppPreferences.putSelectedManagementCompanyId(selectedPropertyId);
                FilterParams.INSTANCE.params.put("property_manager_id", Integer.toString(selectedPropertyId));
            }
        } else {
            AppPreferences.putSelectedManagementCompanyId(0);
            FilterParams.INSTANCE.params.remove("property_manager_id");
        }

        AppPreferences.putSearchMaxPrice(priceSeeker.getProgress());
        FilterParams.INSTANCE.params.put("max_price", Integer.toString(priceSeeker.getProgress()));
        bathIds.addAll(bedIds);
        AppPreferences.putSelectedSearchButtons(bathIds);
        AppPreferences.putManagementCompanySelectionIndex(managementCompanySpinner.getSelectedItemPosition());

        getActivity().finish();
    }

    private void setUpSpinner() {
        managementCompanySpinner.populate(getActivity(), LOADING_TEXT);

        if (PropertyManagementCache.INSTANCE.propertyManagers == null) {
            retrievePropertyManagersFromBackend();
        } else {
            populateFromPropertyManagers(PropertyManagementCache.INSTANCE.propertyManagers);
        }
    }

    private void retrievePropertyManagersFromBackend() {
        GlobalFunctions.getApiCall(ApiManager.propertyManagersUrl(), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                PropertyManagerRoot propertyManagerRoot = new Gson().fromJson(content, PropertyManagerRoot.class);
                populateFromPropertyManagers(propertyManagerRoot.property_managers);
                PropertyManagementCache.INSTANCE.propertyManagers = propertyManagerRoot.property_managers;
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
                managementCompanySpinner.populate(getActivity(), "");
            }
        });
    }

    private void populateFromPropertyManagers(ArrayList<PropertyManager> propertyManagers) {
        List<String> propertyNames = new ArrayList<>();
        propertyNames.add("");
        for (PropertyManager property : propertyManagers) {
            propertyNames.add(property.name);
        }

        managementCompanySpinner.populate(getActivity(), propertyNames);
    }

}
