package com.rentalgeek.android.mvp.login;

/**
 * Created by Alan R on 10/21/15.
 */
public interface Presenter {
    void googelLogin(String fullname, String photoUrl, String id, String email);
    void linkedinLogin(String fullname,String id,String email);
}
