package com.rentalgeek.android.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.pojos.CosignItem;
import com.rentalgeek.android.pojos.Roommate;
import com.rentalgeek.android.utils.DownloadImageTask;

import java.util.List;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class CosignerListAdapter extends RecyclerView.Adapter<CosignerListAdapter.CosignerListViewHolder> {

    private List<CosignItem> cosignItems;

    public static class CosignerListViewHolder extends RecyclerView.ViewHolder {
        public ImageView topImageLayout;
        public TextView streetAddressTextView;
        public TextView cityStateZipAddressTextView;
        public TextView numBedsBathsTextView;
        public TextView costTextView;
        public Button signApproveButton;
        public TextView propertyNameTextView;
        public TextView propertyEmailTextView;
        public TextView propertyPhoneTextView;
        public LinearLayout dynamicNamesLayout;

        public CosignerListViewHolder(View view) {
            super(view);
            topImageLayout = (ImageView)view.findViewById(R.id.top_image_layout);
            streetAddressTextView = (TextView)view.findViewById(R.id.street_address);
            cityStateZipAddressTextView = (TextView)view.findViewById(R.id.city_state_zip_address);
            numBedsBathsTextView = (TextView)view.findViewById(R.id.num_beds_baths);
            costTextView = (TextView)view.findViewById(R.id.cost_text_view);
            signApproveButton = (Button)view.findViewById(R.id.sign_approve_button);
            propertyNameTextView = (TextView)view.findViewById(R.id.property_name);
            propertyEmailTextView = (TextView)view.findViewById(R.id.property_email);
            propertyEmailTextView.setPaintFlags(propertyEmailTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            propertyPhoneTextView = (TextView)view.findViewById(R.id.property_phone);
            dynamicNamesLayout = (LinearLayout)view.findViewById(R.id.lease_signed_lines);
        }
    }

    public CosignerListAdapter(List<CosignItem> cosignItems) {
        this.cosignItems = cosignItems;
    }

    public void setItems(List<CosignItem> cosignItems) {
        this.cosignItems = cosignItems;
    }

    @Override
    public CosignerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cosigner_list_item, parent, false);
        return new CosignerListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CosignerListViewHolder holder, int position) {
        CosignItem item = cosignItems.get(position);
        new DownloadImageTask(holder.topImageLayout).execute(item.getImageUrl());
        holder.streetAddressTextView.setText(item.getAddress().getStreet() + " ");
        holder.cityStateZipAddressTextView.setText(item.getAddress().getAddressline2() + " ");
        holder.numBedsBathsTextView.setText(item.getNumBedBathText() + " ");
        holder.costTextView.setText(item.getMonthlyCostText() + " ");
        holder.signApproveButton.setText(item.getButtonText());
        holder.propertyNameTextView.setText(item.getPropertyContactInfo().getName() + " ");
        holder.propertyEmailTextView.setText(item.getPropertyContactInfo().getEmail() + " ");
        holder.propertyPhoneTextView.setText(item.getPropertyContactInfo().getFormattedPhoneNumber() + " ");

        LinearLayout wholeLayout = holder.dynamicNamesLayout;
        Context context = wholeLayout.getContext();

        for (Roommate roommate : item.getRoommates()) {
            LinearLayout roommateAndCosignerLayout = new LinearLayout(context);
            roommateAndCosignerLayout.setOrientation(LinearLayout.VERTICAL);
            roommateAndCosignerLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                LinearLayout roommateLine = new LinearLayout(context);
                roommateLine.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams roommateLineLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                roommateLineLayoutParams.setMargins(0, 0, 0, 10);
                roommateLine.setLayoutParams(roommateLineLayoutParams);

                    TextView roommateNameText = new TextView(context);
                    roommateNameText.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.75f));
                    roommateNameText.setTextColor(Color.BLACK);
                    roommateNameText.setTextSize(11);
                    roommateNameText.setText(item.getLeftTextForRoomate(roommate));

                    TextView roommateDateText = new TextView(context);
                    roommateDateText.setText(item.getDateTextForRoomate(roommate) + " ");
                    roommateDateText.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));
                    roommateDateText.setGravity(Gravity.RIGHT);
                    roommateDateText.setTextColor(Color.BLACK);
                    roommateDateText.setTextSize(11);

                LinearLayout cosignerLine = new LinearLayout(context);
                cosignerLine.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams cosignerLineLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cosignerLineLayoutParams.setMargins(0, 0, 0, 10);
                cosignerLine.setLayoutParams(cosignerLineLayoutParams);

                    TextView cosignerNameText = new TextView(context);
                    cosignerNameText.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.75f));
                    cosignerNameText.setTextColor(Color.BLACK);
                    cosignerNameText.setTextSize(11);
                    cosignerNameText.setText(item.getLeftTextForCosigner(roommate));

                    TextView cosignerDateText = new TextView(context);
                    cosignerDateText.setText(item.getDateTextForCosigner(roommate) + " ");
                    cosignerDateText.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));
                    cosignerDateText.setGravity(Gravity.RIGHT);
                    cosignerDateText.setTextColor(Color.BLACK);
                    cosignerDateText.setTextSize(11);

            roommateLine.addView(roommateNameText);
            roommateLine.addView(roommateDateText);
            cosignerLine.addView(cosignerNameText);
            cosignerLine.addView(cosignerDateText);
            roommateAndCosignerLayout.addView(roommateLine);
            roommateAndCosignerLayout.addView(cosignerLine);
            wholeLayout.addView(roommateAndCosignerLayout);
        }
    }

    @Override
    public void onViewRecycled(CosignerListViewHolder holder) {
        super.onViewRecycled(holder);
        holder.dynamicNamesLayout.removeAllViews();
    }

    @Override
    public int getItemCount() {
        return cosignItems.size();
    }

}
