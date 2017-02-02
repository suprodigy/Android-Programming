package com.boostcamp.jr.noisyalarm.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.boostcamp.jr.noisyalarm.AlarmService.AlarmServiceActivity;

/**
 * Created by jr on 2017-01-27.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Toast.makeText(context, "알람!!!", Toast.LENGTH_SHORT).show();

        /**
         * 여기서 알람 일주일 후 재설정하면 됨.(알람 반복 설정을 위해)
         * 하지만, 알람을 취소할 방법을 모르기 때문에 재설정안함.
         */

        Intent i = new Intent(context, AlarmServiceActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
