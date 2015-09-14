package com.rentalgeek.android.mvp.common;

import android.util.Log;
import org.json.JSONObject;
import com.rentalgeek.android.pojos.Rental;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.utils.GeekGson;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.storage.RentalCache;
import com.rentalgeek.android.mvp.common.StarView;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.ui.preference.AppPreferences;

public abstract class StarPresenter {
    
    private static final String TAG = StarPresenter.class.getSimpleName();
   
    public void select(final String rental_id,final StarView starView) {
        if( rental_id == null || rental_id.isEmpty() || starView == null ) 
            return;
        else {
            boolean starred = RentalCache.getInstance().get(rental_id).isStarred();
 
            String user_id = SessionManager.Instance.getCurrentUser().id;

            if( !starred ) {
            
    
                selectStar(rental_id,user_id,starView);
            }

            else {
                unselectStar(rental_id,starView);
            }
        }
    }

    private void selectStar(final String rental_id, final String user_id, final StarView starView) {
        
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
                            
                            starView.selectStar();
                        }
                    }
                }

                catch(Exception e) {
                    Log.e(TAG,e.getMessage());
                }
            }

        });
    }
    
    private void unselectStar(final String rental_id,final StarView starView) {
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
                    
                    starView.unselectStar();   
                }
            });
        }
    }
}
