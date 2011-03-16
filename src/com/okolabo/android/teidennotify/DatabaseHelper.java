
package com.okolabo.android.teidennotify;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String TAG = "DatabaseHelper";

    private static final int DB_VERSION = 1;

    private static final String DATABASE_NAME = "teiden_app.db";

    private SQLiteDatabase mDB;

    /** Historis テーブル名 */
    private static final String TBL_HISTORIES = "Historis";

    interface Histories {
        public static final String ID = "_id";

        public static final String HISTORY = "history";

        public static final String CREATED = "created";
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        mDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Historisテーブル
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_HISTORIES + " ("
                + Histories.ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Histories.HISTORY + " TEXT,"
                + Histories.CREATED + " TEXT"
                + ")"
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int olderVersion, int newVersion) {
    }

    public long insertHistories(String history) {
        // インサート
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        
        ContentValues cv = new ContentValues();
        cv.put(Histories.HISTORY, history);
        cv.put(Histories.CREATED, fmt.format(cal.getTime()));
        long rowId = 0;
        rowId = mDB.insert(TBL_HISTORIES, null, cv);
        if (rowId <= 0) {
            Log.e(TAG, "Table: " + TBL_HISTORIES + " INSERT ERROR!");
        }
        return rowId;
    }

    public Cursor getAll() {
        String orderBy = Histories.ID + " DESC";
        return mDB.query(TBL_HISTORIES, null, null, null, null, null, orderBy);
    }
    
    public Cursor get(long id) {
        String where = Histories.ID + " = ?";
        String[] placeHolder = {
            String.valueOf(id)
        };
        return mDB.query(TBL_HISTORIES, null, where, placeHolder, null, null, null);
    }

    public int delete(long id) {
        String where = Histories.ID + " = ?";
        String[] placeHolder = {
            String.valueOf(id)
        };
        return mDB.delete(TBL_HISTORIES, where, placeHolder);
    }
    
    @Override
    protected void finalize() throws Throwable {
        mDB.close();
        this.close();
        super.finalize();
    }
}