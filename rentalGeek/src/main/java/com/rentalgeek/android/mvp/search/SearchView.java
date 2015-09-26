package com.rentalgeek.android.mvp.search;

import android.os.Bundle;

public interface SearchView {
    public void returnRentals(Bundle bundle);
    public void showMessage(String title, String msg);
}
