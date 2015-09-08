package com.rentalgeek.android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.pojos.CosignItem;

import java.util.List;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class CosignerListAdapter extends RecyclerView.Adapter<CosignerListAdapter.CosignerListViewHolder> {

    private List<CosignItem> cosignItems;

    public static class CosignerListViewHolder extends RecyclerView.ViewHolder {
        public TextView leaseSignersTextView;
        public CosignerListViewHolder(View view) {
            super(view);
            leaseSignersTextView = (TextView)view.findViewById(R.id.lease_signers_textview);
        }
    }

    public CosignerListAdapter(List<CosignItem> cosignItems) {
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
        holder.leaseSignersTextView.setText(item.getLeaseSignersText());
    }

    @Override
    public int getItemCount() {
        return cosignItems.size();
    }

}
