package com.boostcamp.jr.noisyalarm;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.boostcamp.jr.noisyalarm.Model.Alarm;
import com.boostcamp.jr.noisyalarm.Model.AlarmObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jr on 2017-01-24.
 */

public class AlarmDetailFragment extends Fragment
        implements TimePickerDialog.OnTimeSetListener{

    private static final String ARG_ALARM_POSITION = "alarm_position";

    private Alarm mAlarm;
    private AlarmObserver mAlarmObserver;

    // 필요한 정보를 받기 위해 Fragment 만들 때 argument 넣은 후 만들도록 강제함.
    public static AlarmDetailFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_ALARM_POSITION, position);
        AlarmDetailFragment fragment = new AlarmDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.timepicker)
    TimePicker mTimePicker;

    @BindViews({R.id.sunday, R.id.monday, R.id.tuesday, R.id.wednesday,
            R.id.thursday, R.id.friday, R.id.saturday})
    List<ToggleButton> dayOfWeekButtons;

    @BindView(R.id.alarm_content_edit_text)
    EditText mEditText;

    @OnClick(R.id.update_button)
    public void onUpdateButtonClicked() {

        int hour, minute;
        if(Build.VERSION.SDK_INT >= 23) {
            hour = mTimePicker.getHour();
            minute = mTimePicker.getMinute();
        } else {
            hour = mTimePicker.getCurrentHour();
            minute = mTimePicker.getCurrentMinute();
        }

        Log.d("", "시간 설정 = " + hour + ":" + minute);

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
                .setId(mAlarm.getId())
                .setHour(hour)
                .setMin(minute)
                .setDayOfWeek(dayOfWeek)
                .setSet(true)
                .setContent(content);
        mAlarmObserver.updateAlarm(alarm);
        Toast.makeText(getContext(), "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @OnClick(R.id.delete_button)
    public void onDeleteButtonClicked() {
        mAlarmObserver.deleteAlarm(mAlarm);
        Toast.makeText(getContext(), "알람이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout file
        View view = inflater.inflate(R.layout.fragment_alarm_detail, container, false);
        // view binding
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlarmObserver = AlarmObserver.getAlarmObserver(getContext());
        int position = getArguments().getInt(ARG_ALARM_POSITION);
        mAlarm = mAlarmObserver.getAlarm(position);
    }

    private void initView() {
        int hour = mAlarm.getHour();
        int minute = mAlarm.getMin();

        if(Build.VERSION.SDK_INT >= 23) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }

        int dayOfWeek = mAlarm.getDayOfWeek();
        for(int i=0; i<7; i++) {
            if(dayOfWeek % 2 == 1) {
                dayOfWeekButtons.get(i).setChecked(true);
            }
            dayOfWeek /= 2;
        }

        if(mAlarm.getContent().length() != 0) {
            mEditText.setText(mAlarm.getContent());
        }
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
