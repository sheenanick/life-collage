package com.doandstevensen.lifecollage.ui.collage_list;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheena on 2/7/17.
 */

public class CollageListRecyclerViewAdapter extends RecyclerView.Adapter<CollageListRecyclerViewAdapter.MyViewHolder> {
    private final Context mContext;
    private ArrayList<CollageResponse> mCollages;
    private ArrayList<PictureResponse> mPictures;
    private CollageListRecyclerViewAdapter.ClickListener mClickListener;

    public CollageListRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<CollageResponse> collages, ArrayList<PictureResponse> pictures) {
        mCollages = collages;
        mPictures = pictures;
    }

    public void setCollages(ArrayList<CollageResponse> collages) {
        mCollages = collages;
    }

    public void setPictures(ArrayList<PictureResponse> pictures) {
        mPictures = pictures;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.collage_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CollageResponse collage = mCollages.get(position);
        final int collageId = collage.getCollageId();
        final String collageName = collage.getTitle();

        holder.textView.setText(collageName);

        for (PictureResponse picture : mPictures) {
            if (picture != null) {
                if (picture.getCollageId() == collageId) {
                    Picasso.Builder builder = new Picasso.Builder(mContext);
                    builder.listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            exception.printStackTrace();
                        }
                    });
                    builder.build().load(picture.getLocation()).into(holder.imageView);
                    break;
                }
            }
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onCollageClick(collageId, collageName, true);
            }
        });

        holder.moreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(mContext, holder.moreIcon);
                popup.getMenuInflater().inflate(R.menu.activity_collage_list_popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        mClickListener.onMenuClick(item, collageId, collageName);
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Object payload = payloads.get(0);
            if (payload instanceof String) {
                holder.textView.setText((String) payload);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mCollages == null) {
            return 0;
        } else {
            return mCollages.size();
        }
    }

    public void setClickListener(CollageListRecyclerViewAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ImageView moreIcon;

        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView = (TextView) view.findViewById(R.id.collageTitleTextView);
            moreIcon = (ImageView) view.findViewById(R.id.moreIcon);
        }
    }

    interface ClickListener {
        void onCollageClick(int collageId, String collageTitle, boolean load);
        void onMenuClick(MenuItem item, int collageId, String collageName);
    }
}
