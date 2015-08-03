// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.geekvision;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GeekVision$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.geekvision.GeekVision target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558579, "method 'ComingSoon'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.ComingSoon(p0);
        }
      });
  }

  public static void reset(com.app.rentalgeek.geekvision.GeekVision target) {
  }
}
