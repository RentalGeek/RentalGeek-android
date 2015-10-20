package com.rentalgeek.android.ui.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.model.User;

/**
 * Created by Alan R on 10/9/15.
 */
public class UserFieldTextWatcher implements TextWatcher {

    private EditText editText;

    public UserFieldTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        String tag = (String) editText.getTag();

        if (tag != null && !tag.isEmpty()) {
            User user = SessionManager.Instance.getCurrentUser();
            user.set(tag, editable.toString());
        }
    }
}
