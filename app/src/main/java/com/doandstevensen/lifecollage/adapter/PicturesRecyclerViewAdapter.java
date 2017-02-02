package com.doandstevensen.lifecollage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.model.Picture;
import com.squareup.picasso.Picasso;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Sheena on 1/31/17.
 */

public class PicturesRecyclerViewAdapter extends RealmRecyclerViewAdapter<Picture,
        PicturesRecyclerViewAdapter.MyViewHolder> {

    private final Context context;

    public PicturesRecyclerViewAdapter(Context context, OrderedRealmCollection<Picture> data) {
        super(context, data, true);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picture photo = getData().get(position);
        String url = photo.getPath();
        Picasso.with(context)
                .load(url)
                .fit()
                .into(holder.imageView);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
