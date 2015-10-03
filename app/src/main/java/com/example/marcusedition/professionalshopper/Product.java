package com.example.marcusedition.professionalshopper;

import android.graphics.drawable.Drawable;

/**
 * Created by victor on 03.10.15.
 */
public class Product {

    String title;
    String describe;
    int price;
    Drawable image;
    int rating;


    public Product (String _title, String _describe, int _price, Drawable _image, int _rating ) {
        title = _title;
        describe = _describe;
        price = _price;
        image = _image;
        rating = _rating;
    }

    public Product (String _title, String _describe, int _price, int _rating ) {
        title = _title;
        describe = _describe;
        price = _price;
        rating = _rating;
    }
}