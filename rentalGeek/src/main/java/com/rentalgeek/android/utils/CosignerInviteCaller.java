package com.rentalgeek.android.utils;

import android.app.Activity;

import com.google.gson.Gson;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.CosignerInviteDTO;
import com.rentalgeek.android.pojos.CosignerInvitesArrayRootDTO;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by rajohns on 9/17/15.
 */
public class CosignerInviteCaller {

    private Activity activity;
    private boolean directlyStartActivity;

    public CosignerInviteCaller(Activity activity, boolean directlyStartActivity) {
        this.activity = activity;
        this.directlyStartActivity = directlyStartActivity;
    }

    public void fetchCosignerInvites() {
        GlobalFunctions.getApiCall(activity, ApiManager.cosignerInvitesUrl(), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
        CosignerInvitesArrayRootDTO cosignerInvitesArrayRootDTO = new Gson().fromJson(response, CosignerInvitesArrayRootDTO.class);
        ArrayList<CosignerInviteDTO> cosignerInvites = removeDuplicates(cosignerInvitesArrayRootDTO.cosigner_invites);
        CosignerDestinationLogic.INSTANCE.setCosignerInvites(cosignerInvites);

        if (directlyStartActivity) {
            CosignerDestinationLogic.INSTANCE.navigateToNextCosignActivity(activity);
        }
    }

    /**
     * Remove items in the array if they have already appeared
     * with same inviter_id and accepted status.
     *
     * @return the array with duplicate items removed
     */
    private ArrayList<CosignerInviteDTO> removeDuplicates(ArrayList<CosignerInviteDTO> originalItems) {
        Map<String, CosignerInviteDTO> uniqueInviteMap = new LinkedHashMap<>();

        for (CosignerInviteDTO cosignerInviteDTO : originalItems) {
            InviteKey key = new InviteKey(cosignerInviteDTO.inviter_id, cosignerInviteDTO.accepted);
            uniqueInviteMap.put(key.toString(), cosignerInviteDTO);
        }

        originalItems.clear();
        originalItems.addAll(uniqueInviteMap.values());
        return originalItems;
    }

    /**
     * Class for handling keys in map so only invites without the
     * same inviter_id and accepted status will be added to map.
     */
    class InviteKey {
        Integer inviter_id;
        Boolean accepted;

        InviteKey(Integer inviter_id, Boolean accepted) {
            this.inviter_id = inviter_id;
            this.accepted = accepted;
        }

        @Override
        public String toString() {
            if (this.accepted != null) {
                return this.inviter_id.toString() + this.accepted.toString();
            }

            return this.inviter_id.toString();
        }
    }

}
