package singles420.entrision.com.singles420;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Utilities.UserUtility userUtil = new Utilities.UserUtility();

        String token = userUtil.getAuthorizationToken(this);

        if (token != "") {
            Intent myIntent = new Intent(this, SinglesActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            this.startActivity(myIntent);
        }
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
            String[] params = { emailAddress.getText().toString(), password.getText().toString() };
            //LoginUser user = (LoginUser) new LoginUser(this).execute(params);

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
                                setUserInfo(jsonString);

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

    public void setUserInfo(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject userObject = jsonObject.getJSONObject("user");
            Integer userID = (Integer) userObject.get("id");
            String token = (String) userObject.get("authentication_token");

            Utilities.UserUtility userUtil = new Utilities.UserUtility();
            userUtil.setUserID(userID, this);
            userUtil.setAuthorizationToken(token, this);

            Intent myIntent = new Intent(this, SinglesActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            this.startActivity(myIntent);
        } catch (Exception e) {
            System.out.println("ERROR - " + e);
        }
    }

    public void joinButtonPressed(View view) {
        Intent myIntent = new Intent(this, SignupActivity.class);
        this.startActivity(myIntent);
    }
}
