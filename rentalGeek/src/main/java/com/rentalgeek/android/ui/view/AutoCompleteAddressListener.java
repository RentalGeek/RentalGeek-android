package com.rentalgeek.android.ui.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.model.Profile;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.adapter.PlaceAutocompleteAdapter;
import com.rentalgeek.android.utils.ParseAddress;

public class AutoCompleteAddressListener implements AdapterView.OnItemClickListener {

    private AutoCompleteTextView autoCompleteTextView;

    public AutoCompleteAddressListener(AutoCompleteTextView autoCompleteTextView) {
        this.autoCompleteTextView = autoCompleteTextView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */

        String place_id = ((PlaceAutocompleteAdapter) parent.getAdapter()).getItem(position).getPlaceId();

        if (place_id != null && !place_id.isEmpty()) {
            String url = ApiManager.getFullAddress(place_id);

            System.out.println(url);

            GlobalFunctions.getApiCall(null, url, null, new GeekHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            ParseAddress.Address address = ParseAddress.parse(response);

                            String prefix = (String) autoCompleteTextView.getTag();

                            if (prefix != null && !prefix.isEmpty()) {
                                String street_field = String.format("%s_street", prefix);
                                String city_field = String.format("%s_city", prefix);
                                String state_field = String.format("%s_state", prefix);
                                String zipcode_field = String.format("%s_zipcode", prefix);

                                Profile profile = SessionManager.Instance.getDefaultProfile();

                                profile.set(street_field, String.format("%s %s", address.getStreetNumber(), address.getStreetName()));
                                profile.set(city_field, address.getCity());
                                profile.set(state_field, address.getState());
                                profile.set(zipcode_field, address.getZipcode());
                            }
                        }

                        @Override
                        public void onFailure(Throwable ex, String response) {
                            System.out.println(response);
                        }
                    }
            );
        }
    }
}
