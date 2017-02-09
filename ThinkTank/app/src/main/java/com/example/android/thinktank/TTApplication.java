package com.example.android.thinktank;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jr on 2017-02-09.
 */

public class TTApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        // Realm.deleteRealm(realmConfig);
        Realm.setDefaultConfiguration(realmConfig);
    }

}
