package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.SearchEvent;
import com.rentalgeek.android.mvp.search.SearchPresenter;
import com.rentalgeek.android.mvp.search.SearchView;
import com.rentalgeek.android.ui.view.SearchOptionButton;
import com.rentalgeek.android.utils.OkAlert;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;



public class FragmentSearch extends GeekBaseFragment implements SearchView {
    
    @InjectViews({R.id.btn_bed0,R.id.btn_bed1,R.id.btn_bed2,R.id.btn_bed3,R.id.btn_bed4})
    List<SearchOptionButton> bedBtns;

    @InjectViews({R.id.btn_bath1,R.id.btn_bath2,R.id.btn_bath3,R.id.btn_bath4})
    List<SearchOptionButton> bathBtns;

    @InjectView(R.id.price_seek) SeekBar priceSeeker;
    @InjectView(R.id.rent_range) TextView rentRangeTextView;

    private SearchPresenter presenter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        presenter = new SearchPresenter(this);

        bedBtns = new ArrayList<SearchOptionButton>();
        bathBtns = new ArrayList<SearchOptionButton>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        ButterKnife.inject(this, view);

        priceSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rentRangeTextView.setText("$" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.btn_bed0,
             R.id.btn_bed1,
             R.id.btn_bed2,
             R.id.btn_bed3,
             R.id.btn_bed4,
             R.id.btn_bath1,
             R.id.btn_bath2,
             R.id.btn_bath3,
             R.id.btn_bath4}) 
    public void onSearchOptionClick(SearchOptionButton button) {
        button.pressed();
    }

    @OnClick(R.id.reset_search)
    public void onResetClick() {
        priceSeeker.setProgress(0);

        for(SearchOptionButton button : bedBtns) {
            button.reset();
        }

        for(SearchOptionButton button : bathBtns) {
            button.reset();
        }
    }

    @OnClick(R.id.search_submit)
    public void onSubmitClick() {
        showProgressDialog(R.string.search_rental);

        ArrayList<String> bathValues = new ArrayList<String>();
        ArrayList<String> bedValues = new ArrayList<String>();
  
        for(SearchOptionButton button : bedBtns) {
            if( button.isSelected() ) {
                bedValues.add(button.getValue());
            }
        }

        for(SearchOptionButton button : bathBtns) {
            if( button.isSelected() ) {
                bathValues.add(button.getValue());
            }
        }
        
        Bundle bundle = new Bundle();

        if( bedValues.size() != 0 ) {
            bundle.putStringArrayList("BED_VALUES", bedValues);
        }
        if( bathValues.size() != 0 ) {
            bundle.putStringArrayList("BATH_VALUES", bathValues);
        }

        bundle.putInt("MAX_PRICE", priceSeeker.getProgress());

        presenter.getRentalOfferings(bundle);
    }

    @Override public void returnRentals(Bundle bundle) {
        hideProgressDialog();   
        AppEventBus.post(new SearchEvent(bundle));
    }

    @Override public void showMessage(String title, String msg) {
             hideProgressDialog();   
             OkAlert.show(getActivity(),title,msg);
    }
}
