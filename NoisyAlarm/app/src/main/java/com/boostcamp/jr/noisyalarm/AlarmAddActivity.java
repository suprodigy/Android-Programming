package com.boostcamp.jr.noisyalarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by jr on 2017-01-24.
 */

public class AlarmAddActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Toolbar Setting
        setTitle(R.string.alarm_add_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected Fragment createFragment() {
        return new AlarmAddFragment();
    }

}
