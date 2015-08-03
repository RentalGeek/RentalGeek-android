// Generated code from Butter Knife. Do not modify!
package com.app.rentalgeek.homepage;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ListInnerPage$$ViewInjector {
  public static void inject(Finder finder, final com.app.rentalgeek.homepage.ListInnerPage target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558599, "field 'main_image'");
    target.main_image = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131558616, "field 'amen_tag'");
    target.amen_tag = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558617, "field 'amenisties_lay'");
    target.amenisties_lay = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131558613, "field 'apply' and method 'Apply'");
    target.apply = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.Apply();
        }
      });
    view = finder.findRequiredView(source, 2131558611, "field 'like'");
    target.like = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131558602, "field 'price_range'");
    target.price_range = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558600, "field 'star_img' and method 'Like'");
    target.star_img = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.Like();
        }
      });
    view = finder.findRequiredView(source, 2131558612, "field 'like_tag'");
    target.like_tag = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558604, "field 'street_name_inner'");
    target.street_name_inner = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558605, "field 'bed_inner'");
    target.bed_inner = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558603, "field 'shower_inner'");
    target.shower_inner = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558606, "field 'am0'");
    target.am0 = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558607, "field 'am1'");
    target.am1 = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558608, "field 'am2'");
    target.am2 = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558609, "field 'am3'");
    target.am3 = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558614, "field 'am4'");
    target.am4 = (ir.noghteh.JustifiedTextView) view;
    view = finder.findRequiredView(source, 2131558610, "method 'Like'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.Like();
        }
      });
    view = finder.findRequiredView(source, 2131558615, "method 'show_mre'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.show_mre();
        }
      });
  }

  public static void reset(com.app.rentalgeek.homepage.ListInnerPage target) {
    target.main_image = null;
    target.amen_tag = null;
    target.amenisties_lay = null;
    target.apply = null;
    target.like = null;
    target.price_range = null;
    target.star_img = null;
    target.like_tag = null;
    target.street_name_inner = null;
    target.bed_inner = null;
    target.shower_inner = null;
    target.am0 = null;
    target.am1 = null;
    target.am2 = null;
    target.am3 = null;
    target.am4 = null;
  }
}
