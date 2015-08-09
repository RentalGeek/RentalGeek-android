// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.homepage;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SlideLeft$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.homepage.SlideLeft target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558712, "field 'list'");
    target.list = (android.widget.ListView) view;
  }

  public static void reset(com.app.rentalgeek.homepage.SlideLeft target) {
    target.list = null;
  }
}
