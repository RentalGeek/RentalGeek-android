package com.rentalgeek.android.mvp.login;

public interface Presenter {

    void googleLogin(String fullname, String photoUrl, String id, String email);
    void linkedinLogin(String fullname,String id,String email);
    void facebookLogin(String fullname,String email);
    void rentalgeekLogin(String email, String password);

}
