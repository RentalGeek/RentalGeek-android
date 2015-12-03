package com.rentalgeek.android.utils;

import android.widget.RadioGroup;

import com.rentalgeek.android.R;
import com.rentalgeek.android.model.YesNoAnswer;

/**
 * Created by rajohns on 9/22/15.
 */
public class YesNoCheckChangedListener implements RadioGroup.OnCheckedChangeListener {

    YesNoAnswer yesNoAnswer;

    public YesNoCheckChangedListener(YesNoAnswer yesNoAnswer) {
        super();
        this.yesNoAnswer = yesNoAnswer;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.segment_no:
                this.yesNoAnswer.ans = false;
                break;
            case R.id.segment_yes:
                this.yesNoAnswer.ans = true;
                break;
            default:
                break;
        }
    }

}
