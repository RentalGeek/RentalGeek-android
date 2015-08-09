// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.geekscores;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class VerifyAccount$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.geekscores.VerifyAccount target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558774, "field 'verify_password'");
    target.verify_password = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131558776, "field 'face_verify' and method 'FaceBookClick'");
    target.face_verify = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.FaceBookClick();
        }
      });
    view = finder.findRequiredView(source, 2131558777, "field 'goog_verify' and method 'GoogleClick'");
    target.goog_verify = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.GoogleClick();
        }
      });
    view = finder.findRequiredView(source, 2131558778, "field 'link_verify' and method 'LinkedClick'");
    target.link_verify = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.LinkedClick();
        }
      });
    view = finder.findRequiredView(source, 2131558775, "field 'rent_verify' and method 'rentVerify'");
    target.rent_verify = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.rentVerify();
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

  public static void reset(com.app.rentalgeek.geekscores.VerifyAccount target) {
    target.verify_password = null;
    target.face_verify = null;
    target.goog_verify = null;
    target.link_verify = null;
    target.rent_verify = null;
  }
}
