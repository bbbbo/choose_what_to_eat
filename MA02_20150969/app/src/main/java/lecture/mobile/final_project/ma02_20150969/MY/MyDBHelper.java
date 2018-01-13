package lecture.mobile.final_project.ma02_20150969.MY;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bkbk0 on 2017-12-27.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "my_db";
    public final static String TABLE_NAME = "my_table";
    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";
    public final static String COL_TEL = "tel";
    public final static String COL_ADDRESS = "address";

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
                + COL_TITLE + " TEXT, " + COL_TEL + " TEXT, " + COL_ADDRESS + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
