package photoprayeroftheday.entrision.com.dailyphotoprayer;

import android.media.Image;

public class Prayer {

    private String date;
    private String location;
    private String prayer;
    private byte[] photo;

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getPrayer() {
        return prayer;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setValues(String date, String loc, String pray, byte[] image) {
        this.date = date;
        this.location = loc;
        this.prayer = pray;
        this.photo = image;
    }
}
