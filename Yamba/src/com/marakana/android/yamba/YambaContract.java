package com.marakana.android.yamba;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public class YambaContract {
    private YambaContract() { }

    public static final int VERSION = 1;

    // timeline update notifications
    public static final String TIMELINE_UPDATE_PERMISSION
        = "com.marakana.android.yamba.timeline.PERMISSION_READ";
    public static final String TIMELINE_UPDATE_BROADCAST
        = "com.marakana.android.yamba.action.NEW_STATUS";
    public static final String TIMELINE_UPDATE_COUNT
        = "com.marakana.android.yamba.action.NEW_STATUS_COUNT";

    public static final String AUTHORITY = "com.marakana.android.yamba.timeline";

    public static final Uri BASE_URI = new Uri.Builder()
        .scheme(ContentResolver.SCHEME_CONTENT)
        .authority(AUTHORITY)
        .build();

    private static final String MINOR_TYPE = "/vnd." + AUTHORITY;

    public static final String ITEM_TYPE
        = ContentResolver.CURSOR_ITEM_BASE_TYPE + MINOR_TYPE;
    public static final String DIR_TYPE
        = ContentResolver.CURSOR_DIR_BASE_TYPE + MINOR_TYPE;

    public static class Timeline {
        private Timeline() { }

        public static final String TABLE = "timeline";

        public static final Uri URI = BASE_URI.buildUpon().appendPath(TABLE).build();

        public static class Columns {
            public static final String ID = BaseColumns._ID;
            public static final String USER = "user";
            public static final String STATUS = "status";
            public static final String CREATED_AT = "created_at";

            public static final String MAX_TIMESTAMP = "maxts";
        }
    }
}
