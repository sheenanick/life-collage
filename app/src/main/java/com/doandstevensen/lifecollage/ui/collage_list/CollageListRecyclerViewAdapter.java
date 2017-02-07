package com.doandstevensen.lifecollage.ui.collage_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.Collage;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.squareup.picasso.Picasso;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Sheena on 2/7/17.
 */

public class CollageListRecyclerViewAdapter extends
        RealmRecyclerViewAdapter<Collage, CollageListRecyclerViewAdapter.MyViewHolder> {

    private final Context context;

    public CollageListRecyclerViewAdapter(Context context, OrderedRealmCollection<Collage> data) {
        super(context, data, true);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.collage_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Collage collage = getData().get(position);

        String title = collage.getName();
        holder.textView.setText(title);

        Picture firstPicture = collage.getPictures().get(0);
        String url = firstPicture.getPath();
        Picasso.with(context).load(url).into(holder.imageView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView = (TextView) view.findViewById(R.id.collageTitleTextView);
        }
    }
}
