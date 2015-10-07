package com.example.marcusedition.professionalshopper;

import android.graphics.drawable.Drawable;

/**
 * Created by victor on 03.10.15.
 */
public class Product {

    String title;
    String describe;
    Float price;
    Drawable image;
    float rating;
    String shop;
    String date;


    public Product (String _title, String _describe, float _price, Drawable _image, float _rating, String _shop, String _date) {
        title = _title;
        describe = _describe;
        price = _price;
        image = _image;
        rating = _rating;
        shop = _shop;
        date = _date;
    }

    public Product (String _title, String _describe, float _price, float _rating ) {
        title = _title;
        describe = _describe;
        price = _price;
        rating = _rating;
    }
}