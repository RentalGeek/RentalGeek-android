package com.rentalgeek.android.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.CosignerInviteDTO;
import com.rentalgeek.android.pojos.CosignerInvitesArrayRootDTO;
import com.rentalgeek.android.ui.activity.ActivityYourCosigner;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.ui.view.CosignerInviteRow;
import com.rentalgeek.android.utils.OkAlert;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rajohns on 9/20/15.
 *
 */
public class FragmentYourCosigner extends GeekBaseFragment {

    private ArrayList<CosignerInviteDTO> sentInvites = new ArrayList<>();
    private String name;
    private String email;

    @InjectView(R.id.has_invited_layout) LinearLayout hasInvitedLayout;
    @InjectView(R.id.none_accepted_layout) LinearLayout noneAcceptedLayout;
    @InjectView(R.id.name_edittext) EditText nameEditText;
    @InjectView(R.id.email_edittext) EditText emailEdiText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_cosigner, container, false);
        ButterKnife.inject(this, view);

        GlobalFunctions.getApiCall(getActivity(), ApiManager.sentCosignerInvites(), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
                parseResponse(content);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
            }
        });

        return view;
    }

    @OnClick(R.id.add_button)
    public void addButtonTapped() {
        if (validInput()) {
            RequestParams params = new RequestParams();
            params.put("cosigner_invite[email]", email);
            params.put("cosigner_invite[name]", name);

            GlobalFunctions.postApiCall(getActivity(), ApiManager.cosignerInvitesUrl(), params, AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
                    getActivity().startActivity(new Intent(getActivity(), ActivityYourCosigner.class));
                }

                @Override
                public void onFailure(Throwable ex, String failureResponse) {
                    super.onFailure(ex, failureResponse);
                    DialogManager.showCrouton(activity, failureResponse);
                }
            });
        }
    }

    private boolean validInput() {
        name = nameEditText.getText().toString().trim();
        email = emailEdiText.getText().toString().trim();

        if (name.equals("")) {
            OkAlert.show(getActivity(), "Name", "Please enter your first and last name.");
            return false;
        }

        if (email.equals("")) {
            OkAlert.show(getActivity(), "Email", "Please enter your email address.");
            return false;
        }

        return true;
    }

    private void parseResponse(String response) {
        CosignerInvitesArrayRootDTO cosignerInvitesArrayRootDTO = new Gson().fromJson(response, CosignerInvitesArrayRootDTO.class);
        sentInvites = cosignerInvitesArrayRootDTO.cosigner_invites;

        if (noAcceptedOffers()) {
            noneAcceptedLayout.setVisibility(View.VISIBLE);
        }

        if (sentInvites.size() == 0) {
            hasInvitedLayout.setVisibility(View.GONE);
        }

        for (int i = 0; i < sentInvites.size(); i++) {
            final CosignerInviteDTO invite = sentInvites.get(i);

            CosignerInviteRow inviteRow = new CosignerInviteRow(getActivity());
            inviteRow.inflateView(this, invite);

            hasInvitedLayout.addView(inviteRow);
        }
    }

    public void deleteInviteAlert(final CosignerInviteDTO invite) {
        new AlertDialog.Builder(getActivity())
            .setTitle("Remove Cosigner")
            .setMessage("Are you sure you want to remove " + invite.invited_name + " as a cosigner?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    deleteInvite(invite);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
            .show();
    }

    private void deleteInvite(CosignerInviteDTO invite) {
        Log.d("tag", "delete /api/v1/cosigner_invites/" + invite.id);
        GlobalFunctions.deleteApiCall(getActivity(), ApiManager.deleteCosignerInvite(invite.id), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
                getActivity().startActivity(new Intent(getActivity(), ActivityYourCosigner.class));
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
                DialogManager.showCrouton(activity, failureResponse);
            }
        });
    }

    private boolean noAcceptedOffers() {
        for (CosignerInviteDTO invite : sentInvites) {
            if (Boolean.TRUE.equals(invite.accepted)) {
                return false;
            }
        }

        return true;
    }

}
