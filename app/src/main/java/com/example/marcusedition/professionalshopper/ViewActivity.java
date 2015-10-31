package com.example.marcusedition.professionalshopper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by victor on 02.10.15.
 */

public class ViewActivity extends Activity {

    /**
     * Поля для роботи з БД
     */
    private SQLiteDatabase mSqLiteDatabase;
    private SQLiteOpenHelper mDatabaseHelper;
    /**
     * Поля для роботи з елементом списку
     */
    private ListView lvMain;
    private BoxAdapter boxAdapter;
    private Spinner mSpinner;
    private SearchView mSearchView;
    private Intent intent;
    private ByteArrayInputStream imageStream;
    private Drawable drawableImage;
    private ArrayList<Product> products;
    private TextView textInfo;

    /**
     * Метод створення activity
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);
        initializationLocalFields();
        fillAllItems();
        try {
            lvMain.isHovered();
            setFilters();
        } catch (Exception ex) {
        }

    }

    /**
     * Метод обробки перезавантаження activity
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        fillAllItems();
        try {
            lvMain.isHovered();
            setFilters();
        } catch (Exception ex) {}

    }

    /**
     * Ініціалізація полів та введення залежностей
     */
    private void initializationLocalFields() {
        mSpinner = (Spinner)findViewById(R.id.spinner);
        mDatabaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, 1);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        mSearchView = (SearchView) findViewById(R.id.searchView);
    }

    /**
     * Фільтр для сортування даних БД за вибраним порядком
     * @param order
     * @param desk
     * @return
     * @throws SQLException
     */
    public Cursor orderByFilter(String order, String desk) throws SQLException {
        String query = "SELECT " + DatabaseHelper._ID + "," +
                DatabaseHelper.GOODS_NAME_COLUMN + "," +DatabaseHelper.SHOP_NAME_COLUMN + ","
                + DatabaseHelper.GOODS_PRICE_COLUMN + "," + DatabaseHelper.GOODS_DESCRIPTION_COLUMN
                + "," +DatabaseHelper.GOODS_RATING_COLUMN + "," +DatabaseHelper.GOODS_PHOTO_COLUMN +
                "," + DatabaseHelper.GOODS_DATE +  " from " + DatabaseHelper.DATABASE_TABLE
                + " ORDER BY " + order +""+ desk +";";
        SQLiteDatabase mDB = mDatabaseHelper.getWritableDatabase();
        Cursor mCursor = mDB.rawQuery(query, null);
        if(mCursor != null) {
            products = new ArrayList<>();
            readFromDataBase(mCursor, products);
            boxAdapter = new BoxAdapter(this, products);
            lvMain.setAdapter(boxAdapter);
            lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lvMain.setItemsCanFocus(false);
            mCursor.close();
        }
        return mCursor;
    }

    /**
     * Фільтр для пошуку даних в БД по введеним даним SELECT ...
     * @param inputText
     * @return
     * @throws SQLException
     */
    public Cursor searchByInputText(String inputText) throws SQLException {
        String query = "SELECT " + DatabaseHelper._ID + "," +
                DatabaseHelper.GOODS_NAME_COLUMN + "," +DatabaseHelper.SHOP_NAME_COLUMN + ","
                + DatabaseHelper.GOODS_PRICE_COLUMN + "," + DatabaseHelper.GOODS_DESCRIPTION_COLUMN
                + "," +DatabaseHelper.GOODS_RATING_COLUMN + "," +DatabaseHelper.GOODS_PHOTO_COLUMN +
                "," + DatabaseHelper.GOODS_DATE +  " from " + DatabaseHelper.DATABASE_TABLE
                + " where " + DatabaseHelper.GOODS_NAME_COLUMN
                + " LIKE "+"\"" + inputText + "%\";";
        SQLiteDatabase mDB = mDatabaseHelper.getWritableDatabase();
        Cursor mCursor = mDB.rawQuery(query, null);
        if(mCursor != null) {
            products = new ArrayList<>();
            readFromDataBase(mCursor, products);
            boxAdapter = new BoxAdapter(this, products);
            lvMain.setAdapter(boxAdapter);
            mCursor.close();
        }
        return mCursor;
    }

    /**
     * Створення опціонального меню
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Установка конкретного фільтру, пошук по параметру який приходить від стрічки пошуку
     */
    private void setFilters() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    searchByInputText(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    searchByInputText(newText);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        /**
         * Метод для фільтрування елементів списку по конкретному
         * фільтру відповідно до її позиції
         */
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        try {
                            orderByFilter(DatabaseHelper.GOODS_PRICE_COLUMN, "");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 0:
                        try {
                            orderByFilter(DatabaseHelper.GOODS_DATE, "");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            orderByFilter(DatabaseHelper.GOODS_RATING_COLUMN, " DESC");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        try {
                            orderByFilter(DatabaseHelper.GOODS_NAME_COLUMN, "");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Заповнення всіх елементів списку зчитуючи з БД і вставляючи в список
     */
    private void fillAllItems() {
        Cursor cursor = getCursor();
        if (isDataBaseEmpty(cursor)) return;
        products = new ArrayList<>();
        readFromDataBase(cursor, products);
        boxAdapter = new BoxAdapter(this, products);
        lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(boxAdapter);
        cursor.close();
    }

    /**
     * Перевірка на пусту БД
     * @param cursor
     * @return
     */
    private boolean isDataBaseEmpty(Cursor cursor) {
        if (cursor.getCount() == (0) || cursor.toString().equals("null")) {
            Toast.makeText(getBaseContext(), "База даних пуста. Зробіть будь ласка хоча б один запис.",
                    Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), RecordActivity.class));
            onStop();
            return true;
        }
        return false;
    }

    /**
     * Метод для отримання курсору , який необхідний для обходу по БД
     * @return
     */
    private Cursor getCursor() {
        return mSqLiteDatabase.query(DatabaseHelper.DATABASE_TABLE, new String[]{
                            DatabaseHelper._ID,
                            DatabaseHelper.GOODS_NAME_COLUMN,
                            DatabaseHelper.SHOP_NAME_COLUMN,
                            DatabaseHelper.GOODS_PRICE_COLUMN,
                            DatabaseHelper.GOODS_RATING_COLUMN,
                            DatabaseHelper.GOODS_DESCRIPTION_COLUMN,
                            DatabaseHelper.GOODS_PHOTO_COLUMN,
                            DatabaseHelper.GOODS_DATE },
                    null, null,
                    null, null, null);
    }

    /**
     * Метод для зчитування з БД
     * @param cursor
     * @param products
     */
    private void readFromDataBase(final Cursor cursor, ArrayList<Product> products) {
        while (cursor.moveToNext()) {
            String goodsName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_NAME_COLUMN));
            String shopName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHOP_NAME_COLUMN));
            String goodsDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_DESCRIPTION_COLUMN));
            Float goodsPrice = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.GOODS_PRICE_COLUMN));
            Float  goodsRating = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.GOODS_RATING_COLUMN));
            byte[] photo = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.GOODS_PHOTO_COLUMN));
            String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_DATE));
            imageStream = new ByteArrayInputStream(photo);
            drawableImage = Drawable.createFromStream(imageStream, "");
            products.add(new Product(goodsName, goodsDescription, goodsPrice, drawableImage, goodsRating, shopName, date));
        }
    }
}

