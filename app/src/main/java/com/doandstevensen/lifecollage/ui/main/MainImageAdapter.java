package com.doandstevensen.lifecollage.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Sheena on 2/3/17.
 */

public class MainImageAdapter extends BaseAdapter {
    private Context mContext;

    public MainImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 6;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView;
        Integer height = viewGroup.getWidth() / 3;
        if (view == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(height, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) view;
        }

        Picasso.with(mContext).load("https://source.unsplash.com/random").into(imageView);
        return imageView;
    }
}
