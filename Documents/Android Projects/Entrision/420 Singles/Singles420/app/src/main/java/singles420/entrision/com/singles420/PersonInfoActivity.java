package singles420.entrision.com.singles420;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by travis on 7/24/15.
 */
public class PersonInfoActivity extends FragmentActivity {

    Person person;
    ViewPager pager;
    PagerAdapter pagerAdapter;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", -1);

        Utilities.UserUtility userUtility = new Utilities.UserUtility();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Token token="+userUtility.getAuthorizationToken(this));
        client.addHeader("X-User-Email", userUtility.getUserEmail(this));
        client.get(this.getResources().getString(R.string.base_address) + "users/" + userID, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    String jsonString = new String(response, "UTF-8");

                    JSONObject jsonPerson = new JSONObject(jsonString);
                    person = new Person();
                    JSONObject jsonUser =  jsonPerson.getJSONObject("user");
                    person.userID = jsonUser.getInt("id");
                    person.firstName = jsonUser.getString("first_name");
                    person.lastName = jsonUser.getString("last_name");
                    person.sex = jsonUser.getString("sex");
                    person.biography = jsonUser.getString("biography");

                    JSONArray jsonImages = jsonUser.getJSONArray("images");
                    person.images = new String[jsonImages.length()];

                    for(int x=0; x <  jsonImages.length(); x++) {
                        JSONObject imageObject = jsonImages.getJSONObject(x);
                        person.images[x] = imageObject.getString("image_url");
                    }

                    if (person.images.length > 0) {
                        setupPager();
                    } else {
                        pager = (ViewPager) findViewById(R.id.pager);
                        pager.setVisibility(View.GONE);

                        ImageView placeholder = (ImageView) findViewById(R.id.placeholder_image);
                        placeholder.setVisibility(View.VISIBLE);
                    }

                    setupUI();
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


    }

    public void setupPager() {
        // Instantiate a ViewPager and a PagerAdapter.
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    public void setupUI() {
        TextView name = (TextView) findViewById(R.id.person_name);
        name.setText(person.firstName);

        TextView location = (TextView) findViewById(R.id.person_location);
        if (person.location == null ||person.location.isEmpty()) {
            location.setText("Location Unknown");
        } else {
            location.setText(person.location);
        }

        TextView bio = (TextView) findViewById(R.id.person_bio);
        bio.setText(person.biography);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new PersonImageFragment();
        }

        @Override
        public int getCount() {
            return person.images.length;
        }
    }
}
