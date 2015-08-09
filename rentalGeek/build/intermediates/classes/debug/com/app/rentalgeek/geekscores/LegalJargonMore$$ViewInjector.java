// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.geekscores;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LegalJargonMore$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.geekscores.LegalJargonMore target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558596, "field 'wv'");
    target.wv = (android.webkit.WebView) view;
    view = finder.findRequiredView(source, 2131558597, "method 'agree'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.agree();
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

  public static void reset(com.app.rentalgeek.geekscores.LegalJargonMore target) {
    target.wv = null;
  }
}
