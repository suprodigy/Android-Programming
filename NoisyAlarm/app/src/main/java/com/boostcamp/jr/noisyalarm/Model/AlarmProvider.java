package com.boostcamp.jr.noisyalarm.Model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.boostcamp.jr.noisyalarm.Model.AlarmContract.AlarmEntry.TABLE_NAME;

/**
 * Created by jr on 2017-01-25.
 * Content Provider 구현
 */

public class AlarmProvider extends ContentProvider {

    // ContentProvider 내부적으로 사용할 DBHelper 객체
    private AlarmDbHelper mDbHelper;

    // Uri 매치할 때 사용할 정수 값
    public static final int ALARMS = 111;
    public static final int ALARM_WITH_ID = 112;

    // UriMatcher 객체 static 변수로 선언
    private static final UriMatcher sMatcher = getUriMatcher();

    // Query 작업을 할 때 사용할 UriMatcher 클래스 만들어서 반환
    public static UriMatcher getUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AlarmContract.AUTHORITY, AlarmContract.PATH_ALARMS, ALARMS);
        matcher.addURI(AlarmContract.AUTHORITY, AlarmContract.PATH_ALARMS + "/#", ALARM_WITH_ID);
        return matcher;
    }

    // DBHelper 객체 생성
    @Override
    public boolean onCreate() {
        Context context= getContext();
        mDbHelper = new AlarmDbHelper(context);
        return true;
    }

    /**
     * 여기서부터 CRUD 작업 정의
     */

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sMatcher.match(uri);
        Uri retUri;

        switch (match) {
            case ALARMS:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    retUri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("데이터 추가 실패!");
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case ALARMS:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case ALARM_WITH_ID:
                String id = uri.getPathSegments().get(1);
                retCursor = db.query(TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        // Content Uri가 변하는 것을 Cursor가 알 수 있도록 등록한 후 Cursor 반환
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sMatcher.match(uri);
        int deletedCount;

        switch (match) {
            case ALARM_WITH_ID:
                String id= uri.getPathSegments().get(1);
                deletedCount = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        return deletedCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sMatcher.match(uri);
        int updatedCount;

        switch (match) {
            case ALARM_WITH_ID:
                String id = uri.getPathSegments().get(1);
                updatedCount = db.update(TABLE_NAME, values, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updatedCount;
    }

    /**
     * Uri의 MIME 타입 반환
     * 사용하지 않는 함수지만 인터넷에서 참고하여 구현해보았음.
     */

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sMatcher.match(uri);
        String retType;

        switch (match) {
            case ALARMS:
                retType = AlarmContract.AlarmEntry.CONTENT_TYPE;
                break;
            case ALARM_WITH_ID:
                retType = AlarmContract.AlarmEntry.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        return retType;
    }

}
