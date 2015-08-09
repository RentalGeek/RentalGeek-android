// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.geekscores;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class FinalGeekScore$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.geekscores.FinalGeekScore target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558633, "method 'ClickFinal'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.ClickFinal();
        }
      });
    view = finder.findRequiredView(source, 2131558563, "method 'infoclick2'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.infoclick2();
        }
      });
    view = finder.findRequiredView(source, 2131558564, "method 'infoclick1'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.infoclick1();
        }
      });
  }

  public static void reset(com.app.rentalgeek.geekscores.FinalGeekScore target) {
  }
}
