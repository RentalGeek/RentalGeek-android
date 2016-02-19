package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.CosignerInviteDTO;
import com.rentalgeek.android.pojos.CosignerInviteSingleRootDTO;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.CosignerDestinationLogic;
import com.rentalgeek.android.utils.OkAlert;
import com.rentalgeek.android.utils.ResponseParser;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentCosignerInvite extends GeekBaseFragment {

    @InjectView(R.id.invite_cosign_textview) TextView inviteTextView;
    @InjectView(R.id.accept_button) Button acceptButton;
    @InjectView(R.id.decline_button) Button declineButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cosigner_invite, container, false);
        ButterKnife.inject(this, view);

        String inviteText = getResources().getString(R.string.invite_cosign_text);
        inviteText = String.format(inviteText, CosignerDestinationLogic.INSTANCE.getNameOfInviter());
        this.inviteTextView.setText(inviteText);

        return view;
    }

    @OnClick(R.id.accept_button)
    public void acceptInvite() {
        int inviteId = CosignerDestinationLogic.INSTANCE.getInviteId();
        GlobalFunctions.postApiCall(getActivity(), ApiManager.getAcceptCosignerInviteUrl(inviteId), null, AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog(R.string.dialog_msg_loading);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideProgressDialog();
            }

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                getActivity().finish();
                updateDestinationLogic(content);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
                ResponseParser.ErrorMsg errorMsg = new ResponseParser().humanizedErrorMsg(failureResponse);
                OkAlert.show(getActivity(), errorMsg.title, errorMsg.msg);
            }
        });
    }

    @OnClick(R.id.decline_button)
    public void declineInvite() {
        int inviteId = CosignerDestinationLogic.INSTANCE.getInviteId();
        GlobalFunctions.postApiCall(getActivity(), ApiManager.getDenyCosignerInviteUrl(inviteId), null, AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog(R.string.dialog_msg_loading);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideProgressDialog();
            }

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                getActivity().finish();
                updateDestinationLogic(content);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
            }
        });
    }

    private void updateDestinationLogic(String response) {
        CosignerInviteSingleRootDTO cosignerInviteSingleRootDTO = new Gson().fromJson(response, CosignerInviteSingleRootDTO.class);
        CosignerInviteDTO updatedInvite = cosignerInviteSingleRootDTO.cosigner_invite;
        CosignerDestinationLogic.INSTANCE.updateInvite(updatedInvite);
        CosignerDestinationLogic.INSTANCE.navigateToNextCosignActivity(getActivity());
    }

}
