package com.boostcamp.jr.noisyalarm;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.boostcamp.jr.noisyalarm.Model.Alarm;
import com.boostcamp.jr.noisyalarm.Model.AlarmObserver;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jr on 2017-01-24.
 */

public class AlarmAddFragment extends Fragment
        implements TimePickerDialog.OnTimeSetListener {

    AlarmObserver mAlarmObserver;

    @BindView(R.id.timepicker)
    TimePicker mTimePicker;

    @BindViews({R.id.sunday, R.id.monday, R.id.tuesday, R.id.wednesday,
            R.id.thursday, R.id.friday, R.id.saturday})
    List<ToggleButton> dayOfWeekButtons;

    @BindView(R.id.alarm_content_edit_text)
    EditText mEditText;

    @OnClick(R.id.save_button)
    public void onSaveButtonClicked() {

        int hour, minute;
        if(Build.VERSION.SDK_INT >= 23) {
            hour = mTimePicker.getHour();
            minute = mTimePicker.getMinute();
        } else {
            hour = mTimePicker.getCurrentHour();
            minute = mTimePicker.getCurrentMinute();
        }

        // Log.d("", "시간 설정 = " + hour + ":" + minute);

        String content = mEditText.getText().toString();

        int dayOfWeek = 0;
        int bit = 1;
        for(int i=0; i<7; i++) {
            if(dayOfWeekButtons.get(i).isChecked()) {
                dayOfWeek += bit;
            }
            bit *= 2;
        }

        Alarm alarm = new Alarm()
                .setHour(hour)
                .setMin(minute)
                .setDayOfWeek(dayOfWeek)
                .setSet(true)
                .setContent(content);
        mAlarmObserver.addAlarm(alarm);

        // 알람 등록 후 activity(fragment) 종료
        Toast.makeText(getContext(), "알람이 등록되었습니다.", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @OnClick(R.id.cancel_button)
    public void onCancelButtonClicked() {
        Calendar c = Calendar.getInstance();
        mTimePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));

        for(int i=0; i<7; i++) {
            dayOfWeekButtons.get(i).setChecked(false);
        }

        mEditText.setText("");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout file
        View view = inflater.inflate(R.layout.fragment_alarm_add, container, false);

        // View binding
        ButterKnife.bind(this, view);

        // Get AlarmObserver
        mAlarmObserver = AlarmObserver.getAlarmObserver(getContext());

        return view;
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(Build.VERSION.SDK_INT >= 23) {
            mTimePicker.setHour(hourOfDay);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hourOfDay);
            mTimePicker.setCurrentMinute(minute);
        }
    }
}
