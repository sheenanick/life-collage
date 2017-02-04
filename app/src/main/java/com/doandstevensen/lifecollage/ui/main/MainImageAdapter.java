package com.doandstevensen.lifecollage.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by Sheena on 2/3/17.
 */

public class MainImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<User> mFeaturedUsers;

    public MainImageAdapter(Context c, ArrayList<User> users) {
        mContext = c;
        mFeaturedUsers = users;
    }

    public int getCount() {
        return mFeaturedUsers.size();
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

        User user = mFeaturedUsers.get(position);
        RealmList<Picture> pictures = user.getCollages().get(0).getPictures();
        int last = pictures.size() - 1;
        Picture picture = pictures.get(last);

        Picasso.with(mContext).load(picture.getPath()).into(imageView);
        return imageView;
    }
}
