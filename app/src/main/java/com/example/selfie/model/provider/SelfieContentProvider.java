package com.example.selfie.model.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class SelfieContentProvider extends ContentProvider {

    private SelfieDatabaseHelper selfieHelper;

    private static final int MULTIPLE_SELFIE = 100;

    private static final int SINGLE_SELFIE = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        selfieHelper = new SelfieDatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MULTIPLE_SELFIE:
                return SelfieProviderContract.SelfieTable.CONTENT_ITEMS_TYPE;
            case SINGLE_SELFIE:
                return SelfieProviderContract.SelfieTable.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] args, String order) {
        Cursor result = null;

        switch (sUriMatcher.match(uri)) {
            case MULTIPLE_SELFIE:
                result = selfieHelper.getReadableDatabase().query(
                        SelfieProviderContract.SelfieTable.TABLE_NAME,
                        projection, selection, args, null, null, order);
                break;

            case SINGLE_SELFIE:
                final long rowId = ContentUris.parseId(uri);
                result = selfieHelper.getReadableDatabase().query(
                        SelfieProviderContract.SelfieTable.TABLE_NAME,
                        projection, SelfieProviderContract.SelfieTable._ID + "=?",
                        new String[]{String.valueOf(rowId)}, null, null, order);
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = selfieHelper.getWritableDatabase();

        Uri result = null;

        switch (sUriMatcher.match(uri)) {
            case MULTIPLE_SELFIE:
                long id = db.insert(SelfieProviderContract.SelfieTable.TABLE_NAME, null, contentValues);

                if (id > 0) {
                    result = SelfieProviderContract.SelfieTable.buildSelfieUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert the selfie row into " + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(result, null);

        return result;
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] args) {
        SQLiteDatabase db = selfieHelper.getWritableDatabase();

        int deletedRows = 0;

        switch (sUriMatcher.match(uri)) {
            case MULTIPLE_SELFIE:
                deletedRows = db.delete(SelfieProviderContract.SelfieTable.TABLE_NAME, whereClause, args);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = selfieHelper.getWritableDatabase();

        int updatedRows = 0;

        switch (sUriMatcher.match(uri)) {
            case MULTIPLE_SELFIE:
                updatedRows = db.update(SelfieProviderContract.SelfieTable.TABLE_NAME, contentValues, whereClause, whereArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (updatedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRows;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(SelfieProviderContract.CONTENT_AUTORITHY,
                SelfieProviderContract.PATH_SELFIE, MULTIPLE_SELFIE);

        matcher.addURI(SelfieProviderContract.CONTENT_AUTORITHY,
                SelfieProviderContract.PATH_SELFIE + "/#", MULTIPLE_SELFIE);

        return matcher;
    }
}
