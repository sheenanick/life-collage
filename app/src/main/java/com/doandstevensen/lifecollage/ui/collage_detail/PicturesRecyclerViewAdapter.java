package com.doandstevensen.lifecollage.ui.collage_detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sheena on 1/31/17.
 */

public class PicturesRecyclerViewAdapter extends RecyclerView.Adapter<PicturesRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<PictureResponse> mPictures = new ArrayList<>();
    private DataManager mDataManager;

    public PicturesRecyclerViewAdapter(Context context) {
        mContext = context;
        mDataManager = new DataManager(context);
    }

    public void setPictures(ArrayList<PictureResponse> pictures) {
        mPictures = pictures;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PictureResponse photo = mPictures.get(position);
        final String url = photo.getLocation();

        int width = mDataManager.getScreenWidth();
        int height = mDataManager.getScreenHeight();

        Picasso.with(mContext)
                .load(url)
                .resize(width, height)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (mPictures != null) {
            return mPictures.size();
        } else {
            return 0;
        }
    }

    public void detach() {
        mContext = null;
        mPictures = null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
