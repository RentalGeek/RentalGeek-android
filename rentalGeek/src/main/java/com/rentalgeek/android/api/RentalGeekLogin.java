package com.rentalgeek.android.api;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.RentalGeekLoginEvent;

/**
 * Created by Alan R on 10/21/15.
 */
public class RentalGeekLogin implements LoginInterface, Validator.ValidationListener {

    private Validator validator;

    @Override
    public void clicked(Activity context) {
        System.out.println("RentalGeek clicked");
        validator.validate();
    }

    @Override
    public void setup(Activity context) {

    }

    @Override
    public void onStart(Activity context) {
        System.out.println("RentalGeek onStart");
    }

    @Override
    public void onStop(Activity context) {
        System.out.println("RentalGeek onStop");
    }

    @Override
    public void onActivityResult(Activity context, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void setValidation(Object controller) {
        if( controller != null ) {
            validator = new Validator(controller);
            validator.setValidationListener(this);
        }
    }

    @Override
    public void onValidationSucceeded() {
        System.out.println("RentalGeek login validation success");
        AppEventBus.post(new RentalGeekLoginEvent());
    }

    @Override
    public void onValidationFailed(View view, Rule<?> rule) {
        System.out.println("RentalGeek login validation failed");

        String message = rule.getFailureMessage();

        if (view instanceof EditText) {
            view.requestFocus();
            ((EditText) view).setError(message);
        }
    }
}
