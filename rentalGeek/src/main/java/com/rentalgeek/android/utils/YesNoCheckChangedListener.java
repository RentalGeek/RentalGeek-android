package com.rentalgeek.android.utils;

import android.widget.RadioGroup;

import com.rentalgeek.android.R;
import com.rentalgeek.android.model.Answer;

/**
 * Created by rajohns on 9/22/15.
 *
 */
public class YesNoCheckChangedListener implements RadioGroup.OnCheckedChangeListener {

    Answer answer;

    public YesNoCheckChangedListener(Answer answer) {
        super();
        this.answer = answer;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.segment_no:
                this.answer.ans = Boolean.FALSE;
                break;
            case R.id.segment_yes:
                this.answer.ans = Boolean.TRUE;
                break;
            default:
                break;
        }
    }

}
