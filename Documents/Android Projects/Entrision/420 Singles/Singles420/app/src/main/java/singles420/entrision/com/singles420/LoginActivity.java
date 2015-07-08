package singles420.entrision.com.singles420;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
            LoginUser user = (LoginUser) new LoginUser(this).execute(params);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Input Error");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Please enter your email address and password")
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
    /*
SharedPreferences prefs = this.getSharedPreferences(
      "com.example.app", Context.MODE_PRIVATE);

     */
}
