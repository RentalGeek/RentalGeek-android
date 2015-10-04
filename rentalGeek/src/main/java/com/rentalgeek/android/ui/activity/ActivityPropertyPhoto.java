package com.rentalgeek.android.ui.activity;

import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.fragment.FragmentPropertyPhoto;

/**
 * Created by rajohns on 10/3/15.
 *
 */
public class ActivityPropertyPhoto extends GeekBaseActivity {

    public static final String PHOTO_URLS = "photoUrls";
    public static final String ORIGINAL_POSITION = "originalPosition";

    public ActivityPropertyPhoto() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment);

        setupNavigation();

        if (savedInstanceState == null) {
            FragmentPropertyPhoto fragment = new FragmentPropertyPhoto();
            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

}
