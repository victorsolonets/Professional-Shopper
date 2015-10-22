package com.example.marcusedition.professionalshopper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by victor on 02.10.15.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    /**
     * Описані дані щодо створення бази даних : назва, версія, таблиця
     */
    public static final String DATABASE_NAME = "eeshop.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "goods";

    /**
     * Описані назви колонок в таблиці : назва товару, назва магазину, ціна, опис, рейтинг, фото, дата
     */
    public static final String GOODS_NAME_COLUMN = "goodsName";
    public static final String SHOP_NAME_COLUMN = "shopName";
    public static final String GOODS_PRICE_COLUMN = "goodsPrice";
    public static final String GOODS_DESCRIPTION_COLUMN = "goodsDescription";
    public static final String GOODS_RATING_COLUMN = "goodsRating";
    public static final String GOODS_PHOTO_COLUMN = "goodsPhoto";
    public static final String GOODS_DATE = "goodsDate";

    /**
     * Змінна для створення бази данних
     */
    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + GOODS_NAME_COLUMN
            + " text not null, " + SHOP_NAME_COLUMN + " text not null, "
            + GOODS_PRICE_COLUMN + " real, " + GOODS_RATING_COLUMN + " real, "
            + GOODS_DESCRIPTION_COLUMN + " text not null, "
            + GOODS_PHOTO_COLUMN + " BLOB, " + GOODS_DATE + " text not null);";

    /**
     * Конструктор для створення екземпляру DatabaseHelper
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Конструктор для створення екземпляру DatabaseHelper
     * @param context
     * @param name
     * @param factory
     * @param version
     */

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    /**
     * Метод для створення бази даних
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);

    }

    /**
     * Метод для оновлення бази даних, якщо змінюється версія,
     * то БД видаляється і створються нова зі старими даними
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);
        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        // Создаём новую таблицу
        onCreate(db);
    }

}
