package com.doandstevensen.lifecollage.ui.collage_detail;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.squareup.picasso.Picasso;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Sheena on 1/31/17.
 */

public class PicturesRecyclerViewAdapter extends
        RealmRecyclerViewAdapter<Picture, PicturesRecyclerViewAdapter.MyViewHolder> {

    private final Context mContext;

    public PicturesRecyclerViewAdapter(Context context, OrderedRealmCollection<Picture> data) {
        super(context, data, true);
        mContext = context;
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
        final String url = photo.getPath();

        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.d("Picasso", url);
                exception.printStackTrace();
            }
        });
        builder.build().load(url).into(holder.imageView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
