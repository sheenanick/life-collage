package com.doandstevensen.lifecollage.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/3/17.
 */

public class MainImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PictureResponse> mPictures;

    public MainImageAdapter(Context c) {
        mContext = c;
    }

    public void setPictures(ArrayList<PictureResponse> pictures) {
        mPictures = pictures;
    }

    public int getCount() {
        if (mPictures != null) {
            return mPictures.size();
        } else {
            return 0;
        }
    }

    public Object getItem(int position) {
        return mPictures.get(position);
    }

    public long getItemId(int position) {
        return mPictures.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        PictureResponse picture = mPictures.get(position);
        ImageView imageView;
        Integer height = viewGroup.getWidth() / 3;
        if (view == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(height, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) view;
        }
        Picasso.with(mContext).load(picture.getLocation()).into(imageView);
        return imageView;
    }
}
