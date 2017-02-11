package com.example.android.thinktank.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jr on 2017-02-09.
 */

public class KeywordItem extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;

    // 다른 Keyword와의 관계를 나타내는 필드 추가

    public KeywordItem() {
        this(UUID.randomUUID());
    }

    private KeywordItem(UUID id) {
        this.id = id.toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public KeywordItem setName(String name) {
        this.name = name;
        return this;
    }

}
