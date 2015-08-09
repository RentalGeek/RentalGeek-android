// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.geekscores;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GeekScoreMain$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.geekscores.GeekScoreMain target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558565, "field 'click_rent'");
    target.click_rent = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558568, "field 'get_started_paid_already' and method 'ClickRent'");
    target.get_started_paid_already = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.ClickRent();
        }
      });
    view = finder.findRequiredView(source, 2131558566, "field 'ddt'");
    target.ddt = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558567, "field 'getStarted' and method 'getStarted'");
    target.getStarted = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.getStarted();
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

  public static void reset(com.app.rentalgeek.geekscores.GeekScoreMain target) {
    target.click_rent = null;
    target.get_started_paid_already = null;
    target.ddt = null;
    target.getStarted = null;
  }
}
