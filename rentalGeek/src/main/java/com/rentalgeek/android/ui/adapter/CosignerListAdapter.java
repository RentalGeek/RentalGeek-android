package com.rentalgeek.android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;

import java.util.List;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class CosignerListAdapter extends RecyclerView.Adapter<CosignerListAdapter.CosignerListViewHolder> {

    private List<String> dataset;

    public static class CosignerListViewHolder extends RecyclerView.ViewHolder {
//        public TextView textView;
        public CosignerListViewHolder(View view) {
            super(view);
//            textView = (TextView)view.findViewById(R.id.textview);
        }
    }

    public CosignerListAdapter(List<String> myDataset) {
        dataset = myDataset;
    }

    @Override
    public CosignerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cosigner_list_item, parent, false);
        return new CosignerListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CosignerListViewHolder holder, int position) {
        String text = dataset.get(position);
//        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}
