// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.tutorials;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ThirdClass$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.tutorials.ThirdClass target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558758, "field 'armed'");
    target.armed = (android.widget.TextView) view;
  }

  public static void reset(com.app.rentalgeek.tutorials.ThirdClass target) {
    target.armed = null;
  }
}
