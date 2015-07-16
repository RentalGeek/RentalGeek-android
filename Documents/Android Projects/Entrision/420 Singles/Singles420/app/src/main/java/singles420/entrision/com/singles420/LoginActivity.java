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


}
