package com.example.selfie.model.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public final class SelfieProviderContract {

    public static final String CONTENT_AUTORITHY = "coursera.assignment3client.selfieUploadDownload";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTORITHY);

    public static final String PATH_SELFIE = SelfieTable.TABLE_NAME;

    public static final class SelfieTable implements BaseColumns {

        public static final String TABLE_NAME = "Selfie";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_ITEMS_TYPE = "vnd.android.cursor.dir/"
                + CONTENT_AUTORITHY + "/" + TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/"
                + CONTENT_AUTORITHY + "/" + TABLE_NAME;

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_CONTENT_TYPE = "content_type";
        public static final String COLUMN_DATA_URL = "data_url";
        public static final String COLUMN_STAR_RATING = "star_rating";
        public static final String COLUMN_LOCAL_URL = "local_url";

        public static Uri buildSelfieUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
