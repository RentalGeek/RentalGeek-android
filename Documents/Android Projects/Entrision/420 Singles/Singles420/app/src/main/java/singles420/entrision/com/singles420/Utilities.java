package singles420.entrision.com.singles420;


import android.content.Context;
import android.content.SharedPreferences;

public class Utilities {

    public static class UserUtility {
        public void setAuthorizationToken (String token, Context context){
            SharedPreferences prefs = context.getSharedPreferences("com.420singles.app", Context.MODE_PRIVATE);
            prefs.edit().putString("authToken", token);
        }

        public String getAuthorizationToken (Context context) {
            SharedPreferences prefs = context.getSharedPreferences("com.420singles.app", Context.MODE_PRIVATE);
            String token = prefs.getString("authToken", "");

            return token;
        }
    }
}
