package com.rentalgeek.android.ui.activity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.CosignerInviteDTO;
import com.rentalgeek.android.pojos.CosignerInviteDetailsDTO;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.util.ArrayList;

/**
 * Created by rajohns on 9/16/15.
 *
 */
public class ActivityCosignDecider extends GeekBaseActivity {

    public ActivityCosignDecider() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment);
        setupNavigation();

        fetchCosignerInvites();

    }

    private void fetchCosignerInvites() {
        GlobalFunctions.getApiCall(this, ApiManager.getCosignerInvitesUrl(), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                parseAndDecideWhichCosignScreen(content);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
            }
        });
    }

    private void parseAndDecideWhichCosignScreen(String response) {
        CosignerInviteDetailsDTO cosignerInviteDetailsDTO = new Gson().fromJson(response, CosignerInviteDetailsDTO.class);

        if (hasOutstandingInvites(cosignerInviteDetailsDTO.cosigner_invites)) {
            // take to cosign1 or place to accept
        } else if (hasAcceptedAtLeastOneCosignerInvite(cosignerInviteDetailsDTO.cosigner_invites)) {
            if (hasCompletedCosignerProfile()) {
                Navigation.navigateActivity(this, ActivityCosignerList.class);
            } else {
                // take to cosignapp
            }
        }
    }

    private boolean hasOutstandingInvites(ArrayList<CosignerInviteDTO> cosignerInvites) {
        for (CosignerInviteDTO cosignerInviteDTO : cosignerInvites) {
            if (cosignerInviteDTO.accepted == null) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCompletedCosignerProfile() {
        return SessionManager.Instance.getCurrentUser().cosigner_profile_id != null;
    }

    private boolean hasAcceptedAtLeastOneCosignerInvite(ArrayList<CosignerInviteDTO> cosignerInvites) {
        for (CosignerInviteDTO cosignerInviteDTO : cosignerInvites) {
            if (Boolean.TRUE.equals(cosignerInviteDTO.accepted)) {
                return true;
            }
        }

        return false;
    }

}
