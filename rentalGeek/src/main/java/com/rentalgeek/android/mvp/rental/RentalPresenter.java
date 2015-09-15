package com.rentalgeek.android.mvp.rental;

import android.util.Log;
import org.json.JSONObject;
import com.rentalgeek.android.pojos.Rental;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.utils.GeekGson;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.storage.RentalCache;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.ui.preference.AppPreferences;

public class RentalPresenter implements RentalPresenter {
    
    private static final String TAG = RentalPresenter.class.getSimpleName();
   
    private RentalView rentalView;
    
    public RentalPresenter(RentalView rentalView) {
        this.rentalView = rentalView;    
    }

    @Override
    public void selectStar(final String rental_id, final String user_id) {
        
        if( ( rental_id == null || rental_id.isEmpty() )  ||  ( user_id == null || user_id.isEmpty() ) ) 
            return;
        
        RequestParams params = new RequestParams();
        params.put("starred_property[user_id]",user_id);
        params.put("starred_property[rental_offering_id]",rental_id);

        String url = ApiManager.postRentalStar();
        String token = AppPreferences.getAuthToken();

        GlobalFunctions.postApiCall(null,url,params,token, new GeekHttpResponseHandler() {
            
            @Override public void onSuccess(String response) {
                try {
                    
                    JSONObject resp_json = new JSONObject(response);
                    
                    if( resp_json.has("starred_property") ) {
                        
                        JSONObject rental_json = resp_json.getJSONObject("starred_property");
                        
                        if( rental_json.has("id") ) {
                            
                            Rental rental = RentalCache.getInstance().get(rental_id);
                            rental.setStarId(rental_json.getString("id"));
                            rental.setStarred(true);
                            
                            RentalCache.getInstance().add(rental);
                            System.out.println(RentalCache.getInstance());
                            
                            rentalView.selectStar();
                        }
                    }
                }

                catch(Exception e) {
                    Log.e(TAG,e.getMessage());
                }
            }

        });
    }
    
    @Override
    public void unselectStar(final String rental_id) {
         if( rental_id == null || rental_id.isEmpty()  ) 
            return;

        else {

            String star_id = RentalCache.getInstance().get(rental_id).getStarId();
            
            String url = ApiManager.deleteRentalStar(star_id);
            String token = AppPreferences.getAuthToken();
            
            GlobalFunctions.deleteApiCall(null,url,token, new GeekHttpResponseHandler() {
                @Override public void onSuccess(String response) {
                    Rental rental = RentalCache.getInstance().get(rental_id);
                    rental.setStarred(false);
                    RentalCache.getInstance().add(rental);
                    rentalView.unselectStar();   
                }
            });
        }
    }
}
