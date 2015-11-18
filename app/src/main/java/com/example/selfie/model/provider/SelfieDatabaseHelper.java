package com.example.selfie.model.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class SelfieDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "selfieassignment.db";

    private static final int DATABASE_VERSION = 4;

    public SelfieDatabaseHelper(Context context) {
        super(context, context.getCacheDir() + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Define an SQL string that creates a table to hold Acronyms.
        // Each Acronym has a list of LongForms in Json.
        final String SQL_CREATE_ACRONYM_TABLE = "CREATE TABLE "
                + SelfieProviderContract.SelfieTable.TABLE_NAME + " ("
                + SelfieProviderContract.SelfieTable._ID + " INTEGER PRIMARY KEY, "
                + SelfieProviderContract.SelfieTable.COLUMN_TITLE + " TEXT NOT NULL, "
                + SelfieProviderContract.SelfieTable.COLUMN_CONTENT_TYPE + " TEXT NOT NULL, "
                + SelfieProviderContract.SelfieTable.COLUMN_DATA_URL + " TEXT NOT NULL, "
                + SelfieProviderContract.SelfieTable.COLUMN_DURATION + " INTEGER NOT NULL, "
                + SelfieProviderContract.SelfieTable.COLUMN_STAR_RATING + " REAL NOT NULL, "
                + SelfieProviderContract.SelfieTable.COLUMN_LOCAL_URL + " TEXT NOT NULL "
                + " );";

        // Create the table.
        db.execSQL(SQL_CREATE_ACRONYM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + SelfieProviderContract.SelfieTable.TABLE_NAME);
        onCreate(db);
    }
}
