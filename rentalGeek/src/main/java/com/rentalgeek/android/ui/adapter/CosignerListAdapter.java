package com.rentalgeek.android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.pojos.CosignItem;
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
        public TextView leaseSignersTextView;
        public TextView propertyNameTextView;
        public TextView propertyEmailTextView;
        public TextView propertyPhoneTextView;
        public CosignerListViewHolder(View view) {
            super(view);
            topImageLayout = (ImageView)view.findViewById(R.id.top_image_layout);
            streetAddressTextView = (TextView)view.findViewById(R.id.street_address);
            cityStateZipAddressTextView = (TextView)view.findViewById(R.id.city_state_zip_address);
            numBedsBathsTextView = (TextView)view.findViewById(R.id.num_beds_baths);
            costTextView = (TextView)view.findViewById(R.id.cost_text_view);
            signApproveButton = (Button)view.findViewById(R.id.sign_approve_button);
            leaseSignersTextView = (TextView)view.findViewById(R.id.lease_signers_textview);
            propertyNameTextView = (TextView)view.findViewById(R.id.property_name);
            propertyEmailTextView = (TextView)view.findViewById(R.id.property_email);
            propertyPhoneTextView = (TextView)view.findViewById(R.id.property_phone);
        }
    }

    public CosignerListAdapter() {
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
        holder.costTextView.setText(item.getMonthlyCostText());
        holder.signApproveButton.setText(item.getButtonText());
        holder.leaseSignersTextView.setText(item.getLeaseSignersText());
        holder.propertyNameTextView.setText(item.getPropertyContactInfo().getName() + " ");
        holder.propertyEmailTextView.setText(item.getPropertyContactInfo().getEmail() + " ");
        holder.propertyPhoneTextView.setText(item.getPropertyContactInfo().getFormattedPhoneNumber() + " ");
    }

    @Override
    public int getItemCount() {
        return cosignItems.size();
    }

}
