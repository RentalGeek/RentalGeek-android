package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.pojos.Address;
import com.rentalgeek.android.pojos.CosignItem;
import com.rentalgeek.android.pojos.LeaseSigner;
import com.rentalgeek.android.pojos.PropertyContactInfo;
import com.rentalgeek.android.ui.adapter.CosignerListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class ActivityCosignerList extends GeekBaseActivity {

    public ActivityCosignerList() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosigner_list);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<LeaseSigner> leaseSigners = new ArrayList<>();
        leaseSigners.add(new LeaseSigner("Matt Smith", "08/10/2015"));
        leaseSigners.add(new LeaseSigner("John Johnson", "08/11/2015"));

        PropertyContactInfo propertyContactInfo = new PropertyContactInfo("Spring Properties", "info@springproperties.com", "(000) 555-1212");

        Address address = new Address("2129 Walnut Dr.", "Manhattan", "KS", "66502");

        CosignItem cosignItem = new CosignItem();
        cosignItem.setAddress(address);
        cosignItem.setMonthlyCost(920);
        cosignItem.setNumBedrooms(2);
        cosignItem.setNumBathrooms(1);
        cosignItem.setSigners(leaseSigners);
        cosignItem.setPropertyContactInfo(propertyContactInfo);
        cosignItem.setImageUrl("https://rental-geek.s3.amazonaws.com/uploads/property_photo/photo/2155/large_IMG_3955.JPG");

        List<CosignItem> test = new ArrayList<>();
        test.add(cosignItem);
        test.add(cosignItem);
        test.add(cosignItem);
        test.add(cosignItem);
        test.add(cosignItem);
        test.add(cosignItem);
        test.add(cosignItem);
        test.add(cosignItem);
        CosignerListAdapter adapter = new CosignerListAdapter(test);
        recyclerView.setAdapter(adapter);
    }

}
