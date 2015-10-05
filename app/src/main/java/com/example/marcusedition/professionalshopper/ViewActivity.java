package com.example.marcusedition.professionalshopper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by victor on 02.10.15.
 */
public class ViewActivity extends Activity{

    private SQLiteDatabase mSqLiteDatabase;
    private ListView lvMain;
    private SQLiteOpenHelper mDatabaseHelper;
    private BoxAdapter boxAdapter;
    private Spinner mSpinner;
    private SearchView mSearchView;
    private Intent intent;
    private ByteArrayInputStream imageStream;
    private Drawable drawableImage;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);
        mSpinner = (Spinner)findViewById(R.id.spinner);
        mSearchView = (SearchView) findViewById(R.id.searchView);

        mDatabaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, 1);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();

        //*** setOnQueryTextFocusChangeListener ***
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

//                Toast.makeText(getBaseContext(), String.valueOf(hasFocus),
//                        Toast.LENGTH_SHORT).show();
            }
        });

        //*** setOnQueryTextListener ***
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                Toast.makeText(getBaseContext(), query,
                        Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

                	Toast.makeText(getBaseContext(), newText,
                Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        readFromDataBase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.view_activity);
        mSpinner = (Spinner)findViewById(R.id.spinner);
        readFromDataBase();
    }

    private void readFromDataBase() {

        Cursor cursor = mSqLiteDatabase.query(DatabaseHelper.DATABASE_TABLE, new String[]{
                        DatabaseHelper.GOODS_NAME_COLUMN,
                        DatabaseHelper.SHOP_NAME_COLUMN,
                        DatabaseHelper.GOODS_PRICE_COLUMN,
                        DatabaseHelper.GOODS_RATING_COLUMN,
                        DatabaseHelper.GOODS_DESCRIPTION_COLUMN,
                        DatabaseHelper.GOODS_PHOTO_COLUMN},
                null, null,
                null, null, null);
        System.out.println(cursor.getPosition());
        System.out.println(cursor.getCount());
        System.out.println(cursor.getPosition() == (-1));
        System.out.println(cursor.getCount() == (0));
        if (cursor.getCount() == (0)) {
            Toast.makeText(getBaseContext(), "База даних пуста", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), RecordActivity.class));
            onStop();
            return;
        }
        ArrayList<Product> products = new ArrayList<>();
        while (cursor.moveToNext()) {
            String goodsName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_NAME_COLUMN));
            String shopName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHOP_NAME_COLUMN));
            String goodsDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_DESCRIPTION_COLUMN));
            String goodsPrice = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_PRICE_COLUMN));
            String  goodsRating = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_RATING_COLUMN));
            byte[] photo = cursor.getBlob(5);
            imageStream = new ByteArrayInputStream(photo);
            System.out.println(cursor.getPosition());
            drawableImage = Drawable.createFromStream(imageStream,"");
            products.add(new Product(goodsName, goodsDescription, goodsPrice, drawableImage, goodsRating));
            // настраиваем список
            lvMain = (ListView) findViewById(R.id.lvMain);
        }
        boxAdapter = new BoxAdapter(this, products);
        lvMain.setAdapter(boxAdapter);
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(boxAdapter);
//         не забываем закрывать курсор
        cursor.close();
    }

    private Cursor getCursor() {
        mDatabaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, 1);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        return mSqLiteDatabase.query(DatabaseHelper.DATABASE_TABLE, new String[]{
                        DatabaseHelper.GOODS_NAME_COLUMN,
                        DatabaseHelper.SHOP_NAME_COLUMN,
                        DatabaseHelper.GOODS_PRICE_COLUMN,
                        DatabaseHelper.GOODS_RATING_COLUMN,
                        DatabaseHelper.GOODS_DESCRIPTION_COLUMN,
                        DatabaseHelper.GOODS_PHOTO_COLUMN},
                null, null,
                null, null, null);
    }

    public void onClickButton(View view){
        if (view.getId() == R.id.but_record) {
            intent = new Intent(getApplicationContext(), RecordActivity.class);
//            onDestroy();
            startActivity(intent);
        } if (view.getId() == R.id.lvMain) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
//            onDestroy();
            startActivity(intent);
        }

    }

}

