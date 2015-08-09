// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.geekscores;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class Payment$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.geekscores.Payment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558529, "field 'verify_card' and method 'Payment'");
    target.verify_card = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.Payment();
        }
      });
    view = finder.findRequiredView(source, 2131558520, "field 'cardNo'");
    target.cardNo = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131558521, "field 'cardName'");
    target.cardName = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131558523, "field 'ed_mm'");
    target.ed_mm = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131558525, "field 'edYYYY'");
    target.edYYYY = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131558527, "field 'edCvv'");
    target.edCvv = (android.widget.EditText) view;
  }

  public static void reset(com.app.rentalgeek.geekscores.Payment target) {
    target.verify_card = null;
    target.cardNo = null;
    target.cardName = null;
    target.ed_mm = null;
    target.edYYYY = null;
    target.edCvv = null;
  }
}
