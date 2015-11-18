package com.example.selfie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.selfie.model.mediator.webdata.Selfie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 10/11/15.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "imagedb";
    private static final String TABLE_IMAGES = "images";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                + KEY_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);

        onCreate(db);
    }

    public void addSelfie(Selfie selfie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put(KEY_NAME, selfie.getName());
        //cv.put(KEY_IMAGE, selfie.getImage());

        db.insert(TABLE_IMAGES, null, cv);
        db.close();
    }

    public List<Selfie> getAllSelfies() {
        List<Selfie> list = new ArrayList<Selfie>();
        String selectQuery = "SELECT * FROM images ORDER BY name";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Selfie selfie = new Selfie();
                //selfie.setID(Integer.parseInt(cursor.getString(0)));
                //selfie.setName(cursor.getString(1));
                //selfie.setImage(cursor.getBlob(2));
                list.add(selfie);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;

    }
}
