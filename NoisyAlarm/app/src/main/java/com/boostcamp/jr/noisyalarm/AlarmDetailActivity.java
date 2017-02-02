package com.boostcamp.jr.noisyalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by jr on 2017-01-24.
 */

public class AlarmDetailActivity extends SingleFragmentActivity {

    private static final String EXTRA_ALARM_POSITION = "alarm_position";

    public static Intent newIntent(Context context, int alarmId) {
        Intent intent = new Intent(context, AlarmDetailActivity.class);
        intent.putExtra(EXTRA_ALARM_POSITION, alarmId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Toolbar Setting
        setTitle(R.string.alarm_detail_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Activity가 다른 Component에 의해 불러질 때, Intent에 담긴 Alarm ID 정보를 Fragment한테도 전달
    @Override
    protected Fragment createFragment() {
        int position = getIntent().getIntExtra(EXTRA_ALARM_POSITION, -1);
        if (position == -1) {
            Log.e("AlarmDetailActivity", "Data not found");
            onBackPressed();
        }
        return AlarmDetailFragment.newInstance(position);
    }

}
