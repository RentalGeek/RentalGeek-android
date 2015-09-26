package com.rentalgeek.android.mvp.rental;

import android.util.Log;

import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.mvp.common.StarPresenter;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.storage.RentalCache;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.GeekGson;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class RentalPresenter extends StarPresenter implements Presenter{
    
    private static final String TAG = RentalPresenter.class.getSimpleName();
   
    private RentalView rentalView;

    public RentalPresenter(RentalView rentalView) {
        this.rentalView = rentalView;    
    }
    
    @Override public void apply(String rental_id) {
        if( rental_id != null && ! rental_id.isEmpty() ) {
            
            String url = ApiManager.postApplication();
            String token = AppPreferences.getAuthToken();
            
            RequestParams params = new RequestParams();
            params.put("application[rental_offering_id]",rental_id);

            System.out.println(url);

            GlobalFunctions.postApiCall(null,url,params,token,new GeekHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    try {
                        System.out.println(response);
                        JSONObject json = new JSONObject(response);
                    }

                    catch(Exception e) {
                        Log.e(TAG,e.getMessage());
                    }
                }

                @Override
                public void onFailure(Throwable ex, String response) {
                    try {
                    
                        System.out.println(response);
                        JSONObject json = new JSONObject(response);

                        if( json.has("success") ) {
                            boolean success = json.getBoolean("success");

                           if( ! success ) {
                                if( json.has("is_cosigner") ) {
                                    boolean is_cosigner = json.getBoolean("is_cosigner");
                                    
                                    //Redirect to cosigner profile creation process
                                    if( is_cosigner ) {
                                    }
                                
                                    //Redirect to profile creation process
                                    else {
                                        rentalView.goToCreateProfile();
                                    }
                                }
                            }

                            //Change button to applied and disable 
                            else {
                        
                            }
                        }
                    }

                    catch(Exception e) {
                        Log.e(TAG,e.getMessage());
                    }
                }
            });
        }
    }

    @Override public void getRental(String rental_id) {
        Rental rental = RentalCache.getInstance().get(rental_id);
        
        if( rental == null ) {

            System.out.println("Not found in cache");
            String url = ApiManager.getRental(rental_id);
            String token = AppPreferences.getAuthToken();
            
            GlobalFunctions.getApiCall(null,url,token, new GeekHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {

                    try {
                        JSONObject json = new JSONObject(response);

                        if( json.has("rental_offering") ) {
                            JSONObject rental_json = json.getJSONObject("rental_offering");
                            Rental rental = GeekGson.getInstance().fromJson(rental_json.toString(),Rental.class);
                            RentalCache.getInstance().add(rental);
                            rentalView.showRental(rental);
                        }
                    }

                    catch(Exception e) {
                        Log.e(TAG,e.getMessage());
                    }
                }
            });
        }
        
        else
            rentalView.showRental(rental);
    }
}

