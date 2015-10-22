package com.example.marcusedition.professionalshopper;

import android.graphics.drawable.Drawable;

/**
 * Created by victor on 03.10.15.
 */
public class Product {

    /**
     * Поля для заповнення BoxAdapter (адаптеру для перегляду)
     */
    private String title;
    private String describe;
    private Float price;
    private Drawable image;
    private float rating;
    private String shop;
    private String date;

    /**
     * Конструктор для заповнення всіх полів
     * @param _title
     * @param _describe
     * @param _price
     * @param _image
     * @param _rating
     * @param _shop
     * @param _date
     */
    public Product (String _title, String _describe, float _price, Drawable _image, float _rating, String _shop, String _date) {
        title = _title;
        describe = _describe;
        price = _price;
        image = _image;
        rating = _rating;
        shop = _shop;
        date = _date;
    }

    /**
     * Конструктор для заповнення всіх полів
     * @param _title
     * @param _describe
     * @param _price
     * @param _rating
     */
    public Product (String _title, String _describe, float _price, float _rating ) {
        title = _title;
        describe = _describe;
        price = _price;
        rating = _rating;
    }
}