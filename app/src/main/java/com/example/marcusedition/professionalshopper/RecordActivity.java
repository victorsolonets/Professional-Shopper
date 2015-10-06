package com.example.marcusedition.professionalshopper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by victor on 02.10.15.
 */
public class RecordActivity extends Activity {

    private static final int CAMERA_RESULT = 1;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private ImageView goodsPhoto;
    private Button changeImage;
    private EditText editGoodsName;
    private EditText editShopName;
    private EditText editGoodsPrice;
    private EditText editGoodsDescriprion;
    private RatingBar editGoodsRating;
    private Bitmap galleryPic = null;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);
        initializationLocalFields();

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
                params.height = height / 2;
                params.width = width / 2;
                goodsPhoto.setLayoutParams(params);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_RESULT);
            }
        });
    }

    private void initializationLocalFields() {
        goodsPhoto = (ImageView)findViewById(R.id.goods_photo);
        editGoodsName = (EditText)findViewById(R.id.goods_name);
        editGoodsPrice = (EditText)findViewById(R.id.goods_price);
        editShopName = (EditText)findViewById(R.id.shop_name);
        editGoodsDescriprion = (EditText)findViewById(R.id.short_description);
        editGoodsRating = (RatingBar)findViewById(R.id.goods_rating);
        changeImage = (Button)findViewById(R.id.change_photo_button);
        mDatabaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, 1);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_RESULT) {
            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            goodsPhoto.setImageBitmap(thumbnailBitmap);
            galleryPic = thumbnailBitmap;
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onClickButton(View v) {
        ContentValues newValues = new ContentValues();

        String goodsNameFromEdit = String.valueOf(editGoodsName.getText());
        String shopNameFromEdit = String.valueOf(editShopName.getText());
        String goodsPriceFromEdit = String.valueOf(editGoodsPrice.getText());
        String goodsRatingFromEdit = String.valueOf(editGoodsRating.getRating());
        String goodsDescriptionFromEdit = String.valueOf(editGoodsDescriprion.getText());

        if (checkToEmptyText(goodsNameFromEdit, shopNameFromEdit, goodsPriceFromEdit, goodsDescriptionFromEdit))
            return;

        writeIntoDB(newValues, goodsNameFromEdit, shopNameFromEdit, goodsPriceFromEdit, goodsRatingFromEdit, goodsDescriptionFromEdit);

        editGoodsDescriprion.setText("");
        editGoodsPrice.setText("");
        editGoodsRating.setRating(0);
        editGoodsName.setText("");
        editShopName.setText("");
        goodsPhoto.setImageDrawable(getResources().getDrawable(R.drawable.photo));

        Toast toast = Toast.makeText(getApplicationContext(), "Новий запис додано", Toast.LENGTH_SHORT);
        toast.show();
        onStop();
    }

    private void writeIntoDB(ContentValues newValues, String goodsNameFromEdit, String shopNameFromEdit, String goodsPriceFromEdit, String goodsRatingFromEdit, String goodsDescriptionFromEdit) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.out.println(galleryPic);
        if(galleryPic == null) {
            galleryPic = BitmapFactory.decodeResource(getResources(),
                    R.drawable.photo);
        }
        galleryPic.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] photo = baos.toByteArray();
        newValues.put(DatabaseHelper.GOODS_NAME_COLUMN, goodsNameFromEdit);
        newValues.put(DatabaseHelper.SHOP_NAME_COLUMN, shopNameFromEdit);
        newValues.put(DatabaseHelper.GOODS_PRICE_COLUMN, goodsPriceFromEdit);
        newValues.put(DatabaseHelper.GOODS_RATING_COLUMN, goodsRatingFromEdit);
        newValues.put(DatabaseHelper.GOODS_DESCRIPTION_COLUMN, goodsDescriptionFromEdit);
        newValues.put(DatabaseHelper.GOODS_PHOTO_COLUMN, photo);

        mSqLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE, null, newValues);
    }

    private boolean checkToEmptyText(String goodsNameFromEdit, String shopNameFromEdit, String goodsPriceFromEdit, String goodsDescriptionFromEdit) {
        if (goodsNameFromEdit.equals("") || shopNameFromEdit.equals("") || goodsPriceFromEdit.equals("")
                || goodsDescriptionFromEdit.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Заповніть всі поля", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(getApplicationContext(), "Дані не збережені", Toast.LENGTH_SHORT).show();
    }
}
