// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.tutorials;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SecondClass$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.tutorials.SecondClass target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558721, "field 'four'");
    target.four = (android.widget.TextView) view;
  }

  public static void reset(com.app.rentalgeek.tutorials.SecondClass target) {
    target.four = null;
  }
}
