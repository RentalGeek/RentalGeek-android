package com.rentalgeek.android.ui.fragment;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import butterknife.InjectView;
import butterknife.ButterKnife;
import android.widget.ListView;
import com.rentalgeek.android.R;
import android.view.LayoutInflater;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.ui.adapter.RentalAdapter;
import com.rentalgeek.android.mvp.list.rental.RentalListView;
import com.rentalgeek.android.mvp.list.rental.RentalListPresenter;

public class FragmentRentalListView extends GeekBaseFragment implements RentalListView {
    
    private static final String TAG = FragmentRentalListView.class.getSimpleName();
    private RentalAdapter adapter;
    private RentalListPresenter presenter;

    @InjectView(R.id.slidelist) ListView rentalListView;
    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        adapter = new RentalAdapter(getActivity(),R.layout.rental_listview_row);
        presenter = new RentalListPresenter(this);
        adapter.setPresenter(presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.geek_listview, container, false);
        ButterKnife.inject(this, view);
        
        rentalListView.setAdapter(adapter);
        return view;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void setRentals(Rental[] rentals) {
        adapter.clear();
        adapter.addAll(rentals);
    }
}
