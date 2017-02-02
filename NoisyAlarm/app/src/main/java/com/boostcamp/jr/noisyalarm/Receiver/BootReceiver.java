package com.boostcamp.jr.noisyalarm.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import com.boostcamp.jr.noisyalarm.Model.Alarm;
import com.boostcamp.jr.noisyalarm.Model.AlarmObserver;

/**
 * Created by jr on 2017-01-27.
 * Phone 재부팅시 알람 다시 등록
 */

public class BootReceiver extends BroadcastReceiver {

    private AlarmSettingAsyncTask mAlarmSettingAsyncTask;
    AlarmObserver mAlarmObserver;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "알람 재등록", Toast.LENGTH_LONG).show();
//        for(int i=0; i<100; i++) {
//            Log.d("BootReceiver", "실행?!");
//        }

        mAlarmObserver = AlarmObserver.getAlarmObserver(context);
        mAlarmSettingAsyncTask = new AlarmSettingAsyncTask();
        mAlarmSettingAsyncTask.execute();
    }

    private class AlarmSettingAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

//            for(int i=0; i<100; i++) {
//                Log.d("BootReceiver", "실행?!");
//            }

            Cursor cursor = mAlarmObserver.getAllAlarms();
            while(cursor.moveToNext()) {
                Alarm alarm = mAlarmObserver.getAlarmObject(cursor);
                if(alarm.isSet()) {
                    mAlarmObserver.setAlarm(alarm);
                }
            }
            return null;
        }

    }
}
