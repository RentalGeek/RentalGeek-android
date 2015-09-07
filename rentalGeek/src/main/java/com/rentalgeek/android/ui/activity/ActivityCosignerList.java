package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.adapter.CosignerListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class ActivityCosignerList extends AppCompatActivity {

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

        List<String> test = new ArrayList<>();
        test.add("hey");
        test.add("world");
        test.add("what is up");
        test.add("hey");
        test.add("world");
        test.add("what is up");
        test.add("hey");
        test.add("world");
        test.add("what is up");
        CosignerListAdapter adapter = new CosignerListAdapter(test);
        recyclerView.setAdapter(adapter);
    }

}
