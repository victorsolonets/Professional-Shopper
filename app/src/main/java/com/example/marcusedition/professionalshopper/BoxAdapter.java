package com.example.marcusedition.professionalshopper;

/**
 * Created by victor on 03.10.15.
 */

import android.content.Context;
import android.content.res.Configuration;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            view = lInflater.inflate(R.layout.item, parent, false);
            holder.itemName = (TextView)view.findViewById(R.id.tvTitle);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
//        String stringItem = mListItems.get(position);

        Product p = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.tvDescr)).setText("    " + p.describe);
        ((TextView) view.findViewById(R.id.tvTitle)).setText(p.title);
        RatingBar ratingBar = ((RatingBar) view.findViewById(R.id.tvRating));
        System.out.println();
        ratingBar.setRating(p.rating);
        ratingBar.setStepSize(0.5f);
        ratingBar.setEnabled(false);
        TextView view1 = ((TextView) view.findViewById(R.id.tvPrice));
        view1.setText(p.price + "$");
        ImageView image = ((ImageView) view.findViewById(R.id.ivImage));
        image.setImageDrawable(p.image);
        ViewGroup.LayoutParams params = getLayoutParams(view1);
        image.setLayoutParams(params);
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

    private static class ViewHolder {
        protected TextView itemName;
    }
}