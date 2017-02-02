package com.boostcamp.jr.noisyalarm.Model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jr on 2017-01-25.
 * DB에 들어가는 각종 이름이 저장된 클래스
 */

public class AlarmContract {

    public static final String AUTHORITY = "com.boostcamp.jr.noisyalarm";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_ALARMS = "alarms";

    public static final class AlarmEntry implements BaseColumns {

        // Content Provider에 요청할 CONTENT URI 정의
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ALARMS).build();

        public static final String TABLE_NAME = "alarms";

        // URI의 MIME TYPE 정의
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_ALARMS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_ALARMS;

        // Name of columns
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_MINUTE = "minute";
        public static final String COLUMN_DAY_OF_WEEK = "day_of_week";
        public static final String COLUMN_IS_SET = "is_set";
        public static final String COLUMN_CONTENT = "content";

    }

}
