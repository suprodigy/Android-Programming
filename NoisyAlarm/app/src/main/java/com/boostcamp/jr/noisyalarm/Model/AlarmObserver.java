package com.boostcamp.jr.noisyalarm.Model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.boostcamp.jr.noisyalarm.Model.AlarmContract.AlarmEntry;
import com.boostcamp.jr.noisyalarm.Receiver.AlarmReceiver;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by jr on 2017-01-25.
 * 알람 데이터의 CRUD 작업을 직접 진행하면서, 알람을 설정/해체하는 기능을 가지고 있는 객체
 * Singleton 패턴을 적용
 */

/* COMPLETED (3) alarm-related functions 완성 */
/* COMPLETED (4) TimePicker 사용해서 UI 업데이트하고, 완성 */
/* COMPLETED (5) UI와 데이터 연결 */
/* COMPLETED (6) 현재 위치 조회하기 */

public class AlarmObserver {

    Context mContext;
    AlarmManager mAlarmManager;
    Cursor mCursor;

    private AlarmObserver(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * Singleton 패턴 적용
     */

    public static AlarmObserver sAlarmObserver;

    public static AlarmObserver getAlarmObserver(Context context) {
        if (sAlarmObserver == null) {
            sAlarmObserver = new AlarmObserver(context);
        }

        return sAlarmObserver;
    }

    // 알람 등록하고 데이터베이스에 Alarm 추가
    public void addAlarm(Alarm alarm) {

        /* 데이터베이스에 추가 */
        ContentValues cv = getContentValues(alarm);
        ContentResolver resolver = mContext.getContentResolver();
        Uri uri = resolver.insert(AlarmEntry.CONTENT_URI, cv);
        Cursor cursor = resolver.query(uri, null, null, null, null);
        cursor.moveToNext();
        Alarm insertedAlarm = getAlarmObject(cursor);

        /* 알람 설정 */
        setAlarm(insertedAlarm);
    }

    /**
     * @param alarm
     * if alarm.isSet() -> 알람 등록 후 업데이트
     * else             -> 단순 알람 데이터 업데이트
     */
    public void updateAlarm(Alarm alarm) {
        if(alarm.isSet()) {
            setAlarm(alarm);
        } else {
            cancelAlarm(alarm);
        }
        ContentValues cv = getContentValues(alarm);
        ContentResolver resolver = mContext.getContentResolver();
        String id = Integer.toString(alarm.getId());
        Uri uri = AlarmEntry.CONTENT_URI.buildUpon().appendPath(id).build();
        resolver.update(uri, cv, null, null);
    }

    // 모든 알람 데이터 조회하고, 그 내용을 mCursor에 저장
    public Cursor getAllAlarms() {
        ContentResolver resolver = mContext.getContentResolver();
        mCursor = resolver.query(AlarmEntry.CONTENT_URI, null, null, null, AlarmEntry._ID);
        return mCursor;
    }

    // 하나의 알람 데이터 조회
    public Alarm getAlarm(int position) {
        mCursor.moveToPosition(position);
        return getAlarmObject(mCursor);
    }

    /**
     * @param alarm
     * if alarm.isSet() -> 알람 등록 해지 후 데이터 삭제
     * else             -> 알람 데이터 삭제
     */
    public int deleteAlarm(Alarm alarm) {
        if(alarm.isSet()) {
            cancelAlarm(alarm);
        }
        ContentValues cv = getContentValues(alarm);
        ContentResolver resolver = mContext.getContentResolver();
        String id = Integer.toString(alarm.getId());
        Uri uri = AlarmEntry.CONTENT_URI.buildUpon().appendPath(id).build();
        return resolver.delete(uri, null, null);
    }

    public void setAlarm(Alarm alarm) {

        // 해당 요일이 체크되어 있으면 알람 설정, 아니면 패스.
        // 여기서 다수의 알람에 대해 동일한 requestCode 적용 가능한지 확실하지 않음.
        // 적용안되서 각각 requestCode 부여
        int dayOfWeek = alarm.getDayOfWeek();
        int requestCode = alarm.getId()*7;

        for(int i=1; i<=7; i++) {
            if(dayOfWeek % 2 == 1) {
                Calendar alarmCalendar = new GregorianCalendar(Locale.KOREA);
                alarmCalendar.set(Calendar.DAY_OF_WEEK, i);
                // 시간 설정
                boolean isPM = false;
                int hour = alarm.getHour();
                int minute = alarm.getMin();
                if(hour >= 12) {
                    hour = hour - 24 + 12;
                    isPM = true;
                }

                alarmCalendar.set(Calendar.HOUR, hour);
                alarmCalendar.set(Calendar.MINUTE, minute);
                alarmCalendar.set(Calendar.SECOND, 0);
                alarmCalendar.set(Calendar.AM_PM, isPM ? Calendar.PM : Calendar.AM);

                // 알람 시간 계산해서 설정
                Long alarmTime = alarmCalendar.getTimeInMillis();

                // 지난 요일에 설정하는 알람은 일주일 더해서 세팅
                if(alarmCalendar.getTimeInMillis() < System.currentTimeMillis()) {
                    int OneWeekInMillis = 604800000;
                    alarmTime += OneWeekInMillis;
                }

                // Alarm ID로 reqeustCode 만든 후 알람 등록
                Intent intent = new Intent(mContext, AlarmReceiver.class);
                intent.putExtra("keyValue", requestCode);
                PendingIntent pi = PendingIntent.getBroadcast(mContext, requestCode + (i-1), intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                if(Build.VERSION.SDK_INT >= 19) {
                    mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pi);
                } else {
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pi);
                }

                // setReapting 함수가 부정확하기 때문에, 위에 있는 setExact 함수 사용
                // mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, 7*AlarmManager.INTERVAL_DAY, pi);
            }
            dayOfWeek /= 2;
        }

    }

    public void cancelAlarm(Alarm alarm) {

        int dayOfWeek = alarm.getDayOfWeek();
        int requestCode = alarm.getId()*7;

        for(int i=1; i<=7; i++) {
            if(dayOfWeek % 2 == 1) {
                Intent intent = new Intent(mContext, AlarmReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(mContext, requestCode + (i-1), intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                mAlarmManager.cancel(pi);
                pi.cancel();
            }
            dayOfWeek /= 2;
        }

    }

    // Alarm 객체로부터 ContentValues 객체 반환
    public ContentValues getContentValues(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(AlarmEntry.COLUMN_HOUR, alarm.getHour());
        cv.put(AlarmEntry.COLUMN_MINUTE, alarm.getMin());
        cv.put(AlarmEntry.COLUMN_DAY_OF_WEEK, alarm.getDayOfWeek());
        cv.put(AlarmEntry.COLUMN_IS_SET, alarm.isSet() ? 1 : 0);
        cv.put(AlarmEntry.COLUMN_CONTENT, alarm.getContent());
        return cv;
    }

    // 하나의 Alarm Object 가리키고 있는 Cursor를 Alarm 객체로 변환
    public Alarm getAlarmObject(Cursor cursor) {
        if(cursor != null) {
            return new Alarm()
                    .setId(cursor.getInt(cursor.getColumnIndex(AlarmEntry._ID)))
                    .setHour(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_HOUR)))
                    .setMin(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_MINUTE)))
                    .setDayOfWeek(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_DAY_OF_WEEK)))
                    .setSet(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_IS_SET)) == 1)
                    .setContent(cursor.getString(cursor.getColumnIndex(AlarmEntry.COLUMN_CONTENT)));
        }
        return null;
    }

}
