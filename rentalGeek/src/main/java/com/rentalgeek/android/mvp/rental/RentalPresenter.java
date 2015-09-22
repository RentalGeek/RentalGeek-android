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

import org.json.JSONObject;

public class RentalPresenter extends StarPresenter implements Presenter{
    
    private static final String TAG = RentalPresenter.class.getSimpleName();
   
    private RentalView rentalView;
    
    public RentalPresenter(RentalView rentalView) {
        this.rentalView = rentalView;    
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
                        Log.i(TAG,response);
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

