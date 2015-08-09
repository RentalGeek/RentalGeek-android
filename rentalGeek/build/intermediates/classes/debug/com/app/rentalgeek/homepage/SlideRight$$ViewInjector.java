// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.homepage;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SlideRight$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.homepage.SlideRight target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558740, "field 'studio' and method 'BedroomClick'");
    target.studio = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.BedroomClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558741, "field 'bt_bedone' and method 'BedroomClick'");
    target.bt_bedone = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.BedroomClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558742, "field 'bt_bedtwo' and method 'BedroomClick'");
    target.bt_bedtwo = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.BedroomClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558743, "field 'bt_bedthree' and method 'BedroomClick'");
    target.bt_bedthree = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.BedroomClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558744, "field 'bt_bedfour' and method 'BedroomClick'");
    target.bt_bedfour = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.BedroomClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558746, "field 'bt_bathone' and method 'Bathroom'");
    target.bt_bathone = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.Bathroom(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558747, "field 'bt_bathtwo' and method 'Bathroom'");
    target.bt_bathtwo = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.Bathroom(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558748, "field 'bt_baththree' and method 'Bathroom'");
    target.bt_baththree = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.Bathroom(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558749, "field 'bt_bathfour' and method 'Bathroom'");
    target.bt_bathfour = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.Bathroom(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558737, "field 'ed_search'");
    target.ed_search = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131558738, "field 'price_seek'");
    target.price_seek = (android.widget.SeekBar) view;
    view = finder.findRequiredView(source, 2131558619, "field 'price_range'");
    target.price_range = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558753, "method 'SearchLocation'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.SearchLocation();
        }
      });
    view = finder.findRequiredView(source, 2131558736, "method 'ClearSearchDet'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.ClearSearchDet();
        }
      });
  }

  public static void reset(com.app.rentalgeek.homepage.SlideRight target) {
    target.studio = null;
    target.bt_bedone = null;
    target.bt_bedtwo = null;
    target.bt_bedthree = null;
    target.bt_bedfour = null;
    target.bt_bathone = null;
    target.bt_bathtwo = null;
    target.bt_baththree = null;
    target.bt_bathfour = null;
    target.ed_search = null;
    target.price_seek = null;
    target.price_range = null;
  }
}
