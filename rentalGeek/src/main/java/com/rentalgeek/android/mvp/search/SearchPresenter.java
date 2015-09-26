package com.rentalgeek.android.mvp.search;

import android.util.Log;

import android.os.Bundle;

import com.rentalgeek.android.R;

import com.rentalgeek.android.api.ApiManager;

import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.net.GeekHttpResponseHandler;

import com.rentalgeek.android.pojos.Rental;

import com.rentalgeek.android.ui.preference.AppPreferences;

import com.rentalgeek.android.utils.GeekGson;

import com.rentalgeek.android.storage.RentalCache;

import com.rentalgeek.android.RentalGeekApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

import java.net.URLEncoder;

import java.lang.StringBuilder;


public class SearchPresenter implements Presenter {
    
    private static final String TAG = SearchPresenter.class.getSimpleName();
    private SearchView searchView;

    public SearchPresenter(SearchView searchView) {
        this.searchView = searchView;
    }

    @Override public void getRentalOfferings(Bundle bundle) {
        if( bundle != null ) {
            
            String token = AppPreferences.getAuthToken();
            StringBuilder parameters = new StringBuilder();

            List<String> bedValues = bundle.getStringArrayList("BED_VALUES");
            List<String> bathValues = bundle.getStringArrayList("BATH_VALUES");

            if( bedValues != null ) {
                parameters.append("&search[bedroom]=");

                final int count = bedValues.size();

                for( int i = 0; i < count; i++ ) {
                    if( i == count - 1)
                        parameters.append(bedValues.get(i));
                    else
                        parameters.append(bedValues.get(i)+",");
                }
            }
            
            if( bathValues != null ) {
                
                parameters.append("&search[bathroom]=");

                final int count = bathValues.size();

                for( int i = 0; i < count; i++ ) {
                    if( i == count - 1)
                        parameters.append(bathValues.get(i));
                    else
                        parameters.append(bathValues.get(i)+" ");
                }
            }
            
            //Need to replace first & with a blank
            String query = parameters.toString().replaceFirst("&","");

            String url = ApiManager.getPropertySearchUrl(query);
            
            System.out.println(url);

            GlobalFunctions.getApiCall(null,url,token,new GeekHttpResponseHandler() {
                @Override public void onStart() {}

                @Override public void onFinish() {}

                @Override public void onSuccess(String response) {
                    
                try {
                        JSONObject json = new JSONObject(response);
                        JSONArray rentalOfferings = json.getJSONArray("rental_offerings");

                        Rental[] rentals = GeekGson.getInstance().fromJson(rentalOfferings.toString(),Rental[].class);
                        

                        if( rentals != null && rentals.length > 0 ) {
                            
                            System.out.println(String.format("Found %d rentals based on query.",rentals.length));
                            
                            ArrayList<String> rental_ids = new ArrayList<String>();

                            for(Rental rental : rentals) {
                                RentalCache.getInstance().add(rental);
                                rental_ids.add(rental.getId());
                            }
                           
                            Bundle rental_bundle = new Bundle();
                            rental_bundle.putStringArrayList("RENTALS",rental_ids);
                            searchView.returnRentals(rental_bundle);
                        }

                        else {
                            String title = RentalGeekApplication.getResourceString(R.string.search_title);
                            String msg = RentalGeekApplication.getResourceString(R.string.search_none);
                            searchView.showMessage(title,msg);
                        }
                    }

                    catch(Exception e) {
                        Log.e(TAG,e.getMessage());
                    }
                }

                @Override public void onAuthenticationFailed() {}
            });
           
        }
    }
}
