package com.example.marcusedition.professionalshopper;

/**
 * Created by victor on 03.10.15.
 */

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class BoxAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Product> objects;
    RatingBar ratingBar;

    public BoxAdapter(Context context, ArrayList<Product> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Product p = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.tvDescr)).setText(p.describe);
        ((TextView) view.findViewById(R.id.tvTitle)).setText(p.title);
        ratingBar = ((RatingBar) view.findViewById(R.id.tvRating));
        ratingBar.setNumStars(p.rating);
        ratingBar.setRating(p.rating);
        ratingBar.setEnabled(false);
        TextView view1 = ((TextView) view.findViewById(R.id.tvPrice));
                view1.setText(p.price + "грн");
        ImageView image = ((ImageView) view.findViewById(R.id.ivImage));
        image.setImageDrawable(p.image);
        ViewGroup.LayoutParams params = getLayoutParams(view1);
        image.setLayoutParams(params);

        // присваиваем чекбоксу обработчик
        // пишем позицию
        // заполняем данными из товаров: в корзине или нет
        return view;
    }

    // товар по позиции
    Product getProduct(int position) {
                return ((Product) getItem(position));
    }

    public ViewGroup.LayoutParams getLayoutParams(TextView image) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) image.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        ViewGroup.LayoutParams params = image.getLayoutParams();
        params.height = height / 3;
        params.width = width / 3;
        return params;
    }
}