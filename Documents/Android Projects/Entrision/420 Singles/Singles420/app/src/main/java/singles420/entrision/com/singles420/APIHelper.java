package singles420.entrision.com.singles420;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class APIHelper {
    private static APIHelper mInstance = null;
    private Context context;

    private APIHelper() {

    }

    public static APIHelper getInstance() {
        if (mInstance == null) {
            mInstance = new APIHelper();
        }
        return mInstance;
    }

    public void setContext(Context c) {
        context = c;
    }

    public void loginUser(String emailAddress, String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            JSONObject jsonParams = new JSONObject();
            JSONObject emailParams = new JSONObject();
            emailParams.put("email", emailAddress);
            emailParams.put("password", password);
            jsonParams.put("user", emailParams);


            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            client.post(context, context.getResources().getString(R.string.base_address) + "sessions", entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    if (statusCode == HttpURLConnection.HTTP_OK) {
                        try {
                            String jsonString = new String(response, "UTF-8");
                            Utilities.UserUtility userUtil = new Utilities.UserUtility();
                            userUtil.setUserInfo(context, jsonString);

                            Intent intent = new Intent(context, SinglesActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });

        } catch (Exception e) {

        }
    }

    public void signUpUser(String firstName, String lastName, String gender, String birthDate, String emailAddress, String password, String facebookUserID) {
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            JSONObject jsonParams = new JSONObject();
            JSONObject userParams = new JSONObject();
            userParams.put("email", emailAddress);
            userParams.put("password", password);
            userParams.put("first_name", firstName);
            userParams.put("last_name", lastName);

            if (!birthDate.matches(context.getResources().getString(R.string.dob_hint)) && !birthDate.matches("")) {
                SimpleDateFormat formatDOB = new SimpleDateFormat("mm/dd/yyyy");
                Date dob = formatDOB.parse(birthDate);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                String formattedDate = format.format(dob);
                userParams.put("birthday", formattedDate.toString());
            } else {
                userParams.put("birthday", "");
            }

            userParams.put("sex", gender);

            if (!facebookUserID.matches("")) {
                userParams.put("facebook_user_id", facebookUserID);
            }

            jsonParams.put("user", userParams);


            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            client.post(context, context.getResources().getString(R.string.base_address) + "users", entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    try {
                        String jsonString = new String(response, "UTF-8");
                        Utilities.UserUtility userUtil = new Utilities.UserUtility();
                        userUtil.setUserInfo(context, jsonString);

                        Intent intent = new Intent(context, SinglesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.w("***** 420 *****", e.toString());
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });

        } catch (Exception e) {
            Log.w("***** 420 *****", e);
        }
    }

    public void facebookLogin(final LoginResult facebookInfo) {
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            JSONObject jsonParams = new JSONObject();
            JSONObject fbParams = new JSONObject();
            fbParams.put("facebook_user_id", facebookInfo.getAccessToken().getUserId());
            fbParams.put("facebook_email", facebookInfo.getAccessToken());
            jsonParams.put("user", fbParams);


            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            client.post(context, context.getResources().getString(R.string.base_address) + "facebook_authentication", entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    if (statusCode == HttpURLConnection.HTTP_OK) {
                        try {
                            String jsonString = new String(response, "UTF-8");
                            Utilities.UserUtility userUtil = new Utilities.UserUtility();
                            userUtil.setUserInfo(context, jsonString);

                            Intent intent = new Intent(context, SinglesActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // the user is not known to the database, so we sign them up using facebook data
                    new GraphRequest(facebookInfo.getAccessToken(), "/me", null, HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
                                    Log.d("*** 420 **** ", response.toString());
                                    try {
                                        JSONObject facebookObject = response.getJSONObject();
                                        APIHelper.getInstance().signUpUser(facebookObject.getString("first_name"), facebookObject.getString("last_name"), facebookObject.getString("gender"), "", facebookObject.getString("email"), facebookInfo.getAccessToken().getUserId(), facebookInfo.getAccessToken().getUserId());
                                    } catch (Exception e) {

                                    }
                                }
                            }
                    ).executeAsync();
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });

        } catch (Exception e) {
            Log.w("**** 420 SINGLES ****", e);
        }
    }

    public String getSuggestionsForUser() {
        String suggestions = "";
        Utilities.UserUtility userUtility = new Utilities.UserUtility();

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Token token="+userUtility.getAuthorizationToken(context));
        client.addHeader("X-User-Email", userUtility.getUserEmail(context));

        String url = context.getResources().getString(R.string.base_address) + "suggestions";
        try {

            client.get(context.getResources().getString(R.string.base_address) + "suggestions", new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    try {
                        String jsonString = new String(response, "UTF-8");
                        Log.d("*** 420 ******", jsonString);
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.w("***** 420 *****", e.toString());
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });


        } catch (Exception e) {

        }

        return suggestions;
    }

    public void updateUserLocation(String latitude, String longitude) {
        Utilities.UserUtility userUtility = new Utilities.UserUtility();

        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("Authorization", "Token token="+userUtility.getAuthorizationToken(context));
        client.addHeader("X-User-Email", userUtility.getUserEmail(context));

        try {
            JSONObject jsonParams = new JSONObject();
            JSONObject locParams = new JSONObject();
            locParams.put("lat", latitude);
            locParams.put("lng", locParams);
            jsonParams.put("user", locParams);

            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            client.patch(context, context.getResources().getString(R.string.base_address) + "users/" + userUtility.getUserID(context), entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    try {
                        String jsonString = new String(response, "UTF-8");
                        Log.d("*** 420 location update", jsonString);
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.w("***** 420 location error", e.toString());
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });


        } catch (Exception e) {

        }

    }
}
