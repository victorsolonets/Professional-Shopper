package com.example.marcusedition.professionalshopper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by victor on 02.10.15.
 */
public class RecordActivity extends Activity {

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private ImageView goodsPhoto;
    private Button changeImage;
    private TextView fromDB;
    private EditText editGoodsName;
    private EditText editShopName;
    private EditText editGoodsPrice;
    private EditText editGoodsDescriprion;
    private RatingBar editGoodsRating;
    public static final int GALLERY_REQUEST = 1;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);
        goodsPhoto = (ImageView)findViewById(R.id.goods_photo);
        fromDB = (TextView)findViewById(R.id.fromDB);
        editGoodsName = (EditText)findViewById(R.id.goods_name);
        editGoodsPrice = (EditText)findViewById(R.id.goods_price);
        editShopName = (EditText)findViewById(R.id.shop_name);
        editGoodsDescriprion = (EditText)findViewById(R.id.short_description);
        editGoodsRating = (RatingBar)findViewById(R.id.goods_rating);
        changeImage = (Button)findViewById(R.id.change_photo_button);
        mDatabaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, 1);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();

        changeImage.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                WindowManager windowManager = (WindowManager) getApplicationContext()
                        .getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;
                ViewGroup.LayoutParams params = goodsPhoto.getLayoutParams();
                params.height = height / 3;
                params.width = width / 2;
                goodsPhoto.setLayoutParams(params);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                changeImage.setText("Змінити фото");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Bitmap galleryPic = null;
        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        galleryPic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        goodsPhoto.setImageBitmap(galleryPic);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
        }
    }
    public void onClickButton(View v) {
        System.out.println("in Click");

        ContentValues newValues = new ContentValues();

        // Задайте значения для каждого столбца

        String goodsNameFromEdit = String.valueOf(editGoodsName.getText());
        String shopNameFromEdit = String.valueOf(editShopName.getText());
        String goodsPriceFromEdit = String.valueOf(editGoodsPrice.getText());
        String goodsRatingFromEdit = String.valueOf(editGoodsRating.getRating());
        String goodsDescriptionFromEdit = String.valueOf(editGoodsDescriprion.getText());
        String goodsPhotoFromView = String.valueOf(goodsPhoto.toString());

        newValues.put(DatabaseHelper.GOODS_NAME_COLUMN, goodsNameFromEdit);
        newValues.put(DatabaseHelper.SHOP_NAME_COLUMN, shopNameFromEdit);
        newValues.put(DatabaseHelper.GOODS_PRICE_COLUMN, goodsPriceFromEdit);
        newValues.put(DatabaseHelper.GOODS_RATING_COLUMN, goodsRatingFromEdit);
        newValues.put(DatabaseHelper.GOODS_DESCRIPTION_COLUMN, goodsDescriptionFromEdit);
        newValues.put(DatabaseHelper.GOODS_PHOTO_COLUMN, goodsPhotoFromView);

        mSqLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE, null, newValues);

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), "Новий запис додано", duration);

        toast.show();

        editGoodsDescriprion.setText("");
        editGoodsPrice.setText("");
        editGoodsRating.setNumStars(0);
        editGoodsName.setText("");
        editShopName.setText("");
        goodsPhoto.setImageDrawable(getResources().getDrawable(R.drawable.photo));

//        Cursor cursor = mSqLiteDatabase.query("goods", new String[]{
//                        DatabaseHelper.GOODS_NAME_COLUMN,
//                        DatabaseHelper.SHOP_NAME_COLUMN,
//                        DatabaseHelper.GOODS_PRICE_COLUMN,
//                        DatabaseHelper.GOODS_RATING,
//                        DatabaseHelper.GOODS_DESCRIPTION_COLUMN},
//                null, null,
//                null, null, null) ;
//
//        cursor.moveToFirst();
//
//        String goodsName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_NAME_COLUMN));
//        String shopName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHOP_NAME_COLUMN));
//        String goodsDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GOODS_DESCRIPTION_COLUMN));
//        int goodsPrice = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GOODS_PRICE_COLUMN));
//        int goodsRating = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GOODS_RATING));
//        fromDB.setText("Товар : " + goodsName + "\nНазва магазину " + shopName
//                + "\nОпис товару : " + goodsDescription + "\nЦіна товару : " +
//                goodsPrice + "$" + "\nРейтинг товару : " + goodsRating);
//
//        // не забываем закрывать курсор
//        cursor.close();

    }


}
