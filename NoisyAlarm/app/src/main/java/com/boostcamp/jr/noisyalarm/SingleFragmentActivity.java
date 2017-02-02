package com.boostcamp.jr.noisyalarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jr on 2017-01-24.
 * 하나의 프래그먼트를 호스팅하는 액티비티가 공통으로 사용하는 코드를 추상 클래스로 구현
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.list_fragment_container);

        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.list_fragment_container, fragment)
                    .commit();
        }
    }
}
