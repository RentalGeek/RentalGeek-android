// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.homepage;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class HomeActivity$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.homepage.HomeActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558582, "field 'map_selector'");
    target.map_selector = view;
    view = finder.findRequiredView(source, 2131558584, "field 'list_selector'");
    target.list_selector = view;
    view = finder.findRequiredView(source, 2131558580, "method 'ImageView_menu'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.ImageView_menu();
        }
      });
    view = finder.findRequiredView(source, 2131558586, "method 'ImageView_lens'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.ImageView_lens();
        }
      });
    view = finder.findRequiredView(source, 2131558581, "method 'ListMapSelection'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.ListMapSelection(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558583, "method 'ListMapSelection'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.ListMapSelection(p0);
        }
      });
  }

  public static void reset(com.app.rentalgeek.homepage.HomeActivity target) {
    target.map_selector = null;
    target.list_selector = null;
  }
}
