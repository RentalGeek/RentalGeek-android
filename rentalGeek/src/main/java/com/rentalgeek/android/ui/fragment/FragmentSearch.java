package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.mvp.search.SearchPresenter;
import com.rentalgeek.android.mvp.search.SearchView;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.ui.view.SearchOptionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class FragmentSearch extends GeekBaseFragment implements SearchView {

    @InjectViews({R.id.btn_bed0, R.id.btn_bed1, R.id.btn_bed2, R.id.btn_bed3, R.id.btn_bed4})
    List<SearchOptionButton> bedBtns;

    @InjectViews({R.id.btn_bath1, R.id.btn_bath2, R.id.btn_bath3, R.id.btn_bath4})
    List<SearchOptionButton> bathBtns;

    @InjectView(R.id.price_seek)
    SeekBar priceSeeker;
    @InjectView(R.id.rent_range)
    TextView rentRangeTextView;

    private SearchPresenter presenter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        presenter = new SearchPresenter();

        bedBtns = new ArrayList<SearchOptionButton>();
        bathBtns = new ArrayList<SearchOptionButton>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
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

        rememberPreviousSearchSettings();

        return view;
    }

    private void rememberPreviousSearchSettings() {
        priceSeeker.setProgress(AppPreferences.getSearchMaxPrice());

        ArrayList<Integer> previouslySelectedButtonIds = AppPreferences.getSearchSelectedButtons();
        ArrayList<SearchOptionButton> allButtons = new ArrayList<>();

        for (SearchOptionButton button : bathBtns) {
            allButtons.add(button);
        }
        for (SearchOptionButton button : bedBtns) {
            allButtons.add(button);
        }

        for (SearchOptionButton button : allButtons) {
            for (int selectedButtonId : previouslySelectedButtonIds) {
                if (button.getId() == selectedButtonId) {
                    button.pressed();
                    break;
                }
            }
        }
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
        priceSeeker.setProgress(1000);

        for (SearchOptionButton button : bedBtns) {
            button.reset();
        }

        for (SearchOptionButton button : bathBtns) {
            button.reset();
        }
    }

    @OnClick(R.id.search_submit)
    public void onSubmitClick() {
        showProgressDialog(R.string.search_rental);

        ArrayList<String> bathValues = new ArrayList<String>();
        ArrayList<Integer> bathIds = new ArrayList<>();
        ArrayList<String> bedValues = new ArrayList<String>();
        ArrayList<Integer> bedIds = new ArrayList<>();

        for (SearchOptionButton button : bedBtns) {
            if (button.isSelected()) {
                bedValues.add(button.getValue());
                bedIds.add(button.getId());
            }
        }

        for (SearchOptionButton button : bathBtns) {
            if (button.isSelected()) {
                bathValues.add(button.getValue());
                bathIds.add(button.getId());
            }
        }

        Bundle bundle = new Bundle();

        if (bedValues.size() != 0) {
            bundle.putStringArrayList("BED_VALUES", bedValues);
        }
        if (bathValues.size() != 0) {
            bundle.putStringArrayList("BATH_VALUES", bathValues);
        }

        bundle.putInt("MAX_PRICE", priceSeeker.getProgress());
        AppPreferences.putSearchMaxPrice(priceSeeker.getProgress());
        bathIds.addAll(bedIds);
        AppPreferences.putSelectedSearchButtons(bathIds);

        presenter.getRentalOfferings(bundle);
    }
}
