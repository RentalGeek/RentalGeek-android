// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.tutorials;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SignIn$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.tutorials.SignIn target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558733, "field 'btnSignIn' and method 'btn_sign_in'");
    target.btnSignIn = (com.google.android.gms.common.SignInButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.btn_sign_in();
        }
      });
    view = finder.findRequiredView(source, 2131558726, "field 'create_aacnt' and method 'SignInButton'");
    target.create_aacnt = (android.widget.TextView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.SignInButton(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558729, "field 'google_plus' and method 'GooglePlusClick'");
    target.google_plus = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.GooglePlusClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558730, "field 'facebook_lay' and method 'facebookClick'");
    target.facebook_lay = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.facebookClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558731, "field 'linked_lay' and method 'LinkedInClick'");
    target.linked_lay = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.LinkedInClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558722, "field 'ed_username'");
    target.ed_username = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131558732, "field 'authButton'");
    target.authButton = (com.facebook.widget.LoginButton) view;
    view = finder.findRequiredView(source, 2131558723, "field 'ed_password'");
    target.ed_password = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131558725, "method 'CreateAccount'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.CreateAccount(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558724, "method 'infoclick1'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.infoclick1();
        }
      });
  }

  public static void reset(com.app.rentalgeek.tutorials.SignIn target) {
    target.btnSignIn = null;
    target.create_aacnt = null;
    target.google_plus = null;
    target.facebook_lay = null;
    target.linked_lay = null;
    target.ed_username = null;
    target.authButton = null;
    target.ed_password = null;
  }
}
