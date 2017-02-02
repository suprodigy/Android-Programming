package com.boostcamp.jr.noisyalarm.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.boostcamp.jr.noisyalarm.Model.AlarmContract.*;

/**
 * Created by jr on 2017-01-25.
 * DB 생성하고 관리해주는 DBHelper 클래스
 */

public class AlarmDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "alarms.db";
    private static final int DB_VERSION = 1;

    AlarmDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + AlarmEntry.TABLE_NAME + " ("
                + AlarmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AlarmEntry.COLUMN_HOUR + " INTEGER NOT NULL, "
                + AlarmEntry.COLUMN_MINUTE + " INTEGER NOT NULL, "
                + AlarmEntry.COLUMN_DAY_OF_WEEK + " INTEGER NOT NULL, "
                + AlarmEntry.COLUMN_IS_SET + " INTEGER NOT NULL, "
                + AlarmEntry.COLUMN_CONTENT + " TEXT); ";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AlarmEntry.TABLE_NAME);
        onCreate(db);
    }

}
