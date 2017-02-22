package com.doandstevensen.lifecollage.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.doandstevensen.lifecollage.data.model.CollageListResponse;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/3/17.
 */

public class MainImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CollageListResponse> mCollages;

    public MainImageAdapter(Context c) {
        mContext = c;
    }

    public void setCollages(ArrayList<CollageListResponse> collages) {
        mCollages = collages;
    }

    public int getCount() {
        if (mCollages != null) {
            return mCollages.size();
        } else {
            return 0;
        }
    }

    public Object getItem(int position) {
        return mCollages.get(position);
    }

    public long getItemId(int position) {
        return mCollages.get(position).getCollage().getCollageId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        CollageListResponse collageListResponse = mCollages.get(position);
        PictureResponse picture = collageListResponse.getCollagePic();

        ImageView imageView;
        Integer height = viewGroup.getWidth() / 3;

        if (view == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(height, height));
        } else {
            imageView = (ImageView) view;
        }

        if (height > 0) {
            Picasso.with(mContext)
                    .load(picture.getLocation())
                    .resize(height, height)
                    .centerCrop()
                    .into(imageView);
        }

        return imageView;
    }

    public void detach() {
        mContext = null;
        mCollages = null;
    }
}
