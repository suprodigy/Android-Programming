package com.example.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by jr on 2017-01-24.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

}
