package com.marakana.android.yamba.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class YambaDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "HELPER";

    public static final String DATABASE = "yamba.db";
    public static final int VERSION = 1;

    public static final String TABLE_TIMELINE = "timeline";
    public static final String COL_ID = "id";
    public static final String COL_USER = "user";
    public static final String COL_STATUS = "status";
    public static final String COL_CREATED_AT = "created_at";


    public YambaDbHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "creating db");
        db.execSQL(
                "create table " + TABLE_TIMELINE + "("
                    + COL_ID + " integer primary key,"
                    + COL_CREATED_AT + " integer not null,"
                    + COL_USER + " text,"
                    + COL_STATUS + " text)"
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVers, int newVers) {
        Log.d(TAG, "updating db");
        db.execSQL("drop table " + TABLE_TIMELINE);
        onCreate(db);
    }
}
