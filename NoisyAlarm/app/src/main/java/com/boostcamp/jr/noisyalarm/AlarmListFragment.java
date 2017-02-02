package com.boostcamp.jr.noisyalarm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boostcamp.jr.noisyalarm.Model.Alarm;
import com.boostcamp.jr.noisyalarm.Model.AlarmObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by jr on 2017-01-24.
 */

// COMPLETED (2) CursorLoader 구현
public class AlarmListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ALARM_LOADER_ID = 1;

    @BindView(R.id.alarm_recycler_view)
    RecyclerView mAlarmRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    AlarmAdapter mAdapter;
    AlarmObserver mAlarmObserver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_list, container, false);
        ButterKnife.bind(this, view);
        mAlarmRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AlarmAdapter();
        mAlarmRecyclerView.setAdapter(mAdapter);

        // swipe event 등록: 이벤트 발생 시 해당 position의 Alarm Data 삭제
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // ViewHolder의 TextView 색깔 초기화
                AlarmHolder alarmHolder = (AlarmHolder) viewHolder;
                List<TextView> textViews = alarmHolder.dayOfWeekTextViews;
                int color = getResources().getColor(R.color.colorGray);
                for(TextView textView : textViews) {
                    textView.setTextColor(color);
                }

                // 다시 Loader 스타트
                if(mAlarmObserver.deleteAlarm(mAlarmObserver.getAlarm(position)) > 0){
                    getActivity().getSupportLoaderManager().restartLoader(ALARM_LOADER_ID, null, AlarmListFragment.this);
                }
            }
        }).attachToRecyclerView(mAlarmRecyclerView);

        mAlarmObserver = AlarmObserver.getAlarmObserver(getContext());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Permission이 허용되어 있지 않으면 위치 권한 요청
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        getActivity().getSupportLoaderManager().initLoader(ALARM_LOADER_ID, null, AlarmListFragment.this);
    }

    @Override
    public void onResume() {
        getActivity().getSupportLoaderManager().restartLoader(ALARM_LOADER_ID, null, AlarmListFragment.this);
        super.onResume();
    }

    /* DB로부터 Query하는 작업을 정의 */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getContext()) {
            private Cursor mCursor;

            @Override
            protected void onStartLoading() {
                setLoadingView();
                if(mCursor == null || mCursor.getCount() == 0) {
                    forceLoad();
                } else {
                    deliverResult(mCursor);
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return mAlarmObserver.getAllAlarms();
                } catch(Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                mCursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        endLoadingView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    /**
     *  RecyclerView 구현에 필요한 Adapter와 ViewHolder
     */

    private class AlarmAdapter extends RecyclerView.Adapter<AlarmHolder> {

        Cursor mCursor;

        private AlarmAdapter() {}

        @Override
        public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_alarm_list, parent, false);
            return new AlarmHolder(view);
        }

        @Override
        public void onBindViewHolder(AlarmHolder holder, int position) {
            Alarm alarm = mAlarmObserver.getAlarm(position);
            holder.bindAlarm(alarm);
        }

        @Override
        public int getItemCount() {
            return (mCursor == null) ? 0 : mCursor.getCount();
        }

        public void swapCursor(Cursor cursor) {
            mCursor = cursor;
            notifyDataSetChanged();
        }

    }

    public class AlarmHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,
            CompoundButton.OnCheckedChangeListener {

        Alarm mAlarm;

        @BindView(R.id.alarm_on_off_icon)
        ToggleButton alarm_on_off_icon;

        @BindView(R.id.alarm_title)
        TextView alarm_title;

        @BindView(R.id.alarm_content)
        TextView alarm_content;

        @BindViews({R.id.sunday, R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday})
        List<TextView> dayOfWeekTextViews;

        public AlarmHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
            alarm_on_off_icon.setOnCheckedChangeListener(this);
        }

        // Adapter로부터 Alarm 객체를 받아서 View에 Binding
        public void bindAlarm(Alarm alarm) {
            mAlarm = alarm;

            // Icon setting
            alarm_on_off_icon.setChecked(alarm.isSet());

            // Text Setting
            boolean isPM = false;
            int hour = alarm.getHour();
            if(hour >= 12) {
                hour = hour - 24 + 12;
                isPM = true;
            }
            String AM_OR_PM = isPM ? "PM" : "AM";

            alarm_title.setText(String.format("%02d", hour) + ":" + String.format("%02d", alarm.getMin()) +
                    "(" + AM_OR_PM + ")");

            String content = alarm.getContent();
            if(content.length() == 0) {
                alarm_content.setText("알람 메시지가 없습니다.");
                int color = getResources().getColor(R.color.colorGray);
                alarm_content.setTextColor(color);
            } else {
                alarm_content.setText(content);
            }

            int dayOfWeek = alarm.getDayOfWeek();
            for(int i=0; i<7; i++) {
                if(dayOfWeek % 2 == 1) {
                    int color = getResources().getColor(R.color.colorAccent);
                    dayOfWeekTextViews.get(i).setTextColor(color);
                } else {
                    int color = getResources().getColor(R.color.colorGray);
                    dayOfWeekTextViews.get(i).setTextColor(color);
                }
                dayOfWeek /= 2;
            }
        }

        /* 클릭하면 Detail Alarm 화면으로 이동 */
        @Override
        public void onClick(View v) {
            Intent intent = AlarmDetailActivity.newIntent(getActivity(), getAdapterPosition());
            startActivity(intent);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mAlarm.setSet(isChecked);
            mAlarmObserver.updateAlarm(mAlarm);
        }
    }

    public void setLoadingView() {
        mAlarmRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void endLoadingView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mAlarmRecyclerView.setVisibility(View.VISIBLE);
    }

}
