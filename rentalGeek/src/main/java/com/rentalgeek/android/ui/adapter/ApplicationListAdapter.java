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
import com.rentalgeek.android.pojos.ApplicationItem;
import com.rentalgeek.android.pojos.RoommateDTO;
import com.rentalgeek.android.ui.activity.ActivitySignLease;
import com.rentalgeek.android.ui.fragment.FragmentSignLease;
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
    private boolean shouldHideButtonAndBottomContactInfo = false;

    public static class ApplicationListViewHolder extends RecyclerView.ViewHolder {
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

        public ApplicationListViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            propertyEmailTextView.setPaintFlags(propertyEmailTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    public ApplicationListAdapter(List<ApplicationItem> applicationItems, Boolean shouldHideButtonAndBottomContactInfo) {
        this.applicationItems = applicationItems;
        this.shouldHideButtonAndBottomContactInfo = shouldHideButtonAndBottomContactInfo;
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
        final ApplicationItem item = applicationItems.get(position);
        new DownloadImageTask(holder.topImageLayout).execute(item.getImageUrl());
        holder.streetAddressTextView.setText(item.getAddress().getStreet() + " ");
        holder.cityStateZipAddressTextView.setText(item.getAddress().getAddressline2() + " ");
        holder.numBedsBathsTextView.setText(item.getNumBedBathText() + " ");
        holder.costTextView.setText(item.getMonthlyCostText() + " ");

        if (shouldHideButtonAndBottomContactInfo) {
            holder.bottomContactBlueBox.setVisibility(View.GONE);
            holder.signApproveButton.setVisibility(View.GONE);
        } else {
            holder.signApproveButton.setText(item.getButtonText());
            holder.propertyNameTextView.setText(item.getPropertyContactInfo().getName() + " ");
            holder.propertyEmailTextView.setText(item.getPropertyContactInfo().getEmail() + " ");
            holder.propertyPhoneTextView.setText(item.getPropertyContactInfo().getFormattedPhoneNumber() + " ");
        }

        addDynamicTextViewsForEachRoommate(holder, item);

        holder.signApproveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.signApproveButton.getContext();
                Intent intent = new Intent(context, ActivitySignLease.class);
                intent.putExtra(FragmentSignLease.STREET, item.getAddress().getStreet());
                intent.putExtra(FragmentSignLease.CITY_STATE_ZIP, item.getAddress().getAddressline2());
                intent.putExtra(FragmentSignLease.PDF_URL, item.getUnsignedLeaseDocumentUrl());
                context.startActivity(intent);
            }
        });
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
    private void addDynamicTextViewsForEachRoommate(ApplicationListViewHolder holder, ApplicationItem item) {
        LinearLayout wholeLayout = holder.dynamicNamesLayout;
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

}
