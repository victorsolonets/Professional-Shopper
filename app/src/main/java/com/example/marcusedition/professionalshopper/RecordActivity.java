package com.example.marcusedition.professionalshopper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
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

    /**
     * Поля для вибору картинки або зробити фото
     */
    public static final int GALLERY_REQUEST = 1;
    public static final int CAMERA_RESULT = 0;
    /**
     * Поля для роботи з БД
     */
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    /**
     * Поля для заповнення інформацією від користувача
     */
    private ImageView goodsPhoto;
    private EditText editGoodsName;
    private EditText editShopName;
    private EditText editGoodsPrice;
    private EditText editGoodsDescriprion;
    private RatingBar editGoodsRating;
    private Bitmap galleryPic = null;
    private Button changeImage;

    /**
     * Метод для створення activity та заповнення залежностей
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);
        initializationLocalFields();
        changeImage.setOnClickListener(new View.OnClickListener() {
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
                        .setIcon(android.R.drawable.sym_contact_card)
                        .setTitle("Вибір фото")
                        .setMessage("Завантажити чи сфотографувати?")
                        .setNegativeButton("Галерея", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                            }

                        })
                        .setNeutralButton("Відміна", null)
                        .setPositiveButton("Фото", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_RESULT);
                            }

                        })
                        .show();
            }
        });
    }

    /**
     * Метод для обробки резульату запиту на кнопку додати фото,
     * обробка помилок при можливих негараздах з даними
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    /**
     * Ініціалазація всіх даних створення залежностей
     */
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


    /**
     * Обробка кнопки додати всі дані до БД
     * @param v
     */
    public void onClickButton(View v) {
        ContentValues newValues = new ContentValues();

        /**
         * Заповнюємо локальні змінні даними з полів заповненими користувачем
         */
        String goodsNameFromEdit = String.valueOf(editGoodsName.getText());
        String shopNameFromEdit = String.valueOf(editShopName.getText());
        String goodsPriceFromEdit = String.valueOf(editGoodsPrice.getText());
        String goodsRatingFromEdit = String.valueOf(editGoodsRating.getRating());
        String goodsDescriptionFromEdit = String.valueOf(editGoodsDescriprion.getText());

        /**
         * Перевірка на пусте поле
         */
        if (checkToEmptyText(goodsNameFromEdit, shopNameFromEdit, goodsPriceFromEdit, goodsDescriptionFromEdit))
            return;
        /**
         * Створюємо запис до БД
         */
        writeIntoDB(newValues, goodsNameFromEdit, shopNameFromEdit, goodsPriceFromEdit, goodsRatingFromEdit, goodsDescriptionFromEdit);

        /**
         * Очищуємо поля після додавання
         */
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

    /**
     * Метод для додавання запису до БД через допоміжний клас DBHelper
     * @param newValues
     * @param goodsNameFromEdit
     * @param shopNameFromEdit
     * @param goodsPriceFromEdit
     * @param goodsRatingFromEdit
     * @param goodsDescriptionFromEdit
     */
    private void writeIntoDB(ContentValues newValues, String goodsNameFromEdit, String shopNameFromEdit, String goodsPriceFromEdit, String goodsRatingFromEdit, String goodsDescriptionFromEdit) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

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

    /**
     * Отримуємо поточну дату додавання
     * @return
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Перевірка на пусті поля не заповнені користувачем
     * @param goodsNameFromEdit
     * @param shopNameFromEdit
     * @param goodsPriceFromEdit
     * @param goodsDescriptionFromEdit
     * @return
     */
    private boolean checkToEmptyText(String goodsNameFromEdit, String shopNameFromEdit, String goodsPriceFromEdit, String goodsDescriptionFromEdit) {
        if (goodsNameFromEdit.equals("") || shopNameFromEdit.equals("") || goodsPriceFromEdit.equals("")
                || goodsDescriptionFromEdit.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Заповніть всі поля", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
        return false;
    }

    /**
     * Метод для обробки паузи
     */
    @Override
    protected void onPause() {
        super.onPause();
    }
}
