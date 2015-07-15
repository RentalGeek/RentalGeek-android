package singles420.entrision.com.singles420;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends Activity {

    CallbackManager callbackManager;
    AccessToken facebookAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        Utilities.UserUtility userUtil = new Utilities.UserUtility();
        String token = userUtil.getAuthorizationToken(this);

        if (token != "") {
            Intent myIntent = new Intent(this, SinglesActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            this.startActivity(myIntent);
        } else {
            callbackManager = CallbackManager.Factory.create();

            LoginButton loginButton = (LoginButton) this.findViewById(R.id.facebook_login_button);
            loginButton.setReadPermissions("email");

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            //loginFaceBookUser(loginResult.getAccessToken().getUserId());
                            APIHelper.getInstance().facebookLogin(loginResult);
                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void loginButtonPressed(View view) {
        Boolean inputError = false;
        EditText emailAddress = (EditText) findViewById(R.id.email_address);
        EditText password = (EditText) findViewById(R.id.password);

        if (emailAddress.getText().toString().matches("")) {
            inputError = true;
        }

        if (password.getText().toString().matches("")) {
            inputError = true;
        }

        if (!inputError) {
            try {
                APIHelper.getInstance().loginUser(emailAddress.getText().toString(), password.getText().toString());
            } catch(Exception e) {
                Log.w("****420 *****", e);
            }
            /*
            AsyncHttpClient client = new AsyncHttpClient();
            try {
                JSONObject jsonParams = new JSONObject();
                JSONObject emailParams = new JSONObject();
                emailParams.put("email", emailAddress.getText().toString());
                emailParams.put("password", password.getText().toString());
                jsonParams.put("user", emailParams);


                ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
                client.post(this, this.getResources().getString(R.string.base_address) + "sessions", entity, "application/json",new AsyncHttpResponseHandler() {

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
                                userUtil.setUserInfo(getApplicationContext(), jsonString);

                            } catch(Exception e) {

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
*/
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Input Error");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Please enter your email address and password")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void joinButtonPressed(View view) {
        Intent myIntent = new Intent(this, SignupActivity.class);
        this.startActivity(myIntent);
    }

    public void loginFaceBookUser(String token) {
        //APIHelper.getInstance().facebookLogin();
        /*
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            JSONObject jsonParams = new JSONObject();
            JSONObject fbParams = new JSONObject();
            fbParams.put("facebook_auth_token", token);
            jsonParams.put("user", fbParams);


            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            client.post(this, this.getResources().getString(R.string.base_address) + "facebook_authentication", entity, "application/json",new AsyncHttpResponseHandler() {

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
                            userUtil.setUserInfo(getApplicationContext(), jsonString);

                        } catch(Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // the user is not known to the database, so we sign them up using facebook data
                    new GraphRequest( AccessToken.getCurrentAccessToken(), "/me", null, HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {

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
        */
    }
}
