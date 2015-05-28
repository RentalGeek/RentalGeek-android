package photoprayeroftheday.entrision.com.dailyphotoprayer;

import android.graphics.Bitmap;
import android.media.Image;

public class Prayer {

    private String date;
    private String location;
    private String prayer;

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getPrayer() {
        return prayer;
    }


    public void setValues(String date, String loc, String pray) {
        this.date = date;
        this.location = loc;
        this.prayer = pray;
    }
}
