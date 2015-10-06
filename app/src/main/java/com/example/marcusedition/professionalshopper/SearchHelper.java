package com.example.marcusedition.professionalshopper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by victor on 06.10.15.
 */
public class SearchHelper {
    public static final String COLUMN_NAME = "name";

    private static final String TAG = "SearchHelper";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = DatabaseHelper.DATABASE_NAME;
    private static final String FTS_VIRTUAL_TABLE = "Info";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE + " USING fts3("
            + COLUMN_NAME
            + " UNIQUE (" + COLUMN_NAME + "));";
    private final Context context;

    public SearchHelper(Context ctx) {
        this.context = ctx;
    }

    public SearchHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    public long createList(String name) {

        ContentValues initialValues = new ContentValues();


        initialValues.put(COLUMN_NAME, name);

        return mDb.insert(FTS_VIRTUAL_TABLE, null, initialValues);

    }


    public Cursor searchByInputText(String inputText) throws SQLException {

        String query = "SELECT docid as _id," +
                COLUMN_NAME +  " from " + FTS_VIRTUAL_TABLE +
                " where " + COLUMN_NAME + " MATCH '" + inputText + "';";

        Cursor mCursor = mDb.rawQuery(query,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


    public boolean deleteAllNames() {

        int doneDelete = mDb.delete(FTS_VIRTUAL_TABLE, null , null);
        return doneDelete > 0;
    }
}
