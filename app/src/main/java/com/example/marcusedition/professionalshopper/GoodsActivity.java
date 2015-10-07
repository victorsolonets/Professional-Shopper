package com.example.marcusedition.professionalshopper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by victor on 06.10.15.
 */
public class GoodsActivity extends Activity {

    private TextView mTitle;
    private ImageView mPhoto;
    private TextView mPrice;
    private RatingBar mRating;
    private TextView mShop;
    private TextView mDescr;
    private TextView mDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity);

        mTitle = (TextView)findViewById(R.id.ChapterGoodsName);
        mPhoto = (ImageView)findViewById(R.id.ChapterGoodsPhoto);
        mPrice = (TextView)findViewById(R.id.ChapterGoodsPrice);
        mRating = (RatingBar)findViewById(R.id.ChapterGoodsRating);
        mShop = (TextView)findViewById(R.id.ChapterShopView);
        mDescr = (TextView) findViewById(R.id.AllDescr);
        mDate = (TextView) findViewById(R.id.Date);

        mRating.setEnabled(false);

        String title = getIntent().getStringExtra("goodsName");
        Bundle extras = getIntent().getExtras();
        Bitmap bmp = (Bitmap) extras.getParcelable("goodsPhoto");
        mPhoto.setImageBitmap(bmp);
        float price = getIntent().getFloatExtra("goodsPrice", 0.0f);
        float rating = getIntent().getFloatExtra("goodsRating",0.0f);
        String descr = getIntent().getStringExtra("goodsDescr");
        String shop = getIntent().getStringExtra("shopName");
        String date = getIntent().getStringExtra("Date");

        mTitle.setText(title);
        mPrice.setText(String.valueOf(price) + " $");
        mRating.setRating(rating);
        mShop.setText(shop);
        mDescr.setText("   "+descr);
        mDate.setText(date);
    }
}