package com.boostcamp.jr.noisyalarm;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

public class AlarmListActivity extends SingleFragmentActivity {

    /**
     * Main 화면인 AlarmListActivity에는 알람을 추가할 수 있는
     * 메뉴 버튼 만듦.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.add_alarm_menu) {
            Intent intent = new Intent(getApplicationContext(), AlarmAddActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    protected Fragment createFragment() {
        return new AlarmListFragment();
    }
}
