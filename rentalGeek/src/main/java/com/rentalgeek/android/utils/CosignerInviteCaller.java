package com.rentalgeek.android.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.CosignerInviteDTO;
import com.rentalgeek.android.pojos.CosignerInviteDetailsDTO;
import com.rentalgeek.android.ui.activity.ActivityCosignerInvite;
import com.rentalgeek.android.ui.activity.ActivityCosignerList;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by rajohns on 9/17/15.
 *
 */
public class CosignerInviteCaller {

    private Context context;
    private boolean directlyStartActivity;

    public CosignerInviteCaller(Context context, boolean directlyStartActivity) {
        this.context = context;
        this.directlyStartActivity = directlyStartActivity;
    }

    public void fetchCosignerInvites() {
        GlobalFunctions.getApiCall(context, ApiManager.getCosignerInvitesUrl(), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
        ArrayList<CosignerInviteDTO> cosignerInvites = removeDuplicates(cosignerInviteDetailsDTO.cosigner_invites);

        if (hasOutstandingInvites(cosignerInvites)) {
            if (directlyStartActivity) {
                context.startActivity(new Intent(context, ActivityCosignerInvite.class));
            } else {
                CosignerDestinationPage.getInstance().setDestination(GlobalStrings.COSIGNER_INVITE_PAGE);
            }
        } else if (hasAcceptedAtLeastOneCosignerInvite(cosignerInvites)) {
            if (hasCompletedCosignerProfile()) {
                if (directlyStartActivity) {
                    context.startActivity(new Intent(context, ActivityCosignerList.class));
                } else {
                    CosignerDestinationPage.getInstance().setDestination(GlobalStrings.COSIGNER_PROPERTY_LIST);
                }
            } else {
                if (directlyStartActivity) {
                    Log.d("tag", "take to cosignapp");
                } else {
                    Log.d("tag", "silently save cosignapp page as destination");
                }
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
