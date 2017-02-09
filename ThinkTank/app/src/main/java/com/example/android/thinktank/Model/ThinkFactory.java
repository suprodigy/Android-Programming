package com.example.android.thinktank.Model;

import android.util.Log;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

/**
 * Created by jr on 2017-02-09.
 */

public class ThinkFactory {

    private static final String TAG = "ThinkFactory";

    private static ThinkFactory sThinkFactory;

    private Realm mRealm;
    private OrderedRealmCollection<ThinkItem> mItems;

    public static ThinkFactory get() {
        if (sThinkFactory == null) {
            sThinkFactory = new ThinkFactory();
        }
        return sThinkFactory;
    }

    private ThinkFactory() {
        mRealm = Realm.getDefaultInstance();
    }

    public void insert(final ThinkItem item) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObject(ThinkItem.class, item.getId())
                        .setContent(item.getContent())
                        .setKeywords(item.getKeywords());
            }
        });
    }

    public void update(final ThinkItem thinkItem) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(thinkItem);
            }
        });
    }

    public void delete(final ThinkItem item) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(ThinkItem.class)
                        .equalTo("id", item.getId())
                        .findAll()
                        .deleteAllFromRealm();
                Log.d(TAG, "" + item.getId());
            }
        }, new Realm.Transaction.OnSuccess(){
            @Override
            public void onSuccess() {
                Log.d(TAG, "성공!");
            }
        }, new Realm.Transaction.OnError(){
            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "실패!");
            }
        });
    }

    public OrderedRealmCollection<ThinkItem> selectAll() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mItems = realm.where(ThinkItem.class).findAll().sort("id");
            }
        });
        return mItems;
    }

}
