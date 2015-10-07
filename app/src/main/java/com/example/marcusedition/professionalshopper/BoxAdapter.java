package com.example.marcusedition.professionalshopper;

/**
 * Created by victor on 03.10.15.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
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

    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<Product> mListItems;
    String [] result;
    Context context;
    int [] imageId;

    public BoxAdapter(Context context, ArrayList<Product> products) {
        ctx = context;
        mListItems = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return mListItems.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return mListItems.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        final Product p = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.tvDescr)).setText("    " + p.describe);
        ((TextView) view.findViewById(R.id.tvTitle)).setText(p.title);
        RatingBar ratingBar = ((RatingBar) view.findViewById(R.id.tvRating));
        ratingBar.setRating(p.rating);
        ratingBar.setStepSize(0.5f);
        ratingBar.setEnabled(false);
        TextView tvPrice = ((TextView) view.findViewById(R.id.tvPrice));
        tvPrice.setText(p.price + "$");
        final ImageView image = ((ImageView) view.findViewById(R.id.ivImage));
        image.setImageDrawable(p.image);
        ((TextView) view.findViewById(R.id.Date)).setText(p.date);
        ViewGroup.LayoutParams params = getLayoutParams(tvPrice);
        image.setLayoutParams(params);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ctx, GoodsActivity.class);
                intent.putExtra("goodsName", p.title);
                intent.putExtra("goodsPrice", p.price);
                intent.putExtra("goodsDescr", p.describe);
                intent.putExtra("shopName", p.shop);
                Bundle extras = new Bundle();
                image.buildDrawingCache();
                Bitmap imageBit = image.getDrawingCache();
                extras.putParcelable("goodsPhoto", imageBit);
                intent.putExtras(extras);
                intent.putExtra("goodsRating", p.rating);
                ctx.startActivity(intent);
            }
        });

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
        if(Configuration.ORIENTATION_LANDSCAPE == ctx.getResources().getConfiguration().orientation) {
            params.height = height / 2;
            params.width = width / 3;
        } else {
            params.height = height / 3;
            params.width = width / 2;
        }
        return params;
    }

}
