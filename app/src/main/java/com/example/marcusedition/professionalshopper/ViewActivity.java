package com.example.marcusedition.professionalshopper;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by victor on 02.10.15.
 */
public class ViewActivity extends Activity{

    private SQLiteDatabase mSqLiteDatabase;
    private ListView lvMain;
    private SQLiteOpenHelper mDatabaseHelper;
    private ArrayList<String> str;
    private ImageView imageView;
    private BoxAdapter boxAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);
        mDatabaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, 1);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.query(DatabaseHelper.DATABASE_TABLE, new String[]{
                        DatabaseHelper.GOODS_NAME_COLUMN,
                        DatabaseHelper.SHOP_NAME_COLUMN,
                        DatabaseHelper.GOODS_PRICE_COLUMN,
                        DatabaseHelper.GOODS_RATING_COLUMN,
                        DatabaseHelper.GOODS_DESCRIPTION_COLUMN,
                        DatabaseHelper.GOODS_PHOTO_COLUMN},
                null, null,
                null, null, null);
        ArrayList<Product> products = new ArrayList<>();
        while (cursor.moveToNext()) {
            String goodsName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_NAME_COLUMN));
            String shopName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHOP_NAME_COLUMN));
            String goodsDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_DESCRIPTION_COLUMN));
            int goodsPrice = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GOODS_PRICE_COLUMN));
            int goodsRating = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GOODS_RATING_COLUMN));
            byte[] photo = cursor.getBlob(5);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            Drawable drawableImage = new BitmapDrawable(getResources(), theImage);
            products.add(new Product(goodsName,goodsDescription,goodsPrice,drawableImage,goodsRating));
            // настраиваем список
            lvMain = (ListView) findViewById(R.id.lvMain);
//            lvMain.setAdapter(boxAdapter);
            System.out.println(str);
        }
        boxAdapter = new BoxAdapter(this, products);
        lvMain.setAdapter(boxAdapter);
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(boxAdapter);
//        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str);
//        lvMain.setAdapter(adapter);
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

}

//ArrayList<Product> products = new ArrayList<Product>();
//BoxAdapter boxAdapter;
//
//    /** Called when the activity is first created. */
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        // создаем адаптер
//        fillData();
//        boxAdapter = new BoxAdapter(this, products);
//
//        // настраиваем список
//        ListView lvMain = (ListView) findViewById(R.id.lvMain);
//        lvMain.setAdapter(boxAdapter);
//    }
//
//    // генерируем данные для адаптера
//    void fillData() {
//        for (int i = 1; i <= 20; i++) {
//            products.add(new Product("Product " + i, i * 1000,
//                    R.drawable.ic_launcher, false));
//        }
//    }
//
//    // выводим информацию о корзине
//    public void showResult(View v) {
//        String result = "Товары в корзине:";
//        for (Product p : boxAdapter.getBox()) {
//            if (p.box)
//                result += "\n" + p.name;
//        }
//        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//    }
//}
