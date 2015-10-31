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

/**
 * Class to create special adapter for list products
 */
public class BoxAdapter extends BaseAdapter {

    /**
     * Context of out view
     */
    private Context ctx;
    /**
     * Special LayoutInflater for list
     */
    private LayoutInflater lInflater;
    /**
     * ArrayList for list product
     */
    private ArrayList<Product> mListItems;
    /**
     * Result array
     */
    private String [] result;
    private int [] imageId;

    /**
     * Constructor for create list of products
     * @param context
     * @param products
     */
    public BoxAdapter(Context context, ArrayList<Product> products) {
        ctx = context;
        mListItems = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    /**
     * Get count of elements
     * @return count of list item
     */
    @Override
    public int getCount() {
        return mListItems.size();
    }

    // элемент по позиции
    /**
     * Get item from position
     * @param position
     * @return item
     */
    @Override
    public Object getItem(int position) {
        return mListItems.get(position);
    }

    /**
     * Get item id from position
     * @param position
     * @return item id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    /**
     * Get one view from list
     * @param position position of view
     * @param convertView
     * @param parent our group
     * @return one view from list
     */
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
        tvPrice.setText(p.price + " $");
        final ImageView image = ((ImageView) view.findViewById(R.id.ivImage));
        image.setImageDrawable(p.image);
        ((TextView) view.findViewById(R.id.Date)).setText(p.date);
        ViewGroup.LayoutParams params = getLayoutParams(tvPrice);
        image.setLayoutParams(params);

        view.setOnClickListener(new View.OnClickListener() {
            /**
             * Method start when click in listview
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, GoodsActivity.class);
                /**
                 * Відправляємо дані до інтенту, які там потім відображаються на окремій сторінці
                 */
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
                intent.putExtra("Date",p.date);
                ctx.startActivity(intent);
            }
        });

        return view;
    }

    /**
     * Get product from position
     * @param position of product
     * @return Product
     */
    public Product getProduct(int position) {
        return ((Product) getItem(position));
    }


    /**
     * Get Layout Parameters for view image
     * @param image what view we redesign parameters width and height
     * @return parameters of new image in view
     */
    public ViewGroup.LayoutParams getLayoutParams(TextView image) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) image.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        ViewGroup.LayoutParams params = image.getLayoutParams();
        /**
         * При зміні орієнтації екрану змінюються пропорції картинки
         */
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
