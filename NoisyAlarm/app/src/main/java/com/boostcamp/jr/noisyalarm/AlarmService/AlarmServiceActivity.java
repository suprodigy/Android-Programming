package com.boostcamp.jr.noisyalarm.AlarmService;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.boostcamp.jr.noisyalarm.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jr on 2017-01-27.
 */

public class AlarmServiceActivity extends Activity {

    private static final int ALARM_NOTIFY_ID = 123;

    private AlarmAsyncTask mAlarmAsyncTack;

    private MediaPlayer mPlayer;
    private Vibrator mVibrator;
    private int mCurrentVolume;

    @OnClick(R.id.pause_button)
    public void onPauseButtonClicked() {
        // Stop playing sound and vibrating
        mPlayer.stop();
        mVibrator.cancel();
        onBackPressed();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);

        // 현재 위치 얻기
        mAlarmAsyncTack = new AlarmAsyncTask(this);
        mAlarmAsyncTack.execute();

        // Play Sound and vibrate;
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        mCurrentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);

        mPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        for(int i=0; i<10; i++) {
            mVibrator.vibrate(1000);
        }
        mPlayer.start();
    }

    @Override
    protected void onStop() {
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolume, 0);
        super.onStop();
    }

    // 위치 정보를 얻어서 Notification 에 세팅
    public class AlarmAsyncTask extends AsyncTask<Void, Void, String> {

        Context mContext;

        public AlarmAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String location = getLocation();
            return location;
        }

        @Override
        protected void onPostExecute(String location) {
            super.onPostExecute(location);
            notifyAlarm(location);
        }

    }

    // 단순히 텍스트값만 입력해서 notify
    private void notifyAlarm(String location) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setSmallIcon(R.drawable.alarm_icon)
                .setContentTitle(this.getString(R.string.alarm_notify_title))
                .setContentText(location)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(ALARM_NOTIFY_ID, notificationBuilder.build());
    }

    // LocationManager를 이용해 현재 주소 얻기
    public String getLocation() {
        String currentLocation = "주소 정보를 찾을 수 없습니다.";

        // Permission이 허용되어 있지 않으면 그냥 반환
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "주소를 얻기 위해서는 권한 설정을 해야 합니다.";
        }

        try {
            LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            // GPS, NETWORK 활성화 여부 확인
            boolean isGPSEnabled = locationManager.
                    isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.
                    isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                return "GPS, NETWORK 모두 비활성화 되어있습니다.";
            } else {
                if (isNetworkEnabled && !isGPSEnabled) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    currentLocation = getAddress(this, location.getLatitude(), location.getLongitude());
                } else if (isGPSEnabled) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    currentLocation = getAddress(this, location.getLatitude(), location.getLongitude());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentLocation;
    }

    // 위도, 경도 정보로 주소 반환
    public static String getAddress(Context mContext, double lat, double lng) {

        String currentLocation ="주소를 가져 올 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 하나의 위도, 경도로부터 여러개의 주소가 나올 수 있음.
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    currentLocation = address.get(0).getAddressLine(0);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentLocation;
    }


}
