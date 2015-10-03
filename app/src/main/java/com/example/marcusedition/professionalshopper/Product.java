package com.example.marcusedition.professionalshopper;

import android.graphics.drawable.Drawable;

/**
 * Created by victor on 03.10.15.
 */
public class Product {

    String title;
    String describe;
    float price;
    Drawable image;
    float rating;


    public Product (String _title, String _describe, String _price, Drawable _image, String _rating ) {
        title = _title;
        describe = _describe;
        price = Float.valueOf(_price);
        image = _image;
        rating = Float.valueOf(_rating);
    }

    public Product (String _title, String _describe, int _price, float _rating ) {
        title = _title;
        describe = _describe;
        price = _price;
        rating = _rating;
    }
}