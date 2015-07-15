package singles420.entrision.com.singles420;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

public class Utilities {

    public static class UserUtility {
        public void setAuthorizationToken (String token, Context context){
            SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefsFile), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("authToken", token);
            editor.commit();
        }

        public String getAuthorizationToken (Context context) {
            SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefsFile), Context.MODE_PRIVATE);
            String token = prefs.getString("authToken", "");

            return token;
        }

        public void setUserID(Integer id, Context context) {
            SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefsFile), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("userID", id);
            editor.commit();
        }

        public Integer getUserID(Context context) {
            SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefsFile), Context.MODE_PRIVATE);
            Integer userID = prefs.getInt("userID", -1);

            return userID;
        }

        public void setUserInfo(Context context, String jsonString) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject userObject = jsonObject.getJSONObject("user");
                Integer userID = (Integer) userObject.get("id");
                String token = (String) userObject.get("authentication_token");

                setUserID(userID, context);
                setAuthorizationToken(token, context);

            } catch (Exception e) {
                Log.w("**** 420 SINGLES ****", e);
            }
        }
    }
}
