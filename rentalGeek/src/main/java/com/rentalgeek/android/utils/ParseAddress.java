package com.rentalgeek.android.utils;

import com.rentalgeek.android.logging.AppLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseAddress {

    private static final String TAG = ParseAddress.class.getSimpleName();

    public static Address parse(String response) {

        if (response != null && !response.isEmpty()) {
            Address address = new Address();

            try {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(response);

                JSONObject result = jsonObj.getJSONObject("result");
                JSONArray addressComponents = result.getJSONArray("address_components");

                address.setState(getAddressComponent(addressComponents, "administrative_area_level_1"));
                address.setZipcode(getAddressComponent(addressComponents, "postal_code"));
                address.setStreetName(getAddressComponent(addressComponents, "route"));
                address.setCity(getAddressComponent(addressComponents, "locality"));
                address.setStreetNumber(getAddressComponent(addressComponents, "street_number"));

                return address;
            } catch (JSONException e) {
                AppLogger.log(TAG, e);
            }
        }

        return null;
    }

    private static String getAddressComponent(JSONArray addressComponents, String type) {
        if (addressComponents == null || addressComponents.length() == 0) return "";
        try {
            for (int i = 0; i < addressComponents.length(); i++) {
                JSONObject jsonObject = addressComponents.getJSONObject(i);
                JSONArray typesObject = jsonObject.getJSONArray("types");
                for (int j = 0; j < typesObject.length(); j++) {
                    String typeValue = typesObject.getString(j);
                    if (typeValue.equals(type) || typeValue.contains(type)) {
                        return jsonObject.getString("long_name");
                    }
                }
            }
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
        return "";
    }

    public static class Address {
        private String street_name;
        private String street_number;
        private String zipcode;
        private String city;
        private String state;

        public String getStreetName() {
            return street_name;
        }

        public String getZipcode() {
            return zipcode;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getStreetNumber() {
            return street_number;
        }

        public void setStreetName(String street_name) {
            this.street_name = street_name;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setStreetNumber(String street_number) {
            this.street_number = street_number;
        }

        public String toString() {
            return String.format("%s %s,%s,%s %s", street_number, street_name, city, state, zipcode);
        }
    }

}
