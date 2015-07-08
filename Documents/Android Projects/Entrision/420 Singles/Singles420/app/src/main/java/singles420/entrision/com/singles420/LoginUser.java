package singles420.entrision.com.singles420;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginUser extends AsyncTask <String, Void, String> {

    private Context context;

    public LoginUser(Context cont) {
        context = cont;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(context.getResources().getString(R.string.base_address)+"sessions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("accept", "application/json");
            //conn.setRequestProperty("apikey", apiKey);

            JSONObject jsonParams = new JSONObject();
            JSONObject emailParams = new JSONObject();
            emailParams.put("email", params[0]);
            emailParams.put("password", params[1]);
            jsonParams.put("user", emailParams);

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(jsonParams.toString());
            out.flush();

            StringBuilder sb = new StringBuilder();
            int result = conn.getResponseCode();
            if (result == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;

                while((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                br.close();
                System.out.println(sb.toString());
            }

        }
        catch(Exception e) {
            System.out.print(e);
        }

        return "";

    }

    protected void onPostExecute(String result) {

    }
}
