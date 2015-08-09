// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.starredprop;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class StarredProperties$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.starredprop.StarredProperties target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558754, "field 'list'");
    target.list = (android.widget.ListView) view;
  }

  public static void reset(com.app.rentalgeek.starredprop.StarredProperties target) {
    target.list = null;
  }
}
