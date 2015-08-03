// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.homepage;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class Registration$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.homepage.Registration target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558714, "field 'email_add_regis'");
    target.email_add_regis = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131558716, "field 'terms'");
    target.terms = (android.widget.CheckBox) view;
    view = finder.findRequiredView(source, 2131558719, "field 'prog'");
    target.prog = (android.widget.FrameLayout) view;
    view = finder.findRequiredView(source, 2131558717, "field 'terms_text' and method 'infoclick1'");
    target.terms_text = (android.widget.TextView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.infoclick1();
        }
      });
    view = finder.findRequiredView(source, 2131558713, "field 'password_regis'");
    target.password_regis = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131558715, "field 'confirm_password_regis'");
    target.confirm_password_regis = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131558718, "field 'create_account' and method 'CreateAccnt'");
    target.create_account = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.CreateAccnt(p0);
        }
      });
  }

  public static void reset(com.app.rentalgeek.homepage.Registration target) {
    target.email_add_regis = null;
    target.terms = null;
    target.prog = null;
    target.terms_text = null;
    target.password_regis = null;
    target.confirm_password_regis = null;
    target.create_account = null;
  }
}
