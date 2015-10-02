package com.example.marcusedition.professionalshopper;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by victor on 02.10.15.
 */
public class ViewActivity extends Activity{

    private SQLiteDatabase mSqLiteDatabase;
    private ListView goodsView;
    private SQLiteOpenHelper mDatabaseHelper;
    private ArrayList<String> str;
    private ImageView[] mGoodsImage;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);
        goodsView = (ListView)findViewById(R.id.listView);
        imageView = (ImageView)findViewById(R.id.imageView);
        mDatabaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, 1);
        str = new ArrayList<>();
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.query(DatabaseHelper.DATABASE_TABLE, new String[]{
                        DatabaseHelper.GOODS_NAME_COLUMN,
                        DatabaseHelper.SHOP_NAME_COLUMN,
                        DatabaseHelper.GOODS_PRICE_COLUMN,
                        DatabaseHelper.GOODS_RATING_COLUMN,
                        DatabaseHelper.GOODS_DESCRIPTION_COLUMN,
                        DatabaseHelper.GOODS_PHOTO_COLUMN},
                null, null,
                null, null, null) ;

        while (cursor.moveToNext()) {

            String goodsName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_NAME_COLUMN));
            String shopName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHOP_NAME_COLUMN));
            String goodsDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_DESCRIPTION_COLUMN));
            int goodsPrice = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GOODS_PRICE_COLUMN));
            int goodsRating = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GOODS_RATING_COLUMN));
            byte[] photo = cursor.getBlob(5);

            str.add(new String("Товар : " + goodsName + "\nНазва магазину : " + shopName
                    + "\nОпис товару : " + goodsDescription + "\nЦіна товару : " +
                    goodsPrice + " грн" + "\nРейтинг товару : " + goodsRating + "\n"));

            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(theImage);
            System.out.println(theImage);
            System.out.println(str);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, str);

        goodsView.setAdapter(adapter);
//         не забываем закрывать курсор
        cursor.close();
    }

}
