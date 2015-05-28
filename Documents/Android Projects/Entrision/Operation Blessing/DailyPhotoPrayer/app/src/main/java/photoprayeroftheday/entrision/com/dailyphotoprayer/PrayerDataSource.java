package photoprayeroftheday.entrision.com.dailyphotoprayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;

import java.sql.SQLException;

public class PrayerDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    //private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_COMMENT };

    public PrayerDataSource (Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Prayer createPrayer(String location, String date, String prayer, Image photo) {
        ContentValues values = new ContentValues();
        values.put("prayer", prayer);
        values.put("date", date);
        values.put("location", location);
        //values.put("photo", photo);

        String[] columns = {"date", "location", "prayer"};
        long insertID = database.insert("prayers", null, values);
        Cursor cursor = database.query("prayers", columns, "prayer_id = " + insertID, null, null, null, null);
        cursor.moveToFirst();
        Prayer newPrayer = new Prayer();
        newPrayer.setValues(cursor.getString(0), cursor.getString(1), cursor.getString(2));

        cursor.close();
        return newPrayer;
    }

    public Prayer getPrayerForDate(String date) {
        Prayer prayer = new Prayer();

        String[] columns = {"date", "location", "prayer"};
        Cursor cursor = database.query("prayers", columns, "date = " + date, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            prayer.setValues(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        } else {
            prayer = null;
        }

        cursor.close();
        return prayer;
    }
}
