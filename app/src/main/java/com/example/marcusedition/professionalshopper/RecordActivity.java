package com.example.marcusedition.professionalshopper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by victor on 02.10.15.
 */
public class RecordActivity extends Activity {

    public static final int GALLERY_REQUEST = 1;
    public static final int CAMERA_RESULT = 0;
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

                new AlertDialog.Builder(RecordActivity.this)
                        .setIcon(android.R.drawable.alert_light_frame)
                        .setTitle("Завантаження фото")
                        .setMessage("Завантажити фото чи сфотографувати ?")
                        .setPositiveButton("Фото", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_RESULT);
                            }

                        })
                        .setNeutralButton("Галерея", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                            }

                        })
                        .setNegativeButton("Відміна", null)
                        .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            try {
                if (requestCode == CAMERA_RESULT) {
                    Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
                    goodsPhoto.setImageBitmap(thumbnailBitmap);
                    galleryPic = thumbnailBitmap;
                } else if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
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
                catch (NullPointerException ex) {
                ex.printStackTrace();
            }

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
        newValues.put(DatabaseHelper.GOODS_DATE, getDateTime());
        mSqLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE, null, newValues);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
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
