package com.rentalgeek.android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.pojos.ApplicationItem;
import com.rentalgeek.android.pojos.RoommateDTO;
import com.rentalgeek.android.ui.activity.ActivityRental;
import com.rentalgeek.android.ui.activity.ActivitySignLease;
import com.rentalgeek.android.ui.activity.ActivityViewLease;
import com.rentalgeek.android.ui.fragment.FragmentBaseApplicationList;
import com.rentalgeek.android.ui.fragment.FragmentCosignerProperties;
import com.rentalgeek.android.ui.fragment.FragmentSignLease;
import com.rentalgeek.android.ui.fragment.FragmentViewLease;
import com.rentalgeek.android.ui.view.PropertyLeftTextView;
import com.rentalgeek.android.ui.view.PropertyPersonHorizontalLinearLayout;
import com.rentalgeek.android.ui.view.PropertyRightTextView;
import com.rentalgeek.android.utils.DownloadImageTask;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class ApplicationListAdapter extends RecyclerView.Adapter<ApplicationListAdapter.ApplicationListViewHolder> {

    private List<ApplicationItem> applicationItems;
    private String requestingFragment;
    private FragmentCosignerProperties fragment;

    public class ApplicationListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @InjectView(R.id.top_image_layout) ImageView topImageLayout;
        @InjectView(R.id.street_address) TextView streetAddressTextView;
        @InjectView(R.id.city_state_zip_address) TextView cityStateZipAddressTextView;
        @InjectView(R.id.num_beds_baths) TextView numBedsBathsTextView;
        @InjectView(R.id.cost_text_view) TextView costTextView;
        @InjectView(R.id.sign_approve_button) Button signApproveButton;
        @InjectView(R.id.property_name) TextView propertyNameTextView;
        @InjectView(R.id.property_email) TextView propertyEmailTextView;
        @InjectView(R.id.property_phone) TextView propertyPhoneTextView;
        @InjectView(R.id.lease_signed_lines) LinearLayout dynamicNamesLayout;
        @InjectView(R.id.bottom_contact_blue_box) LinearLayout bottomContactBlueBox;

        Context context;

        public ApplicationListViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            context = signApproveButton.getContext();
            propertyEmailTextView.setPaintFlags(propertyEmailTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            ApplicationItem selectedItem = applicationItems.get(getAdapterPosition());
            Intent intent = new Intent(context, ActivityRental.class);
            intent.putExtra("RENTAL_ID", selectedItem.getRentalOfferingId().toString());
            context.startActivity(intent);
        }

        public void setButtonVisibility(ApplicationItem item) {
            if (requestingFragment.equals(FragmentBaseApplicationList.PENDING_PROPERTIES)) {
                bottomContactBlueBox.setVisibility(View.GONE);
                signApproveButton.setVisibility(View.GONE);
            } else {
                signApproveButton.setText(item.getButtonText());
                propertyNameTextView.setText(item.getPropertyContactInfo().getName() + " ");
                propertyEmailTextView.setText(item.getPropertyContactInfo().getEmail() + " ");
                propertyPhoneTextView.setText(item.getPropertyContactInfo().getFormattedPhoneNumber() + " ");
            }
        }

        public void setButtonEnabledness(ApplicationItem item) {
            if (!Boolean.TRUE.equals(item.getAccepted())) {
                if (item.getUserId() != null && item.getUserId().toString().equals(SessionManager.Instance.getCurrentUser().id)) {
                    disableButton();
                    return;
                }
            }

            enableButton();
        }

        public void setButtonTapListener(final ApplicationItem item) {
            signApproveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (signApproveButton.getText().equals(ApplicationItem.SIGN_LEASE)) {
                        Intent intent = new Intent(context, ActivitySignLease.class);
                        intent.putExtra(FragmentSignLease.LEASE_ID, item.getLeaseId());
                        intent.putExtra(FragmentSignLease.REQUESTING_FRAGMENT, requestingFragment);
                        context.startActivity(intent);
                    } else if (signApproveButton.getText().equals(ApplicationItem.APPROVE)) {
                        fragment.applyToProperty(item.getRentalOfferingId());
                    } else if (signApproveButton.getText().equals(ApplicationItem.VIEW_LEASE)) {
                        Intent intent = new Intent(context, ActivityViewLease.class);
                        intent.putExtra(FragmentViewLease.LEASE_ID, item.getLeaseId());
                        context.startActivity(intent);
                    }
                }
            });
        }

        /**
         * View hierarchy:
         *
         * wholeLayout
         * --roommateAndCosignerLayout
         * ----roommateLine
         * ------roommateLeftText
         * ------roommateRightText
         * ----cosignerLine
         * ------cosignerLeftText
         * ------cosignerRightText
         */
        public void addDynamicTextViewsForEachRoommate(ApplicationItem item) {
            LinearLayout wholeLayout = dynamicNamesLayout;
            Context context = wholeLayout.getContext();

            for (RoommateDTO roommate : item.getRoommates()) {
                LinearLayout roommateAndCosignerLayout = new LinearLayout(context);
                roommateAndCosignerLayout.setOrientation(LinearLayout.VERTICAL);
                roommateAndCosignerLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    PropertyPersonHorizontalLinearLayout roommateLine = new PropertyPersonHorizontalLinearLayout(context);
                        PropertyLeftTextView roommateLeftText = new PropertyLeftTextView(context);
                        roommateLeftText.setText(item.getLeftTextForRoomate(roommate));
                        PropertyRightTextView roommateRightText = new PropertyRightTextView(context);
                        roommateRightText.setText(item.getRightTextForRoommate(roommate));
                        makeLeftFullWidthIfNoRight(roommateRightText.getText().toString(), roommateLeftText, roommateRightText);

                    PropertyPersonHorizontalLinearLayout cosignerLine = new PropertyPersonHorizontalLinearLayout(context);
                        PropertyLeftTextView cosignerLeftText = new PropertyLeftTextView(context);
                        cosignerLeftText.setText(item.getLeftTextForCosigner(roommate));
                        PropertyRightTextView cosignerRightText = new PropertyRightTextView(context);
                        cosignerRightText.setText(item.getRightTextForCosigner(roommate));
                        makeLeftFullWidthIfNoRight(cosignerRightText.getText().toString(), cosignerLeftText, cosignerRightText);

                roommateLine.addView(roommateLeftText);
                roommateLine.addView(roommateRightText);
                cosignerLine.addView(cosignerLeftText);
                cosignerLine.addView(cosignerRightText);
                roommateAndCosignerLayout.addView(roommateLine);
                roommateAndCosignerLayout.addView(cosignerLine);
                wholeLayout.addView(roommateAndCosignerLayout);
            }
        }

        private void makeLeftFullWidthIfNoRight(String rightString, TextView leftTV, TextView rightTV) {
            if (rightString.trim().isEmpty()) {
                leftTV.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                rightTV.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0f));
            }
        }

        private void enableButton() {
            signApproveButton.setEnabled(true);
            signApproveButton.setBackground(context.getResources().getDrawable(R.drawable.continue_bg));
            signApproveButton.setTextColor(context.getResources().getColor(R.color.white));
        }

        private void disableButton() {
            signApproveButton.setEnabled(false);
            signApproveButton.setBackgroundColor(context.getResources().getColor(R.color.decline_gray));
            signApproveButton.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    public ApplicationListAdapter(List<ApplicationItem> applicationItems, String requestingFragment) {
        this.applicationItems = applicationItems;
        this.requestingFragment = requestingFragment;
    }

    public ApplicationListAdapter(List<ApplicationItem> applicationItems, String requestingFragment, FragmentCosignerProperties fragment) {
        this.applicationItems = applicationItems;
        this.requestingFragment = requestingFragment;
        this.fragment = fragment;
    }

    public void setItems(List<ApplicationItem> applicationItems) {
        this.applicationItems = applicationItems;
    }

    @Override
    public ApplicationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_list_item, parent, false);
        return new ApplicationListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ApplicationListViewHolder holder, final int position) {
        final Context context = holder.signApproveButton.getContext();
        final ApplicationItem item = applicationItems.get(position);
        new DownloadImageTask(holder.topImageLayout).execute(item.getImageUrl());
        holder.streetAddressTextView.setText(item.getAddress().getStreet() + " ");
        holder.cityStateZipAddressTextView.setText(item.getAddress().getAddressline2() + " ");
        holder.numBedsBathsTextView.setText(item.getNumBedBathText() + " ");
        holder.costTextView.setText(item.getMonthlyCostText() + " ");

        holder.setButtonVisibility(item);
        holder.setButtonEnabledness(item);
        holder.addDynamicTextViewsForEachRoommate(item);
        holder.setButtonTapListener(item);
    }

    @Override
    public int getItemCount() {
        return applicationItems.size();
    }



    /**
     * Kind of a hack to prevent dynamic textviews from being re-added
     * when the recycler row is recycled. Settling for this because
     * this list of dynamic roommates inside each item of a recycler
     * view is complicated.
     */
    @Override
    public void onViewRecycled(ApplicationListViewHolder holder) {
        super.onViewRecycled(holder);
        holder.dynamicNamesLayout.removeAllViews();
    }

}
